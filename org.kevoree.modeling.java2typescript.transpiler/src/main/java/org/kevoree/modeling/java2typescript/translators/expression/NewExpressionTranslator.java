
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;
import org.kevoree.modeling.java2typescript.TypeHelper;
import org.kevoree.modeling.java2typescript.translators.AnonymousClassTranslator;

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
                //PsiElement resolved = classReference.resolve();
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
                if (anonymousClass == null) {
                    ctx.append("new ").append(className).append('(');
                    if (element.getArgumentList() != null) {
                        PsiExpression[] arguments = element.getArgumentList().getExpressions();
                        for (int i = 0; i < arguments.length; i++) {
                            ExpressionTranslator.translate(arguments[i], ctx);
                            if (i != arguments.length - 1) {
                                ctx.append(", ");
                            }
                        }
                    }
                    ctx.append(')');
                }
            } else {
                if (arrayInitializer != null) {
                    boolean hasToBeClosed;
                    if (element.getType().equalsToText("int[]")) {
                        ctx.append("new Int32Array([");
                        hasToBeClosed = true;
                    } else if (element.getType().equalsToText("double[]")) {
                        ctx.append("new Float64Array([");
                        hasToBeClosed = true;
                    } else if (element.getType().equalsToText("long[]")) {
                        ctx.append("new Float64Array([");
                        hasToBeClosed = true;
                    } else {
                        ctx.append("[");
                        hasToBeClosed = false;
                    }
                    PsiExpression[] arrayInitializers = arrayInitializer.getInitializers();
                    for (int i = 0; i < arrayInitializers.length; i++) {
                        ExpressionTranslator.translate(arrayInitializers[i], ctx);
                        if (i != arrayInitializers.length - 1) {
                            ctx.append(", ");
                        }
                    }
                    ctx.append("]");
                    if (hasToBeClosed) {
                        ctx.append(")");
                    }
                } else {
                    int dimensionCount = arrayDimensions.length;
                    if (dimensionCount == 1) {
                        if (element.getType().equalsToText("int[]")) {
                            ctx.append("new Int32Array(");
                            ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                            ctx.append(")");
                        } else if (element.getType().equalsToText("double[]")) {
                            ctx.append("new Float64Array(");
                            ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                            ctx.append(")");
                        } else if (element.getType().equalsToText("long[]")) {
                            ctx.append("new Float64Array(");
                            ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                            ctx.append(")");
                        } else {
                            ctx.append("new Array()");
                        }
                    } else {
                        for (int i = 0; i < dimensionCount; i++) {
                            ctx.append("new Array(");
                        }
                        for (int i = 0; i < dimensionCount; i++) {
                            ctx.append(")");
                        }
                    }
                }
            }
        }
    }
}


