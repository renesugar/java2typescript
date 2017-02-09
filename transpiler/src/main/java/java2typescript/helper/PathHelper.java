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

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiJavaFile;
import java2typescript.context.TranslationContext;

import java.io.File;

/**
 *
 * Created by leiko on 20/11/15.
 */
public class PathHelper {

    public static String getPath(String srcPath, String outPath, PsiDirectory dir) {
        return outPath + dir.getVirtualFile().getPath().substring(srcPath.length());
    }

    public static String getPath(String srcPath, String outPath, String dir) {
        return outPath + dir.substring(srcPath.length());
    }

//    public static String getPath(TranslationContext ctx, PsiDirectory dir) {
//        return getPath(ctx.getSrcPath(), ctx.getOutPath(), dir);
//    }

    public static String getPath(String srcPath, String outPath, PsiJavaFile file) {
        return getPath(srcPath, outPath, file.getParent()) + File.separator + file.getName().replaceAll("\\.java$", ".ts");
    }

//    public static String getPath(TranslationContext ctx, PsiJavaFile file) {
//        return getPath(ctx.getSrcPath(), ctx.getOutPath(), file);
//    }

    public static String lastPart(TranslationContext ctx) {
        String path = ctx.getFile().getVirtualFile().getPath();
        return path.substring(ctx.getSrcPath().length()+1);
    }
}
