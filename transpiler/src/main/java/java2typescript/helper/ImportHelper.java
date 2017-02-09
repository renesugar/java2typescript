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
package java2typescript.helper;

import com.intellij.psi.PsiElement;
import java2typescript.context.TranslationContext;

/**
 *
 * Created by leiko on 20/11/15.
 */
public class ImportHelper {

    public static void importIfValid(PsiElement resolution, TranslationContext ctx) {
//        if (resolution != null) {
//            if (!ctx.getFile().getVirtualFile().getPath().equals(resolution.getContainingFile().getVirtualFile().getPath())) {
//                Path elemPath = Paths.get(ctx.getFile().getParent().getVirtualFile().getPath());
//                Path resolPath = Paths.get(resolution.getContainingFile().getVirtualFile().getPath());
//
//                String pathToResol = elemPath.relativize(resolPath).toString();
//                if (!pathToResol.isEmpty()) {
//                    ctx.addImport(((PsiClass) resolution).getName(), "./"+pathToResol.substring(0, pathToResol.lastIndexOf(".")));
//                }
//            }
//        }
    }

    public static String getGeneratedName(PsiElement resolution, TranslationContext ctx) {
//        if (resolution != null) {
//            if (!ctx.getFile().getVirtualFile().getPath().equals(resolution.getContainingFile().getVirtualFile().getPath())) {
//                Path elemPath = Paths.get(ctx.getFile().getParent().getVirtualFile().getPath());
//                Path resolPath = Paths.get(resolution.getContainingFile().getVirtualFile().getPath());
//
//                String pathToResol = elemPath.relativize(resolPath).toString();
//                if (!pathToResol.isEmpty()) {
//                    return ctx.getImportGeneratedName(((PsiClass) resolution).getName(), "./"+pathToResol.substring(0, pathToResol.lastIndexOf(".")));
//                }
//            }
//        }

        return null;
    }
}
