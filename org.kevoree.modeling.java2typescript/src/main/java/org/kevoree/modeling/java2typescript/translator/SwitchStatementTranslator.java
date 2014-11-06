
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiSwitchStatement;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class SwitchStatementTranslator extends Translator<PsiSwitchStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiSwitchStatement element, TranslationContext ctx) {
    ctx.print("switch (");
    element.getExpression().accept(visitor);
    ctx.append(") {\n");

    ctx.increaseIdent();
    element.getBody().accept(visitor);
    ctx.decreaseIdent();

    ctx.print("}\n");
  }

}
