module sources {
  export   module strings {
    export class InFunction {
      private str: string;
      private str2: string;
      public foo(): void {
        let str: string = "foo";
        let str2: string = java.lang.String.join(", ", ["a", "b", "c"]);
        let str3: string = str2.trim();
      }
      public bar(): void {
        this.str = this.str2 + this.str;
        let s: string = this.getString();
        let s2: string = this.getString().replace('o', 'a');
        let s3: string = this.getF().getF().getString();
        (s3.lastIndexOf("a", 0) === 0);
        let tt: string[] = ["f1", "f2"];
        this.essay(["f1", "f2"]);
      }
      public getString(): string {
        return "potato";
      }
      public getF(): sources.strings.InFunction {
        return this;
      }
      public essay(res: string[]): void {}
    }
  }
}
