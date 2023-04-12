/**
 * A simple function for logging messages to the console.
 *
 * @param message The message to log
 * @returns void
 */

export function log(message: string): void {
  const time = new Date().toLocaleTimeString();
  const file = new Error().stack?.split('at ')[2].split(' ')[0];
  const maxLength = 20; // Set the desired maximum length for the file section
  const paddedFile = file?.padEnd(maxLength, ' ');
  console.log(time + ' | ' + paddedFile + '| ' + message);
}
