
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class BinaryExpressionTranslator extends Translator<PsiBinaryExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiBinaryExpression element, TranslationContext ctx) {
    element.getLOperand().accept(visitor);
    ctx.append(' ');
    element.getOperationSign().accept(visitor);
    ctx.append(' ');
    element.getROperand().accept(visitor);
  }

}
