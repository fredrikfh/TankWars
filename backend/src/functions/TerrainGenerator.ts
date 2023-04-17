import slopePreview from './console';

const { createNoise2D } = require('simplex-noise');

/**
 * Creates a more realistic terrain by generating a random number of points and interpolating between them.
 * @param maxY The max height of the terrain
 * @param numValues The number of points to generate
 * @returns An array of y-values
 */
export default function generateYValues(maxY: number, numValues: number): number[] {
  const yValues: number[] = [];
  const noiseScale = 0.0025;

  const noise2D = createNoise2D();

  for (let i = 0; i < numValues; i++) {
    const x = i;
    const t = x / (numValues - 1); // Normalize x to range [0, 1]
    const noiseValue = noise2D(x * noiseScale, t * noiseScale); // Generate Simplex noise value for this point
    const y = maxY * (0.5 + 0.5 * noiseValue); // Scale and shift the noise value to fit in the [0, maxY] range
    yValues.push(y);
  }
  // slopePreview(yValues, maxY, 40, 20);

  return yValues;
}

/**
 * Generates an array of random numbers.
 * @param n number of points to generate
 * @param min minumum value
 * @param max maximum value
 * @returns array of random numbers
 */
export function generateRandomNumbers(n: number, min: number, max: number): number[] {
  const randomNumbers: number[] = [];

  for (let i = 0; i < n; i++) {
    const randomNumber = Math.floor(Math.random() * (max - min + 1)) + min;
    randomNumbers.push(randomNumber);
  }

  return randomNumbers;
}
