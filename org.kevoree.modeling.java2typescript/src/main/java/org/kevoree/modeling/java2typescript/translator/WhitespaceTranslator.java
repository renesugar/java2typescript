
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiWhiteSpace;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class WhitespaceTranslator extends Translator<PsiWhiteSpace> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiWhiteSpace element, TranslationContext ctx) {
    // ctx.print(element.getText());
  }

}
