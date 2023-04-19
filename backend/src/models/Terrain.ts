import { generateRandomNumbers } from '../functions/TerrainGenerator';
import { ITerrain } from '../interfaces/ITerrain';

export class Terrain implements ITerrain {
  yValues: number[];
  minValue: number;
  maxValue: number;
  n: number; // number of points to generate

  constructor(minValue: number, maxValue: number, n: number) {
    this.maxValue = maxValue;
    this.minValue = minValue;
    this.n = n;
    this.yValues = this.generate();
  }

  generate(): number[] {
    // legacy: return generateYValues(this.maxY, this.xPoints);
    return generateRandomNumbers(this.n, this.minValue, this.maxValue);
  }

  getYValues(): number[] {
    return this.yValues;
  }
}
