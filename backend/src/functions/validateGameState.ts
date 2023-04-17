import { IGame } from '../interfaces/IGame';
import { log } from './console';

export default function validate(oldGameState: IGame, newGameState: IGame): boolean {
  // compare the current game state with the new game state
  if (oldGameState.gameId !== newGameState.gameId) {
    log('Game ID mismatch');
    return false;
  }

  if (oldGameState.gameStatus !== newGameState.gameStatus) {
    log('Game status mismatch');
    return false;
  }

  if (oldGameState.users.length !== newGameState.users.length) {
    log('User length mismatch');
    return false;
  }

  return true;
}
