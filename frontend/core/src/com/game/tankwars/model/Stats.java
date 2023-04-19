package com.game.tankwars.model;

public class Stats {
    float position;
    Integer turretAngle;
    Integer power;
    Integer health;
    Integer ammunition;
    Integer score;

    public Stats() {

    }
    public Stats(float position, int turretAngle, int power, int health, int ammunition, int score) {
        this.position = position;
        this.turretAngle = turretAngle;
        this.power = power;
        this.health = health;
        this.ammunition = ammunition;
        this.score = score;
    }

    /**
     * Update relevant fields from the values of a Tank instance.
     *
     * @param tank Tank whose data is used to update the Stats instance
     */
    public void update(Tank tank) {
        position = tank.getPosInVertArr();
        turretAngle = tank.getCannonAngle();
        power = tank.getPower();
        health = tank.getHealth();
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public Integer getTurretAngle() {
        return turretAngle;
    }

    public void setTurretAngle(Integer turretAngle) {
        this.turretAngle = turretAngle;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(Integer ammunition) {
        this.ammunition = ammunition;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
