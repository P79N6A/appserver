package com.idcq.appserver.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.exception.APIBusinessException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 操作Excel表格工具类
 * 
 * @author Administrator
 * 
 */
public class ExcelUtilTool {
	private final static Log logger = LogFactory.getLog(ExcelUtilTool.class);
	private static Map<String, Workbook> workBookMap = new HashMap<String, Workbook>();

	/**
	 * 根据excel路径获取工作区间
	 * 
	 * @param filePath
	 * @return
	 */
	public static Workbook getWorkBook(String filePath) {

		// 校验文件是否为.xls后缀
		if (null == filePath || filePath.trim().length() == 0) {
			throw new APIBusinessException("The input filePath is null.");
		}

		// 校验文件是否存在
		File file = new File(filePath);

		if (!file.exists()) {
			throw new APIBusinessException("The file '" + filePath
					+ "' is not exist.");
		}

		// 缓存中查询
		Workbook workbook = workBookMap.get(filePath);
		if (null != workbook) {
			return workbook;
		}

		try {
			workbook = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			logger.error(e.getMessage(), e);
			throw new APIBusinessException(e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new APIBusinessException(e);
		}

		if (null == workbook) {
			throw new APIBusinessException("Get workBook failed.");
		}
		return workbook;
	}

	/**
	 * 关闭工作区间，释放资源
	 * 
	 * @param workBook
	 */
	public static void closeWorkBook(Workbook workBook) {
		try {
			if (null != workBook) {
				workBook.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取接口定义sheet页签第一页pNo为0
	 */
	public static Sheet getInterfaceSheet(String filePath, int pNO) {
		try {
			Workbook workbook = getWorkBook(filePath);
			return workbook.getSheet(pNO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
