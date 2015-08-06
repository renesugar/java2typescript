class System {

    static gc() {
    }

    static out = {
        println(obj?:any) {
            console.log(obj);
        },
        print(obj:any) {
            console.log(obj);
        }
    };

    static err = {
        println(obj?:any) {
            console.error(obj);
        },
        print(obj:any) {
            console.error(obj);
        }
    };

    static arraycopy(src:any, srcPos:number, dest:any, destPos:number, numElements:number):void {
        if (src instanceof Float64Array && dest instanceof Float64Array) {
            for (var i = 0; i < numElements; i++) {
                dest[destPos + i] = src[srcPos + i];
            }
        } else if (src instanceof Int32Array && dest instanceof Int32Array) {
            for (var i = 0; i < numElements; i++) {
                dest[destPos + i] = src[srcPos + i];
            }
        } else {
            for (var i = 0; i < numElements; i++) {
                dest[destPos + i] = src[srcPos + i];
            }
        }
    }
}

interface Number {
    equals : (other:Number) => boolean;
    longValue() : number;
    floatValue() : number;
    intValue() : number;
    shortValue() : number;
}

Number.prototype.equals = function (other) {
    return this == other;
};

interface String {
    equals : (other:String) => boolean;
    startsWith : (other:String) => boolean;
    endsWith : (other:String) => boolean;
    matches :  (regEx:String) => boolean;
    //getBytes : () => number[];
    isEmpty : () => boolean;
    hashCode : () => number;
}

class StringUtils {
    static copyValueOf(data:string[], offset:number, count:number):string {
        var result:string = "";
        for (var i = offset; i < offset + count; i++) {
            result += data[i];
        }
        return result;
    }
}

String.prototype.matches = function (regEx) {
    if (regEx == null) {
        return false;
    } else {
        var m = this.match(regEx);
        return m != null && m.length > 0;
    }
};

String.prototype.isEmpty = function () {
    return this.length == 0;
};

String.prototype.equals = function (other) {
    return this == other;
};

