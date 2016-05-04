
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;

public class MethodCallExpressionTranslator {

    public static void translate(PsiMethodCallExpression element, TranslationContext ctx) {
        PsiReferenceExpression methodExpr = element.getMethodExpression();
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
                if (methodQualifierExpression.getType().getCanonicalText().equals("String")) {
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
                } else if (methodQualifierExpression.getType().getCanonicalText().equals("AtomicBoolean")) {
                    if (methodExpression.getReferenceName() != null) {
                        if (methodExpression.getReferenceName().equals("get")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("compareAndSet")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(" = ");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[1], ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("set")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(" = ");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[0], ctx);
                            return true;
                        }
                    }

                } else if (methodQualifierExpression.getType().getCanonicalText().equals("AtomicInteger")) {
                    if (methodExpression.getReferenceName() != null) {
                        if (methodExpression.getReferenceName().equals("get")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("getAndIncrement")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append("++");
                            return true;
                        } else if (methodExpression.getReferenceName().equals("incrementAndGet")) {
                            ctx.append("++");
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            return true;
                        } else if (methodExpression.getReferenceName().equals("compareAndSet")) {
                            ExpressionTranslator.translate(methodQualifierExpression, ctx);
                            ctx.append(" = ");
                            ExpressionTranslator.translate(element.getArgumentList().getExpressions()[1], ctx);
                            return true;
                        }
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
                    }
                }
            } else if (methodQualifierExpression instanceof PsiMethodCallExpression) { // remove the [] part in regEx.matcher(expr)[.matches()]
                PsiMethodCallExpression previousMethodCall = (PsiMethodCallExpression) methodQualifierExpression;
                PsiReferenceExpression previousMethodQualifier = (PsiReferenceExpression) previousMethodCall.getMethodExpression().getQualifier();
                String previoudMethodName = previousMethodCall.getMethodExpression().getReferenceName();
                if (previousMethodQualifier.getType().getCanonicalText().equals("Pattern")) {
                    if (previoudMethodName.equals("matcher")) {
                        ExpressionTranslator.translate(previousMethodCall, ctx);
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
