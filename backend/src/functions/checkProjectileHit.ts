import { IGame } from '../interfaces/IGame';
import { log } from './console';

// check if projectile hit a tank
export function checkProjectileHit(gameState: any): boolean {
  const projectileSpeed = 10; // TODO this should be included in the gameState
  const gravity = 9.81;
  const bufferZone = 5;

  // get the current user
  const currentUser = gameState.users[gameState.currentTurn][0];
  const currentUserStats = gameState.users[gameState.currentTurn][1];
  const currentUserPosition = gameState.users[gameState.currentTurn][1].position;
  const turretAngle = currentUserStats.turretAngle;
  const turretAngleInRadians = (turretAngle * Math.PI) / 180;

  // get the other user
  const otherUser = gameState.users[gameState.currentTurn === 0 ? 1 : 0][0];
  const otherUserStats = gameState.users[gameState.currentTurn === 0 ? 1 : 0][1];
  const otherUserPosition = otherUserStats.position;

  const euclideanDistance = Math.sqrt(
    Math.pow(otherUserPosition[0] - currentUserStats.position[0], 2) +
      Math.pow(otherUserPosition[1] - currentUserStats.position[1], 2)
  );

  // Calculate initial velocity components
  const initialVelocityX = projectileSpeed * Math.cos(turretAngleInRadians);
  const initialVelocityY = projectileSpeed * Math.sin(turretAngleInRadians);

  // Calculate time to reach the same x-coordinate as userB
  const timeToReachX = (otherUserPosition[0] - currentUserPosition[0]) / initialVelocityX;

  // Calculate the y-coordinate of the projectile at that time
  const projectileY =
    currentUserPosition[1] +
    initialVelocityY * timeToReachX -
    0.5 * gravity * timeToReachX * timeToReachX;

  // Check if the projectile hits userB (including the buffer zone)
  if (
    projectileY >= otherUserPosition[1] - bufferZone &&
    projectileY <= otherUserPosition[1] + bufferZone
  ) {
    // the projectile hits the other tank
    log('Projectile hit the other tank');
    return true;
  }

  if (projectileY <= 0) {
    // the projectile hits the floor
    log('Projectile hit the floor');
    return false;
  }

  // If none of the above conditions are met, the projectile missed
  log('Projectile totally missed');
  return false;
}
