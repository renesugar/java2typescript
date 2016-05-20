
package org.kevoree.modeling.java2typescript.translators.expression;

import com.intellij.psi.PsiInstanceOfExpression;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.helper.TypeHelper;

public class InstanceOfExpressionTranslator {

    public static void translate(PsiInstanceOfExpression element, TranslationContext ctx) {

        String rightHandType = TypeHelper.printType(element.getCheckType().getType(), ctx, false, false);

        if (!isNativeArray(rightHandType) && element.getCheckType().getType().getArrayDimensions() == 1) {
            if(element.getCheckType().getText().equals("Object[]")){
                ctx.append("Array.isArray(");
                ExpressionTranslator.translate(element.getOperand(), ctx);
                ctx.append(")");
            } else {
                ctx.append("arrayInstanceOf(");
                ExpressionTranslator.translate(element.getOperand(), ctx);
                ctx.append(", ");
                ctx.append(rightHandType);
                ctx.append(")");
            }
        } else {
            ExpressionTranslator.translate(element.getOperand(), ctx);
            ctx.append(" instanceof ");
            if(rightHandType.equals("number") || rightHandType.equals("string") || rightHandType.equals("boolean")) {
                ctx.append(rightHandType.substring(0, 1).toUpperCase()).append(rightHandType.substring(1));
            } else {
                ctx.append(rightHandType);
            }
        }
    }

    private static boolean isNativeArray(String type) {
        return type.equals("Float64Array") || type.equals("Int32Array");
    }

}
