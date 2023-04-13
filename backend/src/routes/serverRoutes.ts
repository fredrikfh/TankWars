import express from 'express';
import * as serverController from '../controllers/serverController';

const router = express.Router();

router.get('/heartbeat', serverController.heartbeat);

export default router;
