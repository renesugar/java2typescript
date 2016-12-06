package sources.varargs;

/**
 * Created by gnain on 06/12/16.
 */
public class MyClass {


    private String[] args;

    MyClass(String ...cargs) {
        this.args = cargs;
    }

    public void tt() {
        MyClass c = new MyClass(args);
        c.tt2(args);
        String[] newArgs = new String[]{"a", "b"};
        c.tt2(newArgs);
        c.tt2(createVarArgs());
    }

    public void tt2(String... its) {

    }

    public String[] createVarArgs() {
        return new String[0];
    }


}
