
package com.siliconmint.ts.translator;

import com.google.common.base.Joiner;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassTranslator extends Translator<PsiClass> {

    private static final Joiner joiner = Joiner.on(", ");

    @Override
    public void translate(PsiElementVisitor visitor, PsiClass element, TranslationContext ctx) {

        boolean anonymousClass = element instanceof PsiAnonymousClass;
        if (!anonymousClass) {
            if(element.getContainingClass() == null) {
                ctx.setClassImports(ctx.getAllImports().get(element.getQualifiedName()));
            }
            printClassDeclaration(element, ctx);
        }

        printClassMembers(visitor, element, ctx);

        if (!ctx.hasWhitespace()) {
            ctx.append("\n");
        }

        if (!anonymousClass) {
            ctx.print("}\n");
            ctx.append("\n");
        }

        printInnerClasses(visitor, element, ctx);

    }

    private void printInnerClasses(PsiElementVisitor visitor, PsiClass element, TranslationContext ctx) {
        PsiClass[] innerClasses = element.getInnerClasses();
        if (innerClasses != null && innerClasses.length > 0) {
            ctx.print("export module ").append(element.getName()).append(" { \n");
            ctx.increaseIdent();
            for (PsiClass innerClass : innerClasses) {
                innerClass.accept(visitor);
                ctx.append("\n");
            }
            ctx.decreaseIdent();
            ctx.print("}\n");
        }
    }

    private void printClassMembers(PsiElementVisitor visitor, PsiClass element, TranslationContext ctx) {
        ctx.increaseIdent();

        PsiField[] fields = element.getFields();
        if (fields != null && fields.length > 0) {
            for (PsiField field: fields) {
                field.accept(visitor);
            }
        }

        if(element.isEnum()) {

        }

        PsiClassInitializer[] initializers = element.getInitializers();
        if (initializers != null && initializers.length > 0) {
            for (PsiClassInitializer initializer : initializers) {
                if (initializer.hasModifierProperty("static")){
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

        if(element.isEnum()) {
            ctx.print("public equals(other: any): boolean {\n" +
                    "        return this == other;\n" +
                    "    }\n");

            ctx.print("public static _"+element.getName()+"VALUES : "+element.getName()+"[] = [\n");
            ArrayList<String> enumFileds = new ArrayList<String>();
            for(int i = 0; i < element.getFields().length; i++) {
                if(element.getFields()[i].hasModifierProperty("static")) {
                    enumFileds.add(element.getName()+"."+element.getFields()[i].getName());
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
    }

    private void printClassDeclaration(PsiClass element, TranslationContext ctx) {
        if (!ctx.hasWhitespace()){
            ctx.append("\n");
        }

        if (element.isInterface()) {
            ctx.print("export interface ");
        } else {
            ctx.print("export class ");
        }

        ctx.append(element.getName());

        PsiTypeParameter[] typeParameters = element.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0) {
            ctx.append('<');
            for (int i=0; i < typeParameters.length; i++) {
                PsiTypeParameter p = typeParameters[i];
                ctx.append(p.getName());
                if(p.getExtendsList() != null) {
                    PsiClassType[] extentions = p.getExtendsList().getReferencedTypes();
                    if(extentions.length > 0) {
                        ctx.append(" extends ");
                        for(PsiClassType ext : extentions) {

                            String fqn = ctx.getClassImports().get(ext.getClassName());
                            if(fqn == null) {
                                fqn = ctx.getClassPackage() + "." + ext.getClassName();
                                ctx.append(ext.getClassName()).append(TypeHelper.getGenericsIfAny(ctx, fqn));
                            } else {
                                ctx.append(fqn).append(TypeHelper.getGenericsIfAny(ctx, fqn));
                            }

                        }
                    }

                }
                if (i != typeParameters.length - 1) {
                    ctx.append(", ");
                }
            }
            ctx.append('>');
        }

        PsiClassType[] extendsList = element.getExtendsListTypes();
        if (extendsList != null && extendsList.length != 0 && !element.isEnum()) {
            ctx.append(" extends ");

            writeTypeList(ctx, extendsList);
        }

        PsiClassType[] implementsList = element.getImplementsListTypes();
        if (implementsList != null && implementsList.length != 0) {
            ctx.append(" implements ");

            writeTypeList(ctx, implementsList);
        }

        ctx.append(" {\n\n");
    }

    private void writeTypeList(TranslationContext ctx, PsiClassType[] typeList) {
        for (int i=0; i < typeList.length; i++) {
            PsiClassType type = typeList[i];

            ctx.append(TypeHelper.getType(type, ctx));

        /*
      PsiType[] parameters = type.getParameters();
      if (parameters != null && parameters.length > 0) {
        ctx.append('<');
        TypeHelper.getTypeParameters(parameters);
        ctx.append('>');
      }
*/
            if (i != typeList.length - 1) {
                ctx.append(", ");
            }
        }
    }

}
