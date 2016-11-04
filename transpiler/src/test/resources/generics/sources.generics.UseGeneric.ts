module sources {
  export   module generics {
    export class UseGeneric {
      public strList: MyList<string>;
      public map: MyMap<string, number>;
      public static LEVELA: number = 0;
      public static LEVELB: number = UseGeneric.LEVELA;
      public static LEVEL: string = "" + UseGeneric.LEVELA;
      constructor() {
        this.strList = new MyArrayList<string>();
        this.map = new MyHashMap<string, number>();
      }
      public arrayAsGenType(list: MyList<MyList[]>): void {}
    }
  }
}
