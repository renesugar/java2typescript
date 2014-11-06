
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiThrowStatement;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ThrowStatementTranslator extends Translator<PsiThrowStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiThrowStatement element, TranslationContext ctx) {
    ctx.print("throw ");
    element.getException().accept(visitor);
    ctx.append(";\n");
  }

}
