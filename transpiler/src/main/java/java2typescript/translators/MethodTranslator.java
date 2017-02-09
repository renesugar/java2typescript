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
import com.intellij.psi.impl.source.PsiClassReferenceType;
import java2typescript.context.TranslationContext;
import java2typescript.helper.DocHelper;
import java2typescript.helper.KeywordHelper;
import java2typescript.helper.TypeHelper;
import java2typescript.metas.DocMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by duke on 11/6/14.
 */
public class MethodTranslator {

    public static void translate(PsiMethod method, TranslationContext ctx, boolean isAnonymous) {
        DocMeta docMeta = DocHelper.process(method.getDocComment());

        if (docMeta.ignored) {
            return;
        }

        PsiModifierList modifierList = method.getModifierList();
        PsiClass containingClass = (PsiClass) method.getParent();
        ArrayList<String> paramGenParamTypeName = new ArrayList<>();
        ctx.setGenericParameterNames(paramGenParamTypeName);
        if (method.isConstructor()) {
            ctx.print("constructor");
        } else {
            if (method.getContainingClass() != null && method.getContainingClass().isInterface()) {
                ctx.print("");
            } else {
                if (modifierList.hasModifierProperty("private")) {
                    ctx.print("private ");
                } else {
                    ctx.print("public ");
                }
            }
            if (modifierList.hasModifierProperty("static")) {
                ctx.append("static ");
            }
            if (!containingClass.isInterface() && modifierList.hasModifierProperty(PsiModifier.ABSTRACT)) {
                ctx.append("abstract ");
            }
            if (!isAnonymous) {
                ctx.append(KeywordHelper.process(method.getName(), ctx));
            }
            HashSet<String> usedGenerics = new HashSet<>();
            PsiTypeParameter[] parameters = method.getTypeParameters();
            for (PsiTypeParameter parameter : parameters) {
                usedGenerics.add(parameter.getName());
            }
            String genericParams = TypeParametersTranslator.print(method.getTypeParameters(), ctx);
            if (!genericParams.isEmpty()) {
                genericParams = "<" + genericParams;
            }
            for (int i = 0; i < method.getParameterList().getParameters().length; i++) {
                PsiParameter param = method.getParameterList().getParameters()[i];
                if (param.getType() instanceof PsiClassReferenceType) {
                    PsiClassReferenceType paramClassRef = (PsiClassReferenceType) param.getType();
                    if (paramClassRef.getParameters().length > 0) {
                        // param has generic params
                        for (PsiType paramGenParamType : paramClassRef.getParameters()) {
                            if (paramGenParamType instanceof PsiWildcardType) {
                                if (((PsiWildcardType) paramGenParamType).getBound() != null) {
                                    if (!genericParams.isEmpty()) {
                                        genericParams += ", ";
                                    } else {
                                        genericParams = "<";
                                    }
                                    String genType = TypeHelper.availableGenericType(usedGenerics);
                                    usedGenerics.add(genType);
                                    paramGenParamTypeName.add(i, genType);
                                    genericParams += genType +
                                            " extends " +
                                            TypeHelper.printType(((PsiWildcardType) paramGenParamType).getBound(), ctx);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (!genericParams.isEmpty()) {
                genericParams += ">";
            }
            ctx.append(genericParams);
        }
        translateMethodParametersDeclaration(method, ctx, docMeta);
        if (!method.isConstructor()) {
            ctx.append(": ");
            ctx.append(TypeHelper.printType(method.getReturnType(), ctx));
        }
        if(containingClass.isInterface() || method.getModifierList().hasModifierProperty("abstract")){
            ctx.append(";\n");
        } else {
            ctx.append(" ");
            if (method.getBody() == null) {
                ctx.append("{\n");
                ctx.increaseIdent();
                ctx.print("throw \"Empty body\";\n");
                ctx.decreaseIdent();
                ctx.print("}\n");
            } else {
                if (method.getBody().getStatements().length > 0) {
                    if (!docMeta.nativeActivated) {

                        CodeBlockTranslator.translate(method.getBody(), ctx);
                        ctx.append("\n");
                    } else {
                        ctx.append("{\n");
                        ctx.increaseIdent();
                        DocTagTranslator.translate(docMeta, ctx);
                        ctx.decreaseIdent();
                        ctx.print("}\n");
                    }
                } else {
                    ctx.append("{}\n");
                }
            }
        }
        ctx.removeGenericParameterNames();
    }

    public static void translateToLambdaType(PsiMethod method, TranslationContext ctx, DocMeta docMeta) {
        translateMethodParametersDeclaration(method, ctx, docMeta);
        ctx.append("=>");
        ctx.append(TypeHelper.printType(method.getReturnType(), ctx));
    }

    private static void translateMethodParametersDeclaration(PsiMethod method, TranslationContext ctx, DocMeta docMeta) {
        ctx.append('(');
        List<String> params = new ArrayList<>();
        StringBuilder paramSB = new StringBuilder();
        for (PsiParameter parameter : method.getParameterList().getParameters()) {
            paramSB.setLength(0);
            if (parameter.isVarArgs()) {
                paramSB.append("...");
            }
            paramSB.append(KeywordHelper.process(parameter.getName(), ctx));
            if (docMeta != null && docMeta.optional.contains(parameter.getName())) {
                paramSB.append("?");
            }
            paramSB.append(": ");

            paramSB.append(TypeHelper.printType(parameter.getType(), ctx, true, false));
            params.add(paramSB.toString());
        }
        ctx.append(String.join(", ", params));
        ctx.append(')');
    }
}
