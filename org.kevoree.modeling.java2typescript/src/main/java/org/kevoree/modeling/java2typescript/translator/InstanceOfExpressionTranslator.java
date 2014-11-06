
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiInstanceOfExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class InstanceOfExpressionTranslator extends Translator<PsiInstanceOfExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiInstanceOfExpression element, TranslationContext ctx) {
    element.getOperand().accept(visitor);
    ctx.append(" instanceof ").append(TypeHelper.getType(element.getCheckType().getType(), ctx, false));
  }

}
