package sources.strings;

public class Primitive {

    String s = "a" + "b" + "c";

    private static Primitive p = newPrim();

    private static Primitive newPrim() {
        Primitive p = new Primitive();
        return p;
    }


    /**
     * {@native ts
     * return "bbb";
     * }
     */
    public String funct(String t) {
        return "aaa";
    }


    /**
     * @native ts
     * return "bbbb";
     */
    public String funct2(String y) {
        return "aaaa";
    }



}
