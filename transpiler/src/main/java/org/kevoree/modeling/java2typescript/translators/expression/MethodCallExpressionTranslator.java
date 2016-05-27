
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;

public class MethodCallExpressionTranslator {

    public static void translate(PsiMethodCallExpression element, TranslationContext ctx) {
        PsiReferenceExpression methodExpr = element.getMethodExpression();
        PsiExpression methodQualifierExpression = methodExpr.getQualifierExpression();

        if (methodQualifierExpression != null) {
            if (methodQualifierExpression.getType() != null) {
                if(methodQualifierExpression.getType() instanceof PsiClassReferenceType) {
                    PsiClass cls = ((PsiClassReferenceType)methodQualifierExpression.getType()).resolve();
                    if(cls != null) {
                        if(TypeHelper.isCallbackClass(cls)) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append('(');
                            printParameters(element.getArgumentList().getExpressions(), ctx);
                            ctx.append(")");
                            return;
                        }
                    }
                }
            }
        }


        boolean hasBeenTransformed = tryNativeTransform(element, ctx);
        if (!hasBeenTransformed) {
            ReferenceExpressionTranslator.translate(methodExpr, ctx);
            ctx.append('(');
            printParameters(element.getArgumentList().getExpressions(), ctx);
            ctx.append(")");
        }
    }

    private static void printParameters(PsiExpression[] arguments, TranslationContext ctx) {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] instanceof PsiReferenceExpression) {
                ReferenceExpressionTranslator.translate((PsiReferenceExpression) arguments[i], ctx);
            } else {
                ExpressionTranslator.translate(arguments[i], ctx);
            }
            if (i != arguments.length - 1) {
                ctx.append(", ");
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
                }
            } else if (methodQualifierExpression instanceof PsiReferenceExpression) {
                PsiReferenceExpression objectRef = (PsiReferenceExpression) methodQualifierExpression;
                if (objectRef.getQualifier() != null) {
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
                    } else if (objectRef.getText().equals("Assert")) {
                        if (methodExpression.getReferenceName().equals("assertEquals")) {
                            if(element.getArgumentList().getExpressions().length == 3) {
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
