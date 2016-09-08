package sources.closures;

/**
 * Created by gnain on 10/08/16.
 */
public class Closures {


    private ClosureInterface ci = () -> { };
    private ClosureInterface ci2 = () -> System.out.println("super !!");
    private ClosureInterfaceWithParam cip = (b) -> {System.out.println("Another");System.out.println("super !!");};
    private ClosureInterfaceWithParamAndResult cipr = (b) -> b && true;


    private FunctionalClosureInterface fci = () -> { };
    private FunctionalClosureInterface fci2 = () -> System.out.println("super !!");
    private FunctionalClosureInterfaceWithParam fcip = (b) -> {System.out.println("Another");System.out.println("super !!");};
    private FunctionalClosureInterfaceWithParamAndResult fcipr = (b) -> b && true;


    public Closures() {

        this.localMethod(bool->bool);
        this.localMethod(bool->{return bool;});
        this.localMethod((bool)->{return bool;});
        this.localMethod(((bool)->{return bool;}));
        this.localMethod2(bool->bool, true);



        this.functionalLocalMethod(bool->bool);
        this.functionalLocalMethod(bool->{return bool;});
        this.functionalLocalMethod((bool)->{return bool;});
        this.functionalLocalMethod(((bool)->{return bool;}));
        this.functionalLocalMethod2(bool->bool, true);
    }


    private void localMethod(ClosureInterfaceWithParamAndResult clos) {
        clos.methodParam(false);
    }

    private void localMethod2(ClosureInterfaceWithParamAndResult clos, boolean c) {
        clos.methodParam(false);
    }

    private void functionalLocalMethod(FunctionalClosureInterfaceWithParamAndResult clos) {
        clos.methodParam(false);
    }

    private void functionalLocalMethod2(FunctionalClosureInterfaceWithParamAndResult clos, boolean c) {
        clos.methodParam(false);
    }
}
