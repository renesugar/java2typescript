export class DoubleArray {
  public arrayD: Array<Float64Array> = new Array<Float64Array>(1);
  public arrayI: Array<Int32Array>;
  public arrayD2: Array<Array<Array<Float64Array>>> = new Array<Array<Array<Float64Array>>>(10);
  public c: any;
  public set(): void {
    var d: number = java.lang.Double.POSITIVE_INFINITY;
    var d2: number = java.lang.Double.NEGATIVE_INFINITY;
    var centroids: Array<Float64Array> = new Array<Float64Array>(1);
    for(var centroids_d1 = 0; centroids_d1 < 1; centroids_d1++){
      centroids[centroids_d1] = new Float64Array(2);
    };
    this.arrayD = new Array<Float64Array>(1);
    for(var arrayD_d1 = 0; arrayD_d1 < 1; arrayD_d1++){
      this.arrayD[arrayD_d1] = new Float64Array(2);
    };
    this.arrayI = new Array<Int32Array>(1);
    this.arrayD2 = new Array<Array<Array<Float64Array>>>(20);
    for(var arrayD2_d1 = 0; arrayD2_d1 < 20; arrayD2_d1++){
      this.arrayD2[arrayD2_d1] = new Array<Array<Float64Array>>(19);
      for(var arrayD2_d2 = 0; arrayD2_d2 < 19; arrayD2_d2++){
        this.arrayD2[arrayD2_d1][arrayD2_d2] = new Array<Float64Array>(18);
        for(var arrayD2_d3 = 0; arrayD2_d3 < 18; arrayD2_d3++){
          this.arrayD2[arrayD2_d1][arrayD2_d2][arrayD2_d3] = new Float64Array(17);
        }
      }
    };
    var _back_colors: boolean[] = [];
  }
  public sortTest(): void {
    var arr: Int32Array = new Int32Array([0, 1, 2, 3, 4]);
    arr.sort();
    for (var i: number = 0; i < 10; i++) {
      org.junit.Assert.assertEquals(2.,3.,1.);
    }
  }
}