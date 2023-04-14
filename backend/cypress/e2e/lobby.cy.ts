describe('Lobby Test', () => {
  const user1 = 'Cypress1';
  const user2 = 'Cypress2';
  const user3 = 'Cypress3';
  const lobby = Math.floor(Math.random() * 1000);
  const lobby2 = Math.floor(Math.random() * 1000);

  it('Creates a lobby and verifies its existence', () => {
    cy.joinLobby(lobby, user1);
  });

  it('Cannot join lobby twice', () => {
    cy.genericRequest('POST', `/lobby/${lobby}/join`, 409, { username: user1 });
  });

  it('Only two users can join a lobby', () => {
    cy.joinLobby(lobby, user2);
    cy.genericRequest('POST', `/lobby/${lobby}/join`, 429, { username: user3 });
  });

  it('Can leave a lobby', () => {
    cy.genericRequest('POST', `/lobby/${lobby2}/leave`, 200, { username: user1 });
  });
});
