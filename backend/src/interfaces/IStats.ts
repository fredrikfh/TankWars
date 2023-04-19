export interface IStats {
  position: number;
  turretAngle: number;
  health: number;
  power: number;
  ammunition: number;
  score: number;

  getPosition(): number;
  setPosition(position: number): void;
  getTurretAngle(): number;
  setTurretAngle(turretAngle: number): void;
  getHealth(): number;
  setHealth(health: number): void;
  getAmmunition(): number;
  setAmmunition(ammunition: number): void;
  getScore(): number;
  setScore(score: number): void;
}
