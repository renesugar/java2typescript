/**
 * Copyright 2017 The Java2TypeScript Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sources.arrays.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by gnain on 03/06/16.
 */
public class DoubleArray {

    public double[][] arrayD = new double[1][];
    public int[][] arrayI;
    public double[][][][] arrayD2 = new double[10][9][8][7];

    private double[][] _values;

    DoubleArray() {
        this._values = new double[10][10];
    }

    public Class<?> c;

    public void set() {

        double d = Double.POSITIVE_INFINITY;
        double d2 = Double.NEGATIVE_INFINITY;

        double[][] centroids = new double[1][2];
        arrayD = new double[1][2];
        arrayI = new int[1][];

        arrayD2 = new double[20][19][18][17];

        boolean[] _back_colors = new boolean[10];
    }

    public void sortTest() {
        int[] arr = new int[]{0, 1, 2, 3, 4};
        Arrays.sort(arr);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(1., 2., 3.);
        }
    }

}
