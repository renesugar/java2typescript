/**
 * Copyright 2017 The Java2TypeScript Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java2typescript.translators.expression;

import com.intellij.psi.PsiInstanceOfExpression;
import java2typescript.context.TranslationContext;
import java2typescript.helper.TypeHelper;

public class InstanceOfExpressionTranslator {

    public static void translate(PsiInstanceOfExpression element, TranslationContext ctx) {

        String rightHandType = TypeHelper.printType(element.getCheckType().getType(), ctx, false, false);

        if (!isNativeArray(rightHandType) && element.getCheckType().getType().getArrayDimensions() == 1) {
            if (element.getCheckType().getText().equals("Object[]")) {
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
        } else if(rightHandType.equals("string")) {
            ctx.append("typeof(");
            ExpressionTranslator.translate(element.getOperand(), ctx);
            ctx.append(") === \"string\"");
        } else {
            ExpressionTranslator.translate(element.getOperand(), ctx);
            ctx.append(" instanceof ");
            if(rightHandType.equals("number") || rightHandType.equals("boolean")) {
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
