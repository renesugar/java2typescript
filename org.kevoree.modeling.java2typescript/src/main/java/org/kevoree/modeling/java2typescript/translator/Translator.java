
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public abstract class Translator<T extends PsiElement> {

  public abstract void translate(PsiElementVisitor visitor, T element, TranslationContext ctx);

}
