/**
 * Copyright 2017 The Java2TypeScript Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java2typescript.mavenplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import java2typescript.FlatJUnitGenerator;
import java2typescript.SourceTranslator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mojo(name = "java2ts", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class Java2TSPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/src/main/java")
    protected File source;

    @Parameter()
    protected List<File> sources = new ArrayList<>();

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/java2ts")
    protected File target;

    @Parameter(defaultValue = "${project.artifactId}")
    private String name;

    @Parameter(defaultValue = "${project.artifactId}")
    private String packageName;

    @Parameter(defaultValue = "${project.version}")
    private String packageVersion;

    @Parameter
    private List<Dependency> dependencies = new ArrayList<Dependency>();

    @Parameter
    private List<String> moduleImports = new ArrayList<String>();

    @Parameter
    private Map<String, String> pkgTransforms = new HashMap<String, String>();

    @Parameter(defaultValue = "false")
    private boolean copyJRE;

    @Parameter(defaultValue = "false")
    private boolean copyJunit;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<String> sources = new ArrayList<>();
        if (sources.isEmpty()) {
            sources.add(source.getAbsolutePath());
        } else {
            sources.addAll(this.sources.stream().map(File::getAbsolutePath).collect(Collectors.toList()));
        }

        SourceTranslator sourceTranslator = new SourceTranslator(sources, target.getPath(), name);
        moduleImports.forEach(sourceTranslator::addModuleImport);
        if (copyJRE) {
            sourceTranslator.addModuleImport("./jre.ts");
        }
        if (copyJunit) {
            sourceTranslator.addModuleImport("./junit.ts");
        }
        pkgTransforms.forEach(sourceTranslator::addPackageTransform);

        String outDir = project.getBuild().getOutputDirectory();
        sourceTranslator.addToClasspath(outDir);
        getLog().info(outDir + " added to Java2TS analyzer");

        for (Artifact a : project.getDependencyArtifacts()) {
            File file = a.getFile();
            if (file != null) {
                if (file.isFile()) {
                    sourceTranslator.addToClasspath(file.getAbsolutePath());
                    getLog().info(file.getAbsolutePath() + " added to Java2TS analyzer");
                }
            }
        }
        for (Artifact a : project.getArtifacts()) {
            File file = a.getFile();
            if (file != null) {
                if (file.isFile()) {
                    sourceTranslator.addToClasspath(file.getAbsolutePath());
                    getLog().info(file.getAbsolutePath() + " added to Java2TS analyzer");
                }
            }
        }
        sourceTranslator.process();
        sourceTranslator.generate();

        if (copyJRE) {
            try {
                Files.copy(getClass().getClassLoader().getResourceAsStream("java.ts"), Paths.get(target.getAbsolutePath(), "jre.ts"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Generate tests
        if (copyJunit) {
            try {
                Files.copy(getClass().getClassLoader().getResourceAsStream("junit.ts"), Paths.get(target.getAbsolutePath(), "junit.ts"), StandardCopyOption.REPLACE_EXISTING);
                FlatJUnitGenerator generator = new FlatJUnitGenerator();
                generator.addModuleImport("./" + name + ".ts");
                generator.generate(source, Paths.get(target.getAbsolutePath()).toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Generate package.json
        try {
            Files.deleteIfExists(target.toPath().resolve("package.json"));
            BufferedWriter br = Files.newBufferedWriter(target.toPath().resolve("package.json"), StandardOpenOption.CREATE);
            br.append("{\n" +
                    "  \"name\": \""+project.getGroupId() + "." + project.getArtifactId()+"\",\n" +
                    "  \"version\": \""+project.getVersion()+"\"\n" +
                    "}");
            br.flush();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}