
package org.kevoree.modeling.java2typescript;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeHelper {


    public static String printType(PsiType element, TranslationContext ctx) {
        String result = element.getPresentableText();

        if (objects.contains(result)) {
            return "any";
        } else if (primitiveNumbers.contains(result) || objectNumbers.contains(result)) {
            return "number";
        } else if (strings.contains(result)) {
            return "string";
        } else if (booleans.contains(result)) {
            return "boolean";
        }

        if (element instanceof PsiPrimitiveType) {
            if (result == null || result.equals("null")) {
                System.err.println("TypeHelper::printType -> Result null with elem:" + element.toString());
            }
            return result;
        } else if (element instanceof PsiArrayType) {
            result = printType(((PsiArrayType) element).getComponentType(), ctx) + "[]";
            return result;
        } else if (element instanceof PsiClassReferenceType) {
            PsiClass resolvedClass = ((PsiClassReferenceType) element).resolve();
            if (resolvedClass != null) {
                if (isCallbackClass(resolvedClass)) {
                    PsiMethod method = resolvedClass.getAllMethods()[0];
                    PsiParameter[] parameters = method.getParameterList().getParameters();
                    String[] methodParameters = new String[parameters.length];
                    for (int i = 0; i < methodParameters.length; i++) {
                        methodParameters[i] = parameters[i].getName() + " : " + printType(parameters[i].getType(), ctx);
                    }
                    result = "(" + String.join(", ", methodParameters) + ") => " + printType(method.getReturnType(), ctx);
                    return result;
                } else {
                    String qualifiedName = resolvedClass.getQualifiedName();
                    if (qualifiedName != null) {
                        result = resolvedClass.getQualifiedName();
                    }
                    if (resolvedClass.getTypeParameters().length > 0) {
                        String[] generics = new String[resolvedClass.getTypeParameters().length];
                        Arrays.fill(generics, "any");
                        result += "<" + String.join(", ", generics) + ">";
                    }
                }
            } else {
                String tryJavaUtil = javaTypes.get(((PsiClassReferenceType) element).getClassName());
                if (tryJavaUtil != null) {
                    result = tryJavaUtil;
                }
                if (((PsiClassReferenceType) element).getParameterCount() > 0) {
                    String[] generics = new String[((PsiClassReferenceType) element).getParameterCount()];
                    PsiType[] genericTypes = ((PsiClassReferenceType) element).getParameters();
                    for (int i = 0; i < genericTypes.length; i++) {
                        generics[i] = printType(genericTypes[i], ctx);
                    }
                    result += "<" + String.join(", ", generics) + ">";
                }
            }
        } else {
            System.err.println("TypeHelper: InstanceOf:" + element.getClass());
        }

        if (result == null || result.equals("null")) {
            System.err.println("TypeHelper::printType -> Result null with elem:" + element.toString());
        }

        return result;
    }

    public static boolean isCallbackClass(PsiClass clazz) {
        return clazz.isInterface() && clazz.getAllMethods().length == 1;
    }

    public static final HashMap<String, String> javaTypes = new HashMap<String, String>();

    static {
        javaTypes.put("Map", "java.util.Map");
        javaTypes.put("HashMap", "java.util.HashMap");
        javaTypes.put("List", "java.util.List");
        javaTypes.put("Set", "java.util.Set");
        javaTypes.put("HashSet", "java.util.HashSet");
        javaTypes.put("StringBuilder", "java.util.StringBuilder");
        javaTypes.put("ArrayList", "java.util.ArrayList");

        javaTypes.put("Long", "java.lang.Long");
        javaTypes.put("Double", "java.lang.Double");
        javaTypes.put("Float", "java.lang.Float");
        javaTypes.put("Integer", "java.lang.Integer");

        javaTypes.put("Throwable", "java.lang.Throwable");
        javaTypes.put("Exception", "java.lang.Exception");
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

}
