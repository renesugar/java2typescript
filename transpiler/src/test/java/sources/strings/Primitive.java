package sources.strings;

public class Primitive {

    String s = "a" + "b" + "c";

    private static Primitive p = newPrim();

    private static Primitive newPrim() {
        Primitive p = new Primitive();
        return p;
    }




}
