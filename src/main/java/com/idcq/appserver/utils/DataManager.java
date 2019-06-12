package com.idcq.appserver.utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.biff.EmptyCell;

import org.apache.commons.lang3.StringUtils;

import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.utils.smsutil.HttpSender;

public class DataManager
{
	private static DataManager dataManager = null;
	
	private static String filePath = "D:\\shopdetail.xls";
	
	private static String url = "http://222.73.117.158/msg/";// 应用地址
	private static String account = "sjyinhew";// 账号
	private static String pswd = "Sjyinhew123";// 密码
	private static String content = "您好！下周提现从2月2日提前到2月1日，请知悉。";

	private DataManager() throws Exception 
	{
		initData();
	}

	/**
	 * 初始化数据
	 * @throws Exception 
	 */
	private void initData() throws Exception 
	{
		// 读取excel的第一个页签
		Sheet interfaceSheet = ExcelUtilTool.getInterfaceSheet(filePath,0);
		getInterfaceData(interfaceSheet);
	}

	private void getInterfaceData(Sheet interfaceSheet)
			throws Exception
	{
		// 计算一共有多少行
		int length = getLength(interfaceSheet);

		if (length > 6000)
		{
			// 不能为空，加载失败
			throw new APIBusinessException("接口数超过系统最大支持数：" + 6000);
		}
		// 读取数据，第一行为标题，第二行为注解，从第三行开始读取,接口页签 0-4为有效数据
		for (int i = 1; i < length; i++)
		{
			// 第2列的第i行数据为编号
			String id = interfaceSheet.getCell(0, i).getContents();

			// 不能为空，加载失败
			if (StringUtils.isBlank(id))
			{
				throw new APIBusinessException("读取编号时，发现第" + i + "行编号为空");
			}
			System.out.println(id);
			String returnString = HttpSender.batchSend(url, account, pswd, id, content, true, null, null);
			System.out.println(returnString);
//
//			// 第4列的第i行数据为接口名称
//			String name = interfaceSheet.getCell(3, i).getContents();
//
//			// 不能为空，加载失败
//			if (StringUtils.isBlank(name))
//			{
//				throw new APIBusinessException("读取编号时，发现第" + i + "行接口名称为空");
//			}
//
//			// 第7列的第i行数据为请求类型，默认为HTTP
//			String reqType = interfaceSheet.getCell(4, i).getContents();
//
//
//			// 第15列的第i行数据为请求URL地址
//			String reqUrl = interfaceSheet.getCell(6, i).getContents();
//
//			// 不能为空，加载失败
//			if (StringUtils.isBlank(reqUrl))
//			{
//				throw new APIBusinessException("读取编号时，发现第" + i + "行请求路径为空");
//			}
//
//			// 第16列的第i行数据为post请求数据
//			String dataStr = interfaceSheet.getCell(7, i)
//					.getContents();
//			String content;
//			if (reqType.contains("POST")) {
//				
//				//代表Post方式
//				 content  = APIHttpClient.post(reqUrl, dataStr);
//			}
//			else {
//				 content = APIHttpClient.get(reqUrl);
//			}
//			System.out.println(reqUrl);
//			System.out.println(content);
			
			
		}
	}

	private int getLength(Sheet interfaceSheet)
	{
		Cell[] cell = interfaceSheet.getColumn(0);

		int length = 0;

		// 出现空行就不在读取
		for (int i = 0; i < cell.length; i++)
		{
			if (cell[i] instanceof EmptyCell)
			{
				break;
			}
			length++;
		}
		return length;
	}
	public static void main(String[] args) throws Exception
	{
		DataManager.getInstance().initData();;
	}
	
	public static void reset()
	{
	    dataManager = null;
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static DataManager getInstance() throws Exception
	{
		if (null == dataManager)
		{
			dataManager = new DataManager();
		}
		return dataManager;
	}
	
//	    // 写文件
//		private void writerTxt(String content, File file) {
//			BufferedWriter fw = null;
//			try {
//				File file = new File("D://text.txt");
//				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
//				fw.append(content);
//				fw.newLine();
//				fw.append("我又写入的内容");
//				fw.flush(); // 全部写入缓存中的内容
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (fw != null) {
//					try {
//						fw.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}

}