package app.document.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public interface ITemplate {
	/**
	 * 根据属性初始化
	 * 
	 * @param prop
	 */
	void init(Properties prop);

	/**
	 * 将数据和模板合并成最终的文档
	 * 
	 * @param context 模板中需要的数据
	 * @param writer 输出的writer
	 * @throws IOException 
	 */
	void merge(Object data, OutputStream stream);
}
