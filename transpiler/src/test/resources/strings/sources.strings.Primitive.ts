module sources {
  export   module strings {
    export class Primitive {
      public s: string = "a" + "b" + "c";
      private static p: sources.strings.Primitive = Primitive.newPrim();
      private static newPrim(): sources.strings.Primitive {
        let p: sources.strings.Primitive = new sources.strings.Primitive();
        return p;
      }
      public funct(t: string): string {
        return "bbb";
      }
      public funct2(y: string): string {
        return "bbbb";
      }
    }
  }
}
