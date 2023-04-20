package com.game.tankwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.acanthite.gdx.graphics.g2d.FreeTypeSkinLoader;

/**
 * A Singleton that manages all assets in application.
 * It uses the LibGDX AssetManager, but encapsulates it to only expose necessary methods.
 *
 * The application uses scene2d.ui and loads Skin objects, each with a
 * TextureAtlas and a JSON file.
 */
public class ResourceManager {

    private static ResourceManager instance = null;
    private final AssetManager manager;
    private final AssetDescriptor<TextureAtlas> MENU_ATLAS =
            new AssetDescriptor<>("menu-textures.atlas", TextureAtlas.class);
    private final AssetDescriptor<Skin> MENU_SKIN =
            new AssetDescriptor<>("menu-textures.json", Skin.class);
    private final AssetDescriptor<Skin> GAMEPLAY_SKIN =
            new AssetDescriptor<>("gameplay-skin.json", Skin.class);

    /**
     * Defines the sound assets used in the game, found at path: assets/sounds
     */
    private final AssetDescriptor<Sound> BUTTON1_SOUND =
            new AssetDescriptor<>("sounds/button-1.mp3", Sound.class);
    private final AssetDescriptor<Sound> BUTTON2_SOUND =
            new AssetDescriptor<>("sounds/button-2.mp3", Sound.class);
    private final AssetDescriptor<Sound> CANNONSHOT_SOUND =
            new AssetDescriptor<>("sounds/cannon-shot.mp3", Sound.class);
    private final AssetDescriptor<Sound> DRIVING_SOUND =
            new AssetDescriptor<>("sounds/driving-tank.mp3", Sound.class);
    private final AssetDescriptor<Sound> GAMESTART_SOUND =
            new AssetDescriptor<>("sounds/game-start.mp3", Sound.class);
    private final AssetDescriptor<Sound> HITGROUND_SOUND =
            new AssetDescriptor<>("sounds/hit-ground.mp3", Sound.class);
    private final AssetDescriptor<Sound> HITTANK_SOUND =
            new AssetDescriptor<>("sounds/hit-tank.mp3", Sound.class);
    private final AssetDescriptor<Sound> LOSS_SOUND =
            new AssetDescriptor<>("sounds/loss.mp3", Sound.class);
    private final AssetDescriptor<Sound> MENUTHEME_SOUND =
            new AssetDescriptor<>("sounds/menu-theme.mp3", Sound.class);
    private final AssetDescriptor<Sound> ROTATETURRET_SOUND =
            new AssetDescriptor<>("sounds/rotate-turret.mp3", Sound.class);
    private final AssetDescriptor<Sound> VICTORY_SOUND =
            new AssetDescriptor<>("sounds/victory.mp3", Sound.class);

    /**
     * Boolean flag checking if main theme is playing
     */
    private boolean menuThemeIsPlaying = false;


    public ResourceManager() {
        manager = new AssetManager();

        manager.setLoader(Skin.class, new FreeTypeSkinLoader(manager.getFileHandleResolver()));
    }

    /**
     * Singleton getter
     * @return The ResourceManager instance
     */
    public static ResourceManager getInstance() {
        if (instance == null) instance = new ResourceManager();
        return instance;
    }

    /**
     * Loads the TextureAtlas and the Skin for the menus,
     * and attaches the TextureAtlas to the Skin.
     *
     * @return loaded Skin object with JSON file and TextureRegions
     *         null on loading error
     */
    public Skin loadAndGetMenuAssets() {
        if (!manager.isLoaded(MENU_ATLAS)) manager.load(MENU_ATLAS);
        if (!manager.isLoaded(MENU_SKIN)) manager.load(MENU_SKIN);

        try {
            manager.finishLoading();
            manager.get(MENU_SKIN).addRegions(manager.get(MENU_ATLAS));
            return manager.get(MENU_SKIN);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    /**
     * Loads the Skin for the gameplay,
     * @return loaded Skin object with JSON file, null on loading error
     */
    public Skin loadAndGetGameplayHudAssets() {
        if (!manager.isLoaded(GAMEPLAY_SKIN)) manager.load(GAMEPLAY_SKIN);
        try {
            manager.finishLoading();
            return manager.get(GAMEPLAY_SKIN);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    /*
    * Loads sounds used in the application, both in menus and in gameplay
    * Returns the correct Sound-class instance
    *
    */
    public Sound loadAndGetButton1Sound(){
        if (!manager.isLoaded(BUTTON1_SOUND)) manager.load(BUTTON1_SOUND);
        try {
            manager.finishLoading();
            return manager.get(BUTTON1_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetButton2Sound(){
        if (!manager.isLoaded(BUTTON2_SOUND)) manager.load(BUTTON2_SOUND);
        try {
            manager.finishLoading();
            return manager.get(BUTTON2_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetCannonShotSound(){
        if (!manager.isLoaded(CANNONSHOT_SOUND)) manager.load(CANNONSHOT_SOUND);
        try {
            manager.finishLoading();
            return manager.get(CANNONSHOT_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetDrivingTankSound(){
        if (!manager.isLoaded(DRIVING_SOUND)) manager.load(DRIVING_SOUND);
        try {
            manager.finishLoading();
            return manager.get(DRIVING_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetGameStartSound(){
        if (!manager.isLoaded(GAMESTART_SOUND)) manager.load(GAMESTART_SOUND);
        try {
            manager.finishLoading();
            return manager.get(GAMESTART_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetHitGroundSound(){
        if (!manager.isLoaded(HITGROUND_SOUND)) manager.load(HITGROUND_SOUND);
        try {
            manager.finishLoading();
            return manager.get(HITGROUND_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetHitTankSound(){
        if (!manager.isLoaded(HITTANK_SOUND)) manager.load(HITTANK_SOUND);
        try {
            manager.finishLoading();
            return manager.get(HITTANK_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetLossSound(){
        if (!manager.isLoaded(LOSS_SOUND)) manager.load(LOSS_SOUND);
        try {
            manager.finishLoading();
            return manager.get(LOSS_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetMenuTheme(){
        this.menuThemeIsPlaying = true;
        if (!manager.isLoaded(MENUTHEME_SOUND)) manager.load(MENUTHEME_SOUND);
        try {
            manager.finishLoading();
            return manager.get(MENUTHEME_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetRotateTurretSound(){
        if (!manager.isLoaded(ROTATETURRET_SOUND)) manager.load(ROTATETURRET_SOUND);
        try {
            manager.finishLoading();
            return manager.get(ROTATETURRET_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    public Sound loadAndGetVictorySound(){
        if (!manager.isLoaded(VICTORY_SOUND)) manager.load(VICTORY_SOUND);
        try {
            manager.finishLoading();
            return manager.get(VICTORY_SOUND);
        } catch(GdxRuntimeException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    /**
     * Methods for getting and setting the menuThemeIsPlaying boolean flag
     */
    public boolean getMenuThemeIsPlaying(){
        return this.menuThemeIsPlaying;
    }

    public void setMenuThemeIsPlaying(boolean bool){
        this.menuThemeIsPlaying = bool;
    }
    /**
     * Block until all currently loaded assets are finished loading
     */
    public void finishLoading() {
        manager.finishLoading();
    }

    /**
     * Unload all currently loaded assets
     */
    public void clear() {
        this.menuThemeIsPlaying = false;
        manager.clear();
    }

    /**
     * Dispose of the manager
     */
    public void dispose() {
        manager.dispose();
    }

}
