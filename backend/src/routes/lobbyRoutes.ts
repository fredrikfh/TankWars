import express from 'express';
import * as lobbyController from '../controllers/lobbyController';

const router = express.Router();

/**
 * @swagger
 * /lobby/{id}/leave:
 *   post:
 *     tags: [Lobby]
 *     summary: Leaves a lobby.
 *     description: The user leaves the lobby with the given id. If the lobby is empty it will be disposed automatically.
 *     parameters:
 *       - name: username
 *         in: path
 *         required: true
 *         description: Username of the user leaving the lobby.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: User left lobby.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Lobby left"
 */
router.post('/:id/leave', lobbyController.leaveLobby);

/**
 * @swagger
 * /lobby/{id}/join:
 *   post:
 *     tags: [Lobby]
 *     summary: Joins a lobby.
 *     description: The user joins the lobby with the given id. If the lobby is full, a game will be started automatically.
 *     parameters:
 *       - name: username
 *         in: path
 *         required: true
 *         description: Username of the user joining the lobby.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Successfully joined lobby. (opponent already joined, game will be started automatically)
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Joined lobby"
 *       201:
 *         description: Successfully created lobby. (needs to wait for opponent to join)
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Created lobby"
 *       404:
 *         description: Lobby not found.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Lobby not found"
 *       409:
 *         description: User already joined lobby.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "User already in lobby"
 *       429:
 *         description: Lobby is full.
 *         content:
 *           text/plain:
 *             schema:
 *               type: string
 *               example: "Lobby is full"
 */
router.post('/:id/join', lobbyController.joinLobby);

export default router;
