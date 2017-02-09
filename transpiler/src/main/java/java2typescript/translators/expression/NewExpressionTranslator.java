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
import java2typescript.helper.GenericHelper;
import java2typescript.context.TranslationContext;
import java2typescript.helper.TypeHelper;
import java2typescript.translators.AnonymousClassTranslator;

import static java2typescript.translators.expression.MethodCallExpressionTranslator.printCallParameters;

public class NewExpressionTranslator {

    public static void translate(PsiNewExpression element, TranslationContext ctx) {

        PsiAnonymousClass anonymousClass = element.getAnonymousClass();
        if (anonymousClass != null) {
            AnonymousClassTranslator.translate(anonymousClass, ctx);
        } else {
            boolean arrayDefinition = false;
            PsiJavaCodeReferenceElement classReference = element.getClassReference();
            String className;
            if (classReference != null) {
                className = TypeHelper.printType(element.getType(), ctx);
            } else {
                className = TypeHelper.printType(element.getType().getDeepComponentType(), ctx);
                arrayDefinition = true;
            }
            PsiExpression[] arrayDimensions = element.getArrayDimensions();
            PsiArrayInitializerExpression arrayInitializer = element.getArrayInitializer();
            if (arrayDimensions.length > 0) {
                arrayDefinition = true;
            }
            if (arrayInitializer != null) {
                arrayDefinition = true;
            }
            if (!arrayDefinition) {
                if (className.equals("string")) {
                    if (element.getArgumentList() != null) {
                        ExpressionListTranslator.translate(element.getArgumentList(), ctx);
                    }
                } else {
                    ctx.append("new ").append(className).append('(');
                    if (element.getArgumentList() != null) {
                        printCallParameters(element.getArgumentList().getExpressions(), ctx);
                        //ExpressionListTranslator.translate(element.getArgumentList(), ctx);
                    }
                    ctx.append(')');
                }
            } else {
                if (arrayInitializer != null) {
                    ArrayInitializerExpressionTranslator.translate(arrayInitializer, ctx);
                } else {
                    String arrayBaseType = TypeHelper.printArrayBaseType(element.getType());

                    int dimensionCount = element.getType().getArrayDimensions();
                    if (arrayBaseType != null) {
                        ctx.append("new ");
                        for (int i = 1; i < dimensionCount; i++) {
                            ctx.append("Array<");
                        }
                        ctx.append(arrayBaseType);
                        for (int i = 1; i < dimensionCount; i++) {
                            ctx.append(">");
                        }
                        ctx.append("(");
                        ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                        ctx.append(")");
                        generateInitializer(ctx, element);
                    } else if (element.getClassOrAnonymousClassReference() != null) {
                        ctx.append("new Array<");
                        PsiJavaCodeReferenceElement ref = element.getClassOrAnonymousClassReference();
                        if (ref.getReference() != null && ref.getReference().resolve() != null) {
                            PsiClass refClass = (PsiClass) ref.getReference().resolve();
                            ctx.append(GenericHelper.process(refClass));
                        } else {
                            ctx.append(TypeHelper.printType(((PsiArrayType) element.getType()).getComponentType(), ctx, false, false));
                        }
                        ctx.append(">(");
                        ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                        ctx.append(")");
                    } else {
                        for (int i = 0; i < dimensionCount; i++) {
                            ctx.append("[]");
                        }
                    }

                }
            }
        }
    }

    private static void generateInitializer(TranslationContext ctx, PsiNewExpression element) {
        int dimensionCount = element.getArrayDimensions().length;
        PsiElement parentElement = element.getParent();
        if (dimensionCount > 1 && (parentElement instanceof PsiLocalVariable || parentElement instanceof PsiAssignmentExpression)) {
            ctx.append(";\n");
            String arrayBaseType = TypeHelper.printArrayBaseType(element.getType());
            String fieldName = "";
            String prefix = "";
            if (parentElement instanceof PsiLocalVariable) {
                fieldName = ((PsiLocalVariable) parentElement).getNameIdentifier().getText();
            } else if (parentElement instanceof PsiAssignmentExpression) {
                PsiExpression left = ((PsiAssignmentExpression) parentElement).getLExpression();
                if (left instanceof PsiReferenceExpression) {
                    PsiReferenceExpression leftExpr = (PsiReferenceExpression) left;
                    fieldName = leftExpr.getReferenceName();

                    PsiExpression qualifierExpression = leftExpr.getQualifierExpression();
                    if (qualifierExpression != null) {
                        if (qualifierExpression instanceof PsiThisExpression) {
                            prefix = "this.";
                        } else if (qualifierExpression instanceof PsiReferenceExpression) {
                            prefix = ReferenceExpressionTranslator.translate((PsiReferenceExpression) qualifierExpression, ctx, false);
                        } else {
                            prefix = qualifierExpression.getText();
                        }
                    } else{
                        PsiElement resolvedReference = leftExpr.getReference().resolve();
                      if(resolvedReference instanceof PsiField) {
                          prefix = "this.";
                      }
                    }
                } else {
                    fieldName = ((PsiAssignmentExpression) parentElement).getLExpression().getText();
                }
            }

            for (int dimension = 1; dimension < dimensionCount; dimension++) {
                String varname = fieldName + "_d" + dimension;
                ctx.print("for(let " + varname + " = 0; " + varname + " < " + element.getArrayDimensions()[dimension - 1].getText() + "; " + varname + "++){\n");
                ctx.increaseIdent();
                ctx.print(prefix + fieldName);
                for (int i = 1; i <= dimension; i++) {
                    String prevVar = fieldName + "_d" + i;
                    ctx.append("[" + prevVar + "]");
                }
                ctx.append(" = new ");
                for (int i = 1; i < dimensionCount - dimension; i++) {
                    ctx.append("Array<");
                }
                ctx.append(arrayBaseType);
                for (int i = 1; i < dimensionCount - dimension; i++) {
                    ctx.append(">");
                }
                ctx.append("(");
                ExpressionTranslator.translate(element.getArrayDimensions()[dimension], ctx);
                ctx.append(");\n");

            }

            for (int dimension = 1; dimension < dimensionCount - 1; dimension++) {
                ctx.decreaseIdent();
                ctx.print("}\n");
            }
            ctx.decreaseIdent();
            ctx.print("}");
        }
    }
}


