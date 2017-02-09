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

import com.intellij.psi.PsiBlockStatement;
import com.intellij.psi.PsiIfStatement;
import java2typescript.context.TranslationContext;
import java2typescript.translators.expression.ExpressionTranslator;

public class IfStatementTranslator {

    public static void translate(PsiIfStatement element, TranslationContext ctx, boolean elseif) {
        if (elseif) {
            ctx.append("if (");
        } else {
            ctx.print("if (");
        }
        ExpressionTranslator.translate(element.getCondition(), ctx);
        ctx.append(")");
        if (!(element.getThenBranch() instanceof PsiBlockStatement)) {
            ctx.append("\n");
            ctx.increaseIdent();
            StatementTranslator.translate(element.getThenBranch(), ctx);
            ctx.decreaseIdent();
        } else {
            ctx.append(" ");
            StatementTranslator.translate(element.getThenBranch(), ctx);
        }
        if (element.getElseElement() != null) {
            if (!(element.getThenBranch() instanceof PsiBlockStatement)) {
                ctx.print("else");
            } else {
                ctx.append(" else");
            }
            if (element.getElseBranch() instanceof PsiIfStatement) {
                ctx.append(" ");
                IfStatementTranslator.translate((PsiIfStatement) element.getElseBranch(), ctx, true);
            } else {
                if (!(element.getElseBranch() instanceof PsiBlockStatement)) {
                    ctx.append("\n");
                    ctx.increaseIdent();
                    StatementTranslator.translate(element.getElseBranch(), ctx);
                    ctx.decreaseIdent();
                } else {
                    ctx.append(" ");
                    StatementTranslator.translate(element.getElseBranch(), ctx);
                }
            }
            ctx.append("\n");
        } else {
            ctx.append("\n");
        }
    }

}
