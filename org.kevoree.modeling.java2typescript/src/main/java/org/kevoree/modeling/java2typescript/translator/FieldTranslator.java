
package org.kevoree.modeling.java2typescript.translator;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.TranslationContext;

public class FieldTranslator extends Translator<PsiField> {

    @Override
    public void translate(PsiElementVisitor visitor, PsiField element, TranslationContext ctx) {
        if (element instanceof PsiEnumConstant) {
            translateEnumConstant(visitor, (PsiEnumConstant) element, ctx);
        } else {
            translateClassField(visitor, element, ctx);
        }
    }

    private void translateEnumConstant(PsiElementVisitor visitor, PsiEnumConstant element, TranslationContext ctx) {
        String enumName = ((PsiClass) element.getParent()).getName();
        ctx.print("public static ").append(element.getName()).append(": ").append(enumName);
        ctx.append(" = new ").append(enumName);
        ctx.append('(');
        if (element.getArgumentList() != null) {
            PsiExpression[] arguments = element.getArgumentList().getExpressions();
            for (int i=0; i < arguments.length; i++) {
                arguments[i].accept(visitor);
                if (i != arguments.length - 1) {
                    ctx.append(", ");
                }
            }
        }
        ctx.append(");\n");
    }

    private void translateClassField(PsiElementVisitor visitor, PsiField element, TranslationContext ctx) {

        /*
        PsiModifierList modifierList = element.getModifierList();
        if (modifierList.hasModifierProperty("private")) {
            ctx.print("private ");
        } else {
            ctx.print("public ");
        }

        if (modifierList.hasModifierProperty("static")) {
            ctx.append("static ");
        }

        ctx.append(element.getName()).append(": ");

        String type = TypeHelper.getFieldType(element, ctx);
        String fqn = ctx.getClassImports().get(type);
        if(fqn == null) {
            fqn = ctx.getClassPackage() + "." + type;
            ctx.append(type).append(TypeHelper.getGenericsIfAny(ctx, fqn));
        } else {
            ctx.append(fqn).append(TypeHelper.getGenericsIfAny(ctx, fqn));
        }

        if (element.hasInitializer()) {
            ctx.append(" = ");

            element.getInitializer().accept(visitor);

            ctx.append(";\n");
        } else {
            if (TypeHelper.isPrimitiveField(element)) {
                ctx.append(" = 0");
            } else {
                ctx.append(" = null");
            }

            ctx.append(";\n");
        }
        */
    }

}