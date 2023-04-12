// a test file for testing the /user route
describe('/user test', () => {
  // generate a random username
  const username = 'Cypress' + Math.floor(Math.random() * 1000000);

  it('Can create a user', () => {
    cy.createUser(username);
  });

  it('Can delete a user', () => {
    cy.deleteUser(username);
  });
  it('Can find a user', () => {
    cy.createUser(username);

    cy.request({
      method: 'GET',
      url: `${Cypress.config('baseUrl')}/user/${username}`,
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('username', username);
    });

    cy.deleteUser(username);
  });

  it('Can get a list of users', () => {
    cy.genericRequest('GET', `/user/`, 200, '');
  });
});
