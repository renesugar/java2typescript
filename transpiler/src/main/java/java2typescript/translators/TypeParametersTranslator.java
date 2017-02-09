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
package java2typescript.translators;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiTypeParameter;
import java2typescript.context.TranslationContext;
import java2typescript.helper.TypeHelper;

/**
 *
 * Created by leiko on 23/11/15.
 */
public class TypeParametersTranslator {

    public static String print(PsiTypeParameter[] parameters, TranslationContext ctx) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            PsiTypeParameter p = parameters[i];
            builder.append(p.getName());
            PsiClassType[] extensions = p.getExtendsList().getReferencedTypes();
            if (extensions.length > 0) {
                builder.append(" extends ");
                for (PsiClassType ext : extensions) {
                    builder.append(TypeHelper.printType(ext, ctx));
                }
            }
            if (i != parameters.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
