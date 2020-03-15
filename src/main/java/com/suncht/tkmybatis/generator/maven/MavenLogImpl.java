package com.suncht.tkmybatis.generator.maven;

import org.apache.maven.plugin.logging.Log;

public class MavenLogImpl implements org.mybatis.generator.logging.Log {
    private final Log mavenLog;

    MavenLogImpl(Log log) {
        mavenLog = log;
    }

    @Override
    public boolean isDebugEnabled() {
        return mavenLog.isDebugEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        mavenLog.error(s, e);
    }

    @Override
    public void error(String s) {
        mavenLog.error(s);
    }

    @Override
    public void debug(String s) {
        mavenLog.debug(s);
    }

    @Override
    public void warn(String s) {
        mavenLog.warn(s);
    }

}
