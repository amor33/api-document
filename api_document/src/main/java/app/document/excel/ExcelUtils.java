package app.document.excel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Lazy(false)
@Component
public class ExcelUtils implements ResourceLoaderAware {
	/**
	 * excel文件的导出下载
	 * 
	 * @param templateLoacation excel模板文件位置，格式参见org.springframework.core.io.ResourceLoader#getResource方法注释
	 * @param data excel模板需要的数据
	 * @param filename 给浏览器端下载的文件名
	 * @param resp
	 * @see org.springframework.core.io.ResourceLoader#getResource(String)
	 * @see com.eling.elcms.component.excel.Metals
	 */
	public static void mergeForDownload(String templateLoacation, Object data, String filename,
			HttpServletResponse resp) {
		mergeForDownload(templateLoacation, data, filename, null, resp);
	}

	/**
	 * excel文件的导出下载
	 * 
	 * @param templateLoacation excel模板文件位置，格式参见org.springframework.core.io.ResourceLoader#getResource方法注释
	 * @param data excel模板需要的数据
	 * @param filename 给浏览器端下载的文件名
	 * @param resp
	 * @see org.springframework.core.io.ResourceLoader#getResource(String)
	 * @see com.eling.elcms.component.excel.Metals
	 */
	public static void mergeForDownload(String templateLoacation, Object data, String filename, Properties prop,
			HttpServletResponse resp) {
		Resource template = resourceLoader.getResource(templateLoacation);
		resp.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
		try {
			resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		try {
			Metals.merge(data, template.getInputStream(), prop, resp.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}




	private static ResourceLoader resourceLoader;
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		setResourceLoader0(resourceLoader);
	}

	private static void setResourceLoader0(ResourceLoader resourceLoader) {
		ExcelUtils.resourceLoader = resourceLoader;
	}

	ExcelUtils(){}
}

