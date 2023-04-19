import Joi from '@hapi/joi';
import { userSchema } from './userSchema';

export const lobbySchema = Joi.object({
  id: Joi.number().required(),
  gameId: Joi.string().allow(null, '').optional(),
  users: Joi.array().items(userSchema).required(),
});
