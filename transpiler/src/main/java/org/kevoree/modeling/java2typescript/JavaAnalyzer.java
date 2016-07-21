package org.kevoree.modeling.java2typescript;

import com.intellij.codeInsight.ContainerProvider;
import com.intellij.core.*;
import com.intellij.lang.java.JavaParserDefinition;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.*;

import java.io.File;

public class JavaAnalyzer {

    private JavaCoreProjectEnvironment environment;

    public JavaAnalyzer() {

        Disposable parentDisposable = ()->{};

        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);

        JavaCoreApplicationEnvironment javaApplicationEnvironment = new JavaCoreApplicationEnvironment(parentDisposable);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
        javaApplicationEnvironment.registerParserDefinition(new JavaParserDefinition());


        environment = new JavaCoreProjectEnvironment(parentDisposable, javaApplicationEnvironment){
            @Override
            protected void preregisterServices() {
                CoreApplicationEnvironment.registerExtensionPoint(Extensions.getArea(getProject()), PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
            }
        };
        environment.registerProjectExtensionPoint(PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
        environment.registerProjectExtensionPoint(PsiElementFinder.EP_NAME, PsiElementFinder.class);


    }

    public void addClasspath(String filePath) {
        environment.addJarToClassPath(new File(filePath));
    }

    public PsiDirectory analyze(File srcDir) {
        VirtualFile vf = environment.getEnvironment().getLocalFileSystem().findFileByIoFile(srcDir);
        if (vf != null) {
            environment.addSourcesToClasspath(vf);

            return PsiManager.getInstance(environment.getProject()).findDirectory(vf);
        }
        return null;
    }
}
