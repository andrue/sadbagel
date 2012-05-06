package com.sadbagel.checkers.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ResourceManager {
	private static String baseDir = null;

	private static float sfxVolume = 1.0f;
	private static float musicVolume = 1.0f;

	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, Music> songs = new HashMap<String, Music>();
	private static HashMap<String, Image> images = new HashMap<String, Image>();
	
	public static void loadResources(String ref) throws IOException {
		loadResources(ResourceLoader.getResourceAsStream(ref));
	}

	public static void loadResources(File ref) throws IOException {
		loadResources(new FileInputStream(ref));
	}

	public static void loadResources(InputStream ref) throws IOException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document document = builder.parse(ref);

			Element element = document.getDocumentElement();
			if (!element.getNodeName().equals("resources")) {
				throw new IOException("Not a resource configuration file");
			}
			// first look for basedir
			NodeList list = element.getElementsByTagName("basedir");
			for (int i = 0; i < list.getLength(); i++) {
				setBaseDirectory((Element) list.item(i));
			}

			// load sounds
			list = element.getElementsByTagName("sound");
			for (int i = 0; i < list.getLength(); i++) {
				loadSound((Element) list.item(i));
			}
			// load songs
			list = element.getElementsByTagName("music");
			for (int i = 0; i < list.getLength(); i++) {
				loadMusic((Element) list.item(i));
			}
			// load images
			list = element.getElementsByTagName("image");
			for (int i = 0; i < list.getLength(); i++) {
				loadImage((Element) list.item(i));
			}
//			// load sheets
//			list = element.getElementsByTagName("sheet");
//			for (int i = 0; i < list.getLength(); i++) {
//				loadSpriteSheet((Element) list.item(i));
//				// load sprite properties
//				loadSprite((Element) list.item(i));
//			}
//			// load fonts
//			list = element.getElementsByTagName("angelcodefont");
//			for (int i = 0; i < list.getLength(); i++) {
//				loadAngelCodeFont((Element) list.item(i));
//			}
		} catch (IOException e) {
			Log.error(e);
			throw e;
		} catch (Exception e) {
			Log.error(e);
			throw new IOException("Unable to load resource configuration file");
		}
	}
	
	private static void setBaseDirectory(Element basedir) throws SlickException {
		String dir = basedir.getAttribute("path");
		setBaseDirectory(dir);
	}

	public static void setBaseDirectory(String baseDirectory)
			throws SlickException {
		Log.debug("Setting ResourceManager base directory to '" + baseDirectory
				+ "'");
		if (baseDirectory == null || baseDirectory.isEmpty())
			throw new SlickException("BaseDirectory must not be null or empty!");
		baseDir = baseDirectory;
		if (!baseDir.endsWith("/"))
			baseDir = baseDir + "/";
	}

	private static void loadMusic(Element music) throws SlickException {
		String key = music.getAttribute("key");
		String file = music.getAttribute("file");
		loadMusic(key, file);
	}
	


	public static void loadMusic(String key, String file) throws SlickException {
		Log.debug("Trying to load music file '" + file + "' at key '" + key
				+ "'...");
		if (songs.get(key) != null)
			throw new SlickException("Music for key " + key
					+ " already existing!");
		if (baseDir != null && !file.startsWith(baseDir))
			file = baseDir + file;
		Music song = new Music(file);
		songs.put(key, song);
	}

	public static Music getMusic(String key) {
		Music music = songs.get(key);
		if (music == null)
			Log.error("No music for key " + key + " found!");
		return music;
	}

	private static void loadSound(Element snd) throws SlickException {
		String key = snd.getAttribute("key");
		String file = snd.getAttribute("file");
		loadSound(key, file);
	}

	public static void loadSound(String key, String file) throws SlickException {
		Log.debug("Trying to load sound file '" + file + "' at key '" + key
				+ "'...");
		if (sounds.get(key) != null)
			throw new SlickException("Sound for key " + key
					+ " already existing!");
		if (baseDir != null && !file.startsWith(baseDir))
			file = baseDir + file;
		Sound sound = new Sound(file);
		sounds.put(key, sound);
	}

	public static Sound getSound(String key) {
		Sound sound = sounds.get(key);
		if (sound == null)
			Log.error("No sound for key " + key + " found!");
		return sound;
	}

	private static void loadImage(Element img) throws SlickException {
		String key = img.getAttribute("key");
		String file = img.getAttribute("file");
		String transColor = img.getAttribute("transparentColor");
		Color transparentColor = null;
		if (transColor != null && !transColor.isEmpty())
			transparentColor = Color.decode(transColor);
		else
			transColor = null;
		loadImage(key, file, transparentColor);
	}

	public static void loadImage(String key, String file, Color transparentColor)
			throws SlickException {
		Log.debug("Trying to load image file '" + file + "' at key '" + key
				+ "'...");
		if (images.get(key) != null)
			throw new SlickException("Image for key " + key
					+ " already existing!");
		Image image;
		if (baseDir != null && !file.startsWith(baseDir))
			file = baseDir + file;
		if (transparentColor != null)
			image = new Image(file, transparentColor);
		else
			image = new Image(file);
		images.put(key, image);
	}

	public static Image getImage(String key) {
		Image image = images.get(key);
		if (image == null)
			Log.error("No image for key " + key + " found!");
		return image;
	}
	
	/**
	 * set the volume of all sound effects to given volume
	 * 
	 * @param sfxVolume
	 *            a value between 0 and 1
	 */
	public static void setSfxVolume(float volume) {
		sfxVolume = volume;
		SoundStore.get().setSoundVolume(sfxVolume);
		Log.debug("sfx volume set to " + sfxVolume);
	}

	/**
	 * set the volume of all songs to given volume
	 * 
	 * @param musicVolume
	 *            a value between 0 and 1
	 */
	public static void setMusicVolume(float volume) {
		musicVolume = volume;
		SoundStore.get().setMusicVolume(musicVolume);
		Log.debug("music volume set to " + musicVolume);
	}

	public static float getMusicVolume() {
		return musicVolume;
	}

	public static float getSfxVolume() {
		return sfxVolume;
	}

	
}
