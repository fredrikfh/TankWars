import express from 'express';
import * as lobbyController from '../controllers/lobbyController';

const router = express.Router();

router.post('/:id/leave', lobbyController.leaveLobby);
router.post('/:id/join', lobbyController.joinLobby);

export default router;
