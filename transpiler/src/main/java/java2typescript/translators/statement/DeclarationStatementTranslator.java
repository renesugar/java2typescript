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
package java2typescript.translators.statement;

import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiStatement;
import java2typescript.context.TranslationContext;

public class DeclarationStatementTranslator {

    public static void translate(PsiDeclarationStatement stmt, TranslationContext ctx) {
        for (int i = 0; i < stmt.getDeclaredElements().length; i++) {

            if(i > 0) {
                ctx.append(", ");
            }

            PsiElement element1 = stmt.getDeclaredElements()[i];
            if (element1 instanceof PsiStatement) {
                StatementTranslator.translate((PsiStatement) element1, ctx);
            } else if (element1 instanceof PsiLocalVariable) {
                LocalVariableTranslator.translate((PsiLocalVariable) element1, ctx);
            } else {
                System.err.println("Not managed " + element1);
            }
        }
    }

}
