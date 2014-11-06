
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiContinueStatement;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ContinueStatementTranslator extends Translator<PsiContinueStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiContinueStatement element, TranslationContext ctx) {
    ctx.print("continue");
    if (element.getLabelIdentifier() != null) {
      ctx.append(' ');
      ctx.append(element.getLabelIdentifier().getText().trim());
    }
    ctx.append(";\n");
  }

}
