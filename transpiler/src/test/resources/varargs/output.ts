export class MyClass {
  private args: string[];
  constructor(...cargs: string[]) {
    this.args = cargs;
  }
  public tt(): void {
    let c: sources.varargs.MyClass = new sources.varargs.MyClass(...this.args);
    c.tt2(...this.args);
    let newArgs: string[] = ["a", "b"];
    c.tt2(...newArgs);
    c.tt2(...this.createVarArgs());
  }
  public tt2(...its: string[]): void {}
  public createVarArgs(): string[] {
    return new Array<string>(0);
  }
}