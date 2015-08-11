var r = new java.util.Random();
junit.Assert.assertEquals(typeof r.nextInt(), 'number');

var n = java.lang.Integer.parseInt('42.5');
junit.Assert.assertEquals(n, 42);

var t0 = [0,1,2];
var t1 = [8,9];
java.lang.System.arraycopy(t0, 0, t1, 1, t0.length);
junit.Assert.assertArrayEquals(t1, [8,0,1,2]);

console.log('All tests ok.');
