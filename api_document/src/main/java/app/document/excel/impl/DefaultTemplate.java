package app.document.excel.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import app.document.excel.ITemplate;
import app.document.excel.Metals;
import app.document.excel.utils.PoiUtils;
import app.document.excel.utils.PropertyUtils;


public class DefaultTemplate implements ITemplate {
	private static Pattern p = Pattern.compile("\\{\\{(.*?)\\}\\}");

	private Properties props = new Properties();

	private Workbook templete;

	public DefaultTemplate(String templeteFilename) {
		try {
			templete = new XSSFWorkbook(FileUtils.openInputStream(new File(templeteFilename)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public DefaultTemplate(InputStream template) {
		try {
			templete = new XSSFWorkbook(template);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void init(Properties prop) {
		if (null != prop) {
			this.props.putAll(prop);
		}
	}

	@Override
	public void merge(Object data, OutputStream stream) {
		for (int s = 0; s < templete.getNumberOfSheets(); s++) {
			Sheet sheet = templete.getSheetAt(s);
			for (int rownum = 0; rownum <= sheet.getLastRowNum(); rownum++) {
				Row row = sheet.getRow(rownum);

				// 找到以{{#开头的行
				String firstValue = null == row.getCell(0) ? "" : row.getCell(0).getStringCellValue();
				if (StringUtils.isNotBlank(firstValue) && firstValue.startsWith("{{#")) {
					String command = getCommand(firstValue);
					String parameter = getParameter(firstValue);
					int endRownum = rownum;
					for (; endRownum <= sheet.getLastRowNum(); endRownum++) {
						Row findRow = sheet.getRow(endRownum);
						if (findRow != null) {
							Cell findCell = findRow.getCell(0);
							if (null != findCell) {
								String subFirstValue = findCell.getStringCellValue();
								// 找到命令的结束行
								if (StringUtils.isNotBlank(subFirstValue) && subFirstValue.startsWith("{{/" + command)) {
									break;
								}
							}
						}
					}
					if (endRownum > sheet.getLastRowNum()) {
						throw new RuntimeException("模板配置错误，没有找到结束标签：" + command);
					}
					// 删除命令的Row
					PoiUtils.removeRow(sheet, endRownum);
					PoiUtils.removeRow(sheet, rownum);
					// 执行命令并重新获取行标
					rownum = executeCommand(command, parameter, rownum, endRownum - 2, data, sheet);
				} else {
					rownum = executeCommand("default", null, rownum, rownum, data, sheet);
				}
			}
		}
		try {
			templete.write(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private String getCommand(String value) {
		if (value.indexOf(" ") == -1) {
			return value.substring(3, value.length() - 2);
		} else {
			return value.substring(3, value.indexOf(" "));
		}
	}

	private String getParameter(String value) {
		return value.substring(value.indexOf(" ") + 1, value.length() - 2);
	}

	private int executeCommand(String command, String parameter, int startRow, int endRow, Object data, Sheet sheet) {
		switch (command) {
		case "default":
			return commandDefaultRow(sheet.getRow(endRow), data);
		case "each":
			return commandEach(parameter, startRow, endRow, data, sheet);
		default:
			throw new RuntimeException("不支持的命令：" + command);
		}
	}

	private int executeRowCommand(String command, String parameter, int colnum, int endColnum, Object data, Row row) {
		switch(command) {
		case "default":
			return commandDefaultCell(row.getCell(colnum), data);
		case "each":
			return commandEachCell(parameter, colnum, endColnum, data, row);
		default:
			throw new RuntimeException("不支持的命令：" + command);
		}
	}

	private int commandDefaultRow(Row row, Object data) {
		row.getFirstCellNum();
		row.getLastCellNum();
		for (int colnum = row.getFirstCellNum(); colnum < row.getLastCellNum(); colnum++) {
			Cell cell = row.getCell(colnum);
			if (null == cell) {
				continue;
			}

			String cellValue = cell.getStringCellValue();
			if (StringUtils.isNotBlank(cellValue) && cellValue.startsWith("{{#")) {
				String command = getCommand(cellValue);
				String parameter = getParameter(cellValue);
				int endColnum = colnum;
				for (; endColnum < row.getLastCellNum(); endColnum++) {
					Cell endCell = row.getCell(endColnum);
					if (null == endCell) {
						continue;
					}
					String endVaule = endCell.getStringCellValue();
					if (StringUtils.isNotBlank(endVaule) && endVaule.startsWith("{{/" + command)) {
						break;
					}
				}
				if (endColnum == row.getLastCellNum()) {
					throw new RuntimeException(MessageFormat.format("模板配置错误，没有找到结束标签：{0}@{1}:{2}", command, row.getRowNum(), colnum));
				}
				// 删除命令的cell
				PoiUtils.removeCell(row, endColnum);
				PoiUtils.removeCell(row, colnum);
				colnum = executeRowCommand(command, parameter, colnum, endColnum - 2, data, row);
			} else {
				colnum = executeRowCommand("default", null, colnum, colnum, data, row);
			}
		}
		return row.getRowNum();
	}

	private int commandDefaultCell(Cell cell, Object data) {
		String cellValue = cell.getStringCellValue();
		Matcher matcher = p.matcher(cellValue);
		while (matcher.find()) {
			String key = matcher.group(1);
			String propertyValue = getValueAsString(data, key);
			cellValue = cellValue.replaceFirst("\\{\\{" + key + "\\}\\}", propertyValue);
		}
		cell.setCellValue(cellValue);
		return cell.getColumnIndex();
	}

	private int commandEach(String parameter, int startRow, int endRow, Object data, Sheet sheet) {
		// 可操作的行数
		int height = endRow - startRow + 1;
		List<?> list = toList(PropertyUtils.getProperty(data, parameter));
		// 按照值的数量复制行
		for(int i = 1; i < list.size(); i++) {
			for(int j = 0; j < height; j++) {
				int curRownum = startRow + height * i + j;
				Row newRow = PoiUtils.insertRow(sheet, curRownum);
				PoiUtils.copyRow(sheet.getRow(startRow + j), newRow, true);
			}
		}
		// 转换
		for(int i = 0; i < list.size(); i++) {
			Object value = list.get(i);
			for(int j = 0; j < height; j++) {
				int curRownum = startRow + height * i + j;
				Row curRow = sheet.getRow(curRownum);
				commandDefaultRow(curRow, value);
			}
		}

		return startRow + height * list.size() - 1;
	}

	private int commandEachCell(String parameter, int startCell, int endCell, Object data, Row row) {
		// 可操作的单元格
		int width = endCell - startCell + 1;
		List<?> list = toList(PropertyUtils.getProperty(data, parameter));
		if (list.isEmpty()) {
			PoiUtils.removeCells(row, startCell, endCell);
			return startCell - 1;
		}
		// 按照值的数量复制单元格
		PoiUtils.insertCell(row, endCell + 1, width * (list.size() - 1));
		for(int i = 1; i < list.size(); i++) {
			for(int j = 0; j < width; j++) {
				int curCell = startCell + width * i + j;
				PoiUtils.copyCell(row.getCell(startCell + j), row.getCell(curCell), true);
			}
		}
		// 转换
		for(int i = 0; i < list.size(); i++) {
			Object value = list.get(i);
			for(int j = 0; j < width; j++) {
				int curCell = startCell + width * i + j;
				commandDefaultCell(row.getCell(curCell), value);
			}
		}

		return startCell + width * list.size() - 1;
	}

	private String getValueAsString(Object bean, String prop) {
		Object value = PropertyUtils.getProperty(bean, prop);
		if (null == value) {
			return "";
		}
		if (value instanceof Date) {
			String fmt = props.getProperty(Metals.DEFAULT_DATE_FORMAT);
			if (StringUtils.isBlank(fmt)) {
				fmt = "yyyy-MM-dd";
			}
			return DateFormatUtils.format((Date)value, fmt);
		}
		return value.toString();
	}

	private static List<Object> toList(Object obj) {
		List<Object> list = new ArrayList<>();
		if (null == obj) {
			return list;
		}

		if (obj instanceof Collection<?>) {
//			if (obj instanceof PersistentCollection && !((PersistentCollection)obj).wasInitialized()) {
//				return list;
//			}
			for (Object subObj : (Collection<?>)obj) {
				list.addAll(toList(subObj));
			}
		} else if (obj.getClass().isArray()) {
			for (Object subObj : (Object[])obj) {
				list.addAll(toList(subObj));
			}
		} else {
			list.add(obj);
		}
		return list;
	}

}