String.prototype.hashCode = function () {
    var hash = 0, i, chr, len;
    if (this.length == 0) return hash;
    for (i = 0, len = this.length; i < len; i++) {
        chr = this.charCodeAt(i);
        hash = ((hash << 5) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
    }
    return hash;
};

String.prototype.startsWith = function (other) {
    return this.slice(0, other.length) == other;
};

String.prototype.endsWith = function (other) {
    return this.slice(-other.length) == other;
};

interface Boolean {
    equals : (other:String) => boolean;
}
Boolean.prototype.equals = function (other) {
    return this == other;
};

module java {

    export module lang {

        export class Double {

            public static MIN_VALUE = Number.MIN_VALUE;
            public static MAX_VALUE = Number.MAX_VALUE;

            public static parseDouble(val:string):number {
                return +val;
            }

            public static isNaN(val:any):boolean {
                return isNaN(val);
            }
        }

        export class Float {
            public static parseFloat(val:string):number {
                return +val;
            }
        }

        export class Integer {
            public static parseInt(val:string):number {
                return +val;
            }
        }

        export class Long {
            public static parseLong(val:string):number {
                return +val;
            }
        }

        export class Boolean {
            public static parseBoolean(val:string):boolean {
                return val == "true";
            }
        }

        export class Short {
            public static MIN_VALUE = -0x8000;
            public static MAX_VALUE = 0x7FFF;

            public static parseShort(val:string):number {
                return +val;
            }
        }

        export class Throwable {

            private message:string;
            private error:Error;

            constructor(message:string) {
                this.message = message;
                this.error = new Error(message);
            }

            printStackTrace() {
                //console.error(this.error['stack']);
                console.error(this.error);
            }
        }


        export class Exception extends Throwable {
        }

        export class RuntimeException extends Exception {
        }

        export class IndexOutOfBoundsException extends Exception {
        }

        export interface Runnable {
            run():void;
        }

        export class StringBuilder {

            buffer = "";

            public length = 0;

            append(val:any):StringBuilder {
                this.buffer = this.buffer + val;
                length = this.buffer.length;
                return this;
            }

            toString():string {
                return this.buffer;
            }

        }

        export module ref {

            export class WeakReference<A> {
            }

        }

    }

    export module util {

        export module concurrent {

            export module atomic {

                export class AtomicReference<A> {
                    _internal : A = null;

                    compareAndSet(expect:A, update:A):boolean {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        } else {
                            return false;
                        }
                    }

                    get() : A {
                        return this._internal
                    }

                    set(newRef : A){
                        this._internal = newRef;
                    }

                    getAndSet(newVal:A):A {
                        var temp = this._internal;
                        this._internal = newVal;
                        return temp;
                    }

                }

                export class AtomicLong {
                    _internal = 0;

                    constructor(init:number) {
                        this._internal = init;
                    }

                    compareAndSet(expect:number, update:number):boolean {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        } else {
                            return false;
                        }
                    }

                    get():number {
                        return this._internal;
                    }

                    incrementAndGet():number {
                        this._internal++;
                        return this._internal;
                    }

                    decrementAndGet():number {
                        this._internal--;
                        return this._internal;
                    }
                }

                export class AtomicInteger {
                    _internal = 0;

                    constructor(init:number) {
                        this._internal = init;
                    }

                    compareAndSet(expect:number, update:number):boolean {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        } else {
                            return false;
                        }
                    }

                    get():number {
                        return this._internal;
                    }

                    set(newVal:number) {
                        this._internal = newVal
                    }

                    getAndSet(newVal:number):number {
                        var temp = this._internal;
                        this._internal = newVal;
                        return temp;
                    }

                    incrementAndGet():number {
                        this._internal++;
                        return this._internal;
                    }

                    decrementAndGet():number {
                        this._internal--;
                        return this._internal;
                    }

                    getAndIncrement():number {
                        var temp = this._internal;
                        this._internal++;
                        return temp;
                    }

                    getAndDecrement():number {
                        var temp = this._internal;
                        this._internal--;
                        return temp;
                    }

                }

            }

        }

        export class Random {
            public nextInt(max:number):number {
                return Math.random() * max;
            }

            public nextDouble():number {
                return Math.random();
            }

            public nextBoolean():boolean {
                return Math.random() >= 0.5;
            }
        }

        export class Arrays {
            static fill(data:any, begin:number, nbElem:number, param:number):void {
                var max = begin + nbElem;
                for (var i = begin; i < max; i++) {
                    data[i] = param;
                }
            }
        }

        export class Collections {

            public static reverse<A>(p:List<A>):void {
                var temp = new List<A>();
                for (var i = 0; i < p.size(); i++) {
                    temp.add(p.get(i));
                }
                p.clear();
                for (var i = temp.size() - 1; i >= 0; i--) {
                    p.add(temp.get(i));
                }
            }

            public static sort<A>(p:List<A>):void {
                p.sort();
            }

        }

        export interface Collection<T> {
            add(val:T):void
            addAll(vals:Collection<T>)
            remove(val:T)
            clear()
            isEmpty():boolean
            size():number
            contains(val:T):boolean
            toArray(a:Array<T>):T[]
        }

        export class XArray {
            constructor() {
                Array.apply(this, arguments);
                return new Array();
            }

            pop():any {
                return ""
            }

            push(val):number {
                return 0;
            }

            splice(newS, arrL) {
            }

            length:number;

            indexOf(val):number {
                return 0
            }

            shift():any {
                return "";
            }

            sort() {
            }

        }
        XArray["prototype"] = new Array();
        export class List<T> extends XArray implements Collection<T> {

            addAll(vals:Collection<T>) {
                var tempArray = vals.toArray(null);
                for (var i = 0; i < tempArray.length; i++) {
                    this.push(tempArray[i]);
                }
            }

            clear() {
                this.length = 0;
            }

            poll():T {
                return this.shift();
            }

            remove(val:T) {

            }

            toArray(a:Array<T>):T[] {
                return <T[]><any>this;
            }

            size():number {
                return this.length;
            }

            add(val:T):void {
                this.push(val);
            }

            get(index:number):T {
                return this[index];
            }

            contains(val:T):boolean {
                return this.indexOf(val) != -1;
            }

            isEmpty():boolean {
                return this.length == 0;
            }
        }

        export class ArrayList<T> extends List<T> {
        }

        export class LinkedList<T> extends List<T> {
        }

        export class Stack<T> {
            content = new Array();

            pop():T {
                return this.content.pop();
            }

            push(t:T):void {
                this.content.push(t);
            }

            isEmpty():boolean {
                return this.content.length == 0;
            }

            peek():T {
                return this.content.slice(-1)[0];
            }

        }

        export class Map<K, V> {

            get(key:K):V {
                return this[<any>key];
            }

            put(key:K, value:V):V {
                var previous_val = this[<any>key];
                this[<any>key] = value;
                return previous_val;
            }

            containsKey(key:K):boolean {
                return this.hasOwnProperty(<any>key);
            }

            remove(key:K):V {
                var tmp = this[<any>key];
                delete this[<any>key];
                return tmp;
            }

            keySet():Set<K> {
                var result = new HashSet<K>();
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        result.add(p);
                    }
                }
                return result;
            }

            isEmpty():boolean {
                return Object.keys(this).length == 0;
            }

            values():Set<V> {
                var result = new HashSet<V>();
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        result.add(this[p]);
                    }
                }
                return result;
            }

            clear():void {
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        delete this[p];
                    }
                }
            }

        }

        export class HashMap<K, V> extends Map<K,V> {
        }

        export class Set<T> implements Collection<T> {

            add(val:T) {
                this[<any>val] = val;
            }

            clear() {
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        delete this[p];
                    }
                }
            }

            contains(val:T):boolean {
                return this.hasOwnProperty(<any>val);
            }

            addAll(vals:Collection<T>) {
                var tempArray = vals.toArray(null);
                for (var i = 0; i < tempArray.length; i++) {
                    this[<any>tempArray[i]] = tempArray[i];
                }
            }

            remove(val:T) {
                delete this[<any>val];
            }

            size():number {
                return Object.keys(this).length;
            }

            isEmpty():boolean {
                return this.size() == 0;
            }

            toArray(a:Array<T>):T[] {
                for (var ik in this) {
                    a.push(this[ik]);
                }
                return a;
            }
        }

        export class HashSet<T> extends Set<T> {

        }

    }

}

