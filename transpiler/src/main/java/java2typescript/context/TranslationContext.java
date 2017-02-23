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
package java2typescript.context;

import com.intellij.psi.PsiJavaFile;

import java.util.*;

public class TranslationContext {

    public boolean NATIVE_ARRAY = true;
    private static final int indentSize = 2;

    private StringBuilder sb = new StringBuilder();
    private int indent = 0;
    private PsiJavaFile file;
    private Set<String> javaClasses = new HashSet<>();
    private String srcPath;
    private List<String> headers = new ArrayList<>();
    private Map<String, String> pkgTransforms = new HashMap<>();
    private ArrayList<String> genericParameterNames;

    public void increaseIdent() {
        indent += indentSize;
    }

    public void decreaseIdent() {
        indent -= indentSize;
        if (indent < 0) {
            throw new IllegalStateException("Decrease indent was called more times than increase");
        }
    }

    public TranslationContext print(String str) {
        indent();
        sb.append(str);
        return this;
    }

    public TranslationContext print(char str) {
        indent();
        sb.append(str);
        return this;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public void setFile(PsiJavaFile file) {
        this.file = file;
    }

    public PsiJavaFile getFile() {
        return file;
    }

    public TranslationContext append(String str) {
        sb.append(str);
        return this;
    }

    public TranslationContext append(char c) {
        sb.append(c);
        return this;
    }

    public void needsJava(String clazz) {
       /*
        if (this.javaClasses.isEmpty()) {
            this.addHeader("* as java from './jre.ts'");
        }
        this.javaClasses.add(clazz);
        */
    }

    public Set<String> needsJava() {
        return this.javaClasses;
    }

    @Override
    public String toString() {
        StringBuilder importsBuilder = new StringBuilder();
        for (String header : headers) {
            importsBuilder.append(header);
            importsBuilder.append("\n");
        }
        if (!headers.isEmpty()) {
            importsBuilder.append("\n");
        }

        return importsBuilder.append(sb.toString()).toString();
    }

    public String getContent() {
        String content = sb.toString();
        sb = new StringBuilder(content);
        return content;
    }

    private TranslationContext indent() {
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return this;
    }

    public void enterPackage(String pkgName, boolean isRoot) {
        if (isRoot) {
            this.print("namespace ");
        } else {
            this.print("export namespace ");
        }
        this.append(pkgName);
        this.append(" {\n");
        this.increaseIdent();
    }

    public void leavePackage() {
        this.decreaseIdent();
        this.print("}\n");
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

    public void addPackageTransform(String initialName, String newName) {
        this.pkgTransforms.put(initialName, newName);
    }

    public String packageTransform(String name) {
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot >= 0) {
            String pkg = name.substring(0, lastIndexOfDot);
            String transform = this.pkgTransforms.get(pkg);
            if (transform != null) {
                if (transform.trim().isEmpty()) {
                    return name.substring(lastIndexOfDot + 1);
                } else {
                    return name.replace(pkg, transform);
                }
            }
        }
        return name;
    }

    public void setGenericParameterNames(ArrayList<String> genericParameterNames) {
        this.genericParameterNames = genericParameterNames;
    }

    public ArrayList<String> getGenericParameterNames() {
        return this.genericParameterNames;
    }

    public void removeGenericParameterNames() {
        this.genericParameterNames = null;
    }
}
