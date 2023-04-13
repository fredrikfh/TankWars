import express from 'express';
import * as highscoreController from '../controllers/highscoreController';

const router = express.Router();

/**
 * @swagger
 * /highscore/top10:
 *   get:
 *     tags: [Highscore]
 *     summary: Get the top 10 highest ranking users.
 *     responses:
 *       200:
 *         description: Top 10 highest ranking users found.
 *         content:
 *          application/json:
 *           schema:
 *            type: array
 *            items:
 *              type: object
 *              properties:
 *                username:
 *                  type: string
 *                  description: The username of the user.
 *                highscore:
 *                 type: number
 *                 description: The highscore of the user.
 *                games:
 *                 type: number
 *                 description: The number of games the user has played.
 *                wins:
 *                 type: number
 *                 description: The number of games the user has won.
 *                losses:
 *                 type: number
 *                 description: The number of games the user has lost.
 *                id:
 *                 type: string
 *                 description: The ID of the user.
 *       204:
 *         description: No highscores found.
 */
router.get('/top10', highscoreController.top10);

export default router;
