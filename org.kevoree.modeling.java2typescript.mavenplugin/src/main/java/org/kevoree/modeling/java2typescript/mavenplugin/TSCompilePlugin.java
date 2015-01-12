package org.kevoree.modeling.java2typescript.mavenplugin;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by duke on 12/01/15.
 */
@Mojo(name = "tsc", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class TSCompilePlugin extends AbstractMojo {

    /**
     * The maven project.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}/classes")
    protected File outputClasses;

    @Parameter(defaultValue = "${project.basedir}/src/main/ts")
    protected File inputTS;

    @Parameter(defaultValue = "${project.basedir}/src/main/js")
    protected File inputJS;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/ts")
    protected File outputTempJS;

    @Parameter
    private File[] libraries;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!outputClasses.exists()) {
            outputClasses.mkdirs();
        }
        if (!outputTempJS.exists()) {
            outputTempJS.mkdirs();
        }
        if (!outputTempJS.exists()) {
            outputTempJS.mkdirs();
        }
        try {
            if (inputTS.exists()) {
                FileUtils.copyDirectory(inputTS, outputTempJS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inputJS.exists()) {
                FileUtils.copyDirectory(inputJS, outputTempJS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Artifact artifact : project.getArtifacts()) {
            File target = artifact.getFile();
            if (target.exists() && target.getName().endsWith(".jar")) {
                unzipArchive(target, outputTempJS);
            }
        }
        try {
            TSCRunner.run(outputTempJS, outputClasses, libraries, true);
        } catch (Exception e) {
            throw new MojoExecutionException("TypeScript compilation failed !", e);
        }
    }

    public void unzipArchive(File archive, File outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (entry.getName().endsWith(".js") || entry.getName().endsWith(".ts")) {
                    unzipEntry(zipfile, entry, outputDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {
        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private void createDir(File dir) {
        if (!dir.mkdirs()) throw new RuntimeException("Can not create dir " + dir);
    }

}
