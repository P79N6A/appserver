package com.idcq.appserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.listeners.ContextInitListener;

public class FdfsUtil
{
    private static final Logger log = LoggerFactory.getLogger(FdfsUtil.class);

    private static Random random = new Random();

    /*
     * 由于
     * StorageClient1不是线程安全的，所以全局使用少数据几个StorageClient1，并采取同步策略来减少竞争，但这里的同步策略十分简单
     * ，如有需要可以更改
     */
    private static Object[] locks = new Object[]
    { new Object(), new Object() };

    private static StorageClient1[] clients = new StorageClient1[2];

    static
    {
        try
        {
            String url = System.getProperty("fdfsPath");
            ClientGlobal.init(url);
        }
        catch (Exception e)
        {
            log.info(e.getMessage(), e);
        }
        clients[0] = initClient();
        clients[1] = initClient();
    }

    private static StorageClient1 initClient()
    {
        TrackerClient tracker1 = new TrackerClient();
        TrackerServer trackerServer1 = null;
        try
        {
            trackerServer1 = tracker1.getConnection();
        }
        catch (IOException e)
        {
            log.info(e.getMessage(), e);
        }
        StorageServer storageServer1 = null;
        return new StorageClient1(trackerServer1, storageServer1);
    }

    /**
     * 获取图片文件全路径
     * @param trackerServer
     * @param url
     * @return
     * @throws Exception
     */
    public static String getFileRealPath(TrackerServer trackerServer, String url) throws Exception
    {
        String file_url = null;
        if (url == null)
        {
            return file_url;
        }
        InetSocketAddress inetSockAddr = trackerServer.getInetSocketAddress();
        file_url = "http://" + inetSockAddr.getAddress().getHostAddress();
        if (ClientGlobal.g_tracker_http_port != 80)
        {
            file_url += ":" + ClientGlobal.g_tracker_http_port;
        }
        file_url += "/" + url;
        if (ClientGlobal.g_anti_steal_token)
        {
            int ts = (int) (System.currentTimeMillis() / 1000);
            String token = ProtoCommon.getToken(url, ts, ClientGlobal.g_secret_key);
            file_url += "?token=" + token + "&ts=" + ts;
        }
        return file_url;
    }

    /**
     * 拼接图片下载路径，对应的服务地址配置在nginx_server中
     * @param url
     * @return
     * @throws Exception
     */
    public static String getFileProxyPath(String url) throws Exception
    {
        String file_url = null;
        if (url == null || "null".equals(url))
        {
            return file_url;
        }
        file_url = "http://" + PropertyUtil.readProperty(ContextInitListener.fdfsFilePath, "nginx_server");
        if (url.startsWith("/"))
        {
            file_url += url;
        }
        else
        {
            file_url += "/" + url;
        }
        return file_url;
    }

    /**
     * 读取代理服务器地址
     * 
     * @return
     * @throws Exception
     */
    public static String getFileProxyServer() throws Exception
    {
        String server = null;
        server = "http://" + PropertyUtil.readProperty(ContextInitListener.fdfsFilePath, "nginx_server");
        return server;
    }

    /**
     * 使用代理服务器地址拼凑文件全限定名
     * 
     * @param proxyServer 代理服务器地址
     * @param filePath 文件相对路径
     * @return
     * @throws Exception
     */
    public static String getFileFQN(String proxyServer, String filePath) throws Exception
    {
        if (StringUtils.isBlank(proxyServer))
        {
            return null;
        }
        String file_url = proxyServer;
        if (filePath.startsWith("/"))
        {
            file_url += filePath;
        }
        else
        {
            file_url += "/" + filePath;
        }
        return file_url;
    }

    /**
     * 
     * 上传文件公共方法
     * @Function: com.idcq.appserver.utils.FdfsUtil.uploadFile
     * @Description:
     * 
     * @param mimeType
     * @param myfile
     * @return
     * @throws IOException
     * @throws Exception
     * 
     * @version:v1.0
     * @author:szp
     * @date:2015年7月30日 下午12:46:18
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年7月30日 szp v1.0.0 create
     */
    public static String uploadFile(String mimeType, MultipartFile myfile) throws IOException, Exception
    {
        String tempFileName = myfile.getOriginalFilename();

        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);

