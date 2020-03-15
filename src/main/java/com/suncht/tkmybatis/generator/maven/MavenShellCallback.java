package com.suncht.tkmybatis.generator.maven;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.messages.Messages;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Shell callback that calculates the Maven output directory.
 * 
 * @author Jeff Butler
 */
public class MavenShellCallback extends DefaultShellCallback {
    private MyBatisGeneratorMojo mybatisGeneratorMojo;

    public MavenShellCallback(MyBatisGeneratorMojo mybatisGeneratorMojo, boolean overwrite) {
        super(overwrite);
        this.mybatisGeneratorMojo = mybatisGeneratorMojo;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage)
            throws ShellException {
        if (!"MAVEN".equals(targetProject)) {
            return super.getDirectory(targetProject, targetPackage);
        }
        
        // targetProject is the output directory from the MyBatis generator
        // Mojo. It will be created if necessary
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes).  The sub directory will be created
        // if it does not already exist
        
        File project = mybatisGeneratorMojo.getOutputDirectory();
        if (!project.exists()) {
            project.mkdirs();
        }
        
        if (!project.isDirectory()) {
            throw new ShellException(Messages.getString("Warning.9", //$NON-NLS-1$
                    project.getAbsolutePath()));
        }
        
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }
        
        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(Messages.getString("Warning.10", //$NON-NLS-1$
                        directory.getAbsolutePath()));
            }
        }
        
        return directory;
    }
}
