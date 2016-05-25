
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.PsiLiteralExpression;
import org.kevoree.modeling.java2typescript.context.TranslationContext;

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
