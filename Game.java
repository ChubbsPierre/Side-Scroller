import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.


public class Game extends GameCore 
{
	// Useful game constants
	static int screenWidth = 768;
	static int screenHeight = 420;

    float	gravity = 0.0002f;
    
    // Game state flags
    private boolean leftRun = false;
    private boolean rightRun = false;
    private boolean pause = false;
    boolean grounded;
    boolean collideLeft = false;
    boolean collideRight = false;
    boolean stopped;

    // Game resources
    private Animation idle;
    private Animation running;
    private Animation jumping;
    private Animation taunt;
    private Animation backgroundImage;
    private Animation plateAnim;
    private Animation titleAnim;
    private Animation startAnim;
    private Animation selectAnim;
    private Animation exitAnim;
    private Animation storyAnim;
    private Animation level1Anim;
    private Animation level2Anim;
    private Animation level3Anim;
    private Animation level4Anim;
    private Animation level5Anim;
    private Animation backAnim;
    private AffineTransform rotateTransform;
    private Sound music;
    private Sound selectSound;
    private Sound deathSound;
    
    
    
    int gameState = 0; //controls at what stage the game is eg. menu, level 1 etc Could be an array later to create a win state???
    
	
    
    private Sprite player = null;
    private Sprite background1 = null;
    private Sprite background2 = null;
    private Sprite title = null;
    private Sprite start = null;
    private Sprite select = null;
    private Sprite exit = null;
    private Sprite story = null;
    private Sprite level1 = null;
    private Sprite level2 = null;
    private Sprite level3 = null;
    private Sprite level4 = null;
    private Sprite level5 = null;
    private Sprite back = null;
    
    private ArrayList<Sprite> enemyList = new ArrayList<Sprite>();
    private ArrayList<Sprite> enemyVerticalList= new ArrayList<Sprite>();

    private TileMap menuMap = new TileMap();
    private TileMap levelSelectMap = new TileMap();	// Our tile map, note that we load it in init()
    private TileMap level1Map = new TileMap();
    private TileMap level2Map = new TileMap();
    private TileMap level3Map = new TileMap();
    private TileMap level4Map = new TileMap();
    private TileMap level5Map = new TileMap();
    
    private long levelTotal;				//the score of all the milk you've stolen across a level which is lost if you're caught
    private long total;         			// The score of all the milk you've stolen across the game
    private long attempts;

    Rectangle playerIntersectRect;

