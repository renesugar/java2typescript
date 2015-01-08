package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.javadoc.PsiInlineDocTag;
import org.kevoree.modeling.java2typescript.TranslationContext;

/**
 * Created by gregory.nain on 08/01/15.
 */

public class NativeTsTranslator {

    public static final String TAG = "native:ts";

    public static void translate(PsiDocComment comment, TranslationContext ctx) {

        PsiDocTag[] tags = comment.getTags();
        for(PsiDocTag tag : tags) {
            if(tag.getName().equals(TAG)) {
                PsiElement[] elements = tag.getDataElements();
                for (PsiElement element : elements) {
                    if(element instanceof PsiInlineDocTag) {
                        PsiInlineDocTag inlineDocTag = (PsiInlineDocTag) element;
                        PsiElement[] inlineCodeContent = inlineDocTag.getDataElements();
                        String _tagValueTemp = "";
                        for(PsiElement inlineElement : inlineCodeContent) {
                            if (!inlineElement.getText().trim().equals("") && !inlineElement.getText().trim().equals("\n")) {
                                if (inlineElement instanceof PsiDocTagValue || inlineElement.getText().startsWith("(")) {
                                    _tagValueTemp = _tagValueTemp + inlineElement.getText() + " ";
                                } else {
                                    ctx.print(_tagValueTemp).append(inlineElement.getText()).append("\n");
                                    _tagValueTemp = "";
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
