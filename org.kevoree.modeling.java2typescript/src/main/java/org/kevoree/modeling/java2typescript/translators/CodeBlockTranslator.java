package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiExpression;
import org.kevoree.modeling.java2typescript.TranslationContext;

/**
 * Created by duke on 11/6/14.
 */
public class CodeBlockTranslator {

    public static void translate(PsiCodeBlock block, TranslationContext ctx){
        System.err.println("BLOCK "+block);
    }

}
