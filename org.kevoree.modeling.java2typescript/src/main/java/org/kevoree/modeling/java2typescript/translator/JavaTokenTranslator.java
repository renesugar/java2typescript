
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.impl.source.tree.ElementType;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class JavaTokenTranslator extends Translator<PsiJavaToken> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiJavaToken element, TranslationContext ctx) {
    if (ElementType.OPERATION_BIT_SET.contains(element.getTokenType())) {
      ctx.append(element.getText());
    }
  }

}
