package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;
import org.kevoree.modeling.java2typescript.TypeHelper;

public class LocalVariableTranslator extends Translator<PsiLocalVariable> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiLocalVariable element, TranslationContext ctx) {
    boolean loopDeclaration = false;

    PsiElement parent = element.getParent();
    if (parent instanceof PsiDeclarationStatement) {
      parent = parent.getParent();
      if (parent instanceof PsiLoopStatement) {
        loopDeclaration = true;
      }
    }

    if (loopDeclaration) {
      ctx.append("var ");
    } else {
      ctx.print("var ");
    }


    ctx.append(element.getName());
    ctx.append(": ");
    ctx.append(TypeHelper.getVariableType(element, ctx));

    if (element.hasInitializer()) {
      ctx.append(" = ");
      element.getInitializer().accept(visitor);
    }

    if (!loopDeclaration) {
      ctx.append(";");
      ctx.append("\n");
    }
  }
}
