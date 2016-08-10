
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.KeywordHelper;
import org.kevoree.modeling.java2typescript.translators.CodeBlockTranslator;

public class LambdaExpressionTranslator {

    public static void translate(PsiLambdaExpression element, TranslationContext ctx) {
        ctx.append("(");
        PsiParameter[] paramList = element.getParameterList().getParameters();
        for (int i = 0; i < paramList.length; i++) {
            PsiParameter param = paramList[i];
            ctx.append(KeywordHelper.process(param.getName(), ctx));
            if (i < paramList.length - 1) {
                ctx.append(", ");
            }
        }
        ctx.append(") => {\n");
        ctx.increaseIdent();
        PsiElement body = element.getBody();
        if (body instanceof PsiCodeBlock) {
            CodeBlockTranslator.translate((PsiCodeBlock) body, ctx);
        /*} else if (body instanceof PsiMethodCallExpression) {
            MethodCallExpressionTranslator.translate((PsiMethodCallExpression) body, ctx);*/
        } else {
            ExpressionTranslator.translate((PsiExpression)body, ctx);
            //System.err.println("LambdaExpressionTranslator:: Unsupported body type:" + body.getClass().getName());
        }
        ctx.decreaseIdent();
        ctx.print("}");
    }

}
