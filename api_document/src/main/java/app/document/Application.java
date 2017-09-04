package app.document;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @author WenZT
 * @Date 2017年9月1日
 */
@SpringBootApplication
public class Application {
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("server.port", 10086);
		app.setDefaultProperties(params);
		app.run(args);
	}

	

	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
		dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setInitialSize(2);
		dataSource.setMaxActive(20);
		dataSource.setMinIdle(0);
		dataSource.setMaxWait(60000);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setPoolPreparedStatements(false);
		return dataSource;
	}
}
