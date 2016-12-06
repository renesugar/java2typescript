export class MyClass {
  private args: string[];
  constructor(...cargs: string[]) {
    this.args = cargs;
  }
  public tt(): void {
    let c: sources.varargs.MyClass = new sources.varargs.MyClass(...this.args);
    c.tt2(...this.args);
  }
  public tt2(...its: string[]): void {}
}