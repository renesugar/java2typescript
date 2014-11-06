
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiDoWhileStatement;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class DoWhileStatementTranslator extends Translator<PsiDoWhileStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiDoWhileStatement element, TranslationContext ctx) {
    ctx.print("do {\n");

    ctx.increaseIdent();
    element.getBody().accept(visitor);
    ctx.decreaseIdent();

    ctx.print("} while (");
    element.getCondition().accept(visitor);
    ctx.append(")\n");
  }
}
