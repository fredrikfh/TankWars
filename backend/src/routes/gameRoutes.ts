import express from 'express';
import * as gameController from '../controllers/gameController';

const router = express.Router();

/**
 * @swagger
 * /game/{lobbyId}:
 *   get:
 *     tags: [Game]
 *     summary: Retrive the gameID of a lobby.
 *     description: Retrive the gameID of a lobby.
 *     responses:
 *       200:
 *         description: A gameId.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "0.0d8441fqd"
 *       404:
 *         description: Lobby does not have ongoing game.
 */
router.get('/:lobbyId', gameController.gameId);

/**
 * @swagger
 * /game/move:
 *   post:
 *     tags: [Game]
 *     summary: Send a move.
 *     description: Client uses this to send a gameState as JSON to the server.
 *     parameters:
 *       - name: gameId
 *         in: path
 *         required: true
 *         description: gameId.
 *         schema:
 *           type: string
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               gameId:
 *                 type: string
 *               gameStatus:
 *                 type: boolean
 *                 description: True if game is ongoing, false if game is over.
 *               currentTurn:
 *                 type: integer
 *               users:
 *                 type: array
 *                 items:
 *                   type: array
 *                   items:
 *                     oneOf:
 *                       - type: object
 *                         properties:
 *                           wins:
 *                             type: integer
 *                           highscore:
 *                             type: integer
 *                           games:
 *                             type: integer
 *                           losses:
 *                             type: integer
 *                           username:
 *                             type: string
 *                       - type: object
 *                         properties:
 *                           position:
 *                             type: integer
 *                           turretAngle:
 *                             type: integer
 *                           health:
 *                             type: integer
 *                           ammunition:
 *                             type: integer
 *                           score:
 *                             type: integer
 *                           isMirrored:
 *                             type: boolean
 *                           tankDirection:
 *                             type: string
 *                           tankType:
 *                             type: string
 *     responses:
 *       200:
 *         description: A move was made.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: Move made
 *       404:
 *         description: A move was made.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: Game not found
 */
router.post('/:gameid/move', gameController.move);

/**
 * @swagger
 * /game/{id}/currentTurn:
 *   post:
 *     tags: [Game]
 *     summary: Check current turn.
 *     description: If it is clients turn, a gamestate JSON will be sent.
 *     parameters:
 *       - name: username
 *         in: query
 *         required: true
 *         description: Username of the user polling the game.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: A move was made.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 gameId:
 *                   type: string
 *                 gameStatus:
 *                   type: boolean
 *                   description: True if game is ongoing, false if game is over.
 *                 currentTurn:
 *                   type: integer
 *                 users:
 *                   type: array
 *                   items:
 *                     type: array
 *                     items:
 *                       oneOf:
 *                         - type: object
 *                           properties:
 *                             wins:
 *                               type: integer
 *                             highscore:
 *                               type: integer
 *                             games:
 *                               type: integer
 *                             losses:
 *                               type: integer
 *                             username:
 *                               type: string
 *                         - type: object
 *                           properties:
 *                             position:
 *                               type: integer
 *                             turretAngle:
 *                               type: integer
 *                             health:
 *                               type: integer
 *                             ammunition:
 *                               type: integer
 *                             score:
 *                               type: integer
 *                             isMirrored:
 *                               type: boolean
 *                             tankDirection:
 *                               type: string
 *                             tankType:
 *                               type: string
 */
router.get('/:gameid/currentTurn', gameController.currentTurn);

/**
 * @swagger
 * /game/{gameId}/terrain:
 *   get:
 *     tags: [Game]
 *     summary: Retrive the terrain of a game.
 *     description: Retrive the terrain of a game.
 *     responses:
 *       200:
 *         description: An array of numbers representing the terrain.
 *         content:
 *           text/plain:
 *             schema:
 *               type: array
 *               example: [1,2,2,1,1,2,3]
 *       404:
 *         description: Game not found.
 */
router.get('/:gameid/terrain', gameController.getTerrain);

export default router;
