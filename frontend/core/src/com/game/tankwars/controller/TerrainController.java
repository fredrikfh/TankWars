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

        //fetchTerrain();
        setTerrainArray();
        System.out.println(terrainArray);
        gameScreen.setTerrain(new Terrain(terrainArray));
    }

    public void fetchTerrain(){
        new HTTPRequestHandler(new Callback() {
            @Override
            public boolean onResult(Net.HttpResponse result) {
                System.out.println(result.getStatus().getStatusCode());
                if(result.getStatus().getStatusCode() == 200){
                    System.out.println("Terrain request result: 200");
                    System.out.println(result.getResultAsString());
                    String resultAsString = result.getResultAsString();
                    Json json = new Json();
                    terrainArray = json.fromJson(ArrayList.class, Integer.class, resultAsString);
                    return true;
                }
                if(result.getStatus().getStatusCode() == 404){
                    Json json = new Json();
                    // Convert the response body to an Array of User objects using the Json instance
                    setTerrainArray(json.fromJson(ArrayList.class, Integer.class, Gdx.files.internal("mockTerrain.json").readString()));
                    System.out.println(json.fromJson(ArrayList.class, Integer.class, Gdx.files.internal("mockTerrain.json").readString()));
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
                .url(String.format("%s/game/%s/terrain", ConfigReader.getProperty("backend.url"),gameId))
                .method(Net.HttpMethods.GET)
                .build()
        ).sendRequest();
    }

    public void setTerrainArray(ArrayList<Integer> arrayList){
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp = arrayList;
        this.terrainArray = arrayList;
    }

    public void setTerrainArray(){
        Json json = new Json();
        //MOCK DATA
        this.terrainArray = json.fromJson(
                ArrayList.class,
                Integer.class,
                Gdx.files.internal("mockTerrain.json").readString());
    }

    public ArrayList<Integer> getTerrainArray(){
        return this.terrainArray;
    }

}
