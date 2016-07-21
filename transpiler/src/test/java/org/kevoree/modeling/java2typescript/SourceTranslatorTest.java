package org.kevoree.modeling.java2typescript;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceTranslatorTest {

    @Test
    public void generics() throws IOException {
        String source = Paths.get("src", "test", "java", "sources", "generics").toAbsolutePath().toString();
        String target = Paths.get("target", "generated-sources", "java2ts").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "generics");
        translator.process();

        String result = translator.getCtx().toString().trim();
        //System.out.println(result);

        Assert.assertEquals(
                FileUtils.readFileToString(Paths.get("src", "test", "resources", "generics", "output.ts").toFile()).trim(),
                result);
    }

    @Test
    public void strings() throws IOException {
        String source = Paths.get("src", "test", "java", "sources", "strings").toAbsolutePath().toString();
        String target = Paths.get("target", "generated-sources", "java2ts").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "strings");
        translator.addPackageTransform("sources.strings", "");
        translator.process();

        String result = translator.getCtx().toString().trim();
        //System.out.println(result);

        Assert.assertEquals(
                FileUtils.readFileToString(Paths.get("src", "test", "resources", "strings", "output.ts").toFile()).trim(),
                result);
    }

    @Test
    public void arrays() throws IOException {
        String source = Paths.get("src", "test", "java", "sources", "arrays", "test").toAbsolutePath().toString();
        String target = Paths.get("target", "generated-sources", "java2ts").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "arrays");
        translator.process();

        String result = translator.getCtx().toString().trim();
        //System.out.println(result);

        Assert.assertEquals(
                FileUtils.readFileToString(Paths.get("src", "test", "resources", "arrays", "output.ts").toFile()).trim(),
                result);
    }

    //@Test
    public void mwdb_core() throws IOException {
        String source = "/Users/gnain/Sources/Kevoree-Modeling/mwDB/core/src/main/java";
        String target = Paths.get("target", "generated-sources", "core").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "strings");
        translator.addPackageTransform("sources.strings", "");
        Path classes = Paths.get("/Users/gnain/Sources/Kevoree-Modeling/mwDB/api/target/api-7-SNAPSHOT.jar");

        if(classes.toFile().exists()) {
            System.out.println("DepAdded");
            translator.addToClasspath(classes.toString());
        }



        translator.process();

        //TODO comment
       // String result = translator.getCtx().toString().trim();
       // System.out.println(result);


    }

    //@Test
    public void mwdb__core_test() throws IOException {
        String source = "/Users/gnain/Sources/Kevoree-Modeling/mwDB/core/src/test/java";
        String target = Paths.get("target", "generated-test-sources", "test").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "test");
        translator.addPackageTransform("sources.strings", "");
        translator.process();

        String result = translator.getCtx().toString().trim();
        System.out.println(result);

    }

    /*
    */

}