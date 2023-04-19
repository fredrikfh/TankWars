import Joi from '@hapi/joi';

export const statsSchema = Joi.object({
  position: Joi.number().required(),
  turretAngle: Joi.number().required(),
  health: Joi.number().required(),
  power: Joi.number().required(),
  ammunition: Joi.number().required(),
  score: Joi.number().required(),
});
