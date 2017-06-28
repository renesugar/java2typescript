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
package java2typescript.translators;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import java2typescript.helper.DocHelper;
import java2typescript.context.TranslationContext;
import java2typescript.helper.TypeHelper;
import java2typescript.metas.DocMeta;
import java2typescript.translators.expression.ExpressionListTranslator;
import java2typescript.translators.expression.ExpressionTranslator;

public class FieldTranslator {

    public static void translate(PsiField element, TranslationContext ctx) {
        //Check for native code
        DocMeta docMeta = DocHelper.process(element.getDocComment());
        if (docMeta.ignored) {
            return;
        }
        if (!docMeta.nativeActivated) {
            if (element instanceof PsiEnumConstant) {
                translateEnumConstant((PsiEnumConstant) element, ctx);
            } else {
                translateClassField(element, ctx, docMeta);
            }
        } else {
            DocTagTranslator.translate(docMeta, ctx);
        }
    }

    private static void translateEnumConstant(PsiEnumConstant element, TranslationContext ctx) {
        String enumName = ((PsiClass) element.getParent()).getName();
        ctx.print("public static ").append(element.getName()).append(": ").append(enumName);
        ctx.append(" = new ").append(enumName);
        ctx.append('(');
        if (element.getArgumentList() != null) {
            ExpressionListTranslator.translate(element.getArgumentList(), ctx);
        }
        ctx.append(");\n");
    }

    private static void translateClassField(PsiField element, TranslationContext ctx, DocMeta docMeta) {
        PsiModifierList modifierList = element.getModifierList();
        if (modifierList != null && modifierList.hasModifierProperty("private")) {
            ctx.print("private ");
        } else {
            ctx.print("public ");
        }
        if (modifierList != null && modifierList.hasModifierProperty("static")) {
            ctx.append("static ");
        }
        ctx.append(element.getName()).append(": ");

        if (element.hasInitializer() && (element.getInitializer() instanceof PsiLambdaExpression)) {
            if (element.getType() instanceof PsiClassReferenceType) {
                try {
                    MethodTranslator.translateToLambdaType(((PsiClassReferenceType) element.getType()).rawType().resolve().getMethods()[0], ctx, docMeta);
                } catch (Exception e){
                    ((PsiClassReferenceType) element.getType()).rawType()
                            .resolve();
                    e.printStackTrace();
                }
            } else {
                System.err.println("FieldTranslator:: Type not instance of PsiClassReferenceType. Could not translate lambda expression. (" + element.getType().getClass().getName() + ")");
            }

        } else {
            ctx.append(TypeHelper.printType(element.getType(), ctx));
        }

        if (element.hasInitializer()) {
            ctx.append(" = ");
            ExpressionTranslator.translate(element.getInitializer(), ctx);
            ctx.append(";\n");
        } else {
            ctx.append(";\n");
        }
    }

}
