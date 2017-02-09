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

import com.intellij.psi.PsiArrayInitializerExpression;
import com.intellij.psi.PsiExpression;
import java2typescript.context.TranslationContext;

public class ArrayInitializerExpressionTranslator {

    public static void translate(PsiArrayInitializerExpression element, TranslationContext ctx) {
        boolean hasToBeClosed, isByteArray = false;
        if (ctx.NATIVE_ARRAY && element.getType() != null && element.getType().equalsToText("int[]")) {
            ctx.append("new Int32Array([");
            hasToBeClosed = true;
        } else if (ctx.NATIVE_ARRAY && element.getType() != null && element.getType().equalsToText("double[]")) {
            ctx.append("new Float64Array([");
            hasToBeClosed = true;
        } else if (ctx.NATIVE_ARRAY && element.getType() != null && element.getType().equalsToText("long[]")) {
            ctx.append("new Float64Array([");
            hasToBeClosed = true;
        } else if (ctx.NATIVE_ARRAY && element.getType() != null && element.getType().equalsToText("byte[]")) {
            ctx.append("new Int8Array([");
            hasToBeClosed = true;
        } else {
            ctx.append("[");
            hasToBeClosed = false;
        }
        PsiExpression[] initializers = element.getInitializers();
        if (element.getType() != null &&
                element.getType().getPresentableText().equals("byte[]")) {
            isByteArray = true;
        }
        if (initializers.length > 0) {
            for (int i = 0; i < initializers.length; i++) {
                ExpressionTranslator.translate(initializers[i], ctx);
                if (isByteArray &&
                        initializers[i].getType() != null &&
                        initializers[i].getType().getPresentableText().equals("char")) {
                    ctx.append(".charCodeAt(0)");
                }
                if (i != initializers.length - 1) {
                    ctx.append(", ");
                }
            }
        }
        ctx.append("]");
        if (hasToBeClosed) {
            ctx.append(")");
        }
    }
}
