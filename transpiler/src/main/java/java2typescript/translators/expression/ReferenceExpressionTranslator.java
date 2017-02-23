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
package java2typescript.translators.expression;

import com.intellij.psi.*;
import java2typescript.context.TranslationContext;
import java2typescript.helper.TypeHelper;

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

                if(element.getQualifierExpression().getText().equals("org.junit")) {
                    result += "junit";
                } else {
                    result += translate((PsiReferenceExpression) element.getQualifierExpression(), ctx, false);
                }
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
