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

import com.google.common.collect.Lists;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import java2typescript.context.TranslationContext;
import java2typescript.translators.ClassTranslator;
import java2typescript.translators.EnumTranslator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceTranslator {

    private JavaAnalyzer analyzer;
    private List<String> srcPaths;
    private String outPath;
    private String name;
    private TranslationContext ctx;
    private boolean rootPassed = false;

    public SourceTranslator(String srcPath, String outPath, String name) {
        this(Lists.newArrayList(srcPath), outPath, name);
    }

    public SourceTranslator(List<String> srcPaths, String outPath, String name) {
        this.analyzer = new JavaAnalyzer();
        this.srcPaths = new ArrayList<>(srcPaths);
        this.outPath = outPath;
        this.name = name;
        this.ctx = new TranslationContext();
    }

    private void process(String srcPath) {
        ctx.setSrcPath(srcPath);
        File srcFolder = new File(srcPath);
        if (srcFolder.exists()) {
            if (srcFolder.isFile()) {
                throw new IllegalArgumentException("Source path is not a directory");
            }
        } else {
            throw new IllegalArgumentException("Source path " + srcPath + " does not exists");
        }
        File outFolder = new File(this.outPath);

        if (!outFolder.exists()) {
            outFolder.mkdirs();
        }

        PsiDirectory root = analyzer.analyze(srcFolder);
        root.getResolveScope().accept(root.getVirtualFile());
        PsiFile[] containedFiles = root.getFiles();
        Arrays.sort(containedFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        for (PsiFile file : containedFiles) {
            if (file instanceof PsiJavaFile) {
                visit((PsiJavaFile) file);
            } else {
                visit(file);
            }
        }
        PsiDirectory[] subDirectories = root.getSubdirectories();
        Arrays.sort(subDirectories, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        for (PsiDirectory subDir : subDirectories) {
            visit(subDir, !rootPassed);
            if (!rootPassed) {
                rootPassed = true;
            }
        }
    }

    public void process() {
        this.srcPaths.forEach(this::process);
    }

    public void generate() {
        String[] modelPath = new String[]{name + ".ts"};
        File modelFile = Paths.get(outPath, modelPath).toFile();
        try {
            FileUtil.writeToFile(modelFile, ctx.toString().getBytes());

            // files
            List<URI> files = new ArrayList<>();
            files.add(Paths.get(".", modelPath).toUri());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void visit(PsiDirectory dir, boolean isRoot) {
        ctx.enterPackage(dir.getName(), isRoot);
        PsiFile[] containedFiles = dir.getFiles();
        Arrays.sort(containedFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        for (PsiFile file : containedFiles) {
            if (file instanceof PsiJavaFile) {
                visit((PsiJavaFile) file);
            } else {
                visit(file);
            }
        }
        PsiDirectory[] subDirectories = dir.getSubdirectories();
        Arrays.sort(subDirectories, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        for (PsiDirectory subDir : subDirectories) {
            visit(subDir, false);
        }
        ctx.leavePackage();
    }

    private void visit(PsiJavaFile file) {
        ctx.setFile(file);
        file.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiClass) {
                    if (((PsiClass) element).isEnum()) {
                        EnumTranslator.translate((PsiClass) element, ctx);
                    } else {
                        ClassTranslator.translate((PsiClass) element, ctx);
                    }
                }
            }
        });
    }

    private void visit(PsiElement elem) {
        System.out.println("Ignored file= " + elem);
    }

    public void addToClasspath(String path) {
        this.analyzer.addClasspath(path);
    }


    public void addHeader(String moduleImport) {
        ctx.addHeader(moduleImport);
    }

    public void addPackageTransform(String initialName, String newName) {
        ctx.addPackageTransform(initialName, newName);
    }

    /**
     * Useful for tests
     * (you should not mess with this unless you know what you are doing)
     *
     * @return ctx TranslationContext
     */
    protected TranslationContext getCtx() {
        return this.ctx;
    }

}
