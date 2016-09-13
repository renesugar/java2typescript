
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.PsiBlockStatement;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiForStatement;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.CodeBlockTranslator;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

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
