var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var java;
(function (java) {
    var lang;
    (function (lang) {
        var System = (function () {
            function System() {
            }
            System.gc = function () {
            };
            System.arraycopy = function (src, srcPos, dest, destPos, numElements) {
                for (var i = 0; i < numElements; i++) {
                    dest[destPos + i] = src[srcPos + i];
                }
            };
            return System;
        })();
        lang.System = System;
        var StringBuilder = (function () {
            function StringBuilder() {
                this._buffer = '';
                this.length = 0;
            }
            StringBuilder.prototype.append = function (val) {
                this._buffer = this._buffer + val;
                length = this._buffer.length;
                return this;
            };
            StringBuilder.prototype.toString = function () {
                return this._buffer;
            };
            return StringBuilder;
        })();
        lang.StringBuilder = StringBuilder;
    })(lang = java.lang || (java.lang = {}));
    var util;
    (function (util) {
        var concurrent;
        (function (concurrent) {
            var atomic;
            (function (atomic) {
                var AtomicIntegerArray = (function () {
                    function AtomicIntegerArray(p) {
                        this._internal = p;
                    }
                    AtomicIntegerArray.prototype.set = function (index, newVal) {
                        this._internal[index] = newVal;
                    };
                    AtomicIntegerArray.prototype.get = function (index) {
                        return this._internal[index];
                    };
                    AtomicIntegerArray.prototype.getAndSet = function (index, newVal) {
                        var temp = this._internal[index];
                        this._internal[index] = newVal;
                        return temp;
                    };
                    AtomicIntegerArray.prototype.compareAndSet = function (index, expect, update) {
                        if (this._internal[index] == expect) {
                            this._internal[index] = update;
                            return true;
                        }
                        else {
                            return false;
                        }
                    };
                    return AtomicIntegerArray;
                })();
                atomic.AtomicIntegerArray = AtomicIntegerArray;
                var AtomicReference = (function () {
                    function AtomicReference() {
                        this._internal = null;
                    }
                    AtomicReference.prototype.compareAndSet = function (expect, update) {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        }
                        else {
                            return false;
                        }
                    };
                    AtomicReference.prototype.get = function () {
                        return this._internal;
                    };
                    AtomicReference.prototype.set = function (newRef) {
                        this._internal = newRef;
                    };
                    AtomicReference.prototype.getAndSet = function (newVal) {
                        var temp = this._internal;
                        this._internal = newVal;
                        return temp;
                    };
                    return AtomicReference;
                })();
                atomic.AtomicReference = AtomicReference;
                var AtomicLong = (function () {
                    function AtomicLong(init) {
                        this._internal = 0;
                        this._internal = init;
                    }
                    AtomicLong.prototype.compareAndSet = function (expect, update) {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        }
                        else {
                            return false;
                        }
                    };
                    AtomicLong.prototype.get = function () {
                        return this._internal;
                    };
                    AtomicLong.prototype.incrementAndGet = function () {
                        this._internal++;
                        return this._internal;
                    };
                    AtomicLong.prototype.decrementAndGet = function () {
                        this._internal--;
                        return this._internal;
                    };
                    return AtomicLong;
                })();
                atomic.AtomicLong = AtomicLong;
                var AtomicInteger = (function () {
                    function AtomicInteger(init) {
                        this._internal = 0;
                        this._internal = init;
                    }
                    AtomicInteger.prototype.compareAndSet = function (expect, update) {
                        if (this._internal == expect) {
                            this._internal = update;
                            return true;
                        }
                        else {
                            return false;
                        }
                    };
                    AtomicInteger.prototype.get = function () {
                        return this._internal;
                    };
                    AtomicInteger.prototype.set = function (newVal) {
                        this._internal = newVal;
                    };
                    AtomicInteger.prototype.getAndSet = function (newVal) {
                        var temp = this._internal;
                        this._internal = newVal;
                        return temp;
                    };
                    AtomicInteger.prototype.incrementAndGet = function () {
                        this._internal++;
                        return this._internal;
                    };
                    AtomicInteger.prototype.decrementAndGet = function () {
                        this._internal--;
                        return this._internal;
                    };
                    AtomicInteger.prototype.getAndIncrement = function () {
                        var temp = this._internal;
                        this._internal++;
                        return temp;
                    };
                    AtomicInteger.prototype.getAndDecrement = function () {
                        var temp = this._internal;
                        this._internal--;
                        return temp;
                    };
                    return AtomicInteger;
                })();
                atomic.AtomicInteger = AtomicInteger;
            })(atomic = concurrent.atomic || (concurrent.atomic = {}));
        })(concurrent = util.concurrent || (util.concurrent = {}));
        var Random = (function () {
            function Random() {
            }
            Random.prototype.nextInt = function (max) {
                if (typeof max === 'undefined') {
                    max = Math.pow(2, 32);
                }
                return Math.floor(Math.random() * max);
            };
            Random.prototype.nextDouble = function () {
                return Math.random();
            };
            Random.prototype.nextBoolean = function () {
                return Math.random() >= 0.5;
            };
            return Random;
        })();
        util.Random = Random;
        var Arrays = (function () {
            function Arrays() {
            }
            Arrays.fill = function (data, begin, nbElem, param) {
                var max = begin + nbElem;
                for (var i = begin; i < max; i++) {
                    data[i] = param;
                }
            };
            return Arrays;
        })();
        util.Arrays = Arrays;
        var Collections = (function () {
            function Collections() {
            }
            Collections.reverse = function (p) {
                var temp = new List();
                for (var i = 0; i < p.size(); i++) {
                    temp.add(p.get(i));
                }
                p.clear();
                for (var i = temp.size() - 1; i >= 0; i--) {
                    p.add(temp.get(i));
                }
            };
            Collections.sort = function (p) {
                p.sort();
            };
            return Collections;
        })();
        util.Collections = Collections;
        var XArray = (function () {
            function XArray() {
                Array.apply(this, arguments);
                return new Array();
            }
            XArray.prototype.pop = function () {
                return "";
            };
            XArray.prototype.push = function (val) {
                return 0;
            };
            XArray.prototype.splice = function (newS, arrL) {
            };
            XArray.prototype.indexOf = function (val) {
                return 0;
            };
            XArray.prototype.shift = function () {
                return "";
            };
            XArray.prototype.sort = function () {
            };
            return XArray;
        })();
        util.XArray = XArray;
        XArray.prototype = new Array();
        var List = (function (_super) {
            __extends(List, _super);
            function List() {
                _super.apply(this, arguments);
            }
            List.prototype.addAll = function (vals) {
                var tempArray = vals.toArray(null);
                for (var i = 0; i < tempArray.length; i++) {
                    this.push(tempArray[i]);
                }
            };
            List.prototype.clear = function () {
                this.length = 0;
            };
            List.prototype.poll = function () {
                return this.shift();
            };
            List.prototype.remove = function (val) {
            };
            List.prototype.toArray = function (a) {
                return this;
            };
            List.prototype.size = function () {
                return this.length;
            };
            List.prototype.add = function (val) {
                this.push(val);
            };
            List.prototype.get = function (index) {
                return this[index];
            };
            List.prototype.contains = function (val) {
                return this.indexOf(val) != -1;
            };
            List.prototype.isEmpty = function () {
                return this.length == 0;
            };
            return List;
        })(XArray);
        util.List = List;
        var ArrayList = (function (_super) {
            __extends(ArrayList, _super);
            function ArrayList() {
                _super.apply(this, arguments);
            }
            return ArrayList;
        })(List);
        util.ArrayList = ArrayList;
        var LinkedList = (function (_super) {
            __extends(LinkedList, _super);
            function LinkedList() {
                _super.apply(this, arguments);
            }
            return LinkedList;
        })(List);
        util.LinkedList = LinkedList;
        var Stack = (function () {
            function Stack() {
                this.content = new Array();
            }
            Stack.prototype.pop = function () {
                return this.content.pop();
            };
            Stack.prototype.push = function (t) {
                this.content.push(t);
            };
            Stack.prototype.isEmpty = function () {
                return this.content.length == 0;
            };
            Stack.prototype.peek = function () {
                return this.content.slice(-1)[0];
            };
            return Stack;
        })();
        util.Stack = Stack;
        var Map = (function () {
            function Map() {
            }
            Map.prototype.get = function (key) {
                return this[key];
            };
            Map.prototype.put = function (key, value) {
                var previous_val = this[key];
                this[key] = value;
                return previous_val;
            };
            Map.prototype.containsKey = function (key) {
                return this.hasOwnProperty(key);
            };
            Map.prototype.remove = function (key) {
                var tmp = this[key];
                delete this[key];
                return tmp;
            };
            Map.prototype.keySet = function () {
                var result = new HashSet();
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        result.add(p);
                    }
                }
                return result;
            };
            Map.prototype.isEmpty = function () {
                return Object.keys(this).length == 0;
            };
            Map.prototype.values = function () {
                var result = new HashSet();
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        result.add(this[p]);
                    }
                }
                return result;
            };
            Map.prototype.clear = function () {
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        delete this[p];
                    }
                }
            };
            return Map;
        })();
        util.Map = Map;
        var HashMap = (function (_super) {
            __extends(HashMap, _super);
            function HashMap() {
                _super.apply(this, arguments);
            }
            return HashMap;
        })(Map);
        util.HashMap = HashMap;
        var Set = (function () {
            function Set() {
            }
            Set.prototype.add = function (val) {
                this[val] = val;
            };
            Set.prototype.clear = function () {
                for (var p in this) {
                    if (this.hasOwnProperty(p)) {
                        delete this[p];
                    }
                }
            };
            Set.prototype.contains = function (val) {
                return this.hasOwnProperty(val);
            };
            Set.prototype.addAll = function (vals) {
                var tempArray = vals.toArray(null);
                for (var i = 0; i < tempArray.length; i++) {
                    this[tempArray[i]] = tempArray[i];
                }
            };
            Set.prototype.remove = function (val) {
                delete this[val];
            };
            Set.prototype.size = function () {
                return Object.keys(this).length;
            };
            Set.prototype.isEmpty = function () {
                return this.size() == 0;
            };
            Set.prototype.toArray = function (a) {
                for (var ik in this) {
                    a.push(this[ik]);
                }
                return a;
            };
            return Set;
        })();
        util.Set = Set;
        var HashSet = (function (_super) {
            __extends(HashSet, _super);
            function HashSet() {
                _super.apply(this, arguments);
            }
            return HashSet;
        })(Set);
        util.HashSet = HashSet;
    })(util = java.util || (java.util = {}));
})(java || (java = {}));
//# sourceMappingURL=java.js.map