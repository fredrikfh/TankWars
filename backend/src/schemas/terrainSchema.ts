import Joi from '@hapi/joi';

export const terrainSchema = Joi.object({
  yValues: Joi.array().items(Joi.number()).required(),
  minValue: Joi.number().required(),
  maxValue: Joi.number().required(),
  n: Joi.number().required(),
});
