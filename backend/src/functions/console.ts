/**
 * log() is a function for logging messages to the console in a standard format.
 *
 * @param message The message to log
 * @returns void
 */
export function log(message: string, status: string = 'info'): void {
  const time = new Date().toLocaleTimeString();
  const file = new Error().stack?.split('at ')[2].split('\n')[0].trim();
  const cleanFile = file?.replace(/[()]/g, '');
  const maxLength = 25; // Set the desired maximum length for the file section
  const paddedFile =
    cleanFile?.split('/').pop()?.padEnd(maxLength, ' ') ?? ''.padEnd(maxLength, ' ');
  // if status is info, log in default/black color. If warning use yellow, if danger use red
  const statusColor =
    status === 'info' ? '\x1b[0m' : status === 'warning' ? '\x1b[33m' : '\x1b[31m';
  process.stdout.write(
    statusColor + time + ' | ' + paddedFile + ' | ' + message + '\x1b[0m\n'
  );
}

/**
 * slopePreview() is a helper function for visualizing the terrain generated by the generateYValues() function.
 * It takes an array of y-values and prints a preview of the terrain to the console.
 *
 * @param yValues An array of y-values
 * @param maxY The maximum y-value in the array
 * @param width The width of the terrain preview
 * @param height The height of the terrain preview
 */
export default function slopePreview(
  yValues: number[],
  maxY: number,
  width: number,
  height: number
) {
  const terrain: string[][] = [];

  for (let i = 0; i < height; i++) {
    terrain[i] = [];
    for (let j = 0; j < width; j++) {
      terrain[i][j] = ' ';
    }
  }

  for (let i = 0; i < yValues.length; i++) {
    const x = Math.floor((i / (yValues.length - 1)) * width);
    const y = height - 1 - Math.floor((yValues[i] / maxY) * height);
    terrain[y][x] = '#';
  }

  const terrainString = terrain.map((row) => row.join('')).join('\n');
  console.log(terrainString);
}
