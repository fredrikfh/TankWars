import generateYValues from '../functions/TerrainGenerator';
import { ITerrain } from '../interfaces/ITerrain';

export class Terrain implements ITerrain {
  yValues: number[];
  maxY: number;
  xPoints: number;

  constructor(maxY: number = 15, numValues: number = 1000) {
    this.maxY = maxY;
    this.xPoints = numValues;
    this.yValues = this.generate();
  }

  generate(): number[] {
    return generateYValues(this.maxY, this.xPoints);
  }

  getYValues(): number[] {
    return this.yValues;
  }
}
