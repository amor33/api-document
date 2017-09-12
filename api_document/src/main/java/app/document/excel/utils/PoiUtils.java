package app.document.excel.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 网上找来的使用poi复制Excel格式类容的工具类，稍作修改。
 * <p>
 * 出处：http://blog.csdn.net/wutbiao/article/details/8696446
 * 
 * @author PlayAround
 * @see http://blog.csdn.net/wutbiao/article/details/8696446
 *
 */
public class PoiUtils {

	/**
	 * Sheet复制
	 * 
	 * @param fromSheet
	 * @param toSheet
	 * @param copyValueFlag
	 */
	public static void copySheet(Workbook wb, Sheet fromSheet, Sheet toSheet, boolean copyValueFlag) {
		// 合并区域处理
		mergerRegion(fromSheet, toSheet);
		for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
			Row tmpRow = rowIt.next();
			Row newRow = toSheet.createRow(tmpRow.getRowNum());
			// 行复制
			copyRow(tmpRow, newRow, copyValueFlag);
		}
	}

	/**
	 * 行复制功能
	 * 
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(Row fromRow, Row toRow, boolean copyValueFlag) {
		for (Cell tmpCell : fromRow) {
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(tmpCell, newCell, copyValueFlag);
		}
	}

	/**
	 * 复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param fromSheet
	 *            原有的sheet
	 * @param toSheet
	 *            新创建sheet
	 */
	public static void mergerRegion(Sheet fromSheet, Sheet toSheet) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(mergedRegionAt);
		}
	}

	/**
	 * 复制单元格
	 * 
	 * @param srcCell
	 * @param distCell
	 * @param copyValue
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(Cell srcCell, Cell distCell, boolean copyValue) {
		// 样式
		distCell.setCellStyle(srcCell.getCellStyle());
		// 评论
		distCell.setCellComment(srcCell.getCellComment());

		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);

		if (copyValue) {
			if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == Cell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}


	/**
	 * Remove a row by its index
	 * 
	 * @param sheet
	 *            a Excel sheet
	 * @param rownum
	 *            a 0 based index of removing row
	 */
	public static void removeRow(Sheet sheet, int rownum) {
		int lastRowNum = sheet.getLastRowNum();
		if (rownum >= 0 && rownum < lastRowNum) {
			sheet.shiftRows(rownum + 1, lastRowNum, -1);
		}
		if (rownum == lastRowNum) {
			Row removingRow = sheet.getRow(rownum);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}

	public static Row insertRow(Sheet sheet, int rownum) {
		int lastRowNum = sheet.getLastRowNum();
		if (rownum >= 0 && rownum <= lastRowNum) {
			sheet.shiftRows(rownum, lastRowNum, 1);
		}
		return sheet.createRow(rownum);
	}

	public static Cell insertCell(Row row, int colnum) {
		return insertCell(row, colnum, 1).get(0);
	}

	public static void removeCell(Row row, int cell) {
		removeCells(row, cell, cell);
	}

	public static void removeCells(Row row, int start, int end) {
		int n = end - start + 1;
		// 将后面的cell前移
		int last = row.getLastCellNum();
		for (int i = 0; i < last - 1 - end; i++) {
			copyCell(row.getCell(end + i + 1), row.getCell(start + i), true);
		}

		// 删除后面的cell
		for (int i = 1; i <= n; i++) {
			row.removeCell(row.getCell(last - i));
		}
	}

	public static List<Cell> insertCell(Row row, int colnum, int number) {
		Validate.isTrue(number >= 0);
		if (number == 0) {
			return Collections.emptyList();
		}
		// 插入位置后面的cell后移
		int lastCellnum = row.getLastCellNum();
		for (int i = 1; i <= lastCellnum-(colnum-1); i++) {
			Cell newCell = row.createCell(lastCellnum - i + number);
			copyCell(row.getCell(lastCellnum - i), newCell, true);
		}

		// 创建新的cell
		List<Cell> cells = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			cells.add(row.createCell(colnum + i));
		}
		return cells;
	}

}
