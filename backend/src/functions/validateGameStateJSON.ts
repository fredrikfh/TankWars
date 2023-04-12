import { IGame } from '../interfaces/IGame';
import { log } from './console';

export function validateGameStateJSON(newGameStateJSON: IGame): boolean {
  let newGameState = newGameStateJSON as IGame;
  log('Validating JSON');

  if (newGameState == null || newGameState == undefined) {
    log('Empty JSON');
    return false;
  }

  // try {
  //   newGameState = JSON.parse(newGameStateJSON);
  // } catch (error) {
  //   console.log("[validateGameStateJSON.ts] Invalid JSON format")
  //   return false;
  // }

  // verify that json-values are legal
  if (
    !newGameState.hasOwnProperty('gameId') ||
    !newGameState.hasOwnProperty('gameStatus') ||
    !newGameState.hasOwnProperty('currentTurn') ||
    !newGameState.hasOwnProperty('users')
  ) {
    log('Invalid JSON properties');
    return false;
  }

  return true;
}
