export interface ClosureInterface {
    method(): void;
}
export interface ClosureInterfaceWithParam {
    methodParam(b: boolean): void;
}
export interface ClosureInterfaceWithParamAndResult {
    methodParam(b: boolean): boolean;
}
export class Closures {
    private ci: ()=>void = () => {
    };
    private ci2: ()=>void = () => {
        console.log("super !!")
    };
    private cip: (b: boolean)=>any = (b) => {
        console.log("super !!");
    };
    private cipr: (b: boolean)=>boolean = (b) => {
        return b && true;
    };

    constructor() {
        this.localMethod((bool) => {
            return bool;
        });
        this.localMethod((bool) => {
            return bool;
        });
        this.localMethod((bool) => {
            return bool;
        });
        this.localMethod(((bool) => {
            return bool;
        }));
    }

    private localMethod(clos: (boolean)=>boolean): void {
        clos(false);
    }
}
