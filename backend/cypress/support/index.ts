// cypress/support/index.d.ts

declare namespace Cypress {
  interface Chainable<Subject> {
    deleteUser(username: string): void;
    createUser(username: string): void;
    getGameByLobbyId(lobbyId: string): void;
    getCurrentTurn(lobbyId: string, userId: string): void;
    sendMove(gameId: string, gameState: JSON): void;
    joinLobby(lobbyId: number, userId: string): void;
    getUser(username: string): void;
    genericRequest(
      method: string,
      url: string,
      resCode: number,
      body: Record<string, unknown> | any
    ): Chainable<Subject>;
  }
}
