
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;

public class ReferenceExpressionTranslator {

    public static String translate(PsiReferenceExpression element, TranslationContext ctx, boolean appendToCtx) {
        String result = "";
        if (element.getReference() != null && element.getReference().resolve() instanceof PsiClass) {
            PsiClass clazz = (PsiClass) element.getReference().resolve();
            if (clazz != null) {
                result += clazz.getQualifiedName();
                if (appendToCtx) {
                    ctx.append(result);
                }
                return result;
            }
        }

        PsiElement resolution = element.resolve();
        if (element.getQualifierExpression() != null) {
            if (element.getQualifierExpression() instanceof PsiReferenceExpression) {
                result += translate((PsiReferenceExpression) element.getQualifierExpression(), ctx, false);
            } else {
                ExpressionTranslator.translate(element.getQualifierExpression(), ctx);
            }
            result += ".";
        } else {
            if (resolution != null) {
                String qualifier = "this";
                if (resolution instanceof PsiField) {
                    PsiField field = (PsiField) resolution;
                    if (field.getModifierList() != null && field.getModifierList().hasModifierProperty("static")) {
                        PsiClass currentClassDef = null;
                        PsiElement parent = element;
                        while(parent != null) {
                            if(parent instanceof PsiClass) {
                                currentClassDef = (PsiClass) parent;
                                break;
                            } else {
                                parent = parent.getParent();
                            }
                        }
                        boolean sameClass = false;
                        if(currentClassDef != null) {
                            sameClass = (currentClassDef == field.getContainingClass());
                        }
                        if (sameClass) {
                            qualifier = field.getContainingClass().getName();
                        } else {
                            qualifier = field.getContainingClass().getQualifiedName();
                        }
                    }
                    result += qualifier + ".";
                } else if (resolution instanceof PsiMethod) {
                    PsiMethod method = (PsiMethod) resolution;
                    if (method.getModifierList().hasModifierProperty("static")) {
                        qualifier = method.getContainingClass().getQualifiedName();
                    }
                    if (!element.getReferenceName().equals("super")) {
                        result += qualifier + ".";
                    }
                }/* else if (resolution instanceof PsiParameter) {
                    if((((PsiParameter) resolution).isVarArgs())) {
                        result += "...";
                    }
                }*/
            }
        }

        String type = TypeHelper.primitiveStaticCall(element.getReferenceName(), ctx);
        if (!result.isEmpty() && type.startsWith(result)) {
            result = type;
        } else {
            result += type;
        }

        if (appendToCtx) {
            ctx.append(result);
        }
        return result;
    }

    public static void translate(PsiReferenceExpression element, TranslationContext ctx) {
        translate(element, ctx, true);
    }
}
