package com.siliconmint.ts;

import com.intellij.core.JavaCoreApplicationEnvironment;
import com.intellij.core.JavaCoreProjectEnvironment;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.FileASTNode;
import com.intellij.mock.MockProject;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.siliconmint.ts.util.FileUtil.*;

public class SourceTranslator {

    private static final String baseDir = "/Users/duke/Documents/dev/dukeboard/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";
    private static final String outputDir = new File("target").getAbsolutePath();

    private PsiFileFactory psiFileFactory;
    private HashMap<String, Integer> genericsCounts;

    public static void main(String[] args) throws IOException {
        SourceTranslator sourceTranslator = new SourceTranslator();
        sourceTranslator.translateSources(baseDir, outputDir);
    }

    public SourceTranslator() {
        this.psiFileFactory = createPsiFactory();
    }

    public void translateSources(String sourcePath, String outputPath) throws IOException {
        File source = new File(sourcePath);
        File outputDir = new File(outputPath);
        if (source.exists()) {
            if (source.isFile()) {
                throw new IllegalArgumentException("Source path is not a directory");
            }
        } else {
            source.mkdirs();
        }
        if (outputDir.exists()) {
            if (outputDir.isFile()) {
                throw new IllegalArgumentException("Output path is not a directory");
            }
        } else {
            outputDir.mkdirs();
        }
        registerGenericsType(source);

        StringBuilder globalBuilder = new StringBuilder();
        final int[] deep = new int[1];
        deep[0] = 0;
        try {
            Files.walkFileTree(source.toPath(), new FileVisitor<Path>() {
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    prefix(globalBuilder, deep[0]);
                    deep[0] = deep[0] + 1;
                    globalBuilder.append("module ");
                    globalBuilder.append(dir.getName(dir.getNameCount() - 1).toString().toLowerCase());
                    globalBuilder.append(" {");
                    globalBuilder.append("\n");
                    return FileVisitResult.CONTINUE;
                }

                @NotNull
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toFile().getName().endsWith(".java")) {
                        transpileFile(globalBuilder, deep[0], file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @NotNull
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    deep[0] = deep[0] - 1;
                    prefix(globalBuilder, deep[0]);
                    globalBuilder.append("}");
                    globalBuilder.append("\n");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        File out = new File(outputDir, "out.ts");
        FileUtil.writeToFile(out, globalBuilder.toString().getBytes());
        System.out.println("Transpile Java2TypeScript ended to " + out.getAbsolutePath());
    }

    private void prefix(StringBuilder builder, int deep) {
        for (int i = 0; i < deep; i++) {
            builder.append("\t");
        }
    }

    private void transpileFile(StringBuilder builder, int deep, File file) throws IOException {
        FileASTNode node = parseJavaSource(file, psiFileFactory);
        TypeScriptTranslator translator = new TypeScriptTranslator();
        translator.getCtx().setTranslatedFile(file);
        translator.getCtx().setGenerics(genericsCounts);
        node.getPsi().accept(translator);
        String[] lines = translator.getCtx().getText().split("\n");
        for (int i = 0; i < lines.length; i++) {
            prefix(builder, deep);
            builder.append(lines[i]);
            builder.append("\n");
        }
    }


    private void registerGenericsType(File source) {
        genericsCounts = new HashMap<String, Integer>();
        genericsCounts.put("JUSet", 1);
        genericsCounts.put("JUHsetSet", 1);
        genericsCounts.put("JUCollection", 1);
        genericsCounts.put("JUList", 1);
        genericsCounts.put("JUArrayList", 1);
        genericsCounts.put("JUMap", 2);
        genericsCounts.put("JUHashMap", 2);
        if (source.isFile()) {
        } else {
            try {
                Files.walkFileTree(source.toPath(), new FileVisitor<Path>() {
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @NotNull
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        try {
                            if (file.toFile().getName().endsWith(".java")) {
                                FileASTNode node = parseJavaSource(file.toFile(), psiFileFactory);
                                node.getPsi().accept(new PsiRecursiveElementWalkingVisitor() {
                                    @Override
                                    public void visitElement(PsiElement element) {
                                        if (element instanceof PsiClass) {
                                            PsiClass clazz = (PsiClass) element;
                                            if (clazz.hasTypeParameters()) {
                                                genericsCounts.put(clazz.getName(), clazz.getTypeParameters().length);
                                            }
                                        }
                                        super.visitElement(element);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @NotNull
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    private void translateSourceDir(File sourceDirectory, File outputDir) {
        File[] sourceFiles = sourceDirectory.listFiles(JAVA_SOURCE_FILE_FILTER);
        Arrays.sort(sourceFiles, FILE_NAME_COMPARATOR);
        for (File sourceFile : sourceFiles) {
            translateSourceFile(sourceFile, outputDir);
        }

        File[] sourceSubDirs = sourceDirectory.listFiles(DIRECTORY_FILTER);
        Arrays.sort(sourceSubDirs, FILE_NAME_COMPARATOR);
        for (File sourceSubDirectory : sourceSubDirs) {
            translateSourceDir(sourceSubDirectory, new File(outputDir, sourceSubDirectory.getName()));
        }
    }

    private void translateSourceFile(File sourceFile, File outputDir) {
        try {
            FileASTNode node = parseJavaSource(sourceFile, psiFileFactory);
            TypeScriptTranslator translator = new TypeScriptTranslator();
            translator.getCtx().setTranslatedFile(sourceFile);
            translator.getCtx().setGenerics(genericsCounts);
            node.getPsi().accept(translator);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            String fileName = sourceFile.getName();
            int extensionPosition = sourceFile.getName().lastIndexOf(".java");
            FileUtil.writeToFile(new File(outputDir, fileName.substring(0, extensionPosition).concat(".ts")), translator.getCtx().getText().getBytes());
            System.out.printf("Successfully translated %s\n", sourceFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.printf("Failed to parse %s: %s\n", sourceFile.getAbsolutePath(), e.getMessage());
        } catch (Exception e) {
            System.err.printf("Failed to translate %s: %s\n", sourceFile.getAbsolutePath(), e.getMessage());
            throw new RuntimeException(e);
        }
    }*/

    private PsiFileFactory createPsiFactory() {
        MockProject mockProject = createProject();
        return PsiFileFactory.getInstance(mockProject);
    }

    private static FileASTNode parseJavaSource(File sourceFile, PsiFileFactory psiFileFactory) throws IOException {
        String javaSource = FileUtil.loadFile(sourceFile);
        PsiFile psiFile = psiFileFactory.createFileFromText(sourceFile.getName(), JavaFileType.INSTANCE, javaSource);
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        return psiJavaFile.getNode();
    }

    private static MockProject createProject() {
        JavaCoreProjectEnvironment environment = new JavaCoreProjectEnvironment(new Disposable() {
            @Override
            public void dispose() {
            }
        }, new JavaCoreApplicationEnvironment(new Disposable() {
            @Override
            public void dispose() {
            }
        }));

        return environment.getProject();
    }

}
