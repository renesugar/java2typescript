
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.PsiDoWhileStatement;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

public class DoWhileStatementTranslator {

  public static void translate(PsiDoWhileStatement element, TranslationContext ctx) {
    ctx.print("do ");
    StatementTranslator.translate(element.getBody(), ctx);
    ctx.append(" while (");
    ExpressionTranslator.translate(element.getCondition(),ctx);
    ctx.append(");\n");
  }
}
