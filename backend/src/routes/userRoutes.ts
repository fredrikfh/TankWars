import express from 'express';
import * as userController from '../controllers/userController';

const router = express.Router();

router.get('/', userController.users);
router.get('/:idOrUsername', userController.getUser);
router.post('/create/:username', userController.createUser);
router.post('/delete/:username', userController.deleteUser); // should be router.delete

export default router;
