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
    }

    public void tt2(String... its) {

    }


}
