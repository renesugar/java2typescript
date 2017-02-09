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
import java2typescript.translators.CodeBlockTranslator;

/**
 * Created by duke on 11/6/14.
 */
public class StatementTranslator {

    public static void translate(PsiStatement statement, TranslationContext ctx) {
        if (statement instanceof PsiExpressionStatement) {
            ExpressionStatementTranslator.translate((PsiExpressionStatement) statement, ctx);
        } else if (statement instanceof PsiIfStatement) {
            IfStatementTranslator.translate((PsiIfStatement) statement, ctx, false);
        } else if (statement instanceof PsiReturnStatement) {
            ReturnStatementTranslator.translate((PsiReturnStatement) statement, ctx);
        } else if (statement instanceof PsiWhileStatement) {
            WhileStatementTranslator.translate((PsiWhileStatement) statement, ctx);
        } else if (statement instanceof PsiBlockStatement) {
            CodeBlockTranslator.translate(((PsiBlockStatement) statement).getCodeBlock(), ctx);
        } else if (statement instanceof PsiForStatement) {
            ForStatementTranslator.translate((PsiForStatement) statement, ctx);
        } else if (statement instanceof PsiThrowStatement) {
            ThrowStatementTranslator.translate((PsiThrowStatement) statement, ctx);
        } else if (statement instanceof PsiDoWhileStatement) {
            DoWhileStatementTranslator.translate((PsiDoWhileStatement) statement, ctx);
        } else if (statement instanceof PsiBreakStatement) {
            BreakStatementTranslator.translate((PsiBreakStatement) statement, ctx);
        } else if (statement instanceof PsiTryStatement) {
            TryStatementTranslator.translate((PsiTryStatement) statement, ctx);
        } else if (statement instanceof PsiContinueStatement) {
            ContinueStatementTranslator.translate((PsiContinueStatement) statement, ctx);
        } else if (statement instanceof PsiSwitchStatement) {
            SwitchStatementTranslator.translate((PsiSwitchStatement) statement, ctx);
        } else if (statement instanceof PsiForeachStatement) {
            ForEachStatementTranslator.translate((PsiForeachStatement) statement, ctx);
        } else if (statement instanceof PsiDeclarationStatement) {
            DeclarationStatementTranslator.translate((PsiDeclarationStatement) statement, ctx);
        } else if (statement instanceof PsiExpressionListStatement) {
            ExpressionListStatementTranslator.translate((PsiExpressionListStatement) statement, ctx);
        } else if (statement instanceof PsiSynchronizedStatement) {
            SynchronizedStatementTranslator.translate((PsiSynchronizedStatement) statement, ctx);
        } else if (statement instanceof PsiEmptyStatement) {
            //Ignore
        } else if (statement instanceof PsiAssertStatement) {
            //Ignore
        } else {
            System.err.println("StatementTranslator ! "+statement.toString());
        }
    }

}
