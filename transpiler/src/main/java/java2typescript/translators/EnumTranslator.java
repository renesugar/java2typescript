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

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import java2typescript.context.TranslationContext;
import java2typescript.helper.DocHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.metas.DocMeta;

public class EnumTranslator {

    public static void translate(PsiClass enumClass, TranslationContext ctx) {
        DocMeta metas = DocHelper.process(enumClass.getDocComment());
        if (metas.ignored) {
            //we skip the class
            return;
        }

        ctx.print("export enum ");

        ctx.append(enumClass.getName());

        ctx.append(" {\n");

        if (metas.nativeActivated) {
            ctx.increaseIdent();
            DocTagTranslator.translate(metas, ctx);
            ctx.decreaseIdent();
        } else {
            printEnumMembers(enumClass, ctx);
        }

        ctx.print("}\n");
    }

    private static void printEnumMembers(PsiClass enumClass, TranslationContext ctx) {

        ctx.increaseIdent();
        boolean isFirst = true;

        for (int i = 0; i < enumClass.getFields().length; i++) {
            if (enumClass.getFields()[i].hasModifierProperty("static")) {
                if (!isFirst) {
                    ctx.append(", ");
                } else {
                    ctx.print("");
                    isFirst = false;
                }
                ctx.append(enumClass.getFields()[i].getName().trim());
            }
        }
        ctx.append("\n");
        ctx.decreaseIdent();
        /*
        ctx.print("public equals(other: any): boolean {\n");
        ctx.increaseIdent();
        ctx.print("return this == other;\n");
        ctx.decreaseIdent();
        ctx.print("}\n");
        ctx.print("public static _" + clazz.getName() + "VALUES : " + clazz.getName() + "[] = [\n");
        ctx.increaseIdent();
        boolean isFirst = true;
        for (int i = 0; i < clazz.getFields().length; i++) {
            if (clazz.getFields()[i].hasModifierProperty("static")) {
                if (!isFirst) {
                    ctx.print(",");
                } else {
                    ctx.print("");
                }
                ctx.append(clazz.getName());
                ctx.append(".");
                ctx.append(clazz.getFields()[i].getName());
                ctx.append("\n");
                isFirst = false;
            }
        }
        ctx.decreaseIdent();
        ctx.print("];\n");

        ctx.print("public static values():");
        ctx.append(clazz.getName());
        ctx.append("[]{\n");
        ctx.increaseIdent();
        ctx.print("return ");
        ctx.append(clazz.getName());
        ctx.append("._");
        ctx.append(clazz.getName());
        ctx.append("VALUES;\n");
        ctx.decreaseIdent();
        ctx.print("}\n");
        */

    }

}
