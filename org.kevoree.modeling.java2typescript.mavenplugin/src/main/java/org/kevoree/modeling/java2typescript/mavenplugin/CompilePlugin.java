package org.kevoree.modeling.java2typescript.mavenplugin;

import org.kevoree.modeling.java2typescript.SourceTranslator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.*;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class CompilePlugin extends AbstractMojo {

    /**
     * Src file
     */
    @Parameter
    private File source;

    /**
     * Target directory
     */
    @Parameter
    private File target;

    /**
     * Target directory
     */
    @Parameter(defaultValue = "${project.artifactId}.ts")
    private String outputFileName;

    /**
     * The maven project.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        source.mkdirs();
        target.mkdirs();
        SourceTranslator sourceTranslator = new SourceTranslator();
        try {
            sourceTranslator.translateSources(source.getPath(), target.getPath() + File.separator + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }
    }

}