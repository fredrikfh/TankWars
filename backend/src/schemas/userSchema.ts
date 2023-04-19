import Joi from '@hapi/joi';

export const userSchema = Joi.object({
  username: Joi.string().required(),
  wins: Joi.number().required(),
  losses: Joi.number().required(),
  highscore: Joi.number().required(),
  games: Joi.number().required(),
  id: Joi.string().optional(),
});
