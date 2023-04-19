import Joi from '@hapi/joi';
import { log } from '../functions/console';

/**
 * Validates a schema
 * @param schema schema used for validation
 * @param object object to validate
 * @returns true if validation was successful, false otherwise
 */
export function validateSchema(schema: Joi.ObjectSchema, object: any): boolean {
  const { error } = schema.validate(object);
  if (error) {
    log('Schema validation failed: ' + error.message);
    return false;
  }
  return true;
}