module org {

    export module junit {

        export class Assert {

            public static assertArrayEquals(p:any, p2:any) {
                if (p == null || p == undefined) {
                    if (p2 == null || p2 == undefined) {
                        return;
                    } else {
                        throw "Assert Error " + p + " and " + p2 + " must be equals"
                    }
                }
                if (p2 == null || p2 == undefined) {
                    if (p == null || p == undefined) {
                        return;
                    } else {
                        throw "Assert Error " + p + " and " + p2 + " must be equals"
                    }
                }
                if (p.length != p2.length) {
                    throw "Assert Error " + p + " and " + p2 + " must be equals"
                }
                for (var i = 0; i < p.length; i++) {
                    if (p[i] != p2[i]) {
                        throw "Assert Error " + p + " and " + p2 + " must be equals"
                    }
                }
            }

            public static assertNotNull(p:any):void {
                if (p == null) {
                    throw "Assert Error " + p + " must not be null";
                }
            }

            public static assertNull(p:any):void {
                if (p != null) {
                    throw "Assert Error " + p + " must be null";
                }
            }

            public static assertEquals(p:any, p2:any):void {
                if (p == null) {
                    if (p2 == null) {
                        return;
                    } else {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
                if (p2 == null) {
                    if (p == null) {
                        return;
                    } else {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
                if (p.equals !== undefined) {
                    if (!p.equals(p2)) {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                } else {
                    if (p != p2) {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
            }

            public static assertNotEquals(p:any, p2:any):void {
                if (p.equals !== undefined) {
                    if (p.equals(p2)) {
                        throw "Assert Error \n" + p + "\n must not be equal to \n" + p2 + "\n";
                    }
                } else {
                    if (p == p2) {
                        throw "Assert Error \n" + p + "\n must not be equal to \n" + p2 + "\n";
                    }
                }
            }

            public static assertTrue(b:boolean):void {
                if (!b) {
                    throw "Assert Error " + b + " must be true";
                }
            }

            public static assertFalse(b:boolean):void {
                if (b) {
                    throw "Assert Error " + b + " must be false";
                }
            }

        }
    }
}

