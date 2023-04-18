package com.game.tankwars.model;

public class GameStateTank {
    float position;
    Integer turretAngle;
    Integer power;
    Integer health;
    Integer ammunition;
    Integer score;
    boolean isMirrored;
    String tankDirection;
    String tankType;

    public GameStateTank() {

    }

    public GameStateTank(float position, int turretAngle, int power, int health, int ammunition, int score, boolean isMirrored, String tankDirection, String tankType) {
        this.position = position;
        this.turretAngle = turretAngle;
        this.power = power;
        this.health = health;
        this.ammunition = ammunition;
        this.score = score;
        this.isMirrored = isMirrored;
        this.tankDirection = tankDirection;
        this.tankType = tankType;
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

    public boolean isMirrored() {
        return isMirrored;
    }

    public void setMirrored(boolean mirrored) {
        isMirrored = mirrored;
    }

    public String getTankDirection() {
        return tankDirection;
    }

    public void setTankDirection(String tankDirection) {
        this.tankDirection = tankDirection;
    }

    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }
}
