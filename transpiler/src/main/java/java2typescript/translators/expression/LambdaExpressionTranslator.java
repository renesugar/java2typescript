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
import java2typescript.helper.KeywordHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.translators.CodeBlockTranslator;

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
