import { User } from '../../types/User';
import { log } from '../functions/console';
import { IGame } from '../interfaces/IGame';
import { ILobby } from '../interfaces/ILobby';

/**
 * A simple class for defining a lobby with a set of users.
 * Used to create a game.
 *
 * @class Lobby
 * @property {User[]} users - The users in the lobby
 * @property {number} id - The id of the lobby
 * @method addUser - Adds a user to the lobby
 * @method removeUser - Removes a user from the lobby
 * @method getUsers - Returns all users from the lobby
 */

export class Lobby implements ILobby {
  private users: User[];
  private game?: IGame;
  gameId: string | undefined;
  id: number;

  constructor(id: number) {
    this.users = [];
    this.id = id;
    log('Lobby ' + this.id + ' created');
  }

  getGameId(): string | undefined {
    return this.gameId;
  }

  setGameId(gameId: string): void {
    this.gameId = gameId;
  }

  setId(id: number): void {
    this.id = id;
  }

  getId(): number {
    return this.id;
  }

  addUser(user: User) {
    if (this.users.length >= 2) {
      log(user?.username + ' tried to join lobby ' + this.id + ' but its full');
      return;
    }
    this.users.push(user);
    log(user?.username + ' joined lobby ' + this.id + '.');
  }

  removeUser(user: User) {
    this.users = this.users.filter((u) => u.id !== user.id);
    log(user?.username + ' left lobby ' + this.id + '.');
  }

  addGame(game: IGame) {
    this.game = game;
    log(game?.gameId + ' added to lobby ' + this.id + '.');
  }

  getUsers() {
    return this.users;
  }

  isFull() {
    return this.game != undefined;
  }
}
