import { GameHandler } from '../gameHandler';
import { log } from './console';

export function disposeInactiveGames() {
  const gameHandler = GameHandler.getInstance();

  // loop through all games
  gameHandler.getGames().forEach((game) => {
    // if game is inactive for 10 minutes
    if (Date.now() - game.getLastActionTimeStamp() > 10 * 60 * 1000) {
      // dispose game and lobby
      log(
        'removing inactive game in lobby:' +
          game.lobby.id +
          ' containing users:' +
          game.lobby
            .getUsers()
            .map((u) => u.username)
            .join(', ')
      );
      gameHandler.removeLobby(game.lobby);
      gameHandler.removeGame(game);
    }
  });
}
