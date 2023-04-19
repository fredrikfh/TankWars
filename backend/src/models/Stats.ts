import { IStats } from '../interfaces/IStats';

export class Stats implements IStats {
  position: number;
  turretAngle: number;
  health: number;
  power: number;
  ammunition: number;
  score: number;

  constructor() {
    this.position = 0;
    this.turretAngle = 0;
    this.health = 100;
    this.power = 0;
    this.ammunition = 100;
    this.score = 0;
  }

  getPosition(): number {
    return this.position;
  }

  setPosition(position: number): void {
    this.position = position;
  }

  getTurretAngle(): number {
    return this.turretAngle;
  }

  setTurretAngle(turretAngle: number): void {
    this.turretAngle = turretAngle;
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

  getScore(): number {
    this.calculateScore();
    return this.score;
  }

  setScore(score: number): void {
    this.score = score;
  }

  calculateScore(): void {
    // this function calculates the score. Currently, the score is the sum of the health and ammunition.
    this.setScore(this.getHealth() + this.getAmmunition());
  }
}
