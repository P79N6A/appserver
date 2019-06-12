package com.idcq.appserver.utils.excel;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 解析单sheet的简单excel
 * 
 * @author Administrator
 * 
 */
public class ExcelParser {
	private static final Logger log = LoggerFactory
			.getLogger(ExcelParser.class);

	public static final Integer EXCEL_TITLE_ROW = 0;

	public static final Integer UP_2007 = 2007;
	
	public static final Integer UP_2003 = 2003;


	/**
	 * 默认2007及以上
	 * 
	 * @param in
	 * @param type
	 * @return
	 */
	public static Map<Integer, List<String>> parseExcelXlsx(InputStream in)
			throws Exception {
		return parseExcel(in, UP_2007);
	}

	   /**
     * 默认2003及以上
     * 
     * @param in
     * @param type
     * @return
     */
    public static Map<Integer, List<String>> parseExcelXls(InputStream in)
            throws Exception {
        return parseExcel(in, UP_2003);
    }

	/**
	 * 根据参数选择2007以上或者说以下版本
	 * 
	 * @param in
	 * @param type
	 * @return
	 */
	public static Map<Integer, List<String>> parseExcel(InputStream in,
			Integer type) throws Exception {
		if (type >= UP_2007) {
			return xlsxParse(in);
		} else {
			return xlsParse(in);
		}
	}

	public static Map<Integer, List<String>> xlsxParse(InputStream in)
			throws Exception {
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		XSSFWorkbook xwb = null;
		try {
			xwb = new XSSFWorkbook(in);
			XSSFSheet se = xwb.getSheetAt(0);
			List<String> rowResult = null;
			XSSFRow row = null;
			// StringBuilder str = null;
//			String cellRawValue = null;
			XSSFCell cell = null;
			String cellValue = null;
			//特别注意column的计算方法有问题
			int columnLength = 0;
			for (int i = 0; i < se.getLastRowNum() + 1; i++) {
				row = se.getRow(i);
				// str = new StringBuilder();
				rowResult = new ArrayList<String>();
				if(columnLength == 0){
					columnLength = row.getLastCellNum();
				}
				int count = 0;
				for (int j = 0; j < columnLength; j++) {
					cell = row.getCell(j);
					if (cell != null) {
						cellValue = proccessRawCellValue(getCell(cell));
						if(cellValue == null || cellValue.trim().length() == 0){
							count++;
						}
					} else {
						cellValue = null;
						count++;
					}
					rowResult.add(cellValue);
				}
				if(count < columnLength){
					result.put(i, rowResult);
				}
				// System.out.println(str.toString());
			}

		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			try {
				xwb.close();
				if (in != null) {
					in.close();
				}
			} catch (Exception ignorable) {
			    log.error(ignorable.getMessage(), ignorable);
			}
		}
		return result;
	}
	
	private static String getCell(Cell cell) {
		DecimalFormat df = new DecimalFormat("#.##");
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}
			return df.format(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
//			System.out.println(cell.getStringCellValue());
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case HSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return null;
	}
	
	public static Map<Integer, List<String>> getExcelData(InputStream in) throws Exception{
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		if(!in.markSupported()) {
			in = new PushbackInputStream(in, 8);
		}

		Workbook wb = null;
		if(POIFSFileSystem.hasPOIFSHeader(in)) {
			wb = new HSSFWorkbook(in);
		}else if (POIXMLDocument.hasOOXMLHeader(in)) {
			wb = new XSSFWorkbook(in);
		}else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"无法识别Excel版本");
		}
        
        try {
            Sheet se = wb.getSheetAt(0);
            List<String> rowResult = null;
            Row row = null;
            Cell cell = null;
            String cellValue = null;
            int columnLength = 0;
            for (int i = 0; i < se.getLastRowNum() + 1; i++) {
                row = se.getRow(i);
                if (row == null) {
                	continue;
                }
                rowResult = new ArrayList<String>();
                if(columnLength == 0){
                    columnLength = row.getLastCellNum();
                }
                int count = 0;
                for (int j = 0; j < columnLength; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        cellValue = proccessRawCellValue(getCell(cell));
                        if(cellValue == null || cellValue.trim().length() == 0){
                            count++;
                        }
                    } else {
                        cellValue = null;
                        count++;
                    }
                    rowResult.add(cellValue);
                }
                if(count < columnLength){
                    result.put(i, rowResult);
                }
            }

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            try {
                wb.close();
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignorable) {
                log.error(ignorable.getMessage(), ignorable);
            }
        }
        return result;
	}
	/**
	 * !!!!!!!!!!!unimplemented
	 * 
	 * @param in
	 * @return
	 */
	public static Map<Integer, List<String>> xlsParse(InputStream in) {
	    Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
	    HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in); 
            HSSFSheet se = wb.getSheetAt(0);
            List<String> rowResult = null;
            HSSFRow row = null;
            // StringBuilder str = null;
//          String cellRawValue = null;
            HSSFCell cell = null;
            String cellValue = null;
            //特别注意column的计算方法有问题
            int columnLength = 0;
            for (int i = 0; i < se.getLastRowNum() + 1; i++) {
                row = se.getRow(i);
                // str = new StringBuilder();
                rowResult = new ArrayList<String>();
                if(columnLength == 0){
                    columnLength = row.getLastCellNum();
                }
                int count = 0;
                for (int j = 0; j < columnLength; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        cellValue = proccessRawCellValue(getCell(cell));
                        if(cellValue == null || cellValue.trim().length() == 0){
                            count++;
                        }
                    } else {
                        cellValue = null;
                        count++;
                    }
                    rowResult.add(cellValue);
                }
                if(count < columnLength){
                    result.put(i, rowResult);
                }
                // System.out.println(str.toString());
            }

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            try {
                wb.close();
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignorable) {
                log.error(ignorable.getMessage(), ignorable);
            }
        }
        return result;
	}

	public static List<String> getTitleRow(
			Map<Integer, List<String>> parseResult) {
		return parseResult.get(EXCEL_TITLE_ROW);
	}

	public static String proccessRawCellValue(String rawCellValue) {
		// TODO
		return rawCellValue;
	}
}
