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
import com.intellij.psi.PsiForStatement;
import java2typescript.context.TranslationContext;
import java2typescript.translators.expression.ExpressionTranslator;

public class ForStatementTranslator {

    public static void translate(PsiForStatement element, TranslationContext ctx) {
        ctx.print("for (");
        if (element.getInitialization() != null) {
            StatementTranslator.translate(element.getInitialization(), ctx);
        }
        ctx.append("; ");
        if (element.getCondition() != null) {
            ExpressionTranslator.translate(element.getCondition(), ctx);
        }
        ctx.append("; ");
        if (element.getUpdate() != null) {
            StatementTranslator.translate(element.getUpdate(), ctx);
        }

        ctx.append(")");
        if(element.getBody() != null) {
            if( !(element.getBody() instanceof PsiBlockStatement)) {
                ctx.append("\n");
                ctx.increaseIdent();
                StatementTranslator.translate(element.getBody(), ctx);
                ctx.decreaseIdent();
            } else {
                ctx.append(" ");
                StatementTranslator.translate(element.getBody(), ctx);
            }
        }
        ctx.append("\n");

    }

}
