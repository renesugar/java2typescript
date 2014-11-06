package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiClassObjectAccessExpression;
import com.intellij.psi.PsiElementVisitor;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class JavaClassObjectAccessExpressionTranslator extends Translator<PsiClassObjectAccessExpression> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiClassObjectAccessExpression element, TranslationContext ctx) {
    ctx.append(TypeHelper.getType(element.getOperand().getType(), ctx));
  }

}