    /**
	 * The obligatory main method that creates
     * an instance of our class and starts it running
     * 
     * @param args	The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {

        Game gct = new Game();
        gct.init();
        // Start in windowed mode with the given screen height and width
        gct.run(false,screenWidth,screenHeight);
    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {         	  

    	   // Create a set of background sprites that we can 
    	   // rearrange to give the illusion of motion
        
    	   idle = new Animation();
    	   idle.loadAnimationFromSheet("images/IdleScot.png", 5, 1, 90);
    	   running = new Animation();
    	   running.loadAnimationFromSheet("images/RunningScot.png", 6, 1, 90);
    	   jumping = new Animation();
    	   jumping.loadAnimationFromSheet("images/JumpingScot.png", 2, 1, 90);
    	   taunt = new Animation();
    	   taunt.loadAnimationFromSheet("images/TauntScot.png", 4, 1, 90);
    	   
    	   backgroundImage = new Animation();
    	   backgroundImage.addFrame(loadImage("images/Background.png"), 1);
    	   
    	   plateAnim = new Animation();
    	   plateAnim.loadAnimationFromSheet("images/dirtyPlate.png", 4, 1, 90); //images/dityPlate.png", 4, 1, 90
    	   
    	   titleAnim = new Animation();
    	   titleAnim.addFrame(loadImage("images/Title.png"), 1);
    	   startAnim = new Animation();
    	   startAnim.addFrame(loadImage("images/StartGame.png"), 1);
    	   selectAnim = new Animation();
    	   selectAnim.addFrame(loadImage("images/LevelSelect.png"), 1);
    	   exitAnim = new Animation();
    	   exitAnim.addFrame(loadImage("images/ExitGame.png"), 1);
    	   storyAnim = new Animation();
    	   storyAnim.addFrame(loadImage("images/Story.png"), 1);
    	   
    	   
    	   level1Anim = new Animation();
    	   level1Anim.addFrame(loadImage("images/level1.png"), 1);
    	   level2Anim = new Animation();
    	   level2Anim.addFrame(loadImage("images/level2.png"), 1);
    	   level3Anim = new Animation();
    	   level3Anim.addFrame(loadImage("images/level3.png"), 1);
    	   level4Anim = new Animation();
    	   level4Anim.addFrame(loadImage("images/level4.png"), 1);
    	   level5Anim = new Animation();
    	   level5Anim.addFrame(loadImage("images/level5.png"), 1);
    	   
    	   backAnim = new Animation();
    	   backAnim.addFrame(loadImage("images/back.png"), 1);
        
    	   // Initialise the player with an animation
    	   player = new Sprite(idle);
    	   
    	   background1 = new Sprite(backgroundImage);
    	   background2 = new Sprite(backgroundImage);
    	   
    	   title = new Sprite(titleAnim);
    	   start = new Sprite(startAnim);
    	   select = new Sprite(selectAnim);
    	   exit = new Sprite(exitAnim);
    	   story = new Sprite(storyAnim);
    	   level1 = new Sprite(level1Anim);
    	   level2 = new Sprite(level2Anim);
    	   level3 = new Sprite(level3Anim);
    	   level4 = new Sprite(level4Anim);
    	   level5 = new Sprite(level5Anim);
    	   
    	   back = new Sprite(backAnim);
    	   
    	   menuMap.loadMap("maps", "menu.txt");
    	   levelSelectMap.loadMap("maps", "menu.txt");
    	   level1Map.loadMap("maps", "map");
    	   level2Map.loadMap("maps", "map2.txt");
    	   level3Map.loadMap("maps", "map3.txt");
    	   level4Map.loadMap("maps", "map4.txt");
    	   level5Map.loadMap("maps", "map5.txt");
    	   
    	   initialiseGame(true);
    }

    public void initialiseGame(boolean firstRun)
    {
    	
    	if(gameState == 0)
    	{
    		story.setX(15);
    		story.setY(40);
    		story.show();
    		
    		title.setX(40);
    		title.setY(130);
    		title.show();
    		
    		start.setX(275);
    		start.setY(200);
    		start.show();
    		
    		select.setX(275);
    		select.setY(260);
    		select.show();
    		
    		exit.setX(275);
    		exit.setY(320);
    		
    		exit.show();
    		
    		selectSound = new Sound("sounds/menuClick.wav",false, false);
    		
    		if(firstRun == true)
        	{
        		music = new Sound("sounds/Lack Of Understanding.wav", true, false); //tune
        		music.start();
        	}
    		
    	}
    	
    	if(gameState == 1)
    	{    		
    		level1.setX(40);
    		level1.setY(100);
    		level1.show();
    		
    		level2.setX(40);
    		level2.setY(160);
    		level2.show();
    		
    		level3.setX(40);
    		level3.setY(220);
    		level3.show();
    		
    		level4.setX(540);
    		level4.setY(100);
    		level4.show();
    		
    		level5.setX(540);
    		level5.setY(160);
    		level5.show();
    		
    		back.setX(280);
    		back.setY(320);
    		back.show();
    		
    		selectSound = new Sound("sounds/menuClick.wav",false, false);
    	}
    	
    	if(gameState > 1)
    	{
    		total = 0;
    	      
    		background1.setX(300); //both backgrounds are placed as to create a longer background
        	background1.setY(175);
        	background1.setVelocityX(0);
        	background1.setVelocityY(0);
        	background1.show();
        	
        	background2.setX(background1.getX() + background1.getWidth());
        	background2.setY(175);
        	background2.setVelocityX(0);
        	background2.setVelocityY(0);
        	background2.show();

    		player.setX(460);
        	player.setY(360);
        	player.setVelocityX(0);
        	player.setVelocityY(0);
        	player.show();
        	
        	
    		}
        	
        	if(gameState == 2)
        	{
        		enemyList.clear();
            	enemyVerticalList.clear();
            	for (int r=0; r<level1Map.getMapHeight(); r++)
        		{
        			for (int c=0; c<level1Map.getMapWidth(); c++)
        			{
        				Tile enemyTile = level1Map.getTile(c, r);
        				if(enemyTile.getCharacter() == 'e')
        				{
        					Sprite enemyH = new Sprite(plateAnim);
        					enemyH.setX(c * 32);
        					enemyH.setY(r * 32);
        					enemyH.setVelocityX(-0.1f);	
        					enemyList.add(enemyH);
        					enemyH.show();
        				}
        				else if(enemyTile.getCharacter() == 'v')
        				{
        					Sprite enemyV = new Sprite(plateAnim);
        					enemyV.setX(c * 32);
        					enemyV.setY(r * 32);
        					enemyV.setVelocityY(-0.1f);	
        					enemyVerticalList.add(enemyV);
        					enemyV.show();
        				}
        			}
        		}
        		if(firstRun == true)
        		{
        			music = new Sound("sounds/Disorder.wav", true, false);
        			music.start();
        		}
        	}
        	else if(gameState == 3)
        	{
        		enemyList.clear();
            	enemyVerticalList.clear();
            	for (int r=0; r<level2Map.getMapHeight(); r++)
        		{
        			for (int c=0; c<level2Map.getMapWidth(); c++)
        			{
        				Tile enemyTile = level2Map.getTile(c, r);
        				if(enemyTile.getCharacter() == 'e')
        				{
        					Sprite enemyH = new Sprite(plateAnim);
        					enemyH.setX(c * 32);
        					enemyH.setY(r * 32);
        					enemyH.setVelocityX(-0.1f);	
        					enemyList.add(enemyH);
        					enemyH.show();
        				}
        				else if(enemyTile.getCharacter() == 'v')
        				{
        					Sprite enemyV = new Sprite(plateAnim);
        					enemyV.setX(c * 32);
        					enemyV.setY(r * 32);
        					enemyV.setVelocityY(-0.1f);	
        					enemyVerticalList.add(enemyV);
        					enemyV.show();
        				}
        			}
        		}
        		if(firstRun == true)
        		{
        			music = new Sound("sounds/Snap Out Of It.wav", true, false);
        			music.start();	
        		}
        	}
	        else if(gameState == 4)
	    	{	
	        	enemyList.clear();
            	enemyVerticalList.clear();
            	for (int r=0; r<level3Map.getMapHeight(); r++)
        		{
        			for (int c=0; c<level3Map.getMapWidth(); c++)
        			{
        				Tile enemyTile = level3Map.getTile(c, r);
        				if(enemyTile.getCharacter() == 'e')
        				{
        					Sprite enemyH = new Sprite(plateAnim);
        					enemyH.setX(c * 32);
        					enemyH.setY(r * 32);
        					enemyH.setVelocityX(-0.1f);	
        					enemyList.add(enemyH);
        					enemyH.show();
        				}
        				else if(enemyTile.getCharacter() == 'v')
        				{
        					Sprite enemyV = new Sprite(plateAnim);
        					enemyV.setX(c * 32);
        					enemyV.setY(r * 32);
        					enemyV.setVelocityY(-0.1f);	
        					enemyVerticalList.add(enemyV);
        					enemyV.show();
        				}
        			}
        		}
	        	if(firstRun == true)
	        	{
	        		music = new Sound("sounds/Can't Stand Me Now.wav", true, false);
	        		music.start();	
	        	}
	    	}
	        else if(gameState == 5)
	        {	
	        	enemyList.clear();
            	enemyVerticalList.clear();
            	for (int r=0; r<level4Map.getMapHeight(); r++)
        		{
        			for (int c=0; c<level4Map.getMapWidth(); c++)
        			{
        				Tile enemyTile = level4Map.getTile(c, r);
        				if(enemyTile.getCharacter() == 'e')
        				{
        					Sprite enemyH = new Sprite(plateAnim);
        					enemyH.setX(c * 32);
        					enemyH.setY(r * 32);
        					enemyH.setVelocityX(-0.1f);	
        					enemyList.add(enemyH);
        					enemyH.show();
        				}
        				else if(enemyTile.getCharacter() == 'v')
        				{
        					Sprite enemyV = new Sprite(plateAnim);
        					enemyV.setX(c * 32);
        					enemyV.setY(r * 32);
        					enemyV.setVelocityY(-0.1f);	
        					enemyVerticalList.add(enemyV);
        					enemyV.show();
        				}
        			}
        		}
	        	if(firstRun == true)
	        	{
	        		music = new Sound("sounds/Someday.wav", true, false);
	        		music.start();	
	        	}
	        }
	        else if(gameState == 6)
	        {
	        	enemyList.clear();
            	enemyVerticalList.clear();
            	for (int r=0; r<level5Map.getMapHeight(); r++)
        		{
        			for (int c=0; c<level5Map.getMapWidth(); c++)
        			{
        				Tile enemyTile = level5Map.getTile(c, r);
        				if(enemyTile.getCharacter() == 'e')
        				{
        					Sprite enemyH = new Sprite(plateAnim);
        					enemyH.setX(c * 32);
        					enemyH.setY(r * 32);
        					enemyH.setVelocityX(-0.1f);	
        					enemyList.add(enemyH);
        					enemyH.show();
        				}
        				else if(enemyTile.getCharacter() == 'v')
        				{
        					Sprite enemyV = new Sprite(plateAnim);
        					enemyV.setX(c * 32);
        					enemyV.setY(r * 32);
        					enemyV.setVelocityY(-0.1f);	
        					enemyVerticalList.add(enemyV);
        					enemyV.show();
        				}
        			}
        		}
	        	if(firstRun == true)
	        	{
	        		music = new Sound("sounds/The Kids Don't Stand A Chance.wav", true, false);
	        		music.start();	
	        	}
	        }
    }
    
    
    /**
     * Draw the current state of the game
     */
    public void draw(Graphics2D g)
    {    	
    	int xo = -(int) player.getX() + 150;
		int yo = -(int) player.getY() + 225;
    	
    	if(gameState == 0)
    	{
    		menuMap.draw(g, 0, 0);
    		story.draw(g);
    		title.draw(g);
    		start.draw(g);
    		select.draw(g);
    		exit.draw(g);
    	}
    	else if(gameState == 1)
    	{
    		menuMap.draw(g, 0, 0);
    		level1.draw(g);
    		level2.draw(g);
    		level3.draw(g);
    		level4.draw(g);
    		level5.draw(g);
    		back.draw(g);
    	}
    	else
    	{   
    		g.setColor(Color.GRAY);
    		g.fillRect(0, 0, getWidth(), getHeight());
        
    		background1.setOffsets(xo, yo);
    		background1.draw(g);
    	
    		background2.setOffsets(xo, yo);
    		background2.draw(g);
            
    		player.setOffsets(xo, yo);  // Apply offsets to player and draw 
    		rotateTransform = new AffineTransform();
    		rotateTransform.translate(player.getX() +player.getWidth() + xo, player.getY() + yo);
    		rotateTransform.scale(-1, 1);
    		if(leftRun == true) //if the players running left then apply a transform to show a change in direction 
    		{          
    			g.drawImage(player.getImage(), rotateTransform, null);
    		}
    		else
    		{
    			player.draw(g);
    		}
        
    		for(int counter = 0; counter < enemyList.size(); counter++)
    		{
    			Sprite enemy = enemyList.get(counter);
    			enemy.setOffsets(xo, yo);
    			enemy.draw(g);
    		}
    		
    		for(int counter = 0; counter < enemyVerticalList.size(); counter++)
    		{
    			Sprite enemy = enemyVerticalList.get(counter);
    			enemy.setOffsets(xo, yo);
    			enemy.draw(g);
    		}
    		
        
    		g.setColor(Color.black);
    		playerIntersectRect = new Rectangle((int) player.getX() + xo, (int)player.getY() + yo, player.getWidth(), player.getHeight()); //rectangle to show the bounds of the player for debugging
    		g.drawRect((int) player.getX() + xo, (int)player.getY() + yo, player.getWidth(), player.getHeight());
          
    		if(gameState == 2)
    		{
    			level1Map.draw(g,xo,yo); // Apply offsets to tile map and draw  it
    		}
    		else if(gameState == 3)
    		{
    			level2Map.draw(g, xo, yo);
    		}
    		else if(gameState == 4)
    		{
    			level3Map.draw(g, xo, yo);
    		}
    		else if(gameState == 5)
    		{
    			level4Map.draw(g, xo, yo);
    		}
    		else if(gameState == 6)
    		{
    			level5Map.draw(g, xo, yo);
    		}

               
    		String msg = String.format("Score: %d   Attempts: %d", total, attempts); // Show score and status information
    		g.setColor(Color.darkGray);
    		g.drawString(msg, getWidth() - 150, 50);
    	}
    }

