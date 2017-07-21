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
package java2typescript;

import com.intellij.codeInsight.ContainerProvider;
import com.intellij.codeInsight.runner.JavaMainMethodProvider;
import com.intellij.core.*;
import com.intellij.lang.MetaLanguage;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.fileTypes.FileTypeExtensionPoint;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.augment.TypeAnnotationModifier;
import com.intellij.psi.compiled.ClassFileDecompilers;
import com.intellij.psi.impl.*;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.meta.MetaDataContributor;
import com.intellij.psi.stubs.BinaryFileStubBuilders;
import com.intellij.psi.util.JavaClassSupers;

import java.io.File;

public class JavaAnalyzer {

    //private JavaCoreProjectEnvironment environment;

    public JavaAnalyzer() {

/*
        Disposable parentDisposable = () -> {
        };
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), MetaLanguage.EP_NAME, MetaLanguage.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), TypeAnnotationModifier.EP_NAME, TypeAnnotationModifier.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), JavaMainMethodProvider.EP_NAME, JavaMainMethodProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);

        JavaCoreApplicationEnvironment javaApplicationEnvironment = new JavaCoreApplicationEnvironment(parentDisposable);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        javaApplicationEnvironment.registerParserDefinition(new JavaParserDefinition());
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClsStubBuilderFactory.EP_NAME, ClsStubBuilderFactory.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), JavaMainMethodProvider.EP_NAME, JavaMainMethodProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClsCustomNavigationPolicy.EP_NAME, ClsCustomNavigationPolicy.class);
        javaApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ClassFileDecompilers.EP_NAME, ClassFileDecompilers.Decompiler.class);

        environment = new JavaCoreProjectEnvironment(parentDisposable, javaApplicationEnvironment) {
            @Override
            protected void preregisterServices() {

                super.preregisterServices();

                if (!Extensions.getRootArea().hasExtensionPoint(DeepestSuperMethodsSearch.EP_NAME.getName())) {
                    Extensions.getRootArea().registerExtensionPoint(DeepestSuperMethodsSearch.EP_NAME.getName(), DeepestSuperMethodsSearch.class.getName(), ExtensionPoint.Kind.BEAN_CLASS);
                }
                CoreApplicationEnvironment.registerExtensionPoint(Extensions.getArea(getProject()), PsiElementFinder.EP_NAME, PsiElementFinder.class);
                CoreApplicationEnvironment.registerExtensionPoint(Extensions.getArea(getProject()), PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);

            }
        };*/

        ExtensionsArea rootArea = Extensions.getRootArea();
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, BinaryFileStubBuilders.EP_NAME, FileTypeExtensionPoint.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, FileContextProvider.EP_NAME, FileContextProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, MetaDataContributor.EP_NAME, MetaDataContributor.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, PsiAugmentProvider.EP_NAME, PsiAugmentProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, JavaMainMethodProvider.EP_NAME, JavaMainMethodProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, ContainerProvider.EP_NAME, ContainerProvider.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, ClassFileDecompilers.EP_NAME, ClassFileDecompilers.Decompiler.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, TypeAnnotationModifier.EP_NAME, TypeAnnotationModifier.class);
        CoreApplicationEnvironment.registerExtensionPoint(rootArea, MetaLanguage.EP_NAME, MetaLanguage.class);
        applicationEnvironment = new ApplicationEnvironment(disposable);
        environment = new ProjectEnvironment(disposable, applicationEnvironment);

        System.setProperty("idea.use.native.fs.for.win","false");
    }

    final ApplicationEnvironment applicationEnvironment;
    final ProjectEnvironment environment;
    static final Disposable disposable = () -> {
    };

    private static class ProjectEnvironment extends JavaCoreProjectEnvironment {
        public ProjectEnvironment(Disposable parentDisposable, CoreApplicationEnvironment applicationEnvironment) {
            super(parentDisposable, applicationEnvironment);
        }

        @Override
        protected void registerJavaPsiFacade() {
            JavaFileManager javaFileManager = getProject().getComponent(JavaFileManager.class);
            CoreJavaFileManager coreJavaFileManager = (CoreJavaFileManager) javaFileManager;
            ServiceManager.getService(getProject(), CoreJavaFileManager.class);
            getProject().registerService(CoreJavaFileManager.class, coreJavaFileManager);
            getProject().registerService(PsiNameHelper.class, PsiNameHelperImpl.getInstance());
            PsiElementFinder finder = new PsiElementFinderImpl(getProject(), coreJavaFileManager);
            ExtensionsArea area = Extensions.getArea(getProject());
            area.getExtensionPoint(PsiElementFinder.EP_NAME).registerExtension(finder);
            super.registerJavaPsiFacade();
        }

        @Override
        protected void preregisterServices() {
            super.preregisterServices();
            ExtensionsArea area = Extensions.getArea(getProject());
            CoreApplicationEnvironment.registerExtensionPoint(area, PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);
            CoreApplicationEnvironment.registerExtensionPoint(area, PsiElementFinder.EP_NAME, PsiElementFinder.class);
        }
    }

    private static class ApplicationEnvironment extends JavaCoreApplicationEnvironment {

        public ApplicationEnvironment(Disposable parentDisposable) {
            super(parentDisposable);
            myApplication.registerService(JavaClassSupers.class, new JavaClassSupersImpl());
        }
    }


    public void addClasspath(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            if (f.isDirectory()) {
                environment.addSourcesToClasspath(environment.getEnvironment().getLocalFileSystem().findFileByIoFile(f));
            } else {
                environment.addJarToClassPath(f);
            }
        }
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
