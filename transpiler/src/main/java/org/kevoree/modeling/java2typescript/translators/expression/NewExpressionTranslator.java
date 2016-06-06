
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.helper.GenericHelper;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;
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
                        ExpressionListTranslator.translate(element.getArgumentList(), ctx);
                    }
                    ctx.append(')');
                }
            } else {
                if (arrayInitializer != null) {
                    ArrayInitializerExpressionTranslator.translate(arrayInitializer, ctx);
                } else {
                    String arrayBaseType = TypeHelper.printArrayBaseType(element.getType());

                    int dimensionCount = element.getType().getArrayDimensions();
                    if(arrayBaseType != null) {
                        ctx.append("new ");
                        for(int i = 1; i < dimensionCount; i++) {
                            ctx.append("Array<");
                        }
                        ctx.append(arrayBaseType);
                        for(int i = 1; i < dimensionCount; i++) {
                            ctx.append(">");
                        }
                        ctx.append("(");
                        ExpressionTranslator.translate(element.getArrayDimensions()[0], ctx);
                        ctx.append(")");
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

}


