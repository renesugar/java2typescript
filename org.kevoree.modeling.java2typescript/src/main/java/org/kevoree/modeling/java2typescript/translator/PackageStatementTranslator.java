package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPackageStatement;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class PackageStatementTranslator extends Translator<PsiPackageStatement> {

  @Override
  public void translate(PsiElementVisitor visitor, PsiPackageStatement element, TranslationContext ctx) {
    //ctx.setClassPackage(element.getPackageName());
  }

}
