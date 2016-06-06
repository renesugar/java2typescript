export class DoubleArray {
  public arrayD: Array<Float64Array> = new Array<Float64Array>(1);
  public arrayI: Array<Int32Array>;
  public set(): void {
    var centroids: Array<Float64Array> = new Array<Float64Array>(1);
    this.arrayD = new Array<Float64Array>(1);
    this.arrayI = new Array<Int32Array>(1);
    var _back_colors: boolean[] = [];
  }
}