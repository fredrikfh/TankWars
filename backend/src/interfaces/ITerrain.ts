export interface ITerrain {
  yValues: number[];
  minValue: number;
  maxValue: number;
  n: number; // number of points to generate
  generate(): number[];
  getYValues(): number[];
}
