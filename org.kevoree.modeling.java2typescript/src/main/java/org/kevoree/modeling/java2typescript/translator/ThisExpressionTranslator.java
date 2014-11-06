
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiThisExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ThisExpressionTranslator extends Translator<PsiThisExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiThisExpression element, TranslationContext ctx) {
    ctx.append("this");
  }

}
