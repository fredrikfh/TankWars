import express from 'express';
import * as highscoreController from '../controllers/highscoreController';

const router = express.Router();

router.get('/', highscoreController.top10);

export default router;
