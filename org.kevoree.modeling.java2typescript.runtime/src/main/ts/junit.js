var org;
(function (org) {
    var junit;
    (function (junit) {
        var Assert = (function () {
            function Assert() {
            }
            Assert.assertArrayEquals = function (p, p2) {
                if (p == null || p == undefined) {
                    if (p2 == null || p2 == undefined) {
                        return;
                    }
                    else {
                        throw "Assert Error " + p + " and " + p2 + " must be equals";
                    }
                }
                if (p2 == null || p2 == undefined) {
                    if (p == null || p == undefined) {
                        return;
                    }
                    else {
                        throw "Assert Error " + p + " and " + p2 + " must be equals";
                    }
                }
                if (p.length != p2.length) {
                    throw "Assert Error " + p + " and " + p2 + " must be equals";
                }
                for (var i = 0; i < p.length; i++) {
                    if (p[i] != p2[i]) {
                        throw "Assert Error " + p + " and " + p2 + " must be equals";
                    }
                }
            };
            Assert.assertNotNull = function (p) {
                if (p == null) {
                    throw "Assert Error " + p + " must not be null";
                }
            };
            Assert.assertNull = function (p) {
                if (p != null) {
                    throw "Assert Error " + p + " must be null";
                }
            };
            Assert.assertEquals = function (p, p2) {
                if (p == null) {
                    if (p2 == null) {
                        return;
                    }
                    else {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
                if (p2 == null) {
                    if (p == null) {
                        return;
                    }
                    else {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
                if (p.equals !== undefined) {
                    if (!p.equals(p2)) {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
                else {
                    if (p != p2) {
                        throw "Assert Error \n" + p + "\n must be equal to \n" + p2 + "\n";
                    }
                }
            };
            Assert.assertNotEquals = function (p, p2) {
                if (p.equals !== undefined) {
                    if (p.equals(p2)) {
                        throw "Assert Error \n" + p + "\n must not be equal to \n" + p2 + "\n";
                    }
                }
                else {
                    if (p == p2) {
                        throw "Assert Error \n" + p + "\n must not be equal to \n" + p2 + "\n";
                    }
                }
            };
            Assert.assertTrue = function (b) {
                if (!b) {
                    throw "Assert Error " + b + " must be true";
                }
            };
            Assert.assertFalse = function (b) {
                if (b) {
                    throw "Assert Error " + b + " must be false";
                }
            };
            return Assert;
        })();
        junit.Assert = Assert;
    })(junit = org.junit || (org.junit = {}));
})(org || (org = {}));
//# sourceMappingURL=junit.js.map