module sources {
  export   module closures {
    export class Closures {
      private ci: ()=>void = ()=>{
      };
      private ci2: ()=>void = ()=>(console.log("super !!"));
      private cip: (b: boolean)=>void = (b)=>{
        console.log("Another");
        console.log("super !!");
      };
      private cipr: (b: boolean)=>boolean = (b)=>(b && true);
      private fci: ()=>void = ()=>{
      };
      private fci2: ()=>void = ()=>(console.log("super !!"));
      private fcip: (b: boolean)=>void = (b)=>{
        console.log("Another");
        console.log("super !!");
      };
      private fcipr: (b: boolean)=>boolean = (b)=>(b && true);
      constructor() {
        this.localMethod((() => {let r:any=()=>{};r.methodParam=(bool)=>(bool);return r;})());
        this.localMethod((() => {let r:any=()=>{};r.methodParam=(bool)=>{
          return bool;
        };return r;})());
        this.localMethod((() => {let r:any=()=>{};r.methodParam=(bool)=>{
          return bool;
        };return r;})());
        this.localMethod(((() => {let r:any=()=>{};r.methodParam=(bool)=>{
          return bool;
        };return r;})()));
        this.localMethod2((() => {let r:any=()=>{};r.methodParam=(bool)=>(bool);return r;})(), true);
        this.functionalLocalMethod((bool)=>(bool));
        this.functionalLocalMethod((bool)=>{
          return bool;
        });
        this.functionalLocalMethod((bool)=>{
          return bool;
        });
        this.functionalLocalMethod(((bool)=>{
          return bool;
        }));
        this.functionalLocalMethod2((bool)=>(bool), true);
      }
      private localMethod(clos: ClosureInterfaceWithParamAndResult): void {
        clos.methodParam(false);
      }
      private localMethod2(clos: ClosureInterfaceWithParamAndResult, c: boolean): void {
        clos.methodParam(false);
      }
      private functionalLocalMethod(clos: FunctionalClosureInterfaceWithParamAndResult): void {
        clos(false);
      }
      private functionalLocalMethod2(clos: FunctionalClosureInterfaceWithParamAndResult, c: boolean): void {
        clos(false);
      }
    }
  }
}
