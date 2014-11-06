
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiWhileStatement;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class WhileStatementTranslator extends Translator<PsiWhileStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiWhileStatement element, TranslationContext ctx) {
    ctx.print("while (");
    element.getCondition().accept(visitor);
    ctx.append("){\n");

    ctx.increaseIdent();
    element.getBody().accept(visitor);
    ctx.decreaseIdent();

    ctx.print("}\n");
  }

}
