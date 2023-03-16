import { User } from '../../types/User';

export interface IStats {
  position: number[][];
  turretAngle: number;
  isMirrored: boolean;
  health: number;
  ammunition: number;
  tankDirection: string; // "left" or "right"
  tankType: string;
  score: number;

  getPosition(): number[][];
  setPosition(position: number[][]): void;
  getTurretAngle(): number;
  setTurretAngle(turretAngle: number): void;
  getIsMirrored(): boolean;
  setIsMirrored(isMirrored: boolean): void;
  getHealth(): number;
  setHealth(health: number): void;
  getAmmunition(): number;
  setAmmunition(ammunition: number): void;
  getTankDirection(): string;
  setTankDirection(tankDirection: string): void;
  getTankType(): string;
  setTankType(tankType: string): void;
  getScore(): number;
  setScore(score: number): void;
}
