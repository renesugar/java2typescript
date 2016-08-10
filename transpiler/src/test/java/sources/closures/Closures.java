package sources.closures;

/**
 * Created by gnain on 10/08/16.
 */
public class Closures {


    private ClosureInterface ci = () -> { };
    private ClosureInterface ci2 = () -> System.out.println("super !!");

    private ClosureInterfaceWithParam cip = (b) -> {System.out.println("Another");System.out.println("super !!");};

    private ClosureInterfaceWithParamAndResult cipr = (b) -> b && true;

    public Closures() {

        this.localMethod(bool->bool);
        this.localMethod(bool->{return bool;});
        this.localMethod((bool)->{return bool;});
        this.localMethod(((bool)->{return bool;}));
    }


    private void localMethod(ClosureInterfaceWithParamAndResult clos) {
        clos.methodParam(false);
    }

}
