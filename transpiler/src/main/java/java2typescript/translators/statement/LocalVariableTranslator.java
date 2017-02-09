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
package java2typescript.translators.statement;

import com.intellij.psi.*;
import java2typescript.context.TranslationContext;
import java2typescript.helper.KeywordHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.translators.expression.ExpressionTranslator;

public class LocalVariableTranslator {

    public static void translate(PsiLocalVariable element, TranslationContext ctx) {
        PsiElement parent = element.getParent();
        boolean loopDeclaration = false;

        if (parent instanceof PsiDeclarationStatement) {
            parent = parent.getParent();
            if (parent instanceof PsiLoopStatement) {
                loopDeclaration = true;
            }
        }
        if (element.getPrevSibling() == null) {
            if (loopDeclaration) {
                ctx.append("let ");
            } else {
                ctx.print("let ");
            }
        }

        ctx.append(KeywordHelper.process(element.getName(), ctx));

        // explicit local variable type
        ctx.append(": ");
        ctx.append(TypeHelper.printType(element.getType(), ctx));

        if (element.hasInitializer() && element.getInitializer() != null) {
            ctx.append(" = ");
            ExpressionTranslator.translate(element.getInitializer(), ctx);
            if (element.getType().getPresentableText().equals("byte") &&
                    element.getInitializer().getType() != null &&
                    element.getInitializer().getType().getPresentableText().equals("char")) {
                ctx.append(".charCodeAt(0)");
            }
        }

        boolean listDecl = false;
        PsiElement next = element.getNextSibling();
        while (next instanceof PsiWhiteSpace) {
            next = next.getNextSibling();
        }
        if (next instanceof PsiJavaToken) {
            listDecl = true;
        }

        if (!loopDeclaration && !listDecl) {
            ctx.append(";");
            ctx.append("\n");
        }
    }
}