        // 设置元信息
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", tempFileName);
        metaList[1] = new NameValuePair("fileExtName", mimeType);
        metaList[2] = new NameValuePair("fileLength", String.valueOf(myfile.getSize()));
        String url = client.upload_file1(myfile.getBytes(), mimeType, metaList);
        // client.download_file1(file_id, new DownloadCallback())
        // client.download_file1(file_id, callback)
        // DownloadCallback callback = new DownloadCallback()
        trackerServer.close();
        CommonValidUtil.validObjectNull(url, CodeConst.CODE_FILE_UPLOAD_FAIL, "无法连接到文件服务器");
        return url;
    }

    /**
     * 获取文件二进制流以及文件信息
     * @param url
     * @return 二进制字节数据通过"file"获取，文件名通过"fileName"获取，mimeType - fileExtName,
     *         fileLength - fileLength
     * @throws Exception
     */
    public static Map<String, Object> downFileFromFtds(String url) throws Exception
    {

        byte[] binary = null;
        NameValuePair[] metedata = null;
        int choice = random.nextInt(2);
        synchronized (locks[choice])    //client并非线程安全的
        {
            StorageClient1 client = clients[choice];
            if (null == client)
            {
                // 排除初始化失败
                client = initClient();
                clients[choice] = client;
            }
            try
            {
                binary = client.download_file1(url);
                metedata = client.get_metadata1(url);
            }
            catch (Exception e)
            {
                log.info("排除连接超时");
                log.info(e.getMessage(), e);
                client = initClient();
                clients[choice] = client;
                metedata = client.get_metadata1(url);
                binary = client.download_file1(url);
            }
        }
        Map<String, Object> result = parseMeteData(metedata);
        result.put("file", binary);
        return result;
    }
    
    /**
     * 将取出的metedata适配成map形式
     * @param metedata
     * @return
     */
    private static Map<String, Object> parseMeteData(NameValuePair[] metedata)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (metedata != null)
        {
            for (NameValuePair temp : metedata)
            {
                result.put(temp.getName(), temp.getValue());
            }
        }
        return result;
    }

    /**
     * just for test
     * @param args
     * @throws Exception
     */
   /* public static void main1(String[] args) throws Exception
    {
        final CountDownLatch count = new CountDownLatch(1);
        
         * // 建立连接 TrackerClient tracker = new TrackerClient(); final
         * TrackerServer trackerServer = tracker.getConnection(); StorageServer
         * storageServer = null; final StorageClient1 client = new
         * StorageClient1(trackerServer, storageServer);
         
        Thread t = new Thread()
        {

            public void run()
            {
                try
                {
                    System.out.println("thread 1 start");
                    count.await();
                    byte[] byt1 = downFileFromFtds("group1/M00/00/17/wKgBnFWi67qANuj4AAGs2ycqmq0171.jpg");
                    File file = new File("C:/Users/Administrator/Desktop/temp/1.jpg");
                    FileOutputStream out1 = new FileOutputStream(file);
                    out1.write(byt1);
                    out1.close();
                    System.out.println(byt1.length);
                    count.await();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread()
        {

            public void run()
            {
                try
                {
                    System.out.println("thread 2 start");
                    count.await();
                    byte[] byt2 = downFileFromFtds("group1/M00/00/17/wKgBm1Wi656AfIkRAABJM4ulen4458.jpg");
                    File file2 = new File("C:/Users/Administrator/Desktop/temp/2.jpg");
                    FileOutputStream out2 = new FileOutputStream(file2);
                    out2.write(byt2);
                    out2.close();
                    System.out.println(byt2.length);
                }
                catch (Exception e)
                { // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        Thread t3 = new Thread()
        {

            public void run()
            {
                try
                {
                    System.out.println("thread 3 start");
                    count.await();
                    byte[] byt2 = downFileFromFtds("group1/M00/00/17/wKgBm1Wi64SAMScWAABJM4ulen4181.jpg");
                    File file2 = new File("C:/Users/Administrator/Desktop/temp/3.jpg");
                    FileOutputStream out2 = new FileOutputStream(file2);
                    out2.write(byt2);
                    out2.close();
                    System.out.println(byt2.length);
                }
                catch (Exception e)
                { // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        Thread t4 = new Thread()
        {

            public void run()
            {
                try
                {
                    System.out.println("thread 4 start");
                    count.await();
                    byte[] byt2 = downFileFromFtds("group1/M00/00/17/wKgBnFWi65uASjXsAAGs2ycqmq0791.jpg");
                    File file2 = new File("C:/Users/Administrator/Desktop/temp/4.jpg");
                    FileOutputStream out2 = new FileOutputStream(file2);
                    out2.write(byt2);
                    out2.close();
                    System.out.println(byt2.length);
                }
                catch (Exception e)
                { // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        t.start();
        t2.start();
        t3.start();
        t4.start();
        Thread.sleep(1000);
        count.countDown();
    }*/
}
