module sources {
  export   module generics {
    export class MyHashMap<K, V> implements MyMap<K, V> {
      public put(key: K, value: V): void {}
      public get(key: K): V {
        return null;
      }
      public remove(key: K): void {}
    }
  }
}
