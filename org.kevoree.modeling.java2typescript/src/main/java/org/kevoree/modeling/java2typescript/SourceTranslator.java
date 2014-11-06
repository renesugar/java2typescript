package org.kevoree.modeling.java2typescript;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.translators.ClassTranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class SourceTranslator {
    //private static final String baseDir = "/Users/duke/Documents/dev/dukeboard/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";
    private static final String baseDir = "/Users/duke/Documents/dev/dukeboard/kevoree-modeling-framework/org.kevoree.modeling.microframework.typescript/target/sources";
    //private static final String baseDir = "/Users/gregory.nain/Sources/KevoreeRepos/kevoree-modeling-framework/org.kevoree.modeling.microframework/src/main/java";
    private static final String outputFile = new File("target/out.ts").getAbsolutePath();

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
        FileUtil.writeToFile(outputFile, ctx.toString().getBytes());
        System.out.println("Transpile Java2TypeScript ended to " + outputFile.getAbsolutePath());
    }
    
}
