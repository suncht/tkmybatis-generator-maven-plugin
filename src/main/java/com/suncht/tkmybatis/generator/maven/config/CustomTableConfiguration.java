package com.suncht.tkmybatis.generator.maven.config;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;

/**
 * @author sunchangtan
 */
@Getter
@Setter
public class CustomTableConfiguration extends TableConfiguration {
	private String packageName;

	public CustomTableConfiguration(Context context) {
		super(context);
	}
}
