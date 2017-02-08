/**
 * Copyright 2017 The Java2TypeScript Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
