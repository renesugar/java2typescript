
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiTypeCastExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class TypeCastExpressionTranslator extends Translator<PsiTypeCastExpression> {

    @Override
    public void translate(PsiElementVisitor visitor, PsiTypeCastExpression element, TranslationContext ctx) {
        /*
        String type = TypeHelper.getType(element.getCastType().getType(), ctx);
        String fqn = ctx.getClassImports().get(type);
        if(fqn == null) {
            fqn = ctx.getClassPackage() + "." + type;
            ctx.append('<').append(type).append(TypeHelper.getGenericsIfAny(ctx, fqn)).append('>');
        } else {
            ctx.append('<').append(fqn).append(TypeHelper.getGenericsIfAny(ctx, fqn)).append('>');
        }
        element.getOperand().accept(visitor);
        */
    }

}
