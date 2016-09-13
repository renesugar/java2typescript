
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.CodeBlockTranslator;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

public class SwitchStatementTranslator {

    public static void translate(PsiSwitchStatement element, TranslationContext ctx) {
        ctx.print("switch (");
        ExpressionTranslator.translate(element.getExpression(), ctx);
        ctx.append(") {\n");
        ctx.increaseIdent();
        PsiStatement prev = null;

        for(PsiStatement s : element.getBody().getStatements()) {
            if(s instanceof PsiSwitchLabelStatement) {
                if(prev != null && (prev instanceof PsiSwitchLabelStatement || prev instanceof PsiBlockStatement)) {
                    ctx.append("\n");
                }
                PsiSwitchLabelStatement label = (PsiSwitchLabelStatement)s;
                if (label.isDefaultCase()) {
                    ctx.print("default: \n");
                } else {
                    ctx.print("case ");
                    ExpressionTranslator.translate(label.getCaseValue(), ctx);
                    ctx.append(":");
                }
            } else if(s instanceof PsiExpressionStatement){
                ctx.append("\n");
                ctx.increaseIdent();
                ctx.print("");
                ExpressionTranslator.translate(((PsiExpressionStatement)s).getExpression(), ctx);
                ctx.append(";\n");
                ctx.decreaseIdent();
            } else if(s instanceof PsiBreakStatement){
                BreakStatementTranslator.translate(((PsiBreakStatement)s), ctx);
            } else if(s instanceof PsiBlockStatement){
                ctx.append(" ");
                CodeBlockTranslator.translate(((PsiBlockStatement)s).getCodeBlock(), ctx);
            } else if(s instanceof PsiReturnStatement){
                ctx.append("\n");
                ctx.increaseIdent();
                ReturnStatementTranslator.translate(((PsiReturnStatement)s), ctx);
                ctx.decreaseIdent();
            } else {
                StatementTranslator.translate(s, ctx);
            }
            prev = s;
        }
        ctx.decreaseIdent();
        ctx.print("}\n");
    }

}
