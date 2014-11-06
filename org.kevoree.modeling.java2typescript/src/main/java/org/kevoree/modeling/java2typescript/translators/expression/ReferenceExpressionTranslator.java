
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ReferenceExpressionTranslator {

    public static void translate(PsiReferenceExpression element, TranslationContext ctx) {

        if (element.getQualifierExpression() != null) {
            ExpressionTranslator.translate(element.getQualifierExpression(), ctx);
            ctx.append('.');
        } else {
            PsiElement resolution = element.resolve();
            if (resolution != null) {
                String qualifier = "this";
                if (resolution instanceof PsiField) {
                    PsiField field = (PsiField) resolution;
                    if (field.getModifierList() != null && field.getModifierList().hasModifierProperty("static")) {
                        qualifier = field.getContainingClass().getName();
                    }
                    ctx.append(qualifier).append('.');
                } else if (resolution instanceof PsiMethod) {
                    PsiMethod method = (PsiMethod) resolution;
                    if (method.getModifierList().hasModifierProperty("static")) {
                        qualifier = method.getContainingClass().getName();
                    }
                    ctx.append(qualifier).append('.');
                }
            }
        }
        ctx.append(element.getReferenceName());
    }

}
