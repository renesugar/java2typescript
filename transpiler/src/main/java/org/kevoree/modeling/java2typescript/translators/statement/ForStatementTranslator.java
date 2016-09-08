
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.expression.ExpressionTranslator;

import java.util.ArrayList;

public class ForStatementTranslator {

    public static void translate(PsiForStatement element, TranslationContext ctx) {
        ctx.print("for (");
        ArrayList<String> itVars = new ArrayList();
        PsiStatement initialization = element.getInitialization();
        if (initialization != null) {
            StatementTranslator.translate(initialization, ctx);
            if(initialization instanceof PsiDeclarationStatement) {
                PsiElement[] elements = ((PsiDeclarationStatement)initialization).getDeclaredElements();
                for(PsiElement e : elements) {
                    if(e instanceof PsiVariable) {
                        itVars.add(((PsiVariable)e).getName());
                    } else {
                        System.err.println("Unknown element type in FosStatementTranslator:" + e.getClass().getName());
                    }
                }
            } else {
                System.err.println("Unknown declaration type in FosStatementTranslator:" + initialization.getClass().getName());
            }
        }
        ctx.append("; ");
        if (element.getCondition() != null) {
            ExpressionTranslator.translate(element.getCondition(), ctx);
        }
        ctx.append("; ");
        if (element.getUpdate() != null) {
            StatementTranslator.translate(element.getUpdate(), ctx);
        }
        ctx.append(") {\n");
        ctx.increaseIdent();
        ctx.print("(function(");
        ctx.append(String.join(",", itVars));
        ctx.append("){\n");
        ctx.increaseIdent();
        StatementTranslator.translate(element.getBody(), ctx);
        ctx.decreaseIdent();
        ctx.print("}).call(this,");
        ctx.append(String.join(",", itVars));
        ctx.append(");\n");
        ctx.decreaseIdent();
        ctx.print("}\n");
    }

}
