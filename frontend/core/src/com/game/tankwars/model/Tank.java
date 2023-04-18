package com.game.tankwars.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.tankwars.TankWarsGame;

import java.util.Random;

public class Tank {
    public static final int TANK_WIDTH = 2;
    public static final int TANK_HEIGHT = 1;
    public static final float CANNON_WIDTH = 0.4f;
    public static final float CANNON_HEIGHT = 1;
    private final int MAX_FUEL = 150;

    private final Vector2 position, cannonPosition;
    private final Sprite chassisSprite, cannonSprite;
    private final FixtureData fixtureData;
    private Bullet bullet;
    private final Body chassis, cannon;
    private final Vector2[] vertices;
    int posInVertArr, newPosInVertArr;
    int cannonAngle, newCannonAngle;
    boolean directionLeft, hasFuelLimit;
    private int power, fuel, health;

    public Tank(int posInVertArr, Texture chassisTexture, Texture cannonTexture,
                Terrain terrain, boolean directionLeft, int cannonAngle, String id) {
        vertices = terrain.getVertices();

        this.posInVertArr = posInVertArr;
        this.cannonAngle = cannonAngle;
        this.directionLeft = directionLeft;
        position = vertices[posInVertArr];
        hasFuelLimit = true;
        fuel = MAX_FUEL;
        health = 100;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TANK_WIDTH, TANK_HEIGHT);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        cannonPosition = new Vector2(bodyDef.position.add(1, -1));

        //Chassis
        chassisSprite = new Sprite(chassisTexture);
        chassis = Box2dWorld.getWorld().createBody(bodyDef);
        chassis.setUserData(chassisSprite);
        chassis.createFixture(fixtureDef);

        fixtureData = new FixtureData(id);
        chassis.getFixtureList().get(0).setUserData(fixtureData);
        chassisSprite.scale(0.2f);
        if (directionLeft) chassisSprite.flip(true, false);

        //Cannon
        shape.setAsBox(CANNON_WIDTH, CANNON_HEIGHT);
        FixtureDef canonFixture = new FixtureDef();
        canonFixture.shape = shape;
        canonFixture.isSensor = true;
        bodyDef.position.set(cannonPosition);

