
package org.kevoree.modeling.java2typescript.context;

import java.util.*;

public class TranslationContext {

    public boolean NATIVE_ARRAY = true;
    private static final int indentSize = 2;

    private StringBuilder sb = new StringBuilder();
    private int indent = 0;
    private String fileName;
    private List<String> moduleImports = new ArrayList<>();
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
            this.addModuleImport("* as java from './jre.ts'");
        }
        this.javaClasses.add(clazz);
        */
    }

    @Override
    public String toString() {
        StringBuilder importsBuilder = new StringBuilder();

        for (String moduleImport : moduleImports) {
            importsBuilder.append("/// <reference path=\"");
            importsBuilder.append(moduleImport.toString());
            importsBuilder.append("\" />\n");
        }

        if (!moduleImports.isEmpty()) {
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

    public void addModuleImport(String moduleImport) {
        this.moduleImports.add(moduleImport);
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
