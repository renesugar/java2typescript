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

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import java2typescript.context.TranslationContext;

import java.util.ArrayList;

/**
 *
 * Created by leiko on 3/15/16.
 */
public class GenericHelper {

    public static String process(PsiClass clazz) {
        String type = clazz.getQualifiedName();
        PsiTypeParameter[] typeParameters = clazz.getTypeParameters();
        if (typeParameters.length > 0) {
            String[] generics = new String[typeParameters.length];
            for (int i = 0; i < typeParameters.length; i++) {
                generics[i] = "any";
            }
            type += "<"+String.join(", ", generics)+">";
        }
        return type;
    }

    public static String process(PsiClassReferenceType classRefType, TranslationContext ctx, boolean withGenerics) {
        String result = "";
        PsiJavaCodeReferenceElement ref = classRefType.getReference();
        if (ref.getText().endsWith("<>")) {
            result = processDiamondOperator(ref.getParent().getParent(), ctx);
        } else {
            ArrayList<String> genericParameterNames = ctx.getGenericParameterNames();
            if (classRefType.getParameterCount() > 0) {
                String[] generics = new String[classRefType.getParameterCount()];
                PsiType[] genericTypes = classRefType.getParameters();
                for (int i = 0; i < genericTypes.length; i++) {
                    if (genericTypes[i] instanceof PsiWildcardType) {
                        if (((PsiWildcardType) genericTypes[i]).getBound() != null) {
                            if (classRefType.getReference().getParent().getParent() instanceof PsiParameter) {
                                PsiParameter param = (PsiParameter) classRefType.getReference().getParent().getParent();
                                PsiParameterList paramList = (PsiParameterList) param.getParent();
                                generics[i] = genericParameterNames.get(paramList.getParameterIndex(param));
                            }
                        } else {
                            generics[i] = "any";
                        }
                    } else {
                        generics[i] = TypeHelper.printType(genericTypes[i], ctx, true, false);
                    }
                }
                result += "<" + String.join(", ", generics) + ">";
            } else if (withGenerics) {
                PsiClass clazz = classRefType.resolve();
                if (clazz != null) {
                    if (clazz.getTypeParameters().length > 0) {
                        String[] genericParams = new String[clazz.getTypeParameters().length];
                        for (int i=0; i < clazz.getTypeParameters().length; i++) {
                            genericParams[i] = "any";
                        }
                        result = "<" + String.join(", ", genericParams) + ">";
                    }
                } else {
                    if (ref.getText().endsWith("<>")) {
                        result = processDiamondOperator(ref.getParent().getParent(), ctx);
                    }
                }
            } else {
                if (classRefType.getPresentableText().contains("<")) {
                    result += "<any>";
                }
            }
        }

        return result;
    }

    private static String processDiamondOperator(PsiElement element, TranslationContext ctx) {
        String result;
        // Java 7 diamond operator not possible in TypeScript
        if (element instanceof PsiField) {
            PsiField field = (PsiField) element;
            result = process((PsiClassReferenceType) field.getType(), ctx, true);
        } else if (element instanceof PsiLocalVariable) {
            System.out.println("DIAMOND LOCALVAR> " + ((PsiLocalVariable) element).getType());
            PsiLocalVariable localVar = (PsiLocalVariable) element;
            result = process((PsiClassReferenceType) localVar.getType(), ctx, true);
        } else if (element instanceof PsiAssignmentExpression) {
            PsiAssignmentExpression assign = (PsiAssignmentExpression) element;
            result = process((PsiClassReferenceType) assign.getLExpression().getType(), ctx, true);
        } else {
            System.out.println("DIAMOND > "+element);
            result = "<any>";
        }
        return result;
    }
}
