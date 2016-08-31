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
  private ci: ()=>void = ()=>{
  };
  private ci2: ()=>void = ()=>(console.log("super !!"));
  private cip: (b: boolean)=>void = (b)=>{
    console.log("Another");
    console.log("super !!");
  };
  private cipr: (b: boolean)=>boolean = (b)=>(b && true);
  constructor() {
    this.localMethod((() => {var r:any=()=>{};r.methodParam=(bool)=>(bool);return r;})());
    this.localMethod((() => {var r:any=()=>{};r.methodParam=(bool)=>{
      return bool;
    };return r;})());
    this.localMethod((() => {var r:any=()=>{};r.methodParam=(bool)=>{
      return bool;
    };return r;})());
    this.localMethod(((() => {var r:any=()=>{};r.methodParam=(bool)=>{
      return bool;
    };return r;})()));
    this.localMethod2((() => {var r:any=()=>{};r.methodParam=(bool)=>(bool);return r;})(), true);
  }
  private localMethod(clos: ClosureInterfaceWithParamAndResult): void {
    clos.methodParam(false);
  }
  private localMethod2(clos: ClosureInterfaceWithParamAndResult, c: boolean): void {
    clos.methodParam(false);
  }
}