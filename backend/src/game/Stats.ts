import { IStats } from '../interfaces/IStats';

export class Stats implements IStats {
  position: number[][];
  turretAngle: number;
  isMirrored: boolean;
  health: number;
  ammunition: number;
  tankDirection: string;
  tankType: string;
  score: number;

  constructor() {
    this.position = [
      [0, 0],
      [0, 0],
    ];
    this.turretAngle = 0;
    this.health = 100;
    this.ammunition = 100;
    this.score = 0;
    this.isMirrored = false;
    this.tankDirection = 'left';
    this.tankType = 'M107';
  }

  // create getters and setters
  getPosition(): number[][] {
    return this.position;
  }

  setPosition(position: number[][]): void {
    this.position = position;
  }

  getTurretAngle(): number {
    return this.turretAngle;
  }

  setTurretAngle(turretAngle: number): void {
    this.turretAngle = turretAngle;
  }

  getIsMirrored(): boolean {
    return this.isMirrored;
  }

  setIsMirrored(isMirrored: boolean): void {
    this.isMirrored = isMirrored;
  }

  getHealth(): number {
    return this.health;
  }

  setHealth(health: number): void {
    this.health = health;
  }

  getAmmunition(): number {
    return this.ammunition;
  }

  setAmmunition(ammunition: number): void {
    this.ammunition = ammunition;
  }

  getTankDirection(): string {
    return this.tankDirection;
  }

  setTankDirection(tankDirection: string): void {
    this.tankDirection = tankDirection;
  }

  getTankType(): string {
    return this.tankType;
  }

  setTankType(tankType: string): void {
    this.tankType = tankType;
  }

  getScore(): number {
    return this.score;
  }

  setScore(score: number): void {
    this.score = score;
  }
}