        cannonSprite = new Sprite(cannonTexture);
        cannonSprite.setOrigin(1, cannonSprite.getHeight()/2);
        cannon = Box2dWorld.getWorld().createBody(bodyDef);
        cannon.setUserData(cannonSprite);
        cannon.createFixture(canonFixture);
        cannon.getFixtureList().get(0).setUserData(new FixtureData("cannon"));
        shape.dispose();
        updateCannonPos();
        moveLeft();
    }

    /**
     * Update certain fields from a GameStateTank instance containing data for an opponent tank.
     * Deactivate fuel limit to avoid errors when max fuel levels differ between tanks.
     * @param stats GameStateTank containing relevant update data
     */
    public void update(Stats stats) {
        hasFuelLimit = false;
        this.newPosInVertArr = (int) stats.getPosition();
        this.newCannonAngle = stats.getTurretAngle();
        this.power = stats.getPower();
        this.health = stats.getHealth();
    }

    public void moveRight() { move(1); }

    public void moveLeft() { move(-1); }

    /**
     * Move the tank one terrain vertex at a time, if possible,
     * to the right or to the left depending on dirUnit.
     * @param dirUnit 1 if moving to the right
     *                -1 if moving to the left
     */
    private void move(int dirUnit) {
        Vector2 curPos = new Vector2(vertices[posInVertArr]);
        Vector2 newPos = new Vector2(vertices[posInVertArr + dirUnit]);
        float angle = new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleRad();

        if ((!hasFuelLimit || fuel > 0) &&
                ((dirUnit > 0 && chassis.getPosition().x < TankWarsGame.GAMEPORT_WIDTH/TankWarsGame.SCALE - TANK_WIDTH) ||
                (dirUnit < 0 && chassis.getPosition().x > TANK_WIDTH))) {
            position.set(newPos);
            chassis.setTransform(newPos.x, newPos.y + 0.11f, angle);
            chassisSprite.setRotation(angle);
            updateCannonPos();
            posInVertArr += dirUnit;
            fuel--;
        } else chassis.setTransform(curPos.x, curPos.y + 0.11f, angle);
    }

    /**
     * Make cannon position correspond to that of the tank.
     */
    public void updateCannonPos(){
        Vector2 chassisPos = chassis.getPosition();
        chassisPos.add(0, 0.35f);
        cannon.setTransform(chassisPos, cannon.getAngle());
        cannonPosition.set(cannon.getPosition());
    }

    public void rotateCannonCounterClockwise() {
        if ((directionLeft && cannonAngle < 270) || (!directionLeft && cannonAngle < 180))
            cannonAngle++;
    }

    public void rotateCannonClockwise() {
        if ((directionLeft && cannonAngle > 180) || (!directionLeft && cannonAngle > 90))
            cannonAngle--;
    }


    /**
     * If tank is hit by a bullet, reduce tank's health by a random, bounded amount
     */
    public void checkBeenHit() {
        if (fixtureData.isHit()) {
            health -= new Random().nextInt(31) + 10;
            fixtureData.resetHit();
        }
    }

    /**
     * Move tank one step on the way from old to new position.
     *
     * @return {@code true} if still moving tank
     *         {@code false} if done moving tank
     */
    public boolean autoMove() {
        if (newPosInVertArr != posInVertArr) {
            if (newPosInVertArr - posInVertArr < 0) moveLeft();
            else moveRight();
            return true;
        }
        return false;
    }

    /**
     * Move cannon one unit on the way from old to new position.
     *
     * @return {@code true} if still moving cannon
     *         {@code false} if done moving cannon
     */
    public boolean autoRotateCannon() {
        if (newCannonAngle != cannonAngle) {
            if (newCannonAngle - cannonAngle < 0) rotateCannonClockwise();
            else rotateCannonCounterClockwise();
            return true;
        }
        return false;
    }

    /**
     * Create a bullet and fire it from the tank.
     * Dispose of the bullet when it hits something or leaves the screen.
     *
     * @param model Box2dWorld instance that handles the world and bodies
     * @return {@code true} if bullet is still in the air
     *         {@code false} if bullet has hit something or left the screen
     */
    public boolean autoFireBullet(Box2dWorld model) {
        if (bullet == null) {
            String bulletId = getId().equals("tank1") ? "bullet1" : "bullet2";
            bullet = new Bullet(this, bulletId);
            bullet.shoot(power);
        }

        if (((FixtureData) bullet.getBody().getFixtureList().get(0).getUserData()).isHit() ||
                bullet.getBody().getPosition().x < 0 ||
                bullet.getBody().getPosition().x > TankWarsGame.VIEWPORT_WIDTH) {
            bullet.getBulletSprite().getTexture().dispose();
            model.addDeadBody(bullet.getBody());
            bullet = null;
            return false;
        }
        return true;
    }

    public float getTankAngle() {
        Vector2 curPos = vertices[posInVertArr];
        Vector2 newPos = vertices[posInVertArr + 1];
        return new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleDeg();
    }

    public String getId() {
        return fixtureData.getId();
    }

    public int getPower() { return power; }

    public int getCannonAngle() { return cannonAngle; }

    public Vector2 getCannonPosition() { return new Vector2(cannonPosition); }

    public int getPosInVertArr() { return posInVertArr; }

    public Sprite getChassisSprite() { return chassisSprite; }

    public Sprite getCannonSprite() { return cannonSprite; }

    public int getHealth() { return health; }

    public void setPower(int power) { this.power = power; }

    public void resetFuel() { fuel = MAX_FUEL; }
}
