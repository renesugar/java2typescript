package org.kevoree.modeling.java2typescript;

import com.intellij.codeInsight.ContainerProvider;
import com.intellij.codeInsight.runner.JavaMainMethodProvider;
import com.intellij.core.*;
import com.intellij.lang.java.JavaParserDefinition;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.compiled.ClassFileDecompilers;
import com.intellij.psi.impl.*;
import com.intellij.psi.impl.compiled.ClsCustomNavigationPolicy;
import com.intellij.psi.impl.compiled.ClsStubBuilderFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JavaAnalyzer {

    private JavaCoreProjectEnvironment environment;

    public JavaAnalyzer() {

        Disposable parentDisposable = ()->{};

        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);


        JavaCoreApplicationEnvironment javaApplicationEnvironment = new JavaCoreApplicationEnvironment(parentDisposable);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        javaApplicationEnvironment.registerParserDefinition(new JavaParserDefinition());
        //javaApplicationEnvironment.registerApplicationService(JavaClassSupers.class, new JavaClassSupersImpl());

        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClsStubBuilderFactory.EP_NAME, ClsStubBuilderFactory.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), JavaMainMethodProvider.EP_NAME, JavaMainMethodProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClsCustomNavigationPolicy.EP_NAME, ClsCustomNavigationPolicy.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClassFileDecompilers.EP_NAME, ClassFileDecompilers.Decompiler.class);



        environment = new JavaCoreProjectEnvironment(parentDisposable, javaApplicationEnvironment){
            @Override
            protected void preregisterServices() {
                registerProjectExtensionPoint(PsiElementFinder.EP_NAME, PsiElementFinder.class);
                CoreApplicationEnvironment.registerExtensionPoint(Extensions.getArea(getProject()), PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
            }
        };


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

    private JavaCoreApplicationEnvironment createJavaCoreApplicationEnvironment(@NotNull Disposable disposable) {
        //Extensions.cleanRootArea(disposable);
       // registerAppExtensionPoints();
        JavaCoreApplicationEnvironment javaApplicationEnvironment = new JavaCoreApplicationEnvironment(disposable);

        // ability to get text from annotations xml files
        //  javaApplicationEnvironment.registerFileType(PlainTextFileType.INSTANCE, "xml");
        // javaApplicationEnvironment.registerFileType(KotlinFileType.INSTANCE, "kt");
        //  javaApplicationEnvironment.registerFileType(KotlinFileType.INSTANCE, "jet");
        // javaApplicationEnvironment.registerFileType(KotlinFileType.INSTANCE, "ktm");

        //  javaApplicationEnvironment.registerParserDefinition(new KotlinParserDefinition());
        //  javaApplicationEnvironment.getApplication().registerService(KotlinBinaryClassCache.class, new KotlinBinaryClassCache());


        //javaApplicationEnvironment.registerParserDefinition(new JavaParserDefinition());
        //javaApplicationEnvironment.registerApplicationService(JavaClassSupers.class, new JavaClassSupersImpl());

        return javaApplicationEnvironment;
    }

    /*
    private static void registerAppExtensionPoints() {
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);
        //CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClsCustomNavigationPolicy.EP_NAME, ClsCustomNavigationPolicy.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClassFileDecompilers.EP_NAME,
                ClassFileDecompilers.Decompiler.class);

        // For j2k converter
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        //CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), JavaMainMethodProvider.EP_NAME, JavaMainMethodProvider.class);
    }*/

    private static void registerProjectExtensionPoints(ExtensionsArea area) {
        CoreApplicationEnvironment.registerExtensionPoint(area, PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
        CoreApplicationEnvironment.registerExtensionPoint(area, PsiElementFinder.EP_NAME, PsiElementFinder.class);
    }


}
