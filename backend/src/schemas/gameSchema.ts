import Joi from '@hapi/joi';
import { userSchema } from './userSchema';
import { lobbySchema } from './lobbySchema';
import { statsSchema } from './statsSchema';
import { terrainSchema } from './terrainSchema';

export const gameSchema = Joi.object({
  users: Joi.array()
    .items(
      Joi.object({
        user: userSchema,
        stats: statsSchema,
      })
    )
    .required(),
  currentTurn: Joi.number().required(),
  lobby: lobbySchema.optional(),
  gameId: Joi.string().required(),
  terrain: terrainSchema.optional(),
  lastActionTimeStamp: Joi.number().optional(),
  isFinished: Joi.boolean().optional(),
});
