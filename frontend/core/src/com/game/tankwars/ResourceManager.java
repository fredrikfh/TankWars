package com.game.tankwars;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
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
        manager.clear();
    }

    /**
     * Dispose of the manager
     */
    public void dispose() {
        manager.dispose();
    }

}
