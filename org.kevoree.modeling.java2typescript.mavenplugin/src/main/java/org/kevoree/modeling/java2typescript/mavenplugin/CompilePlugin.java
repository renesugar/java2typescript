package org.kevoree.modeling.java2typescript.mavenplugin;

import com.siliconmint.ts.SourceTranslator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
     * The maven project.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        source.mkdirs();
        target.mkdirs();
        SourceTranslator sourceTranslator = new SourceTranslator();
        sourceTranslator.translateSources(source.getPath(), target.getPath());
        unzip(this.getClass().getClassLoader().getResourceAsStream("all.zip"), target);
    }

    private static final int BUFFER = 1024;

    public void unzip(InputStream in_zip, File outFolder) {
        try {
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(in_zip));
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                File outTarget = new File(outFolder.getAbsolutePath() + File.separator + entry.getName());
                if(entry.isDirectory()){
                    outTarget.mkdirs();
                } else {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outTarget), BUFFER);
                    while ((count = in.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to extract : " + e.getMessage());
            e.printStackTrace();
        }
    }

}