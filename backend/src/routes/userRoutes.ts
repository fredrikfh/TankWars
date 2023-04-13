import express from 'express';
import * as userController from '../controllers/userController';

const router = express.Router();

/**
 * @swagger
 * /user:
 *   get:
 *     summary: Returns all user IDs.
 *     tags: [User]
 *     responses:
 *       200:
 *         description: List of user IDs.
 *         content:
 *          application/json:
 *           schema:
 *            type: array
 *            items:
 *             type: string
 *             example: ["0t73mMGkAADD7zjJVWXU","1mGO1z9484Y8OoCvjsF4","1yBYv7JzZupLCfmdmqLh","32E9WGluUTYu4DXl6lxh"]
 *
 *       204:
 *         description: No users found.
 */
router.get('/', userController.users);

/**
 * @swagger
 * /user/{username}:
 *   get:
 *     tags: [User]
 *     summary: Returns data for a specific user.
 *     parameters:
 *       - name: username
 *         in: path
 *         required: true
 *         description: Username of the user (can also be the unique user-id).
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: User data.
 *       404:
 *         description: User not found.
 */
router.get('/:username', userController.getUser);

/**
 * @swagger
 * /user/{username}:
 *   post:
 *     tags: [User]
 *     summary: Creates a new user 1.
 *     parameters:
 *       - name: username
 *         in: path
 *         required: true
 *         description: Username of the user.
 *         schema:
 *           type: string
 *     responses:
 *       201:
 *         description: User created.
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
 *       409:
 *         description: User already exists.
 */
router.post('/:username', userController.createUser);

/**
 * @swagger
 * /user/{username}:
 *   delete:
 *     tags: [User]
 *     summary: Deletes a user.
 *     parameters:
 *       - name: username
 *         in: path
 *         required: true
 *         description: Username of the user.
 *         schema:
 *           type: string
 *     responses:
 *       204:
 *         description: User deleted.
 *       404:
 *         description: User not found.
 */
router.delete('/:username', userController.deleteUser);

export default router;
