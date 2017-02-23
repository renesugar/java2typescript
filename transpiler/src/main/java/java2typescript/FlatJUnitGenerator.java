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

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import java2typescript.translators.DocTagTranslator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlatJUnitGenerator {

    public List<String> headers = null;

    public void generate(File sourceDir, File targetDir) {
        try {

            StringBuilder sb = new StringBuilder();
            StringBuilder sbDev = new StringBuilder();

            if (headers != null) {
                for (String s : headers) {
                    sb.append(s + "\n");
                    sbDev.append(s + "\n");
                }
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
                        if (comment != null) {
                            PsiDocTag[] tags = comment.getTags();
                            if (tags != null) {
                                for (PsiDocTag tag : tags) {
                                    if (tag.getName().equals(DocTagTranslator.IGNORE) && tag.getValueElement() != null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                                        ignore = true;
                                    }
                                }
                            }
                        }


                        if (!ignore && !clazz.isInterface() && !clazz.hasModifierProperty(PsiModifier.ABSTRACT)) {
                            generateTestSuite(sb, sbDev, clazz);
                        }
                    } else {
                        element.acceptChildren(this);
                    }
                }
            });


            targetDir.mkdirs();
            File generatedTS = new File(targetDir, "testsRunner.js");
            FileUtil.writeToFile(generatedTS, sb.toString().getBytes());

            File generatedDevTS = new File(targetDir, "testsRunnerDev.js");
            FileUtil.writeToFile(generatedDevTS, sbDev.toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String generateTestSuite(StringBuilder sb, StringBuilder sbDev, PsiClass clazz) {
        boolean classInstanciated = false;
        for (PsiMethod method : clazz.getAllMethods()) {
            boolean ignore = false;
            PsiDocComment comment = method.getDocComment();
            if (comment != null) {
                PsiDocTag[] tags = comment.getTags();
                if (tags != null) {
                    for (PsiDocTag tag : tags) {
                        if (tag.getName().equals(DocTagTranslator.IGNORE) && tag.getValueElement() != null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                            ignore = true;
                        }
                    }
                }
            }
            if (!ignore) {
                PsiAnnotation testAnnot = method.getModifierList().findAnnotation("Test");
                if (testAnnot != null) {
                    if (!classInstanciated) {
                        sb.append(instanciateClass(clazz));
                        sbDev.append(instanciateClassDev(clazz));
                        classInstanciated = true;
                    }
                    generateTestCall(sb, clazz, method);
                    generateTestCallDev(sbDev, clazz, method);
                }
            }
        }
        if (classInstanciated) {
            sb.append("});\n\n");
            sbDev.append("} catch(err) {\n" +
                    "\tconsole.error(err.stack);\n" +
                    "}\n\n");
        }

        return sb.toString();
    }

    private String instanciateClass(PsiClass clazz) {
        return "\ndescribe(\"" + clazz.getQualifiedName() + "\", function() {\n" +
                "   var p_" + clazz.getName().toLowerCase() + " = new " + clazz.getQualifiedName() + "();\n";
    }

    private String instanciateClassDev(PsiClass clazz) {
        return "try {\n\tvar p_" + clazz.getName().toLowerCase() + " = new " + clazz.getQualifiedName() + "();\n";
    }

    private void generateTestCall(StringBuilder sb, PsiClass clazz, PsiMethod method) {
        sb.append("    it(\"" + clazz.getName() + "." + method.getName() + "\", function() {\n");
        sb.append("        expect(p_").append(clazz.getName().toLowerCase()).append(".").append(method.getName()).append(".bind(p_" + clazz.getName().toLowerCase() + ")).not.toThrow();\n");
        sb.append("    });\n");
    }

    private void generateTestCallDev(StringBuilder sb, PsiClass clazz, PsiMethod method) {
        sb.append("\tp_").append(clazz.getName().toLowerCase()).append(".").append(method.getName()).append("();\n");
    }

    /*
    public static void main(String[] args) {
        FlatJUnitGenerator generator = new FlatJUnitGenerator();
        generator.generate(new File("/Users/gnain/Sources/Kevoree-Modeling/mwDB/core/src/test/java"), Paths.get("./transpiler/target", "generated-test-sources", "gentest").toFile());
    }*/

}
