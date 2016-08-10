"use strict";
var Closures = (function () {
    function Closures() {
        this.ci = function () {
        };
        this.ci2 = function () { return (console.log("super !!")); };
        this.cip = function (b) {
            console.log("Another");
            console.log("super !!");
        };
        this.cipr = function (b) { return (b && true); };
        this.localMethod((function () { var r = function () { }; r.methodParam = function (bool) { return (bool); }; return r; })());
        this.localMethod((function () {
            var r = function () { };
            r.methodParam = function (bool) {
                return bool;
            };
            return r;
        })());
        this.localMethod((function () {
            var r = function () { };
            r.methodParam = function (bool) {
                return bool;
            };
            return r;
        })());
        this.localMethod(((function () {
            var r = function () { };
            r.methodParam = function (bool) {
                return bool;
            };
            return r;
        })()));
    }
    Closures.prototype.localMethod = function (clos) {
        clos.methodParam(false);
    };
    return Closures;
}());
exports.Closures = Closures;
