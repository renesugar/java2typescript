module sources {
  export   module generics {
    export interface MyMap<K, V> {
      put(key: K, value: V): void;
      get(key: K): V;
      remove(key: K): void;
    }
  }
}