    /**
     * Update any sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
	public void update(long elapsed)
    {	
		
			if(!pause)
			{
				if((player.getY() + player.getHeight()) > (level1Map.getPixelHeight() - 130)) //if the player fell off the map then restart the level
				{
					deathSound = new Sound("sounds/DeathSound.wav", false, true); 
					deathSound.start();
					total = total - levelTotal;
					levelTotal = 0;
					attempts += 1;
					initialiseGame(false);
				}
		
				player.setVelocityY(player.getVelocityY()+(gravity*elapsed));  // Make adjustments to the speed of the sprite due to gravity 	
				player.setAnimationSpeed(1.0f);
       	
				player.update(elapsed); // Now update the sprites animation and position
				background1.update(elapsed);
				background2.update(elapsed);
				if(gameState == 2)
				{
					handleTileMapCollisions(player,elapsed, true, level1Map); // Then check for any collisions that may have occurred   
					
					for(int counter = 0; counter < enemyList.size(); counter++) //this for loop is to handle all collisions with the horizontal enemies 
					{
						enemyList.get(counter).update(elapsed);
					
						if(boundingBoxCollision(player, enemyList.get(counter))) 
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
						deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyList.get(counter),elapsed, false, level1Map);
					}
					for(int counter = 0; counter < enemyVerticalList.size(); counter++) //this for loop is to handle all collisions with the verical enemies
					{
						enemyVerticalList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyVerticalList.get(counter)))
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyVerticalList.get(counter),elapsed, false, level1Map);
					}
				}
				else if(gameState == 3)
				{
					handleTileMapCollisions(player,elapsed, true, level2Map); // Then check for any collisions that may have occurred   
					
					for(int counter = 0; counter < enemyList.size(); counter++) //this for loop is to handle all collisions with the horizontal enemies 
					{
						enemyList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyList.get(counter))) 
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyList.get(counter),elapsed, false, level2Map);
					}
					for(int counter = 0; counter < enemyVerticalList.size(); counter++) //this for loop is to handle all collisions with the verical enemies
					{
						enemyVerticalList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyVerticalList.get(counter)))
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyVerticalList.get(counter),elapsed, false, level2Map);
					}
				}
				else if(gameState == 4)
				{
					handleTileMapCollisions(player,elapsed, true, level3Map); // Then check for any collisions that may have occurred   
					
					for(int counter = 0; counter < enemyList.size(); counter++) //this for loop is to handle all collisions with the horizontal enemies 
					{
						enemyList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyList.get(counter))) 
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyList.get(counter),elapsed, false, level3Map);
					}
					for(int counter = 0; counter < enemyVerticalList.size(); counter++) //this for loop is to handle all collisions with the verical enemies
					{
						enemyVerticalList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyVerticalList.get(counter)))
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyVerticalList.get(counter),elapsed, false, level3Map);
					}
				}
				else if(gameState == 5)
				{
					handleTileMapCollisions(player,elapsed, true, level4Map); // Then check for any collisions that may have occurred   
					
					for(int counter = 0; counter < enemyList.size(); counter++) //this for loop is to handle all collisions with the horizontal enemies 
					{
						enemyList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyList.get(counter))) 
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyList.get(counter),elapsed, false, level4Map);
					}
					for(int counter = 0; counter < enemyVerticalList.size(); counter++) //this for loop is to handle all collisions with the verical enemies
					{
						enemyVerticalList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyVerticalList.get(counter)))
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyVerticalList.get(counter),elapsed, false, level4Map);
					}
				}
				else if(gameState == 6)
				{
					handleTileMapCollisions(player,elapsed, true, level5Map); // Then check for any collisions that may have occurred   
					
					for(int counter = 0; counter < enemyList.size(); counter++) //this for loop is to handle all collisions with the horizontal enemies 
					{
						enemyList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyList.get(counter))) 
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyList.get(counter),elapsed, false, level5Map);
					}
					for(int counter = 0; counter < enemyVerticalList.size(); counter++) //this for loop is to handle all collisions with the verical enemies
					{
						enemyVerticalList.get(counter).update(elapsed);
						
						if(boundingBoxCollision(player, enemyVerticalList.get(counter)))
						{
							deathSound = new Sound("sounds/DeathSound.wav", false, true); 
							deathSound.start();
							total = total - levelTotal;
							levelTotal = 0;
							attempts += 1;
							initialiseGame(false);
						}
						
						handleTileMapCollisions(enemyVerticalList.get(counter),elapsed, false, level5Map);
					}
				}
				
				if(collideRight == false) //this for loop will move the back grounds in order to give the illusion of a continuous background. This one handles right movement 
				{
					if(player.getX() > (background1.getX() + background1.getWidth() + 500))
					{
						background1.setX(background2.getX() + background2.getWidth());
					}
				
					if(player.getX() > background2.getX() + background2.getWidth() + 500)
					{
						background2.setX(background1.getX() + background1.getWidth());
					}
				}
				else if(collideRight == true || stopped == true)
				{
					background1.setVelocityX(0);
					background2.setVelocityX(0);
				}
				
				if(collideLeft == false) //same as the for loop above but with left movement
				{
					if(player.getX() < (background1.getX() - 700))
					{
						background1.setX(background2.getX() - background2.getWidth());
					}
				
					if(player.getX() < background2.getX() - 700)
					{    	
						background2.setX(background1.getX() - background1.getWidth());
					}
				}
				else if(collideLeft == true || stopped == true)
				{
					background1.setVelocityX(0);
					background2.setVelocityX(0);
				}
			}
    }
    
    
    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'. Initial functionality is limited...
     * 
     * @param s			The Sprite to check collisions for
     * @param elapsed	How time has gone by
     */
    public void handleTileMapCollisions(Sprite s, long elapsed, boolean playerState, TileMap tmap)
    {
	      
        if(playerState)
        {
        	char tileBottomMiddle = tmap.getTileChar(( ((int) s.getX() + (s.getWidth() / 2)) / 32), ( (int) (s.getY() + 40)) / 32);
      
        	if(tileBottomMiddle == 'p')
        	{
        		levelTotal = 0;
        		attempts = 0;
        		if(gameState < 6)
        		{
        			gameState++;
        		}
        		else
        		{
        			gameState = 0;
        		}
        		
        		music.setFinished(true);
        		music.setRepeat(false);
        		initialiseGame(true);
        	}
        	if(tileBottomMiddle == 'l')
        	{
        		tmap.getTile(( ((int) s.getX() + (s.getWidth() / 2)) / 32), ( (int) (s.getY() + 40)) / 32).setCharacter('.');
        		levelTotal += 1;
        		total += 1;
        	}
        
        	if(tileBottomMiddle == 'm')
        	{
        		tmap.getTile(( ((int) s.getX() + (s.getWidth() / 2)) / 32), ( (int) (s.getY() + 40)) / 32).setCharacter('.');
        		levelTotal += 5;
        		total += 5;
        	}
        
        	if(tileBottomMiddle == 'h')
        	{
        		tmap.getTile(( ((int) s.getX() + (s.getWidth() / 2)) / 32), ( (int) (s.getY() + 40)) / 32).setCharacter('.');
        		levelTotal += 10;
        		total += 10;
        	}
        
        	if(tileBottomMiddle == 'y')
        	{
        		tmap.getTile(( ((int) s.getX() + (s.getWidth() / 2)) / 32), ( (int) (s.getY() + 40)) / 32).setCharacter('.');
        		levelTotal -= 10;
        		total -= 10;
        	}
        
        	if(tileBottomMiddle == 'u')
        	{
        		deathSound = new Sound("sounds/DeathSound.wav", false, true); 
				deathSound.start();
        		total = total - levelTotal;
        		levelTotal = 0;
        		attempts += 1;
        		initialiseGame(false);
        	}
        }
        if(playerState)
        {
        	grounded = tmap.spriteCollideDown(s, tmap);
        	collideRight = tmap.spriteCollideRight(s, tmap);
            collideLeft = tmap.spriteCollideLeft(s, tmap);
            tmap.spriteCollideUp(s, tmap);
        }
        else
        {
        	tmap.spriteEnemyCollideDown(s, tmap);
        	tmap.spriteEnemyCollideRight(s, tmap);
            tmap.spriteEnemyCollideLeft(s, tmap);
            tmap.spriteEnemyCollideUp(s, tmap);
        }
        
        if(grounded == true)
        {
        	gravity = 0;
        }
        else
        {
        	gravity = 0.0002f;
        }
        
    }
    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     * 
     *  @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e) 
    { 
    	int key = e.getKeyCode();
    	stopped = false;
    	
    	if(!pause)
    	{
    		switch (key)
    		{
    			case KeyEvent.VK_UP:   	
    									if(grounded == true)
    									{
    										player.setVelocityY(-0.2f);
    										player.setAnimation(jumping);  	
    									}
    									break;
    			case KeyEvent.VK_SPACE: player.setAnimation(taunt);
    									break;
    			case KeyEvent.VK_LEFT : player.setVelocityX(-0.10f);
    									
    									if(grounded == true)
    									{
    										leftRun = true;
    										rightRun = false;
    										player.setAnimation(running); 
    									}
    									else
    									{
    										leftRun = true;
    										rightRun = false;
    										player.setAnimation(jumping);
    									}
    							
    									background1.setVelocityX(0.01f);
    									background2.setVelocityX(0.01f);
    									break; //move to the left at a constant speed
    									
    			case KeyEvent.VK_RIGHT: player.setVelocityX(0.10f);
    									
    									if(grounded == true)
    									{
    										leftRun = false;
    										rightRun = true;
    										player.setAnimation(running); 					
    									}
    									else
    									{
    										leftRun = false;
    										rightRun = true;
    										player.setAnimation(jumping);
    									}
    									background1.setVelocityX(-0.01f);
    									background2.setVelocityX(-0.01f);
    									break; //same with moving to the right
    									
    			case KeyEvent.VK_S	: 	Sound s = new Sound("sounds/caw.wav", false, false); 
    									s.start(); 
    									break; //annoy everyone in the room with one button press
    		}
    	}
    }

    public boolean boundingBoxCollision(Sprite s1, Sprite s2)
    {
    	return ((s1.getX() + s1.getImage().getWidth(null) > s2.getX()) &&
    			(s1.getX() < (s2.getX() + s2.getImage().getWidth(null))) &&
    			((s1.getY() + s1.getImage().getHeight(null) > s2.getY()) &&
    			(s1.getY() < s2.getY() + s2.getImage().getHeight(null))));	
    }


	public void keyReleased(KeyEvent e) { 

		int key = e.getKeyCode();
		stopped = true;
		
		if(!pause) //keys can only be used in the not paused state 
		{
			switch (key)
			{
				case KeyEvent.VK_UP     : 	
											if(grounded == false)
											{
												player.setVelocityY(0.1f);
												player.setVelocityX(0);
												player.setAnimation(idle);
											}							
											break;
				case KeyEvent.VK_SPACE	:	player.setAnimation(idle);
											break;
				case KeyEvent.VK_LEFT	: 	leftRun = false;
											player.setVelocityX(0f);
											background1.setVelocityX(0);
											background2.setVelocityX(0);
											if(grounded == true)
											{
												player.setAnimation(idle);
											}
											
											break;
				case KeyEvent.VK_RIGHT	:	rightRun = false;
											player.setVelocityX(0f);
											background1.setVelocityX(0);
											background2.setVelocityX(0);
											if(grounded == true)
											{
												player.setAnimation(idle);
											}
											
											break;
				
				default :  break;
			}	
		}
			
		switch (key) //these can be used in both in a paused state and a non paused state
		{
			case KeyEvent.VK_ESCAPE : 	stop(); 
										break;
			case KeyEvent.VK_P		: 	pause = !pause;
										break;
			default: break;
		}
	}
	

	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(gameState == 0)
		{
			int x = e.getX();
			int y = e.getY();
		
			if((x > 275 && x < 475) && (y > 200 && y < 250))
			{
				selectSound.start();
				gameState = 2;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);
			}
			else if((x > 275 && x < 475) && (y > 260 && y < 310))
			{
				selectSound.start();
				gameState = 1;
				initialiseGame(true);
			}
			else if((x > 275 && x < 475) && (y > 320 && y < 370))
			{
				selectSound.start();
				stop();
			}
		}
		else if(gameState == 1)
		{
			int x = e.getX();
			int y = e.getY();
			
			if((x > 40 && x < 240) && (y > 100 && y < 150))
			{
				selectSound.start();
				gameState = 2;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);
			}
			else if((x > 40 && x < 240) && (y > 160 && y < 210))
			{
				selectSound.start();
				gameState = 3;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);
			}
			else if((x > 40 && x < 240) && (y > 220 && y < 270))
			{
				selectSound.start();
				gameState = 4;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);

			}
			else if((x > 540 && x < 740) && (y > 100 && y < 150))
			{
				selectSound.start();
				gameState = 5;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);
			}
			else if((x > 540 && x < 740) && (y > 160 && y < 210))
			{
				selectSound.start();
				gameState = 6;
				music.setFinished(true);
        		music.setRepeat(false);
				initialiseGame(true);
			}
			else if((x > 280 && x < 460) && (y > 320 && y < 370))
			{
				selectSound.start();
				gameState = 0;
				initialiseGame(false);
			}
			
			
		}
			
	}
}

