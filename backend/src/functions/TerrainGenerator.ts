const { createNoise2D } = require('simplex-noise');

function generateYValues(maxY: number, numValues: number): number[] {
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

  return yValues;
}

export default generateYValues;
