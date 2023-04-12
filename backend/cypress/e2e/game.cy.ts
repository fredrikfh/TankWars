describe('Game Test', () => {
  const user1 = 'Cypress1' + Math.floor(Math.random() * 1000000);
  const user2 = 'Cypress2' + Math.floor(Math.random() * 1000000);
  const user3 = 'Cypress3' + Math.floor(Math.random() * 1000000);
  const lobby = Math.floor(Math.random() * 1000);

  it('Can create a game', () => {
    cy.genericRequest('GET', `/game/${lobby}`, 404, '');
    cy.createUser(user1);
    cy.createUser(user2);
    cy.joinLobby(lobby, user1);
    cy.joinLobby(lobby, user2);
    cy.genericRequest('GET', `/game/${lobby}`, 200, '');
    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user1 });
    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user2 });
    cy.deleteUser(user1);
    cy.deleteUser(user2);
  });

  it('Can get currentTurn', () => {
    cy.createUser(user1);
    cy.createUser(user2);
    cy.joinLobby(lobby, user1);
    cy.joinLobby(lobby, user2);
    cy.request({
      method: 'GET',
      url: `${Cypress.config('baseUrl')}/game/${lobby}`,
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.not.be.empty;
      const gameId = response.body;
    });

    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user1 });
    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user2 });
    cy.deleteUser(user1);
    cy.deleteUser(user2);
  });

  it('Can send a valid move', () => {
    let gameId = 0;
    cy.createUser(user1);
    cy.createUser(user2);
    cy.joinLobby(lobby, user1);
    cy.joinLobby(lobby, user2);
    cy.request({
      method: 'GET',
      url: `${Cypress.config('baseUrl')}/game/${lobby}`,
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.not.be.empty;
      gameId = response.body;
      cy.request({
        method: 'POST',
        url: `${Cypress.config('baseUrl')}/game/${gameId}/move`,
        body: {
          gameId: gameId,
          gameStatus: false,
          currentTurn: 0,
          users: [
            [
              {
                wins: 0,
                highscore: 0,
                games: 0,
                losses: 0,
                username: user1,
              },
              {
                position: [0, 0],
                turretAngle: 0,
                health: 100,
                ammunition: 100,
                score: 0,
                isMirrored: true,
                tankDirection: 'left',
                tankType: 'M107',
              },
            ],
            [
              {
                wins: 0,
                highscore: 0,
                games: 0,
                losses: 0,
                username: user2,
              },
              {
                position: [10, 0],
                turretAngle: 30,
                health: 100,
                ammunition: 100,
                score: 0,
                isMirrored: false,
                tankDirection: 'right',
                tankType: 'M1A2',
              },
            ],
          ],
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.eq(200);
      });
    });

    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user1 });
    cy.genericRequest('POST', `/lobby/${lobby}/leave`, 200, { userId: user2 });

    cy.deleteUser(user1);
    cy.deleteUser(user2);
  });

  it('Can send a invalid move', () => {
    let gameId = 0;
    cy.createUser(user1);
    cy.createUser(user2);
    cy.joinLobby(lobby, user1);
    cy.joinLobby(lobby, user2);
    cy.request({
      method: 'GET',
      url: `${Cypress.config('baseUrl')}/game/${lobby}`,
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.not.be.empty;

      // send a wrong move
      const newMove2 = {
        gameId: 'wrongID',
        gameStatus: true,
        currentTurn: 0,
        users: [
          [
            {
              wins: 0,
              highscore: 0,
              games: 0,
              losses: 0,
              username: user1,
            },
            {
              position: [0, 0],
              turretAngle: 0,
              health: 100,
              ammunition: 100,
              score: 0,
              isMirrored: true,
              tankDirection: 'left',
              tankType: 'M107',
            },
          ],
          [
            {
              wins: 0,
              highscore: 0,
              games: 0,
              losses: 0,
              username: user2,
            },
            {
              position: [10, 0],
              turretAngle: 30,
              health: 100,
              ammunition: 100,
              score: 0,
              isMirrored: false,
              tankDirection: 'right',
              tankType: 'M1A2',
            },
          ],
        ],
      };

      cy.log(gameId.toString());
      cy.request({
        method: 'POST',
        url: `${Cypress.config('baseUrl')}/game/${gameId}/move`,
        body: {
          newMove2,
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.eq(404);
      });

      cy.deleteUser(user1);
      cy.deleteUser(user2);
    });
  });
});
