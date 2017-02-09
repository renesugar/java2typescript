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
import com.intellij.lang.java.JavaParserDefinition;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.compiled.ClassFileDecompilers;
import com.intellij.psi.impl.*;
import com.intellij.psi.impl.compiled.ClsCustomNavigationPolicy;
import com.intellij.psi.impl.compiled.ClsStubBuilderFactory;
import com.intellij.psi.search.searches.DeepestSuperMethodsSearch;
import com.intellij.psi.search.searches.ExtensibleQueryFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JavaAnalyzer {

    private JavaCoreProjectEnvironment environment;

    public JavaAnalyzer() {

        Disposable parentDisposable = () -> {
        };

        CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), ContainerProvider.EP_NAME, ContainerProvider.class);

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

                Extensions.getRootArea().registerExtensionPoint(DeepestSuperMethodsSearch.EP_NAME.getName(), DeepestSuperMethodsSearch.class.getName(), ExtensionPoint.Kind.BEAN_CLASS);
                registerProjectExtensionPoint(PsiElementFinder.EP_NAME, PsiElementFinder.class);
                CoreApplicationEnvironment.registerExtensionPoint(Extensions.getArea(getProject()), PsiTreeChangePreprocessor.EP_NAME, PsiTreeChangePreprocessor.class);

            }
        };


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
