import express from 'express';
import * as serverController from '../controllers/serverController';

const router = express.Router();

/**
 * @swagger
 * tags:
 *   - name: User
 *     description: Endpoints for managing users.
 *   - name: Game
 *     description: Endpoints for interacting with games.
 *   - name: Server
 *     description: Endpoints for interacting with the server.
 *   - name: Lobby
 *     description: Endpoints for interacting with lobbies.
 *   - name: Highscore
 *     description: Endpoints for interacting with highscores.
 *
 */

/**
 * @swagger
 * /server/heartbeat:
 *   get:
 *     tags: [Server]
 *     summary: Retrieve a response from the server (heartbeat)
 *     description: If the server is alive, it will respond with a 200 status code and the message "Server is alive"
 *     responses:
 *       200:
 *         description: A heartbeat response.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Server is alive"
 */
router.get('/heartbeat', serverController.heartbeat);

export default router;
