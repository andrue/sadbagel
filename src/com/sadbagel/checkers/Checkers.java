package com.sadbagel.checkers;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Checkers extends StateBasedGame {
	
    public static final int TITLESCREENSTATE = 0;
    public static final int GAMESCREENSTATE = 1;
	
    public Checkers() {
        super("Checkers by Team Sad Bagel");
        this.addState(new TitleScreen(TITLESCREENSTATE));
        this.addState(new GameScreen(GAMESCREENSTATE));
        this.enterState(TITLESCREENSTATE);
    }
    
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(TITLESCREENSTATE).init(gameContainer, this);
        this.getState(GAMESCREENSTATE).init(gameContainer, this);
    }
    
	public static void initRessources() throws SlickException
	{
		if (Globals.RESOURCES_INITIATED)
			return;
		try
		{
			ResourceManager.loadResources("data/resources.xml");
		}
		catch (IOException e)
		{
			//Log.error("failed to load ressource file 'data/resources.xml': " + e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		Globals.RESOURCES_INITIATED = true;
	}

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Checkers());
            app.setDisplayMode(800, 600, false);
            app.setTargetFrameRate(60);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}