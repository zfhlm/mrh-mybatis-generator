package org.lushen.mrh.mybatis.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * mybatis 代码生成工具
 * 
 * @author hlm
 */
public class MybatisGenerator {

	private static final Log log = LogFactory.getLog(MybatisGenerator.class.getSimpleName());

	//存放路径：桌面mybatis目录
	public static final String targetProjectPath = new File(FileSystemView.getFileSystemView().getHomeDirectory(), "/mybatis").getPath();

	//model包名
	public static final String modelPackage = "org.lushen.mrh.mybatis.dao.model";

	//mapper包名
	public static final String mapperPackage = "org.lushen.mrh.mybatis.dao.mapper";

	//mapping包名
	public static final String mappingPackage = "org.lushen.mrh.mybatis.dao.mapping";

	//mybatis要生成的表
	public static final List<MybatisTable> tables = Arrays.asList(
		MybatisTable.camelCase("mrh_test")
	);

	static {
		File file = new File(targetProjectPath);
		if(file.exists()) {
			try {
				log.info("清除历史生成文件...");
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		file.mkdirs();
	}

	public static void main(String[] args) throws Exception {

		//驱动配置
		List<String> entrys = new ArrayList<String>();
		entrys.addAll(MybatisConfig.findDriverPaths());

		//注释配置
		log.info("初始化注释配置...");
		CommentGeneratorConfiguration cmtGenCfg = new CommentGeneratorConfiguration();
		cmtGenCfg.addProperty("suppressAllComments", "true");

		//数据库配置
		log.info("初始化数据库连接配置...");
		JDBCConnectionConfiguration jdbcConnCfg = new JDBCConnectionConfiguration();
		jdbcConnCfg.setDriverClass(MybatisConfig.findJdbcDriverClass());
		jdbcConnCfg.setConnectionURL(MybatisConfig.findJdbcUrl());
		jdbcConnCfg.setUserId(MybatisConfig.findJdbcUser());
		jdbcConnCfg.setPassword(MybatisConfig.findJdbcPassword());

		//类型配置
		log.info("初始化Java类型配置...");
		JavaTypeResolverConfiguration javaTypeResCfg = new JavaTypeResolverConfiguration();
		javaTypeResCfg.addProperty("forceBigDecimals", "false");

		//生成实体配置
		log.info("初始化model配置...");
		JavaModelGeneratorConfiguration javaModelGenCfg = new JavaModelGeneratorConfiguration();
		javaModelGenCfg.setTargetPackage(modelPackage);
		javaModelGenCfg.setTargetProject(targetProjectPath);
		javaModelGenCfg.addProperty("enableSubPackages", "true");
		javaModelGenCfg.addProperty("trimStrings", "false");

		//生成映射文件配置
		log.info("初始化mapping配置...");
		SqlMapGeneratorConfiguration sqlMapGenCfg = new SqlMapGeneratorConfiguration();
		sqlMapGenCfg.setTargetPackage(mappingPackage);
		sqlMapGenCfg.setTargetProject(targetProjectPath);
		sqlMapGenCfg.addProperty("enableSubPackages", "true");

		//生成接口配置
		log.info("初始化mapper配置...");
		JavaClientGeneratorConfiguration javaClientGenCfg = new JavaClientGeneratorConfiguration();
		javaClientGenCfg.setConfigurationType("XMLMAPPER");
		javaClientGenCfg.setTargetPackage(mapperPackage);
		javaClientGenCfg.setTargetProject(targetProjectPath);
		javaClientGenCfg.addProperty("enableSubPackages", "true");

		//上下文配置
		log.info("初始化上下文配置...");
		Context context = new Context(null);
		context.setId("MyBatis3");
		context.setTargetRuntime("MyBatis3");
		context.setCommentGeneratorConfiguration(cmtGenCfg);
		context.setJdbcConnectionConfiguration(jdbcConnCfg);
		context.setJavaTypeResolverConfiguration(javaTypeResCfg);
		context.setJavaModelGeneratorConfiguration(javaModelGenCfg);
		context.setSqlMapGeneratorConfiguration(sqlMapGenCfg);
		context.setJavaClientGeneratorConfiguration(javaClientGenCfg);

		//指定生成表
		for(MybatisTable mybatisTable : tables) {
			log.info("加载指定生成表:"+mybatisTable);
			TableConfiguration tableCfg = new TableConfiguration(context);
			tableCfg.setTableName(mybatisTable.getTableName());
			tableCfg.setDomainObjectName(mybatisTable.getDomainName());
			tableCfg.setCountByExampleStatementEnabled(false);
			tableCfg.setDeleteByExampleStatementEnabled(false);
			tableCfg.setSelectByExampleStatementEnabled(false);
			tableCfg.setUpdateByExampleStatementEnabled(false);
			tableCfg.addProperty("useActualColumnNames", "false");
			context.addTableConfiguration(tableCfg);
		}

		//生成文件
		log.info("开始生成mybatis代码...");
		Configuration configuration = new Configuration();
		entrys.forEach(entry -> configuration.addClasspathEntry(entry));
		configuration.addContext(context);
		new MyBatisGenerator(configuration, new DefaultShellCallback(true), null).generate(null);
		log.info("完成生成mybatis代码...");
	}

}
