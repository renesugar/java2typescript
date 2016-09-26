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
