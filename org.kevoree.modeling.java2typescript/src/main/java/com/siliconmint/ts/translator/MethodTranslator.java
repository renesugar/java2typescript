
package com.siliconmint.ts.translator;

import com.google.common.base.Joiner;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

public class MethodTranslator extends Translator<PsiMethod> {

    private static final Joiner joiner = Joiner.on(", ");

    @Override
    public void translate(PsiElementVisitor visitor, PsiMethod element, TranslationContext ctx) {

        if (!ctx.hasWhitespace()) {
            ctx.append("\n");
        }

        PsiModifierList modifierList = element.getModifierList();

        if (element.isConstructor()) {
            ctx.print("constructor");
        } else {
            if (element.getContainingClass().isInterface()) {
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
            ctx.append(element.getName());

            PsiTypeParameter[] typeParameters = element.getTypeParameters();
            if (typeParameters != null && typeParameters.length > 0){
                ctx.append('<');
                for (int i=0; i < typeParameters.length; i++) {
                    PsiTypeParameter p = typeParameters[i];
                    ctx.append(p.getName());
                    if(p.getExtendsList() != null) {
                        PsiClassType[] extentions = p.getExtendsList().getReferencedTypes();
                        if(extentions.length > 0) {
                            ctx.append(" extends ");
                            for(PsiClassType ext : extentions) {
                                String type = ext.getClassName();
                                String fqn = ctx.getClassImports().get(type);
                                if(fqn == null) {
                                    fqn = ctx.getClassPackage() + "." + type;
                                    ctx.append(type).append(TypeHelper.getGenericsIfAny(ctx, fqn));
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
                ctx.append("> ");
            }

        }

        ctx.append('(');
        List<String> params = new ArrayList<String>();
        StringBuilder paramSB = new StringBuilder();

        for (PsiParameter parameter : element.getParameterList().getParameters()) {
            paramSB.setLength(0);
            if (parameter.isVarArgs()) {
                paramSB.append("...");
            }
            paramSB.append(parameter.getName());
            paramSB.append(": ");

            String type = TypeHelper.getParameterType(parameter, ctx);
            String fqn = ctx.getClassImports().get(type);
            if(fqn == null) {
                fqn = ctx.getClassPackage() + "." + type;
                paramSB.append(type).append(TypeHelper.getGenericsIfAny(ctx, fqn));
            } else {
                paramSB.append(fqn).append(TypeHelper.getGenericsIfAny(ctx, fqn));
            }


            /*
            String resolvedType = TypeHelper.getParameterType(parameter, ctx);
            paramSB.append(resolvedType);
*/
            params.add(paramSB.toString());
        }
        ctx.append(joiner.join(params));
        ctx.append(')');

        if (!element.isConstructor()) {
            ctx.append(": ");
            String type = TypeHelper.getMethodReturnType(element, ctx);
            String fqn = ctx.getClassImports().get(type);
            if(fqn == null) {
                fqn = ctx.getClassPackage() + "." + type;
                ctx.append(type).append(TypeHelper.getGenericsIfAny(ctx, fqn));
            } else {
                ctx.append(fqn).append(TypeHelper.getGenericsIfAny(ctx, fqn));
            }

        }

        PsiClass containingClass = (PsiClass) element.getParent();
        if (!containingClass.isInterface()){
            ctx.append(" {\n");

            ctx.increaseIdent();
            if (modifierList.hasModifierProperty("abstract") || element.getBody() == null) {
                ctx.print("throw \"Abstract method\";\n");
            } else {
                element.getBody().accept(visitor);
            }
            ctx.decreaseIdent();

            ctx.print("}\n");
        } else {
            ctx.append(";\n");
        }
        ctx.append("\n");
    }

}
