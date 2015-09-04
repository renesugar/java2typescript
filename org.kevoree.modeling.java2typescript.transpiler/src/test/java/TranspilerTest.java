import org.kevoree.modeling.java2typescript.SourceTranslator;

import java.io.IOException;

/**
 * Created by gnain on 04/09/15.
 */
public class TranspilerTest {

    private static final String baseDir = "/Users/gnain/Sources/Kevoree-Modeling/java2typescript/org.kevoree.modeling.java2typescript.transpiler/src/test/resources";

    public static void main(String[] args) throws IOException {
        SourceTranslator sourceTranslator = new SourceTranslator();
        sourceTranslator.translateSources(baseDir, "target", "out", false, false, true);
    }



}
