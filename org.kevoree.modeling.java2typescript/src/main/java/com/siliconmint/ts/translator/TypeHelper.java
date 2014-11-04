
package com.siliconmint.ts.translator;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeHelper {

    public static String getGenericsIfAny(TranslationContext ctx, String type) {
        String fqn = ctx.getClassImports().get(type);
        String className = type;
        if(fqn != null) {
            className = fqn;
        }

        StringBuilder paramSB = new StringBuilder();
        if(ctx != null) {
            Integer generics = ctx.getGenerics().get(className);
            if (generics != null) {
                paramSB.append("<");
                for (int i = 0; i < generics; i++) {
                    paramSB.append("any");
                    if (i < generics - 1) {
                        paramSB.append(",");
                    }
                }
                paramSB.append(">");
            }
        }
        return paramSB.toString();
    }

    public static String getFieldType(PsiField element, TranslationContext ctx) {
        String res = getType(element.getType(), ctx);
        res = res + getGenericsIfAny(ctx, res);
        return res ;
    }

    public static String getMethodReturnType(PsiMethod element, TranslationContext ctx) {
        if (element.getReturnType() == null) {
            return "void";
        } else {
            String res = getType(element.getReturnType(), ctx);
            res = res + getGenericsIfAny(ctx, res);
            return res ;
        }
    }

    public static String getParameterType(PsiParameter parameter, TranslationContext ctx) {
        String res = getType(parameter.getType(), ctx);
        res = res + getGenericsIfAny(ctx, res);
        return res ;
    }

    public static String getVariableType(PsiLocalVariable variable, TranslationContext ctx){
        String res = getType(variable.getType(), ctx);
        res = res + getGenericsIfAny(ctx, res);
        return res ;
    }

    public static String getAnonymousClassType(PsiAnonymousClass clazz, TranslationContext ctx){
        return getType(clazz.getBaseClassType(), ctx);
    }

    public static String getType(PsiJavaCodeReferenceElement reference, TranslationContext ctx) {
        String typeName;

        if (reference.isQualified()) {
            typeName = translateType(reference.getQualifiedName(), ctx);
        } else {
            typeName = translateType(reference.getReferenceName(), ctx);
        }
        /*
        String fqn = ctx.getClassImports().get(typeName);
        if(fqn != null) {
            typeName = fqn;
        }*/

        PsiType[] typeParameters = reference.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0) {
            return String.format("%s<%s>", typeName, getTypeParameters(typeParameters, ctx));
        } else {
            return typeName;
        }
    }

    public static String getType(PsiType type, TranslationContext ctx) {
        return getType(type, ctx, true);
    }

    public static String getType(PsiType type, TranslationContext ctx, boolean withGenerics) {
        if (type instanceof PsiArrayType) {
            type = ((PsiArrayType) type).getComponentType();
            String translatedType = getType(type, ctx);
            if(withGenerics) {
                String fqn = ctx.getClassImports().get(translatedType);
                if(fqn == null) {
                    fqn = ctx.getClassPackage() + "." + translatedType;
                    translatedType = translatedType.concat(TypeHelper.getGenericsIfAny(ctx, fqn));
                } else {
                    translatedType = fqn.concat(TypeHelper.getGenericsIfAny(ctx, fqn));
                }

            }
            return translatedType.concat("[]");
        } else if (type instanceof PsiClassReferenceType) {
            PsiJavaCodeReferenceElement reference = ((PsiClassReferenceType) type).getReference();
            String resolvedRef = getType(reference, ctx);
            if(withGenerics) {
                resolvedRef = resolvedRef + getGenericsIfAny(ctx, resolvedRef);
            }
            return resolvedRef;
        } else {
            return translateType(type.getCanonicalText(), ctx);
        }
    }

    public static String translateType(String type, TranslationContext ctx){
        if (objects.contains(type)) {
            return "any";
        } else if (primitiveNumbers.contains(type) || objectNumbers.contains(type)) {
            return "number";
        } else if (strings.contains(type)) {
            return "string";
        } else if (booleans.contains(type)) {
            return "boolean";
        } else {
            String fqn = ctx.getClassImports().get(type);
            if(fqn != null) {
                type = fqn;
            }
            return type;
        }
    }

    public static boolean isPrimitiveField(PsiField element) {
        String elementType = element.getType().getCanonicalText();
        return primitiveNumbers.contains(elementType);
    }

    public static String getTypeParameters(PsiType[] typeParameters, TranslationContext ctx) {
        List<String> paramStrings = new ArrayList<String>();
        for (PsiType type: typeParameters) {

            String resolvedType = getType(type, ctx);
            String fqn = ctx.getClassImports().get(resolvedType);
            if(fqn == null) {
                fqn = ctx.getClassPackage() + "." + resolvedType;
                paramStrings.add(resolvedType + TypeHelper.getGenericsIfAny(ctx, fqn));
            } else {
                paramStrings.add(fqn + TypeHelper.getGenericsIfAny(ctx, fqn));
            }
            //paramStrings.add(resolvedType);
        }
        return Joiner.on(", ").join(paramStrings);
    }

    public static final Set<String> primitiveNumbers = ImmutableSet.of("byte", "short", "int", "long", "float", "double");

    public static final Set<String> objectNumbers = ImmutableSet.of(
            Byte.class.getName(),
            Byte.class.getSimpleName(),
            Short.class.getName(),
            Short.class.getSimpleName(),
            Integer.class.getName(),
            Integer.class.getSimpleName(),
            Long.class.getName(),
            Long.class.getSimpleName(),
            Float.class.getName(),
            Float.class.getSimpleName(),
            Double.class.getName(),
            Double.class.getSimpleName(),
            BigInteger.class.getName(),
            BigInteger.class.getSimpleName(),
            BigDecimal.class.getName(),
            BigDecimal.class.getSimpleName()
    );

    public static final Set<String> strings = ImmutableSet.of(
            "char",
            Character.class.getName(),
            Character.class.getSimpleName(),
            String.class.getName(),
            String.class.getSimpleName()
    );

    public static final Set<String> booleans = ImmutableSet.of(
            "boolean",
            Boolean.class.getName(),
            Boolean.class.getSimpleName()
    );

    public static final Set<String> objects = ImmutableSet.of(
            Object.class.getName(),
            Object.class.getSimpleName()
    );

    public static final Set<String> jUtilsExceptions = ImmutableSet.of(
            List.class.getName(),
            List.class.getSimpleName(),
            ArrayList.class.getName(),
            ArrayList.class.getSimpleName(),
            Set.class.getName(),
            Set.class.getSimpleName(),
            HashSet.class.getName(),
            HashSet.class.getSimpleName(),
            Map.class.getName(),
            Map.class.getSimpleName(),
            HashMap.class.getName(),
            HashMap.class.getSimpleName()
    );

}
