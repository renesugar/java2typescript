
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class ExpressionStatementTranslator extends Translator<PsiExpressionStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiExpressionStatement element, TranslationContext ctx) {

    boolean loopDeclaration = false;

    PsiElement parent = element.getParent();
    if (parent instanceof PsiLoopStatement && !((PsiLoopStatement)parent).getBody().equals(element)) {
      loopDeclaration = true;
    }

    if (!loopDeclaration) {
      ctx.print("");
    }

    element.getExpression().accept(visitor);

    if (!loopDeclaration) {
      ctx.append(";\n");
    }
  }

}
