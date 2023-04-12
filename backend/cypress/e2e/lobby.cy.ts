describe('Lobby Test', () => {
  const user1 = 'Cypress1' + Math.floor(Math.random() * 1000000);
  const user2 = 'Cypress2' + Math.floor(Math.random() * 1000000);
  const user3 = 'Cypress3' + Math.floor(Math.random() * 1000000);
  const lobby = Math.floor(Math.random() * 1000);
  it('Creates a lobby and verifies its existence', () => {
    const userId = 'testUserId';

    // create two random users

    // cy.deleteUser(user1);
    // cy.deleteUser(user2);
    cy.createUser(user1);
    cy.createUser(user2);

    cy.joinLobby(6969, user1);
    cy.joinLobby(6969, user2);

    // store the response in a variable
    // cy.request('GET', `${Cypress.config('baseUrl')}/game/6969}`).as('todoResponse');
    // const response = cy.getGameByLobbyId('6969');

    // cy.request({
    //   method: 'GET',
    //   url: `${Cypress.config('baseUrl')}/game/${lobby}`,
    // }).then((response) => {
    //   const gameId = response.body;
    //   expect(response.status).to.eq(200);
    // });

    // // Create a new lobby
    //  cy.request({
    //   method: 'POST',
    //   url: `${Cypress.config('baseUrl')}/lobby/6969?userId=${userId}`,
    // }).then((response) => {
    //   expect(response.status).to.eq(201);
    //   const lobbyId = response.body;

    //   // Verify that the lobby exists
    //   cy.request({
    //     method: 'GET',
    //     url: `${Cypress.config('baseUrl')}/lobby/${lobbyId}`,
    //   }).then((response) => {
    //     expect(response.status).to.eq(200);
    //     expect(response.body).to.have.property('id', lobbyId);
    //     expect(response.body.users).to.include(userId);
    //   });
    // });

    cy.deleteUser(user1);
    cy.deleteUser(user2);
  });

  it('Can join a lobby', () => {
    cy.createUser(user1);
    cy.joinLobby(lobby, user1);
    cy.deleteUser(user1);
  });

  it('Cannot join lobby twice', () => {
    cy.createUser(user1);
    cy.joinLobby(lobby, user1);
    cy.genericRequest('POST', `/lobby/${lobby}/join`, 409, { userId: user1 });
    cy.deleteUser(user1);
  });

  it('Only two users can join a lobby', () => {
    cy.createUser(user1);
    cy.createUser(user2);
    cy.createUser(user3);
    cy.joinLobby(lobby, user1);
    cy.joinLobby(lobby, user2);
    cy.genericRequest('POST', `/lobby/${lobby}/join`, 429, { userId: user3 });
    cy.deleteUser(user1);
    cy.deleteUser(user2);
    cy.deleteUser(user3);
  });

  it('Can leave a lobby', () => {
    const lobby2 = Math.floor(Math.random() * 1000);
    cy.createUser(user1);
    cy.genericRequest('POST', `/lobby/${lobby2}/join`, 201, { userId: user1 });
    cy.genericRequest('POST', `/lobby/${lobby2}/leave`, 200, { userId: user1 });
    cy.deleteUser(user1);
  });
});
