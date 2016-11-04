module sources {
  export   module strings {
    export class ClassFields {
      public str: string = "foo";
      public str2: string = java.lang.String.join(", ", ["a", "b", "c"]);
      public values: string[] = new Array<string>(0);
    }
  }
}
