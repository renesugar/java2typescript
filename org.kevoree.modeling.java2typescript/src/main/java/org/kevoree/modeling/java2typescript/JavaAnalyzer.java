package org.kevoree.modeling.java2typescript;

import com.intellij.core.JavaCoreApplicationEnvironment;
import com.intellij.core.JavaCoreProjectEnvironment;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by duke on 11/6/14.
 */
public class JavaAnalyzer {

    private static final String baseDir = "/Users/duke/Documents/dev/dukeboard/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";

    public static void main(String[] args) {
        JavaAnalyzer analyzer = new JavaAnalyzer();
        analyzer.analyze(new File(baseDir));
    }


    public PsiDirectory analyze(File srcDir) {
        JavaCoreProjectEnvironment environment = new JavaCoreProjectEnvironment(new Disposable() {
            @Override
            public void dispose() {
            }
        }, new JavaCoreApplicationEnvironment(new Disposable() {
            @Override
            public void dispose() {
            }
        }));

        environment.addJarToClassPath(new File("/Users/duke/.m2/repository/org/kevoree/modeling/org.kevoree.modeling.microframework/4.0.0-SNAPSHOT/org.kevoree.modeling.microframework-4.0.0-SNAPSHOT.jar"));

        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(environment.getProject());
        VirtualFile root_vf = environment.getEnvironment().getLocalFileSystem().findFileByIoFile(srcDir);
        environment.addSourcesToClasspath(root_vf);
        try {
            Files.walkFileTree(srcDir.toPath(), new FileVisitor<Path>() {
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    VirtualFile vf = environment.getEnvironment().getLocalFileSystem().findFileByIoFile(file.toFile());
                    environment.addSourcesToClasspath(vf);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsiDirectory rootDirectory = PsiManager.getInstance(environment.getProject()).findDirectory(root_vf);
        return rootDirectory;
    }

}
