package org.kevoree.modeling.java2typescript;

import com.intellij.codeInsight.ContainerProvider;
import com.intellij.codeInsight.runner.JavaMainMethodProvider;
import com.intellij.core.*;
import com.intellij.lang.java.JavaParserDefinition;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.roots.PackageIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleSettingsFacade;
import com.intellij.psi.compiled.ClassFileDecompilers;
import com.intellij.psi.impl.*;
import com.intellij.psi.impl.compiled.ClsCustomNavigationPolicy;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.impl.source.resolve.JavaResolveCache;
import com.intellij.psi.impl.source.resolve.PsiResolveHelperImpl;
import com.intellij.psi.util.JavaClassSupers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.KotlinFileType;
import org.jetbrains.kotlin.load.kotlin.KotlinBinaryClassCache;
import org.jetbrains.kotlin.parsing.KotlinParserDefinition;

import java.io.File;

public class JavaAnalyzer {

    private JavaCoreProjectEnvironment environment;

    public JavaAnalyzer() {

        /*
        JavaCoreApplicationEnvironment applicationEnvironment = createJavaCoreApplicationEnvironment(new Disposable() {
            @Override
            public void dispose() {

            }
        });
        environment = new JavaCoreProjectEnvironment(new Disposable() {
            @Override
            public void dispose() {

            }
        }, applicationEnvironment) {
            @Override
            protected void preregisterServices() {
                registerProjectExtensionPoints(Extensions.getArea(getProject()));
            }
        };*/


        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);


        environment = new JavaCoreProjectEnvironment(() -> {
        }, new JavaCoreApplicationEnvironment(() -> {



        }){



        }){
            @Override
            protected void preregisterServices() {
                registerProjectExtensionPoints(Extensions.getArea(getProject()));

            }
        };

        environment.getEnvironment().registerApplicationService(JavaClassSupers.class, new JavaClassSupersImpl());


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

        javaApplicationEnvironment.registerParserDefinition(new JavaParserDefinition());
        javaApplicationEnvironment.registerApplicationService(JavaClassSupers.class, new JavaClassSupersImpl());

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
