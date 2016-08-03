export class ClassFields {
  public str: string = "foo";
  public str2: string = java.lang.String.join(", ", ["a", "b", "c"]);
  public values: string[] = new Array<string>(0);
}
export class InFunction {
  private str: string;
  private str2: string;
  public foo(): void {
    var str: string = "foo";
    var str2: string = java.lang.String.join(", ", ["a", "b", "c"]);
    var str3: string = str2.trim();
  }
  public bar(): void {
    this.str = this.str2 + this.str;
    var s: string = this.getString();
    var s2: string = this.getString().replace('o', 'a');
    var s3: string = this.getF().getF().getString();
    (s3.lastIndexOf("a", 0) === 0);
    var tt: string[] = ["f1", "f2"];
    this.essay(["f1", "f2"]);
  }
  public getString(): string {
    return "potato";
  }
  public getF(): InFunction {
    return this;
  }
  public essay(res: string[]): void {}
}
export class Primitive {
  public s: string = "a" + "b" + "c";
  private static p: Primitive = Primitive.newPrim();
  private static newPrim(): Primitive {
    var p: Primitive = new Primitive();
    return p;
  }
}