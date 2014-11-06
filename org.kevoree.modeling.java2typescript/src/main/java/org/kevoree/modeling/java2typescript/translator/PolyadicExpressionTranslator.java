
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiPolyadicExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class PolyadicExpressionTranslator extends Translator<PsiPolyadicExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiPolyadicExpression element, TranslationContext ctx) {
    for (PsiExpression expression: element.getOperands()) {
      PsiJavaToken token = element.getTokenBeforeOperand(expression);
      if (token != null) {
        ctx.append(' ');
        token.accept(visitor);
        ctx.append(' ');
      }

      expression.accept(visitor);
    }
  }
}
