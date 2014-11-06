
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiArrayAccessExpression;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ArrayAccessExpressionTranslator extends Translator<PsiArrayAccessExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiArrayAccessExpression element, TranslationContext ctx) {
    element.getArrayExpression().accept(visitor);
    ctx.append('[');
    element.getIndexExpression().accept(visitor);
    ctx.append(']');
  }

}
