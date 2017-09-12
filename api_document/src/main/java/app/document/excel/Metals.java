package app.document.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.ObjectUtils;

import app.document.excel.impl.DefaultTemplate;

/**
 * 后台excel模板框架
 * 
 * @author KEN
 *
 */
public class Metals {
	public static final String DEFAULT_DATE_FORMAT = ObjectUtils.CONST("DEFAULT_DATE_FORMAT");

	public static ITemplate getTemplate(String template) {
		return new DefaultTemplate(template);
	}

	public static void merge(Object data, InputStream template, OutputStream os) {
		merge(data, template, null, os);
	}

	public static void merge(Object data, InputStream template, Properties props, OutputStream os) {
		ITemplate temp = new DefaultTemplate(template);
		temp.init(props);
		temp.merge(data, os);
	}
}
