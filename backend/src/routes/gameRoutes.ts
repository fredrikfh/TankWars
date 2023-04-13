import express from 'express';
import * as gameController from '../controllers/gameController';

const router = express.Router();

router.get('/:lobbyId', gameController.gameId);
router.post('/:gameid/move', gameController.move);
router.get('/:gameid/currentTurn', gameController.currentTurn);
router.get('/:gameid/terrain', gameController.getTerrain);

export default router;
