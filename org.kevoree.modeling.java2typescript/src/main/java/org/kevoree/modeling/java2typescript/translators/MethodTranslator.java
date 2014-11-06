package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.PsiMethod;
import org.kevoree.modeling.java2typescript.TranslationContext;

/**
 * Created by duke on 11/6/14.
 */
public class MethodTranslator {

    public static void translate(PsiMethod method, TranslationContext ctx){
        System.err.println("METHOD "+method);
    }

}
