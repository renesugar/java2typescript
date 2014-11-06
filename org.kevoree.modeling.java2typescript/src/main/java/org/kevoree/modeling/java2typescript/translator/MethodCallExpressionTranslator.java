
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class MethodCallExpressionTranslator extends Translator<PsiMethodCallExpression> {

    @Override
    public void translate(PsiElementVisitor visitor, PsiMethodCallExpression element, TranslationContext ctx) {
        element.getMethodExpression().accept(visitor);
        if (!element.getMethodExpression().toString().endsWith(".length")) {
            ctx.append('(');
            PsiExpression[] arguments = element.getArgumentList().getExpressions();
            for (int i = 0; i < arguments.length; i++) {
                arguments[i].accept(visitor);
                if (i != arguments.length - 1) {
                    ctx.append(", ");
                }
            }
            ctx.append(")");
        }
    }

}
