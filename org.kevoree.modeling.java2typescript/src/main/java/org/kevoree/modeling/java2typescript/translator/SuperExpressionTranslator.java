
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSuperExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class SuperExpressionTranslator extends Translator<PsiSuperExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiSuperExpression element, TranslationContext ctx) {
    ctx.append("super");
  }

}
