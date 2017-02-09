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

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiTryStatement;
import java2typescript.context.TranslationContext;
import java2typescript.helper.KeywordHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.translators.CodeBlockTranslator;

public class TryStatementTranslator {

    private static final String EXCEPTION_VAR = "$ex$";

    public static void translate(PsiTryStatement element, TranslationContext ctx) {
        ctx.print("try ");
        CodeBlockTranslator.translate(element.getTryBlock(), ctx);
        PsiCatchSection[] catchSections = element.getCatchSections();
        if (catchSections.length > 0) {
            ctx.append(" catch (").append(EXCEPTION_VAR).append(") {\n");
            ctx.increaseIdent();
            for (int i = 0; i < catchSections.length; i++) {
                PsiCatchSection catchSection = catchSections[i];
                String exceptionType = TypeHelper.printType(catchSection.getCatchType(), ctx);
                ctx.print("if (").append(EXCEPTION_VAR).append(" instanceof ").append(exceptionType).append(") {\n");
                ctx.increaseIdent();
                ctx.print("var ").append(KeywordHelper.process(catchSection.getParameter().getName(), ctx));
                ctx.append(": ").append(exceptionType).append(" = <").append(exceptionType).append(">").append(EXCEPTION_VAR).append(";\n");
                CodeBlockTranslator.translate(catchSection.getCatchBlock(), ctx);
                ctx.decreaseIdent();
                ctx.print("} else ");
            }

            ctx.append("{\n");
            ctx.increaseIdent();
            ctx.print("throw ").append(EXCEPTION_VAR).append(";\n");
            ctx.decreaseIdent();
            ctx.print("}\n");

            ctx.decreaseIdent();
            ctx.print("}");
        }
        if (element.getFinallyBlock() != null) {
            ctx.append(" finally {\n");

            ctx.increaseIdent();
            CodeBlockTranslator.translate(element.getFinallyBlock(), ctx);
            ctx.decreaseIdent();
            ctx.print("}");
        }
        ctx.append("\n");
    }

}
