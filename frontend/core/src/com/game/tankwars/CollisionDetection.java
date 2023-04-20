package com.game.tankwars;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.game.tankwars.model.Box2dWorld;
import com.game.tankwars.model.FixtureData;

public class CollisionDetection implements ContactListener {

    World world;

    public CollisionDetection() {
        this.world = Box2dWorld.getWorld();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        FixtureData dataA = (FixtureData) fixA.getUserData();
        FixtureData dataB = (FixtureData) fixB.getUserData();

        if (dataA.getId().equals("tank1") && dataB.getId().equals("bullet2")) {
            ResourceManager.getInstance().loadAndGetHitTankSound().play();
            System.out.println("The bullet has hit tank 1");
            dataB.hasCollided();
            dataA.hasCollided();
        }

        else if (dataA.getId().equals("tank2") && dataB.getId().equals("bullet1")) {
            ResourceManager.getInstance().loadAndGetHitTankSound().play();
            System.out.println("The bullet has hit tank 2");
            dataB.hasCollided();
            dataA.hasCollided();
        }

        else if (dataA.getId().equals("terrain") && dataB.getId().startsWith("bullet")) {
            ResourceManager.getInstance().loadAndGetHitGroundSound().play();
            System.out.println("The bullet has hit the ground!");
            dataB.hasCollided();

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
