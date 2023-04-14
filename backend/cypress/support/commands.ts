/// <reference types="cypress" />
// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
//
// declare global {
//   namespace Cypress {
//     interface Chainable {
//       login(email: string, password: string): Chainable<void>
//       drag(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       dismiss(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       visit(originalFn: CommandOriginalFn, url: string, options: Partial<VisitOptions>): Chainable<Element>
//     }
//   }
// }

Cypress.Commands.add('deleteUser', (username: string) => {
  cy.request({
    method: 'DELETE',
    url: `${Cypress.config('baseUrl')}/user/${username}`,
    failOnStatusCode: false,
  }).then((response) => {
    expect(response.status).to.be.oneOf([204, 404]);
  });
});

Cypress.Commands.add('createUser', (username: string) => {
  cy.request({
    method: 'POST',
    url: `${Cypress.config('baseUrl')}/user/${username}`,
    failOnStatusCode: false,
  }).then((response) => {
    expect(response.status).to.eq(201);
  });
});

Cypress.Commands.add('getUser', (username: string) => {
  cy.request({
    method: 'GET',
    url: `${Cypress.config('baseUrl')}/user/${username}`,
  }).then((response) => {
    expect(response.status).to.eq(200);
    return response.body;
  });
});

Cypress.Commands.add('joinLobby', (lobbyId: number, username: string) => {
  cy.request({
    method: 'POST',
    url: `${Cypress.config('baseUrl')}/lobby/${lobbyId}/join`,
    failOnStatusCode: false,
    body: {
      username: username,
    },
  }).then((response) => {
    expect(response.status).to.be.oneOf([200, 201, 409]);
    return response.body;
  });
});

Cypress.Commands.add('getGameByLobbyId', (lobbyId: string) => {
  cy.request({
    method: 'GET',
    url: `${Cypress.config('baseUrl')}/game/${lobbyId}`,
  }).then((response) => {
    expect(response.status).to.eq(200);
    return response.body;
  });
});

Cypress.Commands.add('getCurrentTurn', (gameId: string, username: string) => {
  cy.request({
    method: 'GET',
    url: `${Cypress.config('baseUrl')}/game/${gameId}/currentTurn`,
    body: {
      username: username,
    },
  }).then((response) => {
    expect(response.status).to.eq(200);
    return response.body;
  });
});

Cypress.Commands.add('sendMove', (gameId: string, gameState: JSON) => {
  cy.request({
    method: 'POST',
    url: `${Cypress.config('baseUrl')}/game/${gameId}/move`,
    body: gameState,
  }).then((response) => {
    expect(response.status).to.eq(200);
    return response.body;
  });
});

Cypress.Commands.add(
  'genericRequest',
  (method: string, url: string, resCode: number, body: Record<string, unknown> | any) => {
    cy.request({
      method: method,
      url: `${Cypress.config('baseUrl')}` + url,
      body: body,
      failOnStatusCode: false,
    }).then((response) => {
      expect(response.status).to.eq(resCode);
    });
  }
);
