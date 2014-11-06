
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiIfStatement;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class IfStatementTranslator extends Translator<PsiIfStatement> {

    @Override
    public void translate(PsiElementVisitor visitor, PsiIfStatement element, TranslationContext ctx) {
        ctx.print("if (");
        element.getCondition().accept(visitor);
        ctx.append(") {\n");
        ctx.increaseIdent();
        element.getThenBranch().accept(visitor);
        ctx.decreaseIdent();
        if (element.getElseElement() != null) {
            ctx.print("} else {\n");
            ctx.increaseIdent();
            element.getElseBranch().accept(visitor);
            ctx.decreaseIdent();
            ctx.print("}\n");
        } else {
            ctx.print("}\n");
        }
    }

}
