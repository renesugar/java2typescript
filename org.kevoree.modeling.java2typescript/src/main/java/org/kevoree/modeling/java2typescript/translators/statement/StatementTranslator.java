package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

/**
 * Created by duke on 11/6/14.
 */
public class StatementTranslator {

    public static void translate(PsiStatement statement, TranslationContext ctx) {
        if (statement instanceof PsiExpressionStatement) {
            ExpressionTranslator.translate(((PsiExpressionStatement) statement).getExpression(), ctx);
        } else if (statement instanceof PsiIfStatement) {
            IfStatementTranslator.translate((PsiIfStatement) statement, ctx);
        } else if (statement instanceof PsiReturnStatement) {
            ReturnStatementTranslator.translate((PsiReturnStatement) statement, ctx);
        } else {
            System.err.println("STMT " + statement);
        }


        /*
        if (expression instanceof PsiLiteralExpression) {
            LiteralTranslator.translate((PsiLiteralExpression) expression, ctx);
        } else if (expression instanceof PsiNewExpression) {
            NewExpressionTranslator.translate((PsiNewExpression) expression, ctx);
        } else if (expression instanceof PsiBinaryExpression) {
            BinaryExpressionTranslator.translate((PsiBinaryExpression) expression, ctx);
        } else if (expression instanceof PsiReferenceExpression) {
            ReferenceExpressionTranslator.translate((PsiReferenceExpression) expression, ctx);
        } else if (expression instanceof PsiPrefixExpression) {
            PrefixExpressionTranslator.translate((PsiPrefixExpression) expression, ctx);
        } else {
        }*/


    }

}
