package org.lushen.mrh.mybatis.generator;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * mybatis table 属性
 * 
 * @author hlm
 */
public class MybatisTable {

	/**
	 * domain名称小写
	 * 
	 * @param tableName
	 * @return
	 */
	public static final MybatisTable lowerCase(String tableName) {
		return new MybatisTable(tableName, StringUtils.lowerCase(tableName));
	}

	/**
	 * domain名称大写
	 * 
	 * @param tableName
	 * @return
	 */
	public static final MybatisTable upperCase(String tableName) {
		return new MybatisTable(tableName, StringUtils.upperCase(tableName));
	}

	/**
	 * domain名称驼峰
	 * 
	 * @param tableName
	 * @return
	 */
	public static final MybatisTable camelCase(String tableName) {
		return new MybatisTable(tableName, Arrays.stream(StringUtils.split(tableName, "_")).filter(StringUtils::isNotBlank).map(e -> e.toCharArray()).map(chars -> {
			for(int index=0; index<chars.length; index++) {
				if(index == 0) {
					chars[0] = Character.toUpperCase(chars[0]);
				} else {
					chars[index] = Character.toLowerCase(chars[index]);
				}
			}
			return chars;
		}).map(String::new).collect(Collectors.joining()));
	}

	private String tableName;

	private String domainName;

	private MybatisTable(String tableName, String domainName) {
		super();
		this.tableName = tableName;
		this.domainName = domainName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDomainName() {
		return domainName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[tableName=");
		builder.append(tableName);
		builder.append(", domainName=");
		builder.append(domainName);
		builder.append("]");
		return builder.toString();
	}

}
