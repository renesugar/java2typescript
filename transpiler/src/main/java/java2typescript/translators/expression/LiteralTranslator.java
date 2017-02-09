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

import com.intellij.psi.PsiLiteralExpression;
import java2typescript.context.TranslationContext;

public class LiteralTranslator {

    public static void translate(PsiLiteralExpression element, TranslationContext ctx) {
        String value = element.getText().trim();

        if (value.toLowerCase().equals("null")) {
            ctx.append(value);
            return;
        }

        if (value.toLowerCase().endsWith("l")) {
            ctx.append(value.substring(0, value.length() - 1));
            return;
        }

        if (value.toLowerCase().endsWith("f") || value.toLowerCase().endsWith("d")) {
            if (value.contains("0x")) {
                ctx.append(value);
            } else {
                ctx.append(value.substring(0, value.length() - 1));
            }
            return;
        }
        ctx.append(value);

    }

}
