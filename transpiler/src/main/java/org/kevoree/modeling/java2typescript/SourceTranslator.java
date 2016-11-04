package org.kevoree.modeling.java2typescript;

import com.google.common.collect.Lists;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.ClassTranslator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

public class SourceTranslator {

    private JavaAnalyzer analyzer;
    private List<String> srcPaths;
    private String outPath;
    private String name;
    private List<TranslationContext> ctxs;
    private boolean rootPassed = false;

    private List<String> globalModuleImports;
    private Map<String, String> globalPkgTransforms;

    public SourceTranslator(String srcPath, String outPath, String name) {
        this(Lists.newArrayList(srcPath), outPath, name);
    }

    public SourceTranslator(List<String> srcPaths, String outPath, String name) {
        this.analyzer = new JavaAnalyzer();
        this.srcPaths = new ArrayList<>(srcPaths);
        this.outPath = outPath;
        this.name = name;
        this.ctxs = new ArrayList<>();
        this.globalModuleImports = new ArrayList<>();
        globalPkgTransforms = new HashMap<>();
    }

    private void process(String srcPath) {
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
        Arrays.sort(subDirectories, (f1, f2) -> f1.getName().compareTo(f2.getName()) );
        for (PsiDirectory subDir : subDirectories) {
            visit(subDir, !rootPassed);
            if(!rootPassed) {
                rootPassed = true;
            }
        }
    }

    public void process() {
        this.srcPaths.forEach(this::process);
    }

    public void generate() {
        for(int i=0;i<ctxs.size();i++) {
            String[] modelPath = new String[]{ctxs.get(i).getFileName() +  ".ts"};
            File modelFile = Paths.get(outPath, modelPath).toFile();
            try {
                FileUtil.writeToFile(modelFile, ctxs.get(i).toString().getBytes());

                // files
                List<URI> files = new ArrayList<>();
                files.add(Paths.get(".", modelPath).toUri());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void visit(PsiDirectory dir, boolean isRoot) {
        PsiFile[] containedFiles = dir.getFiles();
        Arrays.sort(containedFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()) );
        for (PsiFile file : containedFiles) {
            if (file instanceof PsiJavaFile) {
                visit((PsiJavaFile) file);
            } else {
                visit(file);
            }
        }

        PsiDirectory[] subDirectories = dir.getSubdirectories();
        Arrays.sort(subDirectories, (f1, f2) -> f1.getName().compareTo(f2.getName()) );
        for (PsiDirectory subDir : subDirectories) {
            visit(subDir, false);
        }
    }

    private void visit(PsiJavaFile file) {
        TranslationContext translationContext = new TranslationContext();
        globalModuleImports.forEach(translationContext::addModuleImport);
        globalPkgTransforms.forEach(translationContext::addPackageTransform);
        ctxs.add(translationContext);
        file.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiClass) {
                    ClassTranslator.translate((PsiClass) element, ctxs.get(ctxs.size() - 1));
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


    public void addModuleImport(String moduleImport) {
        globalModuleImports.add(moduleImport);
    }

    public void addPackageTransform(String initialName, String newName) {
        globalPkgTransforms.put(initialName,newName);
    }

    /**
     * Useful for tests
     * (you should not mess with this unless you know what you are doing)
     *
     * @return ctx TranslationContext
     */
    protected List<TranslationContext> getCtx() {
        return this.ctxs;
    }
}
