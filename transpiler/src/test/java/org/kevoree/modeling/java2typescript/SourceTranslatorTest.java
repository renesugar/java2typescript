package org.kevoree.modeling.java2typescript;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceTranslatorTest {


    private void transpilerTest(String srcF, String[] src, String outF, String[] out, String expF, String[] expPath) throws IOException {
        String source = Paths.get(srcF,src).toAbsolutePath().toString();
        String target = Paths.get(outF, out).toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "generics");
        translator.process();
        translator.generate();

        File expectedRes = Paths.get(expF, expPath).toFile();
        File actualResult = Paths.get(outF,out).toFile();
        Assert.assertArrayEquals(expectedRes.list(),actualResult.list());

        File[] expFiles = expectedRes.listFiles();
        File[] actFiles = actualResult.listFiles();
        for(int i=0;i<expFiles.length;i++) {
            File exp = expFiles[i];
            File act = actFiles[i];

            BufferedReader readerExp = new BufferedReader(new FileReader(exp));
            BufferedReader readerAct = new BufferedReader(new FileReader(act));

            String lgnExp = readerExp.readLine();
            String lgnAct = readerAct.readLine();

            while (lgnAct!= null || lgnExp != null) {
                Assert.assertEquals(lgnExp,lgnAct);
                lgnAct = readerAct.readLine();
                lgnExp = readerExp.readLine();
            }

            Assert.assertEquals(lgnExp,lgnAct);

        }
    }

    @Test
    public void generics() throws IOException {
        transpilerTest("src", new String[]{"test", "java", "sources", "generics"},
                "target",new String[]{"generated-sources", "java2ts", "generics"},
                "src", new String[]{"test", "resources", "generics"});
    }

    @Test
    public void strings() throws IOException {
        transpilerTest("src", new String[]{"test", "java", "sources", "strings"},
                "target",new String[]{"generated-sources", "java2ts","strings"},
                "src",new String[]{"test", "resources", "strings"});
    }

    @Test
    public void arrays() throws IOException {
        transpilerTest("src", new String[]{"test", "java", "sources", "arrays","test"},
                "target",new String[]{"generated-sources", "java2ts","arrays"},
                "src",new String[]{"test", "resources", "arrays"});
    }

    @Test
    public void closures() throws IOException {
        transpilerTest("src", new String[]{"test", "java", "sources", "closures"},
                "target",new String[]{"generated-sources", "java2ts","closures"},
                "src",new String[]{"test", "resources", "closures"});
    }

    @Test
    public void baseElements() throws IOException {
        transpilerTest("src", new String[]{"test", "java", "sources", "base"},
                "target",new String[]{"generated-sources", "java2ts","base"},
                "src",new String[]{"test", "resources", "base"});

    }

    //@Test
    public void mwdb_core() throws IOException {
        String source = "/Users/gnain/Sources/Kevoree-Modeling/mwDB/core/src/main/java";
        //String source = "/Users/gnain/Sources/Kevoree-Modeling/mwDB/api/src/main/java";

        String target = Paths.get("target", "generated-sources", "core").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "strings");
        translator.addPackageTransform("sources.strings", "");
        Path classes = Paths.get("/Users/gnain/Sources/Kevoree-Modeling/mwDB/api/target/api-7-SNAPSHOT.jar");

        if(classes.toFile().exists()) {
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
        Path classes = Paths.get("/Users/gnain/Sources/Kevoree-Modeling/mwDB/api/target/api-7-SNAPSHOT.jar");

        if(classes.toFile().exists()) {
            System.out.println("DepAdded");
            translator.addToClasspath(classes.toString());
        }

        translator.process();

        String result = translator.getCtx().toString().trim();
        //System.out.println(result);

    }

    //@Test
    public void mwdb_ml() throws IOException {
        String source = "/Users/gnain/Sources/Kevoree-Modeling/mwDB/plugins/ml/src/main/java";
        String target = Paths.get("target", "generated-sources", "main").toAbsolutePath().toString();

        SourceTranslator translator = new SourceTranslator(source, target, "test");
        translator.addPackageTransform("sources.strings", "");
        Path classes = Paths.get("/Users/gnain/Sources/Kevoree-Modeling/mwDB/api/target/api-7-SNAPSHOT.jar");

        if(classes.toFile().exists()) {
            translator.addToClasspath(classes.toString());
        }

        translator.process();

        String result = translator.getCtx().toString().trim();
        //System.out.println(result);

    }

    /*
    */

}