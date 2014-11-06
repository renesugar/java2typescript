
package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;
import org.kevoree.modeling.java2typescript.translator.TypeHelper;

import java.util.ArrayList;

public class ClassTranslator {

    public static void translate(PsiClass clazz, TranslationContext ctx) {
        if (clazz.isInterface()) {
            ctx.print("export interface ");
        } else {
            ctx.print("export class ");
        }
        ctx.append(clazz.getName());
        PsiTypeParameter[] typeParameters = clazz.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0) {
            ctx.append('<');
            for (int i = 0; i < typeParameters.length; i++) {
                PsiTypeParameter p = typeParameters[i];
                ctx.append(p.getName());
                if (p.getExtendsList() != null) {
                    PsiClassType[] extentions = p.getExtendsList().getReferencedTypes();
                    if (extentions.length > 0) {
                        ctx.append(" extends ");
                        for (PsiClassType ext : extentions) {
                            ctx.append(TypeHelper.printType(ext, ctx));
                        }
                    }

                }
                if (i != typeParameters.length - 1) {
                    ctx.append(", ");
                }
            }
            ctx.append('>');
        }
        PsiClassType[] extendsList = clazz.getExtendsListTypes();
        if (extendsList != null && extendsList.length != 0 && !clazz.isEnum()) {
            ctx.append(" extends ");
            writeTypeList(ctx, extendsList);
        }
        PsiClassType[] implementsList = clazz.getImplementsListTypes();
        if (implementsList != null && implementsList.length != 0) {
            ctx.append(" implements ");
            writeTypeList(ctx, implementsList);
        }
        ctx.append(" {\n\n");
        printClassMembers(clazz, ctx);
        ctx.print("}\n");
        ctx.append("\n");
        printInnerClasses(clazz, ctx);
    }

    private static void printInnerClasses(PsiClass element, TranslationContext ctx) {
        PsiClass[] innerClasses = element.getInnerClasses();
        if (innerClasses != null && innerClasses.length > 0) {
            ctx.print("export module ").append(element.getName()).append(" { \n");
            ctx.increaseIdent();
            for (PsiClass innerClass : innerClasses) {
                translate(innerClass, ctx);
                ctx.append("\n");
            }
            ctx.decreaseIdent();
            ctx.print("}\n");
        }
    }

    private static void printClassMembers(PsiClass element, TranslationContext ctx) {

        /*

        ctx.increaseIdent();

        PsiField[] fields = element.getFields();
        if (fields != null && fields.length > 0) {
            for (PsiField field : fields) {
                field.accept(visitor);
            }
        }

        if (element.isEnum()) {

        }

        PsiClassInitializer[] initializers = element.getInitializers();
        if (initializers != null && initializers.length > 0) {
            for (PsiClassInitializer initializer : initializers) {
                if (initializer.hasModifierProperty("static")) {
                    ctx.print("//TODO Resolve static initializer\n");
                    ctx.print("static {\n");
                } else {
                    ctx.print("//TODO Resolve instance initializer\n");
                    ctx.print("{\n");
                }
                ctx.increaseIdent();
                initializer.getBody().accept(visitor);
                ctx.decreaseIdent();
                ctx.print("}\n");
            }
        }

        PsiMethod[] methods = element.getMethods();
        if (methods != null && methods.length > 0) {
            for (PsiMethod method : methods) {
                method.accept(visitor);
            }
        }

        if (element.isEnum()) {
            ctx.print("public equals(other: any): boolean {\n" +
                    "        return this == other;\n" +
                    "    }\n");

            ctx.print("public static _" + element.getName() + "VALUES : " + element.getName() + "[] = [\n");
            ArrayList<String> enumFileds = new ArrayList<String>();
            for (int i = 0; i < element.getFields().length; i++) {
                if (element.getFields()[i].hasModifierProperty("static")) {
                    enumFileds.add(element.getName() + "." + element.getFields()[i].getName());
                }
            }
            ctx.print(String.join(",\n", enumFileds));
            ctx.print("];\n" +
                    "\n" +
                    "public static values():" + element.getName() + "[] {\n" +
                    "   return " + element.getName() + "._" + element.getName() + "VALUES;\n" +
                    "}\n");
        }

        ctx.decreaseIdent();

        */

    }

    private static void writeTypeList(TranslationContext ctx, PsiClassType[] typeList) {
        for (int i = 0; i < typeList.length; i++) {
            PsiClassType type = typeList[i];
            ctx.append(TypeHelper.printType(type, ctx));
            if (i != typeList.length - 1) {
                ctx.append(", ");
            }
        }
    }

}
