package org.kevoree.modeling.java2typescript;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.translators.ClassTranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;

public class SourceTranslator {

    private static final String baseDir = "/Users/duke/Documents/dev/dukeboard/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";
    //private static final String baseDir = "/Users/gregory.nain/Sources/KevoreeRepos/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";
    private static final String outputFile = new File("target/out.ts").getAbsolutePath();

    private HashMap<String, Integer> genericsCounts;
    private HashMap<String, HashMap<String, String>> fqns;

    public static void main(String[] args) throws IOException {
        SourceTranslator sourceTranslator = new SourceTranslator();
        sourceTranslator.translateSources(baseDir, outputFile);
    }

    public void translateSources(String sourcePath, String outputPath) throws IOException {
        File source = new File(sourcePath);
        File outputFile = new File(outputPath);
        if (source.exists()) {
            if (source.isFile()) {
                throw new IllegalArgumentException("Source path is not a directory");
            }
        } else {
            source.mkdirs();
        }
        if (outputFile.isDirectory()) {
            throw new IllegalArgumentException("OutputFile should not be a directory");
        } else {
            Files.createDirectories(outputFile.toPath().getParent());
        }

        TranslationContext ctx = new TranslationContext();
        //copy default library
        InputStreamReader is = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("java.ts"));
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while (read != null) {
            ctx.append(read);
            ctx.append("\n");
            read = br.readLine();
        }

        JavaAnalyzer analyzer = new JavaAnalyzer();
        PsiDirectory root = analyzer.analyze(source);
        root.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiDirectory) {
                    PsiDirectory currentDir = (PsiDirectory) element;
                    if (root != currentDir.getParent()) {
                        ctx.print("export module ");
                    } else {
                        ctx.print("module ");
                    }
                    ctx.append(currentDir.getName());
                    ctx.append(" {");
                    ctx.append("\n");
                    ctx.increaseIdent();
                    element.acceptChildren(this);
                    ctx.decreaseIdent();
                    ctx.print("}");
                    ctx.append("\n");
                } else if (element instanceof PsiClass) {
                    ClassTranslator.translate((PsiClass) element, ctx);
                } else {
                    element.acceptChildren(this);
                }
            }
        });



/*
        try {
            Files.walkFileTree(source.toPath(), new FileVisitor<Path>() {
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir != source.toPath()) {
                        prefix(globalBuilder, deep[0]);
                        if (deep[0] != 0) {
                            globalBuilder.append("export ");
                        }
                        globalBuilder.append("module ");
                        globalBuilder.append(dir.getName(dir.getNameCount() - 1).toString().toLowerCase());
                        globalBuilder.append(" {");
                        globalBuilder.append("\n");
                        deep[0] = deep[0] + 1;
                    }
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
                    if (dir != source.toPath()) {
                        deep[0] = deep[0] - 1;
                        prefix(globalBuilder, deep[0]);
                        globalBuilder.append("}");
                        globalBuilder.append("\n");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        FileUtil.writeToFile(outputFile, ctx.toString().getBytes());
        System.out.println("Transpile Java2TypeScript ended to " + outputFile.getAbsolutePath());
    }

    private void prefix(StringBuilder builder, int deep) {
        for (int i = 0; i < deep; i++) {
            builder.append("\t");
        }
    }

    /*
    private void transpileFile(StringBuilder builder, int deep, File file) throws IOException {
        FileASTNode node = parseJavaSource(file, psiFileFactory);
        TypeScriptTranslator translator = new TypeScriptTranslator();
        translator.getCtx().setTranslatedFile(file);
        translator.getCtx().setGenerics(genericsCounts);
        translator.getCtx().setAllImports(fqns);
        node.getPsi().accept(translator);
        String[] lines = translator.getCtx().getText().split("\n");
        for (int i = 0; i < lines.length; i++) {
            prefix(builder, deep);
            builder.append(lines[i]);
            builder.append("\n");
        }
    }*/


    /*
    private void registerGenericsType(File source) {
        fqns = new HashMap<String, HashMap<String, String>>();
        genericsCounts = new HashMap<String, Integer>();
        genericsCounts.put("java.util.Set", 1);
        genericsCounts.put("java.util.HsetSet", 1);
        genericsCounts.put("java.util.Collection", 1);
        genericsCounts.put("java.util.List", 1);
        genericsCounts.put("java.util.ArrayList", 1);
        genericsCounts.put("java.util.LinkedList", 1);
        genericsCounts.put("java.util.Map", 2);
        genericsCounts.put("java.util.HashMap", 2);
        if (source.isFile()) {
        } else {
            try {
                Files.walkFileTree(source.toPath(), new FileVisitor<Path>() {
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    private void completeImports(HashMap<String, String> imports) {
                        imports.put("Throwable", "java.lang.Throwable");
                        imports.put("Exception", "java.lang.Exception");
                        imports.put("Long", "java.lang.Long");
                        imports.put("Double", "java.lang.Double");
                        imports.put("Integer", "java.lang.Integer");
                        imports.put("Short", "java.lang.Short");
                        imports.put("Float", "java.lang.Float");
                        imports.put("StringBuilder", "java.lang.StringBuilder");
                    }

                    @NotNull
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        try {
                            if (file.toFile().getName().endsWith(".java")) {
                                FileASTNode node = parseJavaSource(file.toFile(), psiFileFactory);
                                node.getPsi().accept(new PsiRecursiveElementWalkingVisitor() {
                                    HashMap<String, String> imports = new HashMap<String, String>();

                                    @Override
                                    public void visitElement(PsiElement element) {
                                        if (element instanceof PsiClass) {
                                            PsiClass clazz = (PsiClass) element;
                                            if (clazz.hasTypeParameters()) {
                                                genericsCounts.put(clazz.getQualifiedName(), clazz.getTypeParameters().length);
                                            }
                                            completeImports(imports);
                                            fqns.put(clazz.getQualifiedName(), imports);
                                            imports = new HashMap<String, String>();
                                        } else if (element instanceof PsiImportStatement) {
                                            PsiImportStatement impor = (PsiImportStatement) element;
                                            imports.put(impor.getQualifiedName().substring(impor.getQualifiedName().lastIndexOf(".") + 1), impor.getQualifiedName());
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
    */

}
