import { util, lang } from 'java';
import { Assert } from 'junit';

var r = new util.Random();
Assert.assertEquals(typeof r.nextInt(), 'number');

var n = lang.Integer.parseInt('42.5');
Assert.assertEquals(n, 42);

var t0 = [0,1,2];
var t1 = [8,9];
lang.System.arraycopy(t0, 0, t1, 1, t0.length);
Assert.assertArrayEquals(t1, [8,0,1,2]);

console.log('All tests ok.');
