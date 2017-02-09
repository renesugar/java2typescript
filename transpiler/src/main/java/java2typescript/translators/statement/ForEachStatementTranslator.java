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

import com.intellij.psi.PsiForeachStatement;
import com.intellij.psi.PsiParameter;
import java2typescript.context.TranslationContext;
import java2typescript.helper.KeywordHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.translators.expression.ExpressionTranslator;

public class ForEachStatementTranslator {

    public static void translate(PsiForeachStatement element, TranslationContext ctx) {
        PsiParameter parameter = element.getIterationParameter();
        ctx.print("");
        ExpressionTranslator.translate(element.getIteratedValue(), ctx);
        ctx.append(".forEach((");
        ctx.append(KeywordHelper.process(parameter.getName(), ctx));
        ctx.append(": ");
        ctx.append(TypeHelper.printType(parameter.getType(), ctx));
        ctx.append(") => {\n");
        ctx.increaseIdent();
        StatementTranslator.translate(element.getBody(), ctx);
        ctx.decreaseIdent();
        ctx.print("});\n");
    }
}
