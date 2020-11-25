package org.lushen.mrh.mybatis.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * mybatis 配置
 * 
 * @author hlm
 */
public class MybatisConfig {

	private static final Log log = LogFactory.getLog(MybatisConfig.class.getSimpleName());

	private static final String driverClassPathLibs = "classpath:drivers/";

	private static final String mybatisClassPathProp = "classpath:mybatis.properties";

	private static final String driverClass = "jdbc.driverClass";

	private static final String url = "jdbc.url";

	private static final String user = "jdbc.user";

	private static final String password = "jdbc.password";

	/**
	 * 读取驱动列表
	 * 
	 * @return
	 */
	public static final List<String> findDriverPaths() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		try {
			List<String> paths = Arrays.stream(resourceLoader.getResource(driverClassPathLibs).getFile().listFiles()).map(File::getPath).collect(Collectors.toList());
			log.info("读取驱动列表:" + paths);
			return paths;
		} catch (IOException e) {
			log.info("读取驱动列表错误:" + e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * 读取连接数据库 driver class
	 * 
	 * @return
	 */
	public static final String findJdbcDriverClass() {
		return findProperty(driverClass);
	}

	/**
	 * 读取连接数据库 url
	 * 
	 * @return
	 */
	public static final String findJdbcUrl() {
		return findProperty(url);
	}

	/**
	 * 读取连接数据库 user
	 * 
	 * @return
	 */
	public static final String findJdbcUser() {
		return findProperty(user);
	}

	/**
	 * 读取连接数据库 password
	 * 
	 * @return
	 */
	public static final String findJdbcPassword() {
		return findProperty(password);
	}

	private static final String findProperty(String name) {
		try {
			InputStream inputStream = new DefaultResourceLoader().getResource(mybatisClassPathProp).getInputStream();
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
			return properties.getProperty(name);
		} catch (IOException e) {
			log.info("读取配置文件错误:" + e.getMessage(), e);
			return null;
		}
	}

}
