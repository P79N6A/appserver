package com.idcq.appserver.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.idcq.appserver.common.CodeConst;

/** 
 * 海报图片文字水印 
 * @author fysheji 
 */  
public class PosterImageUtil {
	public static String file_url = "";
    public static String ServerPath = "/poster/PosterAssets/";
    public static String OutPath = "/poster/PosterAssets/";
   /* static{
    	try {
    		//file_url = "http://" + PropertyUtil.readProperty(ContextInitListener.fdfsFilePath, "nginx_server");
    		file_url="C:/Users/Administrator/Desktop";
			//file_url="http://" +request.getRemoteHost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/
    public  Map createPorster(Integer posterId,String logoText,String dateTime,ArrayList<String> list, HttpServletRequest request)
    {
    	/*StringBuffer urlBuffer=request.getRequestURL();
    	String url =urlBuffer.toString();
    	String[] urlArray=url.split("/");
    	if(urlArray.length>3){
    		file_url=urlArray[0]+"//"+urlArray[2];
    	}
    	System.out.println("\r\n\r\n\r\n=============================="+file_url+"\r\n\r\n\r\n\r\n\r\n");*/
    	Map map =new HashMap<String, Object>();
    	switch (posterId) {
		case 10001:
			map=createPorster10001(logoText,dateTime,list);
			break;
		case 10002:
			map=createPorster10002(logoText,dateTime,list);
			break;
		case 10003:
			map=createPorster10003(logoText,dateTime,list);
			break;
		default:
			map=createPorster10004(logoText,dateTime,list);
			break;
		}
    	return map;
    	
    }
    public Map createPorster10001(String logoText,String dateTime,ArrayList<String> list)
    {
        String outImgPath = PosterImageUtil.OutPath+"hb_ouput/poster_10001.png";  
    	String srcImgPath = PosterImageUtil.file_url+ServerPath+"hb_10001/hb_10001.png";  
        String logoImgPath = PosterImageUtil.file_url+ServerPath+"hb_10001/mansong.png";  
        Map<String,Object> map = new HashMap<String,Object>();
		map.put("满", PosterImageUtil.file_url+ServerPath+"hb_10001/man.png");
		map.put("0", PosterImageUtil.file_url+ServerPath+"hb_10001/0.png");
		map.put("1", PosterImageUtil.file_url+ServerPath+"hb_10001/1.png");
		map.put("2", PosterImageUtil.file_url+ServerPath+"hb_10001/2.png");
		map.put("3", PosterImageUtil.file_url+ServerPath+"hb_10001/3.png");
		map.put("4", PosterImageUtil.file_url+ServerPath+"hb_10001/4.png");
		map.put("5", PosterImageUtil.file_url+ServerPath+"hb_10001/5.png");
		map.put("6", PosterImageUtil.file_url+ServerPath+"hb_10001/6.png");
		map.put("7", PosterImageUtil.file_url+ServerPath+"hb_10001/7.png");
		map.put("8", PosterImageUtil.file_url+ServerPath+"hb_10001/8.png");
		map.put("9", PosterImageUtil.file_url+ServerPath+"hb_10001/9.png");
		map.put("送", PosterImageUtil.file_url+ServerPath+"hb_10001/song.png");
		map.put(".", PosterImageUtil.file_url+ServerPath+"hb_10001/dian.png");
		map.put("元", PosterImageUtil.file_url+ServerPath+"hb_10001/yuan.png");
		
		
		
        
        Font f = new Font("方正卡通简体", Font.PLAIN, 32);
        BufferedImage srcBufImage;
        try {     
        	srcBufImage = ImageIO.read(new File(srcImgPath));   
        	
        	 //添加海报标题水印  
            srcBufImage = PosterImageUtil.markImageByText(
            		logoText,srcBufImage,(720-PosterImageUtil.getTextWidth(logoText,f))/2,40,f,Color.WHITE,null);
            //添加满就送水印  
            srcBufImage = PosterImageUtil.markImageByIcon(srcBufImage,logoImgPath,200,53,null);
        	//添加“到店消费的顾客，可以参加以下活动：”
            String porsterText1 = "到店消费的顾客，可以参加以下活动：";
            Font f1 = new Font("方正卡通简体", Font.PLAIN, 24);
            srcBufImage = PosterImageUtil.markImageByText(
            		porsterText1,srcBufImage,140,140,f1,Color.BLACK,null);
            
            //添加“活动日期”
            Font f2 = new Font("方正卡通简体", Font.PLAIN, 22);
            srcBufImage = PosterImageUtil.markImageByText(
            		dateTime,srcBufImage,(720-PosterImageUtil.getTextWidth(dateTime,f2))/2,320-30,f2,Color.WHITE,null);
            
           //添加活动规则
            int columnH =214 - (3*40)/2;
            
            for(int i = 0;i < list.size(); i ++){
                srcBufImage = PosterImageUtil.markImageByIconText(srcBufImage,map,list.get(i),200,columnH+i*40,null);
            }
            //获取图片上传路径
            Map map1= saveImage(logoText,srcBufImage,outImgPath);
            srcBufImage = null;
            
            
            
            return map1;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        
        return null;
    }
    public  Map createPorster10002(String logoText,String dateTime,ArrayList<String> list)
    {
        String outImgPath = PosterImageUtil.OutPath+"hb_ouput/poster_10002.png";  
    	String srcImgPath = PosterImageUtil.file_url+ServerPath+"hb_10002/hb_10002.png";  
        String logoImgPath = PosterImageUtil.file_url+ServerPath+"hb_10002/mansong.png";  
        Map<String,Object> map = new HashMap<String,Object>();
		map.put("满", PosterImageUtil.file_url+ServerPath+"hb_10002/man.png");
		map.put("0", PosterImageUtil.file_url+ServerPath+"hb_10002/0.png");
		map.put("1", PosterImageUtil.file_url+ServerPath+"hb_10002/1.png");
		map.put("2", PosterImageUtil.file_url+ServerPath+"hb_10002/2.png");
		map.put("3", PosterImageUtil.file_url+ServerPath+"hb_10002/3.png");
		map.put("4", PosterImageUtil.file_url+ServerPath+"hb_10002/4.png");
		map.put("5", PosterImageUtil.file_url+ServerPath+"hb_10002/5.png");
		map.put("6", PosterImageUtil.file_url+ServerPath+"hb_10002/6.png");
		map.put("7", PosterImageUtil.file_url+ServerPath+"hb_10002/7.png");
		map.put("8", PosterImageUtil.file_url+ServerPath+"hb_10002/8.png");
		map.put("9", PosterImageUtil.file_url+ServerPath+"hb_10002/9.png");
		map.put("送", PosterImageUtil.file_url+ServerPath+"hb_10002/song.png");
		map.put(".", PosterImageUtil.file_url+ServerPath+"hb_10002/dian.png");
		map.put("元", PosterImageUtil.file_url+ServerPath+"hb_10002/yuan.png");
		
        
        Font f = new Font("方正卡通简体", Font.PLAIN, 32);
        BufferedImage srcBufImage;
        try {     
        	srcBufImage = ImageIO.read(new File(srcImgPath));   
        	
        	 //添加海报标题水印  
            srcBufImage = PosterImageUtil.markImageByText(
            		logoText,srcBufImage,(720-PosterImageUtil.getTextWidth(logoText,f))/2,40,f,Color.WHITE,null);
            //添加满就送水印  
            srcBufImage = PosterImageUtil.markImageByIcon(srcBufImage,logoImgPath,222,54,null);
        	//添加“到店消费的顾客，可以参加以下活动：”
            String porsterText1 = "到店消费的顾客，可以参加以下活动：";
            Font f1 = new Font("方正卡通简体", Font.PLAIN, 24);
            srcBufImage = PosterImageUtil.markImageByText(
            		porsterText1,srcBufImage,(720-PosterImageUtil.getTextWidth(dateTime,f1))/2,140,f1,Color.WHITE,null);
            
            //添加“活动日期”
            Font f2 = new Font("方正卡通简体", Font.PLAIN, 22);
            srcBufImage = PosterImageUtil.markImageByText(
            		dateTime,srcBufImage,(720-PosterImageUtil.getTextWidth(dateTime,f2))/2,320-30,f2,Color.WHITE,null);
            
          //添加活动规则
            int columnH =214 - (3*40)/2;
            
            for(int i = 0;i < list.size(); i ++){
                srcBufImage = PosterImageUtil.markImageByIconText(srcBufImage,map,list.get(i),235,columnH+i*40,null);
            }
            
            Map map1 = saveImage(logoText,srcBufImage,outImgPath);
            srcBufImage = null;
            return map1;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        
        return null;
    }

    public Map createPorster10003(String logoText,String dateTime,ArrayList<String> list)
    {
        String outImgPath = PosterImageUtil.OutPath+"hb_ouput/poster_10003.png";  
    	
    	
    	
    	String srcImgPath = PosterImageUtil.file_url+ServerPath+"hb_10003/hb_10003.png";  
        String logoImgPath = PosterImageUtil.file_url+ServerPath+"hb_10003/mansong.png";  
        Map<String,Object> map = new HashMap<String,Object>();
		map.put("满", PosterImageUtil.file_url+ServerPath+"hb_10003/man.png");
		map.put("0", PosterImageUtil.file_url+ServerPath+"hb_10003/0.png");
		map.put("1", PosterImageUtil.file_url+ServerPath+"hb_10003/1.png");
		map.put("2", PosterImageUtil.file_url+ServerPath+"hb_10003/2.png");
		map.put("3", PosterImageUtil.file_url+ServerPath+"hb_10003/3.png");
		map.put("4", PosterImageUtil.file_url+ServerPath+"hb_10003/4.png");
		map.put("5", PosterImageUtil.file_url+ServerPath+"hb_10003/5.png");
		map.put("6", PosterImageUtil.file_url+ServerPath+"hb_10003/6.png");
		map.put("7", PosterImageUtil.file_url+ServerPath+"hb_10003/7.png");
		map.put("8", PosterImageUtil.file_url+ServerPath+"hb_10003/8.png");
		map.put("9", PosterImageUtil.file_url+ServerPath+"hb_10003/9.png");
		map.put("送", PosterImageUtil.file_url+ServerPath+"hb_10003/song.png");
		map.put(".", PosterImageUtil.file_url+ServerPath+"hb_10003/dian.png");
		map.put("元", PosterImageUtil.file_url+ServerPath+"hb_10003/yuan.png");
		Font f = new Font("方正古隶简体", Font.PLAIN, 32);
        BufferedImage srcBufImage;
        try {     
        	srcBufImage = ImageIO.read(new File(srcImgPath));   
        	
        	 //添加海报标题水印  
            srcBufImage = PosterImageUtil.markImageByText(
            		logoText,srcBufImage,(720-PosterImageUtil.getTextWidth(logoText,f))/2,40,f,Color.BLACK,null);
            //添加满就送水印  
            srcBufImage = PosterImageUtil.markImageByIcon(srcBufImage,logoImgPath,350,64,null);
        	//添加“到店消费的顾客，可以参加以下活动：”
            String porsterText1 = "到店消费的顾客，可以参加以下活动：";
            Font f1 = new Font("方正古隶简体", Font.PLAIN, 24);
            srcBufImage = PosterImageUtil.markImageByText(
            		porsterText1,srcBufImage,280,130,f1,Color.BLACK,null);
            
            //添加“活动日期”
            Font f2 = new Font("方正古隶简体", Font.PLAIN, 22);
            srcBufImage = PosterImageUtil.markImageByText(
            		dateTime,srcBufImage,(720-PosterImageUtil.getTextWidth(dateTime,f2))/2+125,320-20,f2,Color.BLACK,null);
            
          //添加活动规则
            int columnH =214 - (3*40)/2;
            
            for(int i = 0;i < list.size(); i ++){
                srcBufImage = PosterImageUtil.markImageByIconText(srcBufImage,map,list.get(i),384,columnH+i*40,null);
            }
            
           Map map1 = saveImage(logoText,srcBufImage,outImgPath);
            srcBufImage = null;
            return map1;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
		
    	return null;
    }

    public Map createPorster10004(String logoText,String dateTime,ArrayList<String> list)
    {
        String outImgPath = PosterImageUtil.OutPath+"hb_ouput/poster_10004.png";  
    	
    	
    	String srcImgPath = PosterImageUtil.file_url+ServerPath+"hb_10004/hb_10004.png";  
        String logoImgPath = PosterImageUtil.file_url+ServerPath+"hb_10004/mansong.png";  
        Map<String,Object> map = new HashMap<String,Object>();
		map.put("满", PosterImageUtil.file_url+ServerPath+"hb_10004/man.png");
		map.put("0", PosterImageUtil.file_url+ServerPath+"hb_10004/0.png");
		map.put("1", PosterImageUtil.file_url+ServerPath+"hb_10004/1.png");
		map.put("2", PosterImageUtil.file_url+ServerPath+"hb_10004/2.png");
		map.put("3", PosterImageUtil.file_url+ServerPath+"hb_10004/3.png");
		map.put("4", PosterImageUtil.file_url+ServerPath+"hb_10004/4.png");
		map.put("5", PosterImageUtil.file_url+ServerPath+"hb_10004/5.png");
		map.put("6", PosterImageUtil.file_url+ServerPath+"hb_10004/6.png");
		map.put("7", PosterImageUtil.file_url+ServerPath+"hb_10004/7.png");
		map.put("8", PosterImageUtil.file_url+ServerPath+"hb_10004/8.png");
		map.put("9", PosterImageUtil.file_url+ServerPath+"hb_10004/9.png");
		map.put("送", PosterImageUtil.file_url+ServerPath+"hb_10004/song.png");
		map.put(".", PosterImageUtil.file_url+ServerPath+"hb_10004/dian.png");
		map.put("元", PosterImageUtil.file_url+ServerPath+"hb_10004/yuan.png");
		
        
        Font f = new Font("方正卡通简体", Font.PLAIN, 32);
        BufferedImage srcBufImage;
        try {     
        	srcBufImage = ImageIO.read(new File(srcImgPath));   
        	
        	 //添加海报标题水印  
            srcBufImage = PosterImageUtil.markImageByText(
            		logoText,srcBufImage,(720-PosterImageUtil.getTextWidth(logoText,f))/2,40,f,Color.WHITE,null);
            //添加满就送水印  
            srcBufImage = PosterImageUtil.markImageByIcon(srcBufImage,logoImgPath,222,60,null);
        	//添加“到店消费的顾客，可以参加以下活动：”
            String porsterText1 = "到店消费的顾客，可以参加以下活动：";
            Font f1 = new Font("方正卡通简体", Font.PLAIN, 24);
            srcBufImage = PosterImageUtil.markImageByText(
            		porsterText1,srcBufImage,140,140,f1,Color.BLACK,null);
            
            //添加“活动日期”
            Font f2 = new Font("方正卡通简体", Font.PLAIN, 22);
            srcBufImage = PosterImageUtil.markImageByText(
            		dateTime,srcBufImage,(720-PosterImageUtil.getTextWidth(dateTime,f2))/2,320-30,f2,Color.BLACK,null);
            
          //添加活动规则
            int columnH =214 - (3*40)/2;
            
            for(int i = 0;i < list.size(); i ++){
                srcBufImage = PosterImageUtil.markImageByIconText(srcBufImage,map,list.get(i),200,columnH+i*40,null);
            }
            
            Map map1 = saveImage(logoText,srcBufImage,outImgPath);
            srcBufImage = null;
            return map1;
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        
        return null;
    }
    public static BufferedImage markImageByIconText(BufferedImage srcBufImage,Map<String,Object> map,String iconText,int x,int y, Integer degree)
    {
		int len=iconText.length();
		char ch;
		int i;
		int imgW = 0;
		String path;
		for(i=0;i<len;i++)
		{
			ch=iconText.charAt(i);
			if( map.get(String.valueOf(ch)) != null )
			{
				path = String.valueOf(map.get(String.valueOf(ch)));
				srcBufImage = PosterImageUtil.markImageByIcon(srcBufImage,path, x+imgW, y, null);
				imgW = imgW + PosterImageUtil.getImageWidth(path);
			}
			else
				imgW = imgW + 12;
		}
		return srcBufImage;
    }
    public Map saveImage(String logoText,BufferedImage srcBufImage,String outImgPath) throws Exception
    {
    	 ByteArrayOutputStream out = new ByteArrayOutputStream();  
         boolean flag = ImageIO.write(srcBufImage, "jpeg", out);
         byte[] data = out.toByteArray();
		//建立连接 
		TrackerClient tracker = new TrackerClient();
  		TrackerServer trackerServer = tracker.getConnection();
  		StorageServer storageServer = null;
  		StorageClient1 client = new StorageClient1(trackerServer, storageServer);
  		
  		//设置元信息  
        NameValuePair[] metaList = new NameValuePair[3];  
        metaList[0] = new NameValuePair("fileName", logoText);  
        metaList[1] = new NameValuePair("fileExtName", "jpeg");  
        metaList[2] = new NameValuePair("fileLength", String.valueOf(data.length));
        
  		String url = client.upload_file1(data, "jpeg", metaList);
    	
  		trackerServer.close();
  		CommonValidUtil.validObjectNull(url, CodeConst.CODE_FILE_UPLOAD_FAIL, "无法连接到文件服务器");
  		
		//封装返回对象
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fileSize", data.length);
		map.put("url", url);
  		
		return map;
    }  
    public static int getTextWidth(String logoText,Font font)
    {
    	 FontMetrics fm = new JLabel().getFontMetrics(font);
         int textWidth = fm.stringWidth( logoText );
         return textWidth;
    }
    public static int getImageWidth(String srcImgPath)
    {
    	try {  
    		BufferedImage  srcImg = ImageIO.read(new File(srcImgPath)); 
    		int textWidth = srcImg.getWidth();
    		return textWidth;
    	}
    	catch (Exception e) {     
            e.printStackTrace();     
        }
        return 0;
    }
    /**   
     * 给图片添加水印、可设置水印图片旋转角度   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径   
     * @param targerPath 目标图片路径   
     * @param degree 水印图片旋转角度 
     */    
    public static BufferedImage markImageByIcon(BufferedImage srcBufImage,String iconPath,int x,int y, Integer degree) {     
        //OutputStream os = null;     
        try {     
            Image srcImg = srcBufImage;//ImageIO.read(new File(srcImgPath));   
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),     
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
            // 得到画笔对象     
            // Graphics g= buffImg.getGraphics();     
            Graphics2D g = buffImg.createGraphics();     
    
            // 设置对线段的锯齿状边缘处理     
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
    
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg     
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);     
    
            if (null != degree) {     
                // 设置水印旋转     
                g.rotate(Math.toRadians(degree),     
                        (double) buffImg.getWidth() / 2, (double) buffImg     
                                .getHeight() / 2);     
            }     
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度    
            ImageIcon imgIcon = new ImageIcon(iconPath);     
            // 得到Image对象。     
            Image img = imgIcon.getImage();     
            float alpha = 1; // 透明度     
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,     
                    alpha));     
            // 表示水印图片的位置     
            g.drawImage(img, x, y, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
            g.dispose();     
            //os = new FileOutputStream(targerPath);     
            // 生成图片     
            //ImageIO.write(buffImg, "JPG", os);     
            return buffImg;
        } catch (Exception e) {     
            e.printStackTrace();     
        }
        return null;
    } 
    /** 
     * 给图片添加水印、可设置水印的旋转角度 
     * @param logoText 
     * @param srcImgPath 
     * @param outImgPath 
     * @param degree 
     */  
    public static BufferedImage markImageByText(String logoText, BufferedImage srcBufImage,int x,int y,Font font,Color color,Integer degree) {  
        // 主图片的路径  
        InputStream is = null;  
        try {  
        	
            Image srcImg = srcBufImage;//ImageIO.read(new File(srcImgPath));  
  
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),  
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);  
  
            // 得到画笔对象  
            // Graphics g= buffImg.getGraphics();  
            Graphics2D g = buffImg.createGraphics();  
  
            // 设置对线段的锯齿状边缘处理  
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
  
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg  
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);  
  
            if (null != degree) {  
                // 设置水印旋转  
                g.rotate(Math.toRadians(degree),  
                        (double) buffImg.getWidth() / 2, (double) buffImg  
                                .getHeight() / 2);  
            }  
            
             
        	
            
            
            
            
            
  
            // 设置颜色  
            g.setColor(color); 
            
          
            // 设置 Font  
            g.setFont(font);  
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            float alpha = 1f;  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha
                    ));  
  
            // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .  
            g.drawString(logoText, x, y);  
  
            g.dispose();  
  
            //System.out.println("图片完成添加文字印章。。。。。。"); 
            
            return buffImg; 
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (null != is)  
                    is.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return null;
    } 
    /** 
     *  
     * 自己设置压缩质量来把图片压缩成byte[] 
     *  
     * @param image 
     *            压缩源图片 
     * @param quality 
     *            压缩质量，在0-1之间， 
     * @return 返回的字节数组 
     */  
    public static byte[] bufferedImageTobytes(BufferedImage image, float quality) {  
        //System.out.println("jpeg" + quality + "质量开始打包" + getCurrentTime());  
        // 如果图片空，返回空  
        if (image == null) {  
            return null;  
        }     
        // 得到指定Format图片的writer  
        Iterator<ImageWriter> iter = ImageIO  
                .getImageWritersByFormatName("jpeg");// 得到迭代器  
        ImageWriter writer = (ImageWriter) iter.next(); // 得到writer  
  
        // 得到指定writer的输出参数设置(ImageWriteParam )  
        ImageWriteParam iwp = writer.getDefaultWriteParam();  
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩  
        iwp.setCompressionQuality(quality); // 设置压缩质量参数  
  
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);  
  
        ColorModel colorModel = ColorModel.getRGBdefault();  
        // 指定压缩时使用的色彩模式  
        iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,  
                colorModel.createCompatibleSampleModel(16, 16)));  
  
        // 开始打包图片，写入byte[]  
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流  
        IIOImage iIamge = new IIOImage(image, null, null);  
        try {  
            // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput  
            // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput  
            writer.setOutput(ImageIO  
                    .createImageOutputStream(byteArrayOutputStream));  
            writer.write(null, iIamge, iwp);  
        } catch (IOException e) {  
            System.out.println("write errro");  
            e.printStackTrace();  
        }  
        //System.out.println("jpeg" + quality + "质量完成打包-----" + getCurrentTime()  
        //        + "----lenth----" + byteArrayOutputStream.toByteArray().length);  
        return byteArrayOutputStream.toByteArray();  
    }  
}  