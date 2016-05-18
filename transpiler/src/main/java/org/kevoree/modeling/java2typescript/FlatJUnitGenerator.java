package org.kevoree.modeling.java2typescript;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import org.kevoree.modeling.java2typescript.translators.DocTagTranslator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by gregory.nain on 03/12/14.
 */
public class FlatJUnitGenerator {

    private ArrayList<String> moduleImports = new ArrayList<>();

    public void addModuleImport(String s) {
        moduleImports.add(s);
    }

    public void generate(File sourceDir, File targetDir) {
        try {


            StringBuilder sb = new StringBuilder();

            for(String s : moduleImports) {
                sb.append("/// <reference path=\"");
                sb.append(s);
                sb.append("\" />\n");
            }


            JavaAnalyzer javaAnalyzer = new JavaAnalyzer();
            PsiDirectory parsedDir = javaAnalyzer.analyze(sourceDir);
            parsedDir.acceptChildren(new PsiElementVisitor() {
                @Override
                public void visitElement(PsiElement element) {
                    boolean ignore = false;
                    if (element instanceof PsiClass) {
                        PsiClass clazz = (PsiClass) element;

                        PsiDocComment comment = clazz.getDocComment();
                        if(comment != null) {
                            PsiDocTag[] tags = comment.getTags();
                            if(tags != null) {
                                for(PsiDocTag tag : tags) {
                                    if (tag.getName().equals(DocTagTranslator.IGNORE) && tag.getValueElement()!=null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                                        ignore = true;
                                    }
                                }
                            }
                        }


                        if (!ignore && !clazz.isInterface() && !clazz.hasModifierProperty(PsiModifier.ABSTRACT)) {
                            sb.append(generateTestSuite(clazz));
                        }
                    } else {
                        element.acceptChildren(this);
                    }
                }
            });


            targetDir.mkdirs();
            File generatedTS = new File(targetDir, "testsRunner.ts");
            FileUtil.writeToFile(generatedTS, sb.toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String instanciateClass(PsiClass clazz) {

        return "try {\n let p_" + clazz.getName().toLowerCase() + " : " + clazz.getQualifiedName() + " = new " + clazz.getQualifiedName() + "();\n";
    }


    private String generateTestSuite(PsiClass clazz) {
        StringBuilder sb = new StringBuilder();
        boolean classInstanciated = false;
        for (PsiMethod method : clazz.getAllMethods()) {
            boolean ignore = false;
            PsiDocComment comment = method.getDocComment();
            if(comment != null) {
                PsiDocTag[] tags = comment.getTags();
                if(tags != null) {
                    for(PsiDocTag tag : tags) {
                        if (tag.getName().equals(DocTagTranslator.IGNORE) && tag.getValueElement()!=null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                            ignore = true;
                        }
                    }
                }
            }
            if(!ignore) {
                PsiAnnotation testAnnot = method.getModifierList().findAnnotation("Test");
                if (testAnnot != null) {
                    if (!classInstanciated) {
                        sb.append(instanciateClass(clazz));
                        classInstanciated = true;
                    }
                    sb.append("console.log(\"executing "+clazz.getName()+"."+method.getName()+"...\");\n");
                    sb.append("p_").append(clazz.getName().toLowerCase()).append(".").append(method.getName()).append("();\n");
                    sb.append("console.log(\"done\")\n");
                }
            }
        }
        if (classInstanciated) {
            sb.append(" } catch ($ex$) {\nconsole.error($ex$);\n}\n");
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        FlatJUnitGenerator generator = new FlatJUnitGenerator();
        generator.generate(new File("/Users/gnain/Sources/Kevoree-Modeling/mwDB/core/src/test/java"), Paths.get("./transpiler/target", "generated-test-sources", "gentest").toFile());
    }


}
