package com.sadbagel.checkers;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;

public class Checkers extends BasicGame {
	
	boolean backgroundSwap = false;
	Image background[] = new Image[2];
	Image bg1 = null;
	Image bg2 = null;
	Image title = null;
	
	long t1, t2;	
	int titleX, titleY;
	
	boolean trippyBackground = false;
	
	boolean drawMenu = false;
	
	//Background Effects
    private int red, green, blue;
    long t3, t4;
    boolean shouldAdd = false;
    
    //Menu Effect
    int leftSideTitle, rightSideTitle, leftLine, rightLine;
    boolean underlineTitle = false;
    
    //Music
    boolean musicSwap = false;
    Music music = null;
    Music music2 = null;
    
    //Scroll Background
    boolean scrollingBackground = false;
    int leftmost = 0;
    int x1, y1, x2, x3;
	

    public Checkers() {
        super("Checkers by Team Sad Bagel");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
    	//Initialize objects here...
    	bg1 = new Image("data/bg1.png");
    	bg2 = new Image("data/bg2.jpg");
    	
    	background[0] = new Image("data/bg1.png");
    	background[1] = new Image("data/bg1.png");
    	
    	//Setup vars for scrolling bg
    	x1 = -100;
    	y1 = -200;
    	
    	title = new Image("data/checkersTitleText.png");
    	
    	//Init scroll timer
    	t1 = System.currentTimeMillis();
    	
    	//Setup Title Scroll Settings
    	titleX = 50;
    	titleY = 0 - title.getHeight();
    	
    	//Start music
    	music = new Music("data/titlescreen.ogg");
    	music2 = new Music("data/bg2.ogg");
    	music.loop();
    }

    @Override
    public void update(GameContainer gc, int delta)
            throws SlickException {
    	
    	if(titleY <= 49){    	
    		titleY += 1;
    	}
    	else{
    		drawMenu = true;
    	}
    	
    	Input input = gc.getInput();
        if(input.isKeyPressed(Input.KEY_R))
        {
        	drawMenu = false;
        	underlineTitle = false;
        	leftLine = 0;
        	titleY = 0 - title.getHeight();
        }
        
        if(input.isKeyPressed(Input.KEY_G))
        {
        	trippyBackground = (trippyBackground ^ true);
        }
        
        if(input.isKeyPressed(Input.KEY_M))
        {
        	if(musicSwap ^ true){
        		music2.loop();
            	musicSwap = true;
        	}
        	else{
        		music.play();
        		musicSwap = false;
        	}

        }
        
        if(input.isKeyPressed(Input.KEY_B))
        {
        	if(backgroundSwap ^ true){
        		background[0] = bg2;
        		background[1] = bg2;
        		backgroundSwap = true;
        	}
        	else{
        		background[0] = bg1;
        		background[1] = bg1;
        		backgroundSwap = false;
        	}

        }
        
        if(input.isKeyPressed(Input.KEY_S))
        {
        	if(scrollingBackground ^ true){
        		scrollingBackground = true;
        	}
        	else{
        		scrollingBackground = false;
        		leftmost = 0;
            	x1 = -100;
            	y1 = -200;
        		
        	}

        }
        
    	t3 = System.currentTimeMillis();
    	
    	if(t3 >= t4 + 100){
    		t4 = System.currentTimeMillis();
    		shouldAdd = true;
    	}
        
    }
    
    
    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
    	
    	if(trippyBackground){
    		for(int i=0;i<15;i++){
    			g.setColor(new Color(red, green, blue));
    			g.drawRect(0, i*40, 800, 40);
    			g.fillRect(0, i*40, 800, 40);
    			//g.drawLine(0, i, 800, i);
    			
    			red += 17;
    			green += 17;
    			blue += 17;
    			
    			red %= 255;
    			green %= 255;
    			blue %= 255;
    		}
    		
    		if(shouldAdd){
    			red += 17;
    			green += 17;
    			blue += 17;
    			
    			red %= 255;
    			green %= 255;
    			blue %= 255;
    			
    			shouldAdd = false;
    		}    		
    	}
    	else{
    		if(!scrollingBackground){
    			background[0].draw(-100,-200);
    		}
    		else{
    			x1--;
    			x2 = x1 + background[leftmost].getWidth();
    			
    			if(x1 + background[leftmost].getWidth() <= 0){
    				int temp = x1;
    				leftmost = (leftmost + 1) % 2;
    				x1 = x2;
    				x2 = temp + background[leftmost].getWidth();    				
    			}
    			
    			background[leftmost].draw(x1,y1);
    			background[(leftmost+1)%2].draw(x2,y1);
    			
    		}
    	}
    	
        //Draw Grid Board Thing
        int gridX = 400, gridY = 200, blockColor = 0;
        
        for(int i=0;i<8*32;i+=32){
        	for(int j=0;j<8*32;j+=32){
        		blockColor = (blockColor + 1) % 2;
        		
        		if(blockColor == 0){
        			g.setColor(new Color(0,0,0));
        		}
        		else{
        			g.setColor(new Color(255,255,255));
        		}
        		
        		g.drawRect(gridX + i, gridY + j, 32, 32);
        		
        	}
        	blockColor = (blockColor + 1) % 2;
        }
    	
    	g.setColor(new Color(255,255,255));
        title.draw(titleX, titleY);
        
        if(drawMenu){
        	leftSideTitle = titleX;
        	rightSideTitle = titleX + title.getWidth();
        	
        	if(!underlineTitle){
        		if(leftLine == 0){
        			leftLine = rightSideTitle / 2;
        			rightLine = rightSideTitle / 2;
        		}
        		        		
        		g.drawLine(rightSideTitle / 2, titleY - 7, rightLine, titleY - 7);
        		g.drawLine(rightSideTitle / 2, titleY - 7, leftLine, titleY - 7);
        		g.drawLine(rightSideTitle / 2, titleY - 5, rightLine, titleY - 5);
        		g.drawLine(rightSideTitle / 2, titleY - 5, leftLine, titleY - 5);
        		
        		g.drawLine(rightSideTitle / 2, titleY + title.getHeight() + 5, rightLine, titleY + title.getHeight() + 5);
        		g.drawLine(rightSideTitle / 2, titleY + title.getHeight() + 5, leftLine, titleY + title.getHeight() + 5);
        		
        		g.drawLine(rightSideTitle / 2, titleY + title.getHeight() + 7, rightLine, titleY + title.getHeight() + 7);
        		g.drawLine(rightSideTitle / 2, titleY + title.getHeight() + 7, leftLine, titleY + title.getHeight() + 7);
        		
        		if(leftLine > leftSideTitle){
            		leftLine--;
        		}
        		if(rightLine < rightSideTitle){
            		rightLine++;
        		}
        		
        		if(leftLine <= leftSideTitle && rightLine >= rightSideTitle){
        			underlineTitle = true;
        		}
        		
        	}
        	else{
        		g.drawLine(leftSideTitle, titleY - 7, rightSideTitle, titleY - 7);
        		g.drawLine(leftSideTitle, titleY - 5, rightSideTitle, titleY - 5);
        		g.drawLine(leftSideTitle, titleY + title.getHeight() + 5, rightSideTitle, titleY + title.getHeight() + 5);
        		g.drawLine(leftSideTitle, titleY + title.getHeight() + 7, rightSideTitle, titleY + title.getHeight() + 7);
        	}
        }        
        
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Checkers());
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}