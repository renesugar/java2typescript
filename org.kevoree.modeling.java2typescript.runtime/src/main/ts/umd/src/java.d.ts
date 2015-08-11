declare module 'java' {
  export module lang {
      class Float {
          static parseFloat(val: string): number;
      }
      class Integer {
          static parseInt(val: string): number;
      }
      class Long {
          static parseLong(val: string): number;
      }
      class Boolean {
          static parseBoolean(val: string): boolean;
      }
      class Short {
          static MIN_VALUE: number;
          static MAX_VALUE: number;
          static parseShort(val: string): number;
      }
      class Throwable {
          private message;
          private error;
          constructor(message: string);
          printStackTrace(): void;
      }
      class System {
          static gc(): void;
          static out: {
              println(obj?: any): void;
              print(obj: any): void;
          };
          static err: {
              println(obj?: any): void;
              print(obj: any): void;
          };
          static arraycopy(src: any[] | ArrayBuffer, srcPos: number, dest: any[] | ArrayBuffer, destPos: number, numElements: number): void;
      }
      class Exception extends Throwable {
      }
      class RuntimeException extends Exception {
      }
      class IndexOutOfBoundsException extends Exception {
      }
      interface Runnable {
          run(): void;
      }
      class StringBuilder {
          private buffer;
          private length;
          append(val: any): StringBuilder;
          toString(): string;
      }
      module ref {
          class WeakReference<A> {
          }
      }
  }
  export module util {
      module concurrent {
          module atomic {
              class AtomicIntegerArray {
                  _internal: Int32Array;
                  constructor(p: Int32Array);
                  set(index: number, newVal: number): void;
                  get(index: number): number;
                  getAndSet(index: number, newVal: number): number;
                  compareAndSet(index: number, expect: number, update: number): boolean;
              }
              class AtomicReference<A> {
                  _internal: A;
                  compareAndSet(expect: A, update: A): boolean;
                  get(): A;
                  set(newRef: A): void;
                  getAndSet(newVal: A): A;
              }
              class AtomicLong {
                  _internal: number;
                  constructor(init: number);
                  compareAndSet(expect: number, update: number): boolean;
                  get(): number;
                  incrementAndGet(): number;
                  decrementAndGet(): number;
              }
              class AtomicInteger {
                  _internal: number;
                  constructor(init: number);
                  compareAndSet(expect: number, update: number): boolean;
                  get(): number;
                  set(newVal: number): void;
                  getAndSet(newVal: number): number;
                  incrementAndGet(): number;
                  decrementAndGet(): number;
                  getAndIncrement(): number;
                  getAndDecrement(): number;
              }
          }
      }
      class Random {
          nextInt(max?: number): number;
          nextDouble(): number;
          nextBoolean(): boolean;
      }
      class Arrays {
          static fill(data: any, begin: number, nbElem: number, param: number): void;
      }
      class Collections {
          static reverse<A>(p: List<A>): void;
          static sort<A>(p: List<A>): void;
      }
      interface Collection<T> {
          add(val: T): void;
          addAll(vals: Collection<T>): void;
          remove(val: T): void;
          clear(): void;
          isEmpty(): boolean;
          size(): number;
          contains(val: T): boolean;
          toArray(a: Array<T>): T[];
      }
      class XArray {
          length: number;
          constructor();
          pop(): any;
          push(val: any): number;
          splice(newS: any, arrL: any): void;
          indexOf(val: any): number;
          shift(): any;
          sort(): void;
      }
      class List<T> extends XArray implements Collection<T> {
          addAll(vals: Collection<T>): void;
          clear(): void;
          poll(): T;
          remove(val: T): void;
          toArray(a: Array<T>): T[];
          size(): number;
          add(val: T): void;
          get(index: number): T;
          contains(val: T): boolean;
          isEmpty(): boolean;
      }
      class ArrayList<T> extends List<T> {
      }
      class LinkedList<T> extends List<T> {
      }
      class Stack<T> {
          content: any[];
          pop(): T;
          push(t: T): void;
          isEmpty(): boolean;
          peek(): T;
      }
      class Map<K, V> {
          get(key: K): V;
          put(key: K, value: V): V;
          containsKey(key: K): boolean;
          remove(key: K): V;
          keySet(): Set<K>;
          isEmpty(): boolean;
          values(): Set<V>;
          clear(): void;
      }
      class HashMap<K, V> extends Map<K, V> {
      }
      class Set<T> implements Collection<T> {
          add(val: T): void;
          clear(): void;
          contains(val: T): boolean;
          addAll(vals: Collection<T>): void;
          remove(val: T): void;
          size(): number;
          isEmpty(): boolean;
          toArray(a: Array<T>): T[];
      }
      class HashSet<T> extends Set<T> {
      }
  }
}
