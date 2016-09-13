
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.PsiBlockStatement;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiIfStatement;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

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
