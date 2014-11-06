package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class AssignmentExpressionTranslator extends Translator<PsiAssignmentExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiAssignmentExpression element, TranslationContext ctx) {
    element.getLExpression().accept(visitor);
    ctx.append(' ');
    element.getOperationSign().accept(visitor);
    ctx.append(' ');
    element.getRExpression().accept(visitor);
  }

}
