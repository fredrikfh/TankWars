package com.game.tankwars.controller;

import static com.game.tankwars.model.CurrentUser.getCurrentUser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.game.tankwars.Callback;
import com.game.tankwars.ConfigReader;
import com.game.tankwars.HTTPRequestHandler;
import com.game.tankwars.model.Terrain;
import com.game.tankwars.view.GameScreen;

import java.util.ArrayList;

public class TerrainController {
    private final String gameId;
    private ArrayList<Integer> terrainArray;
    private GameScreen gameScreen;

    public TerrainController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.gameId = getCurrentUser().getGameId();
        System.out.println(gameId);

        setTerrainArray();
        gameScreen.setTerrain(new Terrain(terrainArray));
    }

    public void fetchTerrain() {
        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse result) {
                System.out.println(result.getStatus().getStatusCode());
                if (result.getStatus().getStatusCode() == 200) {
                    System.out.println("Terrain request result: 200");
                    String resultAsString = result.getResultAsString();
                    System.out.println(resultAsString);
                    Json json = new Json();
                    terrainArray = json.fromJson(ArrayList.class, Integer.class, resultAsString);
                    gameScreen.setTerrain(new Terrain(terrainArray));
                    return true;
                }
                if (result.getStatus().getStatusCode() == 404) {
                    Json json = new Json();
                    setTerrainArray();
                    gameScreen.setTerrain(new Terrain(terrainArray));
                    return true;
                }
                return false;
            }

            @Override
            public void onFailed(Throwable t) {
                System.err.println(t);
            }
        }, new HttpRequestBuilder()
                .newRequest()
                .url(String.format("%s/game/%s/terrain", ConfigReader.getProperty("backend.url"), gameId))
                .method(Net.HttpMethods.GET)
                .build()
        ).sendRequest();
    }

    public void setTerrainArray() {
        Json json = new Json();
        //MOCK DATA
        this.terrainArray = json.fromJson(
                ArrayList.class,
                Integer.class,
                Gdx.files.internal("terrain-seed.json").readString());
    }
}
