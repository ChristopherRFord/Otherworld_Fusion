package com.fusion.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;

import com.fusion.err.AssetLoadingException;

import java.io.IOException;

/**
 * AssetGroupManager
 * @author Christopher Ford
 *
 * Loads and unloads groups of assets by parsing XML
 * files and putting assets into the LIBGDX Asset Manager.
 * Everything else is LIBGDX Asset Manager functionality.
 * AssetGroupManager employs the Singleton design pattern
 * and can be accessed from any where in the Fusion
 * framework and game.
 */
public class AssetGroupManager implements Disposable
{
	private static AssetGroupManager Singleton = null;
	
	private AssetManager AssetManager;
	
	private XmlReader XmlReader;
	
	// Private constructor
	private AssetGroupManager()
	{
		AssetManager = new AssetManager();
		AssetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		XmlReader = new XmlReader();
	}
	
	// Static singleton retrieve message
	public static AssetGroupManager GetAssetGroupManager()
	{
		if (Singleton == null)
			Singleton = new AssetGroupManager();

		return Singleton;
	}
	
	/**
	 * LoadAssetGroup
	 * @param location - Location in project directory of XML file
	 * 
	 * Parses a XML file in the project directory. Loads every individual
	 * asset into the LIBGDX asset manager.
	 */
	public boolean LoadAssetGroup(String location)
	{
		try
		{
			Gdx.app.log("-Loading XML", location);

			// Retrieve the head element and all of the assets
			XmlReader.Element elements = XmlReader.parse(Gdx.files.classpath(location));
			Array<XmlReader.Element> assets = elements.getChildrenByName("asset");

			// Start parsing
			for (XmlReader.Element element : assets)
			{
				// Retrieve the type and location of a specific asset
				String type = element.get("type").toString();
				String assetLocation = element.get("location").toString();

				// Find out what type of asset this is, if it is found
				// load it in. If it isn't then throw a AssetLoadingException
				switch(type)
				{
				case "Texture":
					AssetManager.load(assetLocation, Texture.class);
					break;
				case "Sound":
					AssetManager.load(assetLocation, Sound.class);
					break;
				case "Music":
					AssetManager.load(assetLocation, Music.class);
					break;
				case "TMX":
					AssetManager.load(assetLocation, TiledMap.class);
					break;
				default:
					throw new AssetLoadingException(assetLocation, type);
				}
				
				Gdx.app.log("--Loading asset", assetLocation + ", " + type);
			}

			// Close up the stream
			AssetManager.finishLoading();
			
			return true;
		}
		catch (IOException | GdxRuntimeException | SerializationException | AssetLoadingException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return false;
		}
	}
	
	/**
	 * UnloadAssetGroup
	 * @param location - Location in project directory of XML file
	 * 
	 * Parses a XML file in the project directory. Unloads every individual
	 * asset into the LIBGDX asset manager.
	 */
	public boolean UnloadAssetGroup(String location)
	{
		try
		{
			Gdx.app.log("-Unloading XML", location);

			// Retrieve the head element and all of the assets
			XmlReader.Element elements = XmlReader.parse(Gdx.files.classpath(location));
			Array<XmlReader.Element> assets = elements.getChildrenByName("asset");

			// Start parsing
			for (XmlReader.Element element : assets)
			{
				// Retrieve the type and location of a specific asset
				String type = element.get("type").toString();
				String assetLocation = element.get("location").toString();

				// Unload asset from the LIBGDX asset manager
				AssetManager.unload(assetLocation);

				Gdx.app.log("-Unloading asset", assetLocation + ", " + type);
			}
			
			return true;
		}
		catch(IOException | GdxRuntimeException | SerializationException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return false;
		}
	}
	
	/**
	 * get
	 * @param location - Location in project directory
	 * @param type - Type of asset group
	 * @return The asset if it exists
	 * 
	 *  Just a method call to the LIBGDX AssetManager's
	 *  get method call.
	 */
	public <T> T Get(String location, Class<T> type)
	{
		try
		{
			return AssetManager.get(location, type);
		}
		catch(GdxRuntimeException e)
		{
			Gdx.app.log(e.getClass() + "", e.getMessage());
			return null;
		}
	}
	
	@Override
	public void dispose()
	{
		AssetManager.dispose();
	}
}
