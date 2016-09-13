
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.KeywordHelper;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;
import org.kevoree.modeling.java2typescript.translators.CodeBlockTranslator;

public class LambdaExpressionTranslator {

    public static void translate(PsiLambdaExpression element, TranslationContext ctx) {
        boolean asFunctionParameter = false;
        PsiElement parent = element.getParent();
        while (parent != null && !(parent instanceof PsiExpressionList)) {
            parent = parent.getParent();
        }
        if (parent != null && parent.getParent() != null && parent.getParent() instanceof PsiMethodCallExpression) {
            if (!TypeHelper.isCallbackClass(((PsiClassType.Stub) element.getFunctionalInterfaceType()).rawType().resolve())) {
                asFunctionParameter = true;
            }
        }
        if (asFunctionParameter) {
            ctx.append("(() => {let r:any=()=>{};r.");
            ctx.append(((PsiClassType.Stub) element.getFunctionalInterfaceType()).rawType().resolve().getMethods()[0].getName());
            ctx.append("=");
        }
        ctx.append("(");
        PsiParameter[] paramList = element.getParameterList().getParameters();
        for (int i = 0; i < paramList.length; i++) {
            PsiParameter param = paramList[i];
            ctx.append(KeywordHelper.process(param.getName(), ctx));
            if (i < paramList.length - 1) {
                ctx.append(", ");
            }
        }
        ctx.append(")=>");
        PsiElement body = element.getBody();
        if (body instanceof PsiCodeBlock) {
            //ctx.append("{\n");
            //ctx.increaseIdent();
            CodeBlockTranslator.translate((PsiCodeBlock) body, ctx);
            //ctx.decreaseIdent();
            //ctx.print("}");
        } else {
            ctx.append("(");
            ExpressionTranslator.translate((PsiExpression) body, ctx);
            ctx.append(")");
            //System.err.println("LambdaExpressionTranslator:: Unsupported body type:" + body.getClass().getName());
        }
        if (asFunctionParameter) {
            ctx.append(";return r;})()");
        }

    }

}
