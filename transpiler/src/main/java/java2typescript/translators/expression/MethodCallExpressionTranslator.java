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

public class MethodCallExpressionTranslator {

    public static void translate(PsiMethodCallExpression element, TranslationContext ctx) {
        PsiReferenceExpression methodExpr = element.getMethodExpression();
        PsiExpression methodQualifierExpression = methodExpr.getQualifierExpression();

        if (methodQualifierExpression != null) {
            if (methodQualifierExpression.getType() != null) {
                if (methodQualifierExpression.getType() instanceof PsiClassType) {
                    PsiClass cls = ((PsiClassType) methodQualifierExpression.getType()).resolve();
                    if (cls == null) {
                        PsiClassType rawType = ((PsiClassType) methodQualifierExpression.getType()).rawType();
                        if (rawType != null) {
                            cls = rawType.resolve();
                        }
                    }
                    if (cls != null) {
                        if (TypeHelper.isCallbackClass(cls)) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append('(');
                            printCallParameters(element.getArgumentList().getExpressions(), ctx);
                            ctx.append(")");
                            return;
                        }
                    } else {

                    }
                }
            }
        }

        boolean hasBeenTransformed = tryNativeTransform(element, ctx);
        if (!hasBeenTransformed) {
            if ((methodExpr.getQualifier() == null)
                    && (element.getParent() instanceof PsiField)
                    && (methodExpr.resolve().getParent() == ((PsiField) element.getParent()).getParent())) {
                ctx.append(((PsiClass) methodExpr.getReference().resolve().getParent()).getName());
                ctx.append('.');
                ctx.append(methodExpr.getReferenceName());
            } else {
                ReferenceExpressionTranslator.translate(methodExpr, ctx);
            }
            ctx.append('(');
            printCallParameters(element.getArgumentList().getExpressions(), ctx);
            ctx.append(")");
        }
    }

    public static void printCallParameters(PsiExpression[] arguments, TranslationContext ctx) {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] instanceof PsiMethodCallExpression) {
                if (arguments[i].getType() != null && arguments[i].getType().getArrayDimensions() > 0) {
                    checkVarArgs(arguments, i, ctx);
                }
                ExpressionTranslator.translate(arguments[i], ctx);
            } else if (arguments[i] instanceof PsiReferenceExpression) {
                PsiReference ref = arguments[i].getReference();
                if (ref != null) {
                    PsiElement resolved = ref.resolve();
                    if (resolved != null) {
                        if (resolved instanceof PsiParameter) {
                            if ((((PsiParameter) resolved).isVarArgs())) {
                                ctx.append("...");
                            }
                        } else if (resolved instanceof PsiVariable) {
                            boolean isArray = ((PsiVariable) resolved).getType().getArrayDimensions() > 0;
                            if (isArray) {
                                checkVarArgs(arguments, i, ctx);
                            }
                        }
                    }
                }
                ReferenceExpressionTranslator.translate((PsiReferenceExpression) arguments[i], ctx);
            } else {
                ExpressionTranslator.translate(arguments[i], ctx);
            }
            if (i != arguments.length - 1) {
                ctx.append(", ");
            }
        }
    }

    private static void checkVarArgs(PsiExpression[] arguments, int index, TranslationContext ctx) {
        PsiElement parentExp = arguments[index].getParent();
        if (parentExp != null) {
            PsiElement grandParentExp = parentExp.getParent();
            if (grandParentExp != null) {
                if (grandParentExp instanceof PsiNewExpression) {
                    PsiElement resolvedClass = ((PsiNewExpression) grandParentExp).getClassOrAnonymousClassReference().resolve();
                    if (resolvedClass instanceof PsiClass) {
                        for (PsiMethod m : ((PsiClass) resolvedClass).getConstructors()) {
                            PsiParameter[] parameters = m.getParameterList().getParameters();
                            if (parameters.length == arguments.length) {
                                if (parameters[index].isVarArgs()) {
                                    ctx.append("...");
                                    break;
                                }
                            }
                        }
                    }
                } else if (grandParentExp instanceof PsiMethodCallExpression) {
                    PsiElement method = ((PsiMethodCallExpression) grandParentExp).getMethodExpression().resolve();
                    if (method != null && method instanceof PsiMethod) {
                        PsiParameter[] parameters = ((PsiMethod) method).getParameterList().getParameters();
                        if (parameters.length == arguments.length) {
                            if (parameters[index].isVarArgs()) {
                                ctx.append("...");
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean tryNativeTransform(PsiMethodCallExpression element, TranslationContext ctx) {

        // a.b.c.method()
        PsiReferenceExpression methodExpression = element.getMethodExpression();
        // a.b.c
        PsiExpression methodQualifierExpression = methodExpression.getQualifierExpression();

        if (methodQualifierExpression != null) {
            if (methodQualifierExpression.getType() != null) {
                if (methodQualifierExpression.getType().getCanonicalText().contains("String")) {
                    if (methodExpression.getReferenceName() != null) {

                        if (methodExpression.getReferenceName().equals("length")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            if (element.getArgumentList().getExpressions().length == 0) {
                                ctx.append(".length");
                                return true;
                            }


                        } else if (methodExpression.getReferenceName().equals("codePointAt")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(".charCodeAt(");
                            PsiExpression[] arguments = element.getArgumentList().getExpressions();
                            for (int i = 0; i < arguments.length; i++) {
                                ExpressionTranslator.translate(arguments[i], ctx);
                                if (i != arguments.length - 1) {
                                    ctx.append(", ");
                                }
                            }
                            ctx.append(")");
                            ctx.needsJava(TypeHelper.javaTypes.get("String"));
                            return true;


                        } else if (methodExpression.getReferenceName().equals("concat")) {

                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(" + ");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("equals")) {

                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(" === ");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("getBytes")) {

                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(".split('').map(function(e){return e.charCodeAt(0);})");
                            return true;
                        } else if (methodExpression.getReferenceName().equals("startsWith")) {
                            ctx.append("(");
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(".lastIndexOf(");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            ctx.append(", 0) === 0)");
                            return true;
                        }
                    }


                } else if (methodQualifierExpression.getType().getCanonicalText().equals("Exception")
                        || methodQualifierExpression.getType().getCanonicalText().equals("Error")
                        || methodQualifierExpression.getType().getCanonicalText().equals("Throwable")) {
                    //error .printStackTrace
                    if (methodExpression.getReferenceName() != null &&
                            methodExpression.getReferenceName().equals("printStackTrace")) {
                        ctx.append("console.error(");
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(")");
                        return true;
                    } else if (methodExpression.getReferenceName() != null &&
                            methodExpression.getReferenceName().equals("getMessage")) {
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(".message");
                        return true;
                    }
                } else if (methodQualifierExpression.getType().getCanonicalText().equals("PrintStream")) {
                    //error .printStackTrace
                    if (methodExpression.getReferenceName() != null &&
                            methodExpression.getReferenceName().equals("println")) {
                        ctx.append("console.log(");
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(")");
                        return true;
                    }
                } else if (methodQualifierExpression.getType().getCanonicalText().equals("Pattern")) {
                    if (methodExpression.getReferenceName() != null &&
                            methodExpression.getReferenceName().equals("matcher")) {
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(".test(");
                        ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                        ctx.append(")");
                        return true;
                    }
                } else if (methodQualifierExpression.getType().getCanonicalText().equalsIgnoreCase("Double")) {
                    if (methodExpression.getReferenceName() != null &&
                            methodExpression.getReferenceName().equals("longValue")) {
                        ctx.append("(");
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(" < 0 ? Math.ceil(");
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(") : Math.floor(");
                        ExpressionTranslator.translate(methodQualifierExpression, ctx);
                        ctx.append(")");
                        ctx.append(")");
                        return true;
                    }
                }
            } else if (methodQualifierExpression instanceof PsiReferenceExpression) {
                PsiReferenceExpression objectRef = (PsiReferenceExpression) methodQualifierExpression;
                if (objectRef.getQualifier() != null) {
                    if (element.getArgumentList().getExpressions().length > 0) {
                        if (objectRef.getQualifier().getText().equals("System")) {
                            if (objectRef.getReferenceName().equals("out")) {
                                ctx.append("console.log(");
                                ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                                ctx.append(")");
                                return true;
                            } else if (objectRef.getReferenceName().equals("err")) {
                                ctx.append("console.error(");
                                ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                                ctx.append(")");
                                return true;
                            }
                        }
                    }
                } else {
                    if (objectRef.getText().equals("Pattern")) {
                        if (methodExpression.getReferenceName().equals("compile")) {
                            ctx.append("new RegExp(");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            ctx.append(")");
                            return true;
                        }
                    } else if (objectRef.getText().equals("System")) {
                        if (methodExpression.getReferenceName().equals("currentTimeMillis")) {
                            ctx.append("Date.now()");
                            return true;
                        }
                    } else if (objectRef.getText().equals("Arrays")) {
                        if (methodExpression.getReferenceName().equals("sort")) {
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            ctx.append(".sort()");
                            return true;
                        }
                    } else if (objectRef.getText().equals("Double")) {
                        if (methodExpression.getReferenceName().equals("parseDouble")) {
                            ctx.append("parseFloat(");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            ctx.append(")");
                            return true;
                        }
                    } else if (objectRef.getText().equals("Assert")) {
                        if (methodExpression.getReferenceName().equals("assertEquals") || methodExpression.getReferenceName().equals("assertNotEquals")) {
                            if (element.getArgumentList().getExpressions().length == 3) {
                                ExpressionTranslator.translate(methodQualifierExpression, ctx);
                                ctx.append("." + methodExpression.getReferenceName() + "(");
                                ExpressionTranslator.translate(element.getArgumentList().getExpressions()[1], ctx);
                                ctx.append(",");
                                ExpressionTranslator.translate(element.getArgumentList().getExpressions()[2], ctx);
                                ctx.append(",");
                                ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                                ctx.append(")");
                                return true;
                            }
                        }
                    }
                }
            } else if (methodQualifierExpression instanceof PsiMethodCallExpression) { // remove the [] part in regEx.matcher(expr)[.matches()]
                PsiMethodCallExpression previousMethodCall = (PsiMethodCallExpression) methodQualifierExpression;

                if (previousMethodCall.getMethodExpression().getReferenceName().equals("toString")) {
                    if (methodExpression.getReferenceName().equals("length")) {
                        ExpressionTranslator.translate(previousMethodCall, ctx);
                        ctx.append(".length");
                        return true;
                    }
                }

                PsiElement previousMethodQualifier = previousMethodCall.getMethodExpression().getQualifier();
                if (previousMethodQualifier instanceof PsiReferenceExpression) {
                    PsiReferenceExpression previousMethodReference = (PsiReferenceExpression) previousMethodQualifier;
                    String previoudMethodName = previousMethodCall.getMethodExpression().getReferenceName();
                    if (previousMethodReference.getType() != null && previousMethodReference.getType().getCanonicalText().equals("Pattern")) {
                        if (previoudMethodName.equals("matcher")) {
                            ExpressionTranslator.translate(previousMethodCall, ctx);
                            return true;
                        }
                    } else if (previousMethodReference.getType() != null && previousMethodReference.getType().getCanonicalText().equals("String")) {

                    }
                }
            }
        }

        return false;
    }

}
