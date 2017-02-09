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

import com.intellij.psi.PsiAssignmentExpression;
import java2typescript.context.TranslationContext;
import java2typescript.translators.JavaTokenTranslator;

public class AssignmentExpressionTranslator {

    public static void translate(PsiAssignmentExpression element, TranslationContext ctx) {
        ExpressionTranslator.translate(element.getLExpression(), ctx);
        ctx.append(' ');
        JavaTokenTranslator.translate(element.getOperationSign(), ctx);
        ctx.append(' ');
        ExpressionTranslator.translate(element.getRExpression(), ctx);

        /*
        if (element.getLExpression().getType() != null &&
                element.getRExpression() != null &&
                element.getRExpression().getType() != null &&
                element.getLExpression().getType().getPresentableText().equals("byte") &&
                element.getRExpression().getType().getPresentableText().equals("char")) {
            ctx.append(".charCodeAt(0)");
        }
        */
    }

}
