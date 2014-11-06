package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.PsiExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

/**
 * Created by duke on 11/6/14.
 */
public class ExpressionTranslator {

    public static void translate(PsiExpression expression, TranslationContext ctx){
        System.err.println("EXPR "+expression);
    }

}
