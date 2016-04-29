
package org.kevoree.modeling.java2typescript.translators.statement;

import com.intellij.psi.PsiSynchronizedStatement;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.translators.CodeBlockTranslator;

public class SynchronizedStatementTranslator {

    public static void translate(PsiSynchronizedStatement element, TranslationContext ctx) {
        CodeBlockTranslator.translate(element.getBody(), ctx);
    }

}
