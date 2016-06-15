package sources.strings;

public class InFunction {

    private String str;
    private String str2;

    public void foo() {
        String str = new String("foo");
        String str2 = String.join(", ", new String[] { "a", "b", "c" });
        String str3 = str2.trim();
    }

    public void bar() {
        this.str = this.str2.concat(this.str);
        String s = getString();
        String s2 = getString().replace('o', 'a');
        String s3 = this.getF().getF().getString();

        s3.startsWith("a");
    }

    public String getString() {
        return "potato";
    }

    public InFunction getF() {
        return this;
    }
}