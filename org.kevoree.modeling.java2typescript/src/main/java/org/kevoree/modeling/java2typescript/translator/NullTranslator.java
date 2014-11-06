
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class NullTranslator extends Translator<PsiElement> {

  public static final NullTranslator INSTANCE = new NullTranslator();

  @Override
  public void translate(PsiElementVisitor visitor, PsiElement element, TranslationContext ctx) {
    // Do nothing
  }

}
