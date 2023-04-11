package com.game.tankwars.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.game.tankwars.TankWarsGame;

public class Tank {
    TankWarsGame tankWarsGame;
    public static final int TANK_WIDTH = 2;
    public static final int TANK_HEIGHT = 1;
    public static final float CANNON_WIDTH = 0.4f;
    public static final float CANNON_HEIGHT = 1;
    int VIEWPORT_WIDTH;
    int VIEWPORT_HEIGHT;
    private Vector2 position;
    private Vector2 cannonPosition;
    private Rectangle bounds;
    private Texture chassisTexture;
    private Texture cannonTexture;
    private Sprite chassisSprite;
    private Sprite cannonSprite;
    World world;
    Body chassis;
    Body cannon;
    BodyDef bodyDef = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();
    Terrain terrain;
    private Vector2[] vertices;
    int posInVertArr;
    float cannonAngle = 90;
    private int power;
    boolean directionLeft;

    public Tank(int posInVertArr, Texture chassisTexture, Texture cannonTexture, Terrain terrain, TankWarsGame tankWarsGame, boolean directionLeft) {
        VIEWPORT_HEIGHT = tankWarsGame.getViewportHeight();
        VIEWPORT_WIDTH = tankWarsGame.getViewportWidth();

        this.terrain = terrain;
        vertices = terrain.getVertices();

        this.posInVertArr = posInVertArr;
        position = vertices[posInVertArr];

        this.power = 25;

        this.bounds = new Rectangle(position.x, position.y, TANK_WIDTH, TANK_HEIGHT);

        this.chassisTexture = chassisTexture;
        chassisSprite = new Sprite(chassisTexture);
        chassisSprite.scale(0.4f);

        this.cannonTexture = cannonTexture;
        cannonSprite = new Sprite(cannonTexture);
        cannonSprite.scale(-0.1f);

        world = Box2dWorld.getWorld();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position.x, position.y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TANK_WIDTH, TANK_HEIGHT);
        fixtureDef.shape = shape;

        //Chassis
        chassis = world.createBody(bodyDef);
        chassis.setUserData(chassisSprite);
        chassis.createFixture(fixtureDef);

        //Cannon
        shape.setAsBox(CANNON_WIDTH, CANNON_HEIGHT);

        cannonSprite.setOrigin(0, cannonSprite.getHeight()/2);
        cannon = world.createBody(bodyDef);
        cannon.setUserData(cannonSprite);

        shape.dispose();
        updateCannonPos();
        moveLeft();
        if(directionLeft){
            moveRight();
        }
        else{
            moveLeft();
        }
    }

    public void moveRight() {
        Vector2 curPos = new Vector2(vertices[posInVertArr]);
        Vector2 newPos = new Vector2(vertices[posInVertArr + 1]);
        float angle = new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleRad();

        if(directionLeft){
            chassisSprite.flip(true, false);
            directionLeft = false;
        }
        if (chassis.getPosition().x <= TankWarsGame.GAMEPORT_WIDTH - TANK_WIDTH){
            setPosition(newPos);
            chassis.setTransform(newPos.x, newPos.y + 0.11f, angle);
            chassisSprite.setRotation(angle);
            updateCannonPos();
            posInVertArr++;
        }
        else {
            chassis.setTransform(curPos.x, curPos.y + 0.11f, angle);
        }
    }

    public void moveLeft() {
        Vector2 curPos = new Vector2(vertices[posInVertArr]);
        Vector2 newPos = new Vector2(vertices[posInVertArr - 1]);
        float angle = new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleRad();

        if(!directionLeft){
            chassisSprite.flip(true, false);
            directionLeft = true;
        }

        if (chassis.getPosition().x >= TANK_WIDTH){
            setPosition(newPos);
            chassis.setTransform(newPos.x, newPos.y + 0.11f, angle);
            chassisSprite.setRotation(angle);
            updateCannonPos();
            posInVertArr--;
        }
        else {
            chassis.setTransform(curPos.x, curPos.y + 0.11f, angle);
        }
    }

    public float getAngle(){
        Vector2 curPos = vertices[posInVertArr];
        Vector2 newPos = vertices[posInVertArr + 1];
        return new Vector2(newPos.x - curPos.x, newPos.y - curPos.y).angleDeg();
    }

    public void updateCannonPos(){
        Vector2 chassisPos = chassis.getPosition();
        chassisPos.add(0, 1);
        cannon.setTransform(chassisPos, cannon.getAngle());
    }

    public void rotateCannonRight(){
        if(cannonAngle > -90) {
            cannonAngle--;
        }
    }

    public void rotateCannonLeft(){
        if(cannonAngle < 90) {
            cannonAngle++;
        }
    }

    public float getCannonAngle(){
        return cannonAngle;
    }
    public Vector2 getPosition() {
        return new Vector2(position);
    }
    public Vector2 getCannonPosition(){ return new Vector2(cannonPosition); }
    public Rectangle getBounds() {
        return bounds;
    }
    public Texture getChassisTexture() {
        return chassisTexture;
    }
    public Texture getCannonTexture() {
        return cannonTexture;
    }
    public Sprite getChassisSprite() {return chassisSprite;}
    public Sprite getCannonSprite() {return cannonSprite;}

    public int getPower() {
        return power;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setChassisTexture(Texture texture) {
        this.chassisTexture = texture;
    }
    public void setCannonTexture(Texture texture) {
        this.cannonTexture = texture;
    }
}
