module sources {
  export   module generics {
    export class MyArrayList<E> implements MyList<E> {
      public add(elem: E): void {}
      public add(index: number, elem: E): void {}
      public remove(elem: E): void {}
      public remove(index: number): void {}
    }
  }
}
