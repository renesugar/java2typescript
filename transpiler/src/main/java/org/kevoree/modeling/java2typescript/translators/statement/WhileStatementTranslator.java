
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.PsiWhileStatement;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

public class WhileStatementTranslator {

    public static void translate(PsiWhileStatement element, TranslationContext ctx) {
        ctx.print("while (");
        ExpressionTranslator.translate(element.getCondition(), ctx);
        ctx.append(") ");
        StatementTranslator.translate(element.getBody(), ctx);
        ctx.append("\n");
    }

}
