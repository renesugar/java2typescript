module sources {
  export   module generics {
    export interface MyList<E> {
      add(elem: E): void;
      add(index: number, elem: E): void;
      remove(elem: E): void;
      remove(index: number): void;
    }
  }
}
