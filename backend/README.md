# Backend

This backend handles connection with the database and ongoing gameStates.

The backend uses the express framework to handle requests.

Implemation in Typescript.

Package handler is Yarn.

## Setup

You must add a file in the following directory: `backend/keys/fb-key.json`

The key is login details to a Firebase service account, needed to access the database.

## How to run

Run the following commands in your terminal:

- `yarn` last ned avhengigheter
- `yarn start` start serveren

## Nyttige kommandoer

Formatere koden: `yarn prettier`
Kjøre tester: `yarn test`

## VM Deployment

The backend runs on NTNU VM.
IP: 10.212.26.72
Port: 80

`vm.sh` defines the code that is needed to boot the vm (after CI connectes to the VM with SSH)
