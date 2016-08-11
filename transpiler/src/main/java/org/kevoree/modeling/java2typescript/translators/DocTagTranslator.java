package org.kevoree.modeling.java2typescript.translators;

import com.intellij.psi.javadoc.*;
import org.kevoree.modeling.java2typescript.context.TranslationContext;
import org.kevoree.modeling.java2typescript.metas.DocMeta;

/**
 * Created by gregory.nain on 08/01/15.
 */

public class DocTagTranslator {

    public static final String NATIVE = "native";

    public static final String IGNORE = "ignore";

    public static final String TS = "ts";

    public static final String TS_CALLBACK = "ts_callback";

    public static final String OPTIONAL = "optional";


    public static void translate(DocMeta docMeta, TranslationContext ctx) {
        if(docMeta.nativeActivated && docMeta.nativeBodyLines != null) {
            for(String line : docMeta.nativeBodyLines) {
                ctx.print(line);
            }
        }
    }
}
