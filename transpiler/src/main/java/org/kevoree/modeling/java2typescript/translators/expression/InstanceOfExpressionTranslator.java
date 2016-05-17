
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.PsiInstanceOfExpression;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;

public class InstanceOfExpressionTranslator {

    public static void translate(PsiInstanceOfExpression element, TranslationContext ctx) {
        if (element.getCheckType().getType().getArrayDimensions() == 1) {
            if(element.getCheckType().getText().equals("Object[]")){
                ctx.append("Array.isArray(");
                ExpressionTranslator.translate(element.getOperand(), ctx);
                ctx.append(")");
            } else {
                ctx.append("arrayInstanceOf(");
                ExpressionTranslator.translate(element.getOperand(), ctx);
                ctx.append(", ");
                ctx.append(TypeHelper.printType(element.getCheckType().getType(), ctx, false, false));
                ctx.append(")");
            }
        } else {
            ExpressionTranslator.translate(element.getOperand(), ctx);
            ctx.append(" instanceof ");
            String type = TypeHelper.printType(element.getCheckType().getType(), ctx, false, false);
            if(type.equals("number") || type.equals("string") || type.equals("boolean")) {
                ctx.append(type.substring(0, 1).toUpperCase()).append(type.substring(1));
            } else {
                ctx.append(type);
            }
        }
    }

}
