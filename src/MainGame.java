import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * Project - TheArenaGame
 * MainGame class
 * Main class that will control the game: Includes JFrame and Panel, gameloop, players controls, etc...
 * @author Arman Atharinejad && Isaac Nay
 */
public class MainGame extends JFrame {
    //Declaring variables
    static int frameCount = 0;
    static int frameDelay = 5;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final int TOWARDS = 0;
    static final int AWAY = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;

    static final int PROJECTILE_VELOCITY = 8;
    static final int BASE_VELOICTY = 3;
    static final int BOOSTED_VELOCITY = 5;

    static Boolean isAttacking = false;
    static Boolean isShooting = false;
    static boolean enemyHit = false;
    static boolean gameRunning = true;

    static ArrayList<Projectile> projectiles = new ArrayList<>();
    static Player character = new Player(1, 3, TOWARDS);
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Map map = new Map();
    static States states = new States();
    static GameManager gameManager = new GameManager(1, states.getMAIN_MENU());
    static Database database = new Database();
    static BufferedImage[][][] playerPics = database.getPlayerPics();
    static BufferedImage[] projectilePics = database.getProjectilePics();
    static BufferedImage[][][] rangedEnemyPics = database.getRangedEnemyPics();
    static BufferedImage shopImage = database.getStorePic();
    static BufferedImage goldImage = database.getGoldPic();
    static BufferedImage healthPic = database.getHealthPic();
    static BufferedImage damagePic = database.getDamagePic();
    static BufferedImage speedPic = database.getSpeedPic();
    static BufferedImage mainMenuPic = database.getMainMenuPic();
    static Font hudFont = database.getHudFont();
    static HubPanel mainPanel = new HubPanel();
    static BufferedImage gameOver = database.getGameOverPic();
    static BufferedImage[][][] meleeEnemyPics = database.getMeleeEnemyPics();
    static BufferedImage controlsOne = database.getControlsOne();
    static BufferedImage controlsTwo = database.getControlsTwo();
    static BufferedImage controlsThree = database.getControlsThree();
    static BufferedImage controlsFour = database.getControlsFour();
    static BufferedImage gameStartPic = database.getGameStart();

    /**
     * MainGame Constructor
     * Sets up the main frame, adds keylistener and JPanel to the frame
     */
    MainGame(){
        //Frame setup
        this.setUndecorated(true);
        this.setSize(992, 612);
        this.setResizable(false);
        this.setFocusable(true);
        this.setLocation((int)screenSize.getWidth() / 2 - 496, (int) screenSize.getHeight() / 2 - 306);

        ControlListener keyListener = new ControlListener();
        this.addKeyListener(keyListener);
        MenuMouseListener mouseListener = new MenuMouseListener();
        this.addMouseListener(mouseListener);
        //Adding hub panel to main frame
        this.add(mainPanel);

        //Starting game
        this.setVisible(true);

        //Running game loop
        runGameLoop();
    }

    /**
     * runGameLoop method
     * Core loop controlling the game mechanics - Updating positions, detecting collisions, switching states, etc...
     */
    public static void runGameLoop(){

        //Play Music
        database.getMainSong().start();
        database.getMainSong().loop(Clip.LOOP_CONTINUOUSLY);

        while (gameRunning) {
            //Code to run if player is in game
            if (gameManager.getState() == states.getIN_GAME()) {
                //Requesting repaint of the panel
                mainPanel.repaint();
                //Thread sleep in order to achieve smooth animation
                try {
                    Thread.sleep(19);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                frameCount++;

                //Determining what to do if all enemies are killed
                if(enemies.size() == 0){
                    //Spawning new enemies if there are still waves to go
                    if(gameManager.getWavesRemaining() > 0){
                        for (int i = 0; i < 4; i++) {
                            enemies.add(new Enemy(gameManager.getroundNum(), 2, TOWARDS, i));
                        }
                        gameManager.setWavesRemaining(gameManager.getWavesRemaining() - 1);
                    }
                    else {
                        //Sending player to store if all waves are completed
                        gameManager.setState(states.getIN_STORE());
                    }
                }


                //Calling characters move method to move them and their hitbox
                character.move();
                //Calling methods to move enemies
                for(int i = 0; i < enemies.size(); i++){
                    enemies.get(i).animate(frameCount, frameDelay);
                    enemies.get(i).chooseAction(character.getxCoordinate(), character.getyCoordinate(), frameCount, projectilePics);
                }

                //cycle through correct image for the player
                character.cycleImage(frameDelay, frameCount);

                //Characters melee attacks
                if(character.isAttacking()) {
                    if (character.getDirection() == character.getAWAY()) {
                        //Making hitbox aligned with direction character is facing
                        Rectangle hitbox = new Rectangle(character.getxCoordinate(), character.getyCoordinate() - 18, 20, 18);
                        for (int i = 0; i < enemies.size(); i++) {
                            //Checking if character hit an enemy
                            if (hitbox.intersects(enemies.get(i).getHitbox())) {
                                //Dealing damage to enemy
                                enemies.get(i).takeDamage((int) (character.getDamage() * 1.5));
                            }
                        }
                    }
                    else if (character.getDirection() == character.getTOWARDS()){
                        //aligning hitbox with direction character faces
                        Rectangle hitbox = new Rectangle(character.getxCoordinate() + 30, character.getyCoordinate() + 60, 20, 18);
                        for (int i = 0; i < enemies.size(); i++) {
                            //Checking if character hit an enemy
                            if (hitbox.intersects(enemies.get(i).getHitbox())) {
                                //Dealing damage to enemy
                                enemies.get(i).takeDamage((int) (character.getDamage() * 1.5));
                            }
                        }
                    }
                    else if (character.getDirection() == character.getRIGHT()){
                        //aligning hitbox with direction character faces
                        Rectangle hitbox = new Rectangle(character.getxCoordinate() + 42, character.getyCoordinate() + 42, 18, 20);
                        for (int i = 0; i < enemies.size(); i++) {
                            //Checking if character hit an enemy
                            if (hitbox.intersects(enemies.get(i).getHitbox())) {
                                //Dealing damage to enemy
                                enemies.get(i).takeDamage((int) (character.getDamage() * 1.5));
                            }
                        }
                    }
                    else if (character.getDirection() == character.getLEFT()){
                        //aligning hitbox with direction character faces
                        Rectangle hitbox = new Rectangle(character.getxCoordinate() - 18, character.getyCoordinate() + 42, 18, 20);
                        for (int i = 0; i < enemies.size(); i++) {
                            //Checking if character hit an enemy
                            if (hitbox.intersects(enemies.get(i).getHitbox())) {
                                //Dealing damage to enemy
                                enemies.get(i).takeDamage((int) (character.getDamage() * 1.5));
                            }
                        }
                    }
                }

                //Calling method to set the players action
                character.changeAction();

                //Checking for projectile collision with wall
                for (int i = 0; i < projectiles.size(); i++) {
                    projectiles = projectiles.get(i).checkWallCollision(map.getWalls(), projectiles, i);
                }

                //Checking for enemy projectile collision with walls
                for(int i = 0; i < enemies.size(); i++){
                    for(int j = 0; j < enemies.get(i).getProjectiles().size(); j++){
                        enemies.get(i).setProjectiles(enemies.get(i).getProjectiles().get(j).checkWallCollision(map.getWalls(), enemies.get(i).getProjectiles(), j));
                    }
                }

                //Checking for projectile collision with enemy
                for (int i = 0; i < projectiles.size(); i++) {
                    for (int j = 0; j < enemies.size() && !enemyHit; j++) {
                        if (projectiles.get(i).getHitbox().intersects(enemies.get(j).getHitbox())) {
                            if(!character.isBoosted()) {
                                enemies.get(j).takeDamage(character.getDamage());
                            }
                            else{
                                enemies.get(j).takeDamage(character.getBoostedDamage());
                            }
                            projectiles.remove(i);
                            enemyHit = true;
                        }
                    }
                    enemyHit = false;
                }

                //Checking for enemy projectile collison with player
                for(int i = 0;  i < enemies.size(); i++){
                    for(int j = 0; j < enemies.get(i).getProjectiles().size(); j++){
                        if(enemies.get(i).getProjectiles().get(j).getHitbox().intersects(character.getHitbox())){
                            character.takeDamage(enemies.get(i).getDamage());
                            enemies.get(i).getProjectiles().remove(j);
                        }
                    }
                }

                //Removing enemy if their health is equal to or below 0, and updating players stats
                for (int i = 0; i < enemies.size(); i++) {
                    if (!enemies.get(i).isAlive()) {
                        //giving player gold (random from 1-3)
                        character.setGold(character.getGold() + ((int) (Math.random() * 3) + 1));
                        //giving player experience based (level of enemy killed
                        character.setCurrentExperience(character.getCurrentExperience() + (enemies.get(i).getLevel()));
                        //removing enemy
                        enemies.remove(i);
                    }
                }

                //Checking collision between attacking melee enemies and player
                for(int i = 0; i < enemies.size(); i++){
                    //Making sure enemy is melee type
                    if(!enemies.get(i).isRanged){
                        //Making sure enemy is in attacking animation
                        if(enemies.get(i).isAttacking()){
                            //Checking hitbox collision
                            if(enemies.get(i).getSwordHitbox().intersects(character.getHitbox()) && enemies.get(i).getPicNum() == 2){
                                //Dealing damage to character
                                character.takeDamage(enemies.get(i).getDamage());
                            }
                        }
                    }
                }
                //For loop to go through all the projectiles and move them
                for (int i = 0; i < projectiles.size(); i++) {
                    projectiles.get(i).move();
                }

                for(int i = 0; i < enemies.size(); i++){
                    for(int j = 0; j < enemies.get(i).getProjectiles().size(); j++){
                        enemies.get(i).getProjectiles().get(j).move();
                    }
                }

                //Calling method to detect collision between player and walls
                character.detectWallCollision(map.getWalls());

                //calling method to check enemies collision with walls
                for(int i = 0; i < enemies.size(); i++){
                    enemies.get(i).detectWallCollision(map.getWalls());
                }

                //Checking if character is dead, setting state to game over
                if(character.getCurrentHealth() <= 0){
                    gameManager.setState(states.getGAME_OVER());
                    //Play game over sound
                    try {
                        File audioFile = new File("music/gameOver.wav");
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                        Clip gameOver = (Clip) AudioSystem.getLine(info);
                        gameOver.open(audioStream);
                        gameOver.start();
                    }catch (Exception f) {
                        f.printStackTrace();
                    }
                }

                //Checking if player levelled up
                if(character.getCurrentExperience() > character.getMaxExperience()){
                    character.levelUp();
                }
            }
            else if(gameManager.getState() == states.getIN_STORE()){
                //Resetting game conditions and switching state back to in game
                projectiles.clear();
                character.setxCoordinate(character.getSTARTING_X());
                character.setyCoordinate(character.getSTARTING_Y());
                character.setVelocity(BASE_VELOICTY);
                character.setBoosted(false);
                character.setxVelocity(0);
                character.setyVelocity(0);
                map.reset();
            }
            else if(gameManager.getState() == states.getGAME_OVER()){
                //Resetting character conditions and game conditions
                projectiles.clear();
                character.setxCoordinate(character.getSTARTING_X());
                character.setyCoordinate(character.getSTARTING_Y());
                character.setVelocity(BASE_VELOICTY);
                character.setBoosted(false);
                character.setxVelocity(0);
                character.setyVelocity(0);
                character.setCurrentHealth(character.getMaxHealth());
                enemies.clear();
                map.reset();
                gameManager.setWavesRemaining(3);
                gameManager.setRoundNum(1);
                mainPanel.repaint();
            }
            else if(gameManager.getState() == states.getMAIN_MENU()){
                //Requesting repaint
                mainPanel.repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_ONE()){
                //Requesting repaint
                mainPanel.repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_TWO()){
                //Requesting repaint
                mainPanel.repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_THREE()){
                //Requesting repaint
                mainPanel.repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_FOUR()){
                //Requesting repaint
                mainPanel.repaint();
            }
            else if (gameManager.getState() == states.getGAME_START()){
                //Requesting repaint
                mainPanel.repaint();
            }
        }
    }//runGameLoop end

    /**
     *HubPanel class
     * Inner class of MainHub class
     * JPanel to have game drawn inside of
     */
    static class HubPanel extends JPanel {
        //Constructor
        public HubPanel() {
            this.setSize(992, 612);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Drawing arena components if player is in game
            if (gameManager.getState() == states.getIN_GAME()) {
                //draw map
                map.drawMap(g);
                //drawing player
                g.drawImage(playerPics[character.getAction()][character.getDirection()][character.getPicNum()], character.getxCoordinate(), character.getyCoordinate(), this);
                //drawing players projectiles
                for (int i = 0; i < projectiles.size(); i++) {
                    g.drawImage(projectiles.get(i).getImage(), projectiles.get(i).getxCoordinate(), projectiles.get(i).getyCoordinate(), this);
                }
                //Drawing enemies
                for (int i = 0; i < enemies.size(); i++) {
                    if(enemies.get(i).isRanged()) {
                        g.drawImage(rangedEnemyPics[enemies.get(i).getAction()][enemies.get(i).getDirection()][enemies.get(i).getPicNum()], enemies.get(i).getxCoordinate(), enemies.get(i).getyCoordinate(), this);
                    }
                    else{
                        g.drawImage(meleeEnemyPics[enemies.get(i).getAction()][enemies.get(i).getDirection()][enemies.get(i).getPicNum()], enemies.get(i).getxCoordinate(), enemies.get(i).getyCoordinate(), this);
                    }
                }
                for (int i = 0; i < enemies.size(); i++){
                    for(int j = 0; j < enemies.get(i).getProjectiles().size(); j++){
                        g.drawImage(enemies.get(i).getProjectiles().get(j).getImage(), enemies.get(i).getProjectiles().get(j).getxCoordinate(), enemies.get(i).getProjectiles().get(j).getyCoordinate(), this);
                    }
                }
                if (character.getAction() == character.getATTACKING() && character.getPicNum() == 2 && character.getDirection() == TOWARDS){
                    g.drawImage(projectilePics[6], character.getxCoordinate()+30, character.getyCoordinate()+60, this);
                }else if (character.getAction() == character.getATTACKING() && character.getPicNum() == 2 && character.getDirection() == AWAY){
                    g.drawImage(projectilePics[7], character.getxCoordinate(), character.getyCoordinate()-18, this);
                }else if (character.getAction() == character.getATTACKING() && character.getPicNum() == 2 && character.getDirection() == LEFT){
                    g.drawImage(projectilePics[8], character.getxCoordinate()-18, character.getyCoordinate()+42, this);
                }else if (character.getAction() == character.getATTACKING() && character.getPicNum() == 2 && character.getDirection() == RIGHT){
                    g.drawImage(projectilePics[9], character.getxCoordinate()+42, character.getyCoordinate()+42, this);
                }
                //**HUD**
                //Filling bottom of screen black
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                //Players gold count, health, experience, level
                g.setFont(hudFont);
                g.setColor(Color.WHITE);
                g.drawString("HP: " + Integer.toString(character.getCurrentHealth()) + "/" + Integer.toString(character.getMaxHealth()), 32, 570);
                g.drawImage(goldImage, 200, 540, this);
                g.drawString(Integer.toString(character.getGold()), 250, 570 );
                g.drawString("LVL: " + Integer.toString(character.getLevel()), 300, 570);
                g.drawString("EXP: " + Integer.toString(character.getCurrentExperience()) + "/" + character.getMaxExperience(), 420, 570);
                //Players potions & how many they have
                g.drawImage(healthPic, 600, 528, this);
                g.drawString(Integer.toString(character.getHealthPotions()), 675, 570);
                g.drawImage(damagePic, 710, 528, this);
                g.drawString(Integer.toString(character.getDamagePotions()), 785, 570);
                g.drawImage(speedPic, 820, 528, this);
                g.drawString(Integer.toString(character.getSpeedPotions()), 895, 570);
                //**HUD END**
                //Request repaint
                repaint();
            }
            else if(gameManager.getState() == states.getIN_STORE()){
                //draw store image
                g.drawImage(shopImage, 0, 0, this);
                //**HUD**
                //Filling bottom of screen black
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                //Players gold count, health, experience, level
                g.setFont(hudFont);
                g.setColor(Color.WHITE);
                g.drawString("HP: " + Integer.toString(character.getCurrentHealth()) + "/" + Integer.toString(character.getMaxHealth()), 32, 570);
                g.drawImage(goldImage, 200, 540, this);
                g.drawString(Integer.toString(character.getGold()), 250, 570 );
                g.drawString("LVL: " + Integer.toString(character.getLevel()), 300, 570);
                g.drawString("EXP: " + Integer.toString(character.getCurrentExperience()) + "/" + character.getMaxExperience(), 420, 570);
                //Players potions & how many they have
                g.drawImage(healthPic, 600, 528, this);
                g.drawString(Integer.toString(character.getHealthPotions()), 675, 570);
                g.drawImage(damagePic, 710, 528, this);
                g.drawString(Integer.toString(character.getDamagePotions()), 785, 570);
                g.drawImage(speedPic, 820, 528, this);
                g.drawString(Integer.toString(character.getSpeedPotions()), 895, 570);
                //**HUD END**
                //Request repaint
                repaint();
            }
            else if(gameManager.getState() == states.getGAME_OVER()){
                //Drawing game over screen
                g.drawImage(gameOver, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if(gameManager.getState() == states.getMAIN_MENU()){
                //Drawing main menu
                g.drawImage(mainMenuPic, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_ONE()){
                //Drawing controls screen
                g.drawImage(controlsOne, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_TWO()){
                //Drawing controls screen
                g.drawImage(controlsTwo, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_THREE()){
                //Drawing controls screen
                g.drawImage(controlsThree, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if (gameManager.getState() == states.getCONTROLS_FOUR()){
                //Drawing controls screen
                g.drawImage(controlsFour, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
            }
            else if (gameManager.getState() == states.getGAME_START()){
                g.drawImage(gameStartPic, 0, 0, this);
                g.setColor(Color.BLACK);
                g.fillRect(0, 512, 996, 100);
                repaint();
                g.setFont(hudFont);
                g.setColor(Color.WHITE);
                g.drawString("LVL " + character.getLevel(), 585, 340);
            }
        }
    }

    /**
     * ControlListener Class
     * keylistener for the players in game controls (movement and attacks)
     */
    static class ControlListener implements KeyListener{
        public void keyPressed(KeyEvent e) {
            //Only allow characters values to be changed when in game
            if (gameManager.getState() == states.getIN_GAME()) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    //If statement to prevent the character from moving in certain direction if they are against a wall
                    if (character.canMoveUp()) {
                        character.setDirection(AWAY);
                        character.setyVelocity(-character.getVelocity());
                        character.setxVelocity(0);
                        character.setAttacking(false);
                    }
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    if (character.canMoveDown()) {
                        character.setDirection(TOWARDS);
                        character.setyVelocity(character.getVelocity());
                        character.setxVelocity(0);
                        character.setAttacking(false);
                    }
                } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    if (character.canMoveLeft()) {
                        character.setDirection(LEFT);
                        character.setxVelocity(-character.getVelocity());
                        character.setyVelocity(0);
                        character.setAttacking(false);
                    }
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    if (character.canMoveRight()) {
                        character.setDirection(RIGHT);
                        character.setxVelocity(character.getVelocity());
                        character.setyVelocity(0);
                        character.setAttacking(false);
                    }
                } else if (key == KeyEvent.VK_Z || key == KeyEvent.VK_J && !isShooting && !isAttacking) {
                    character.setAttacking(true);
                    character.setxVelocity(0);
                    character.setyVelocity(0);
                    character.setPicNum(0);
                    //Read in and play attack sound
                    try {
                        File audioFile = new File("music/attack.wav");
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                        Clip attack = (Clip) AudioSystem.getLine(info);
                        attack.open(audioStream);
                        attack.start();
                    }catch (Exception f) {
                        f.printStackTrace();
                    }

                } else if (key == KeyEvent.VK_X || key == KeyEvent.VK_K && !isShooting && !isAttacking) {
                    //Preventing character from shooting if there are more that 5 bullets on the screen
                    if (projectiles.size() < 5) {
                        character.setShooting(true);
                        character.setAttacking(false);
                        character.setxVelocity(0);
                        character.setyVelocity(0);
                        character.setPicNum(0);
                        try {
                            File audioFile = new File("music/shoot.wav");
                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                            Clip shoot = (Clip) AudioSystem.getLine(info);
                            shoot.open(audioStream);
                            shoot.start();
                        }catch (Exception f) {
                            f.printStackTrace();
                        }

                        if (character.getDirection() == TOWARDS) {
                            projectiles.add(new Projectile((character.getxCoordinate() + 3), (character.getyCoordinate() + 60), 0, PROJECTILE_VELOCITY, 0, projectilePics[TOWARDS], 15, 9));
                        } else if (character.getDirection() == AWAY) {
                            projectiles.add(new Projectile((character.getxCoordinate() + 39), character.getyCoordinate(), 0, -PROJECTILE_VELOCITY, 0, projectilePics[AWAY], 15, 9));
                        } else if (character.getDirection() == LEFT) {
                            projectiles.add(new Projectile((character.getxCoordinate() + 15), (character.getyCoordinate() + 36), -PROJECTILE_VELOCITY, 0, 0, projectilePics[LEFT], 9, 15));
                        } else {
                            projectiles.add(new Projectile((character.getxCoordinate() + 42), (character.getyCoordinate() + 36), PROJECTILE_VELOCITY, 0, 0, projectilePics[RIGHT], 9, 15));
                        }
                    }
                }
                else if (key == KeyEvent.VK_1){
                    //Checking if the player has any potions to use
                    if(character.getHealthPotions() > 0){
                        //Checking if the character is not at full health
                        if(character.getCurrentHealth() < character.getMaxHealth()) {
                            //Adding 50% of characters max health, preventing it from going over max health
                            character.setCurrentHealth((int) ( character.getCurrentHealth() + (character.getMaxHealth() * 0.5)));
                            if(character.getCurrentHealth() > character.getMaxHealth()){
                                character.setCurrentHealth(character.getMaxHealth());
                            }
                            //Removing potion from players inventory
                            character.setHealthPotions(character.getHealthPotions() - 1);
                        }
                    }
                }
                else if (key == KeyEvent.VK_2){
                    //Checking if player has potions to use
                    if(character.getDamagePotions() > 0){
                        //checking to make sure damage boost is not in effect
                        if(!character.isBoosted()){
                            //Increasing damage, removing potion
                            character.setBoosted(true);
                            character.setDamagePotions(character.getSpeedPotions() - 1);
                        }
                    }
                }
                else if (key == KeyEvent.VK_3){
                    //Checking if player has potions to use
                    if(character.getSpeedPotions() > 0){
                        //Checking to make sure speed boost is not already in effect
                        if(character.getVelocity() == BASE_VELOICTY){
                            //Increasing characters velocity, removing potion from inventory
                            character.setVelocity(BOOSTED_VELOCITY);
                            character.setSpeedPotions(character.getSpeedPotions() - 1);
                        }
                    }
                }
                else if (key == KeyEvent.VK_ENTER) {
                    database.getMainSong().close();
                    database.getEasterEgg().start();
                    database.getEasterEgg().loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        }
        public void keyReleased(KeyEvent e){
            //Only allow characters values to be changed when in game
            if (gameManager.getState() == states.getIN_GAME()) {
                //Resetting the characters velocity to 0 when they release movement carries
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    character.setyVelocity(0);
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    character.setyVelocity(0);
                } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    character.setxVelocity(0);
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    character.setxVelocity(0);
                }
            }
        }
        public void keyTyped(KeyEvent e){

        }
    }
    /**MenuMouseListener class
     * Inner class to represent the players interactions with menu && in game shop
     */
    static class MenuMouseListener implements MouseListener{
       public void mousePressed(MouseEvent e){

       }
       public void mouseReleased(MouseEvent e){

       }
       public void mouseEntered(MouseEvent e){

       }
       public void mouseExited(MouseEvent e){

       }
       public void mouseClicked(MouseEvent e){
           //Executing this code when on shop screen
           if(gameManager.getState() == states.getIN_STORE()){
               //Checking coordinates of mouse press to see what user is interacting with
               if(450 <= e.getX() && e.getX() <= 546 && e.getY() >= 190 && e.getY() <= 284){
                   //Checking if player has enough gold to get item
                   if(character.getGold() >= 20){
                       //Adding potion to players inventory, taking gold away from total gold
                       character.setHealthPotions(character.getHealthPotions() + 1);
                       character.setGold(character.getGold() - 20);

                       //Play purchase sound
                       try {
                           File audioFile = new File("music/purchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip purchase = (Clip) AudioSystem.getLine(info);
                           purchase.open(audioStream);
                           purchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }
                   }
                   else{
                       //Play cannot purchase sound
                       try {
                           File audioFile = new File("music/cannotPurchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip cannotPurchase = (Clip) AudioSystem.getLine(info);
                           cannotPurchase.open(audioStream);
                           cannotPurchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }
                   }
               }
               else if(600 <= e.getX() && e.getX() <= 692 && e.getY() >= 190 && e.getY() <= 284){
                   //Checking if player has enough gold to get item
                   if(character.getGold() >= 20){
                       //Adding potion to players inventory, taking gold away from total gold
                       character.setDamagePotions(character.getDamagePotions() + 1);
                       character.setGold(character.getGold() - 20);

                       //Play purchase sound
                       try {
                           File audioFile = new File("music/purchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip purchase = (Clip) AudioSystem.getLine(info);
                           purchase.open(audioStream);
                           purchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }

                   }else{
                       //Play cannot purchase sound
                       try {
                           File audioFile = new File("music/cannotPurchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip cannotPurchase = (Clip) AudioSystem.getLine(info);
                           cannotPurchase.open(audioStream);
                           cannotPurchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }
                   }
               }
               else if(600 <= e.getX() && e.getX() <= 692 && e.getY() >= 190 && e.getY() <= 284){
                   //Checking if player has enough gold to get item
                   if(character.getGold() >= 20){
                       //Adding potion to players inventory, taking gold away from total gold
                       character.setDamagePotions(character.getDamagePotions() + 1);
                       character.setGold(character.getGold() - 20);
                   }
               }
               else if(740 <= e.getX() && e.getX() <= 834 && e.getY() >= 190 && e.getY() <= 284){
                   //Checking if player has enough gold to get item
                   if(character.getGold() >= 20) {
                       character.setSpeedPotions(character.getSpeedPotions() + 1);
                       character.setGold(character.getGold() - 20);

                       //Play purchase sound
                       try {
                           File audioFile = new File("music/purchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip purchase = (Clip) AudioSystem.getLine(info);
                           purchase.open(audioStream);
                           purchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }
                   }else{

                       //Play cannot purchase sound
                       try {
                           File audioFile = new File("music/cannotPurchase.wav");
                           AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                           DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
                           Clip cannotPurchase = (Clip) AudioSystem.getLine(info);
                           cannotPurchase.open(audioStream);
                           cannotPurchase.start();
                       }catch (Exception f) {
                           f.printStackTrace();
                       }
                   }
               }
               else if(e.getX() >= 734 && e.getX() <= 924 && e.getY() >= 415 && e.getY() <= 464){
                   //Saving players stats and starting next round
                   character.saveStats(character.getLevel(), character.getCurrentExperience(), character.getMaxExperience(), character.getGold(), character.getMaxHealth());
                   gameManager.startNextRound();
               }
           }
           else if (gameManager.getState() == states.getGAME_OVER()){
               if(e.getX() >= 293 && e.getX() <= 483 && e.getY() >= 377 && e.getY() <=428){
                   gameManager.setState(states.getIN_GAME());
               }
               else if(e.getX() >= 526 && e.getX() <= 716 && e.getY() >= 377 && e.getY() <= 428){
                   gameManager.setState(states.getMAIN_MENU());
               }
           }
           else if (gameManager.getState() == states.getMAIN_MENU()){
               //Sending player to game
               if(e.getX() >= 293 && e.getX() <= 483 && e.getY() >= 377 && e.getY() <=428){
                   gameManager.setState(states.getGAME_START());
               }
               //exiting game
               else if(e.getX() >= 939 && e.getX() <= 973 && e.getY() >= 19 && e.getY() <= 53){
                   System.exit(0);
               }
               //Sending to controls screen
               else if(e.getX() >= 527 && e.getX() <= 717 && e.getY() >= 378 && e.getY() <= 428){
                   gameManager.setState(states.getCONTROLS_ONE());
               }
           }
           else if (gameManager.getState() == states.getGAME_START()){
               if(e.getX() >= 293 && e.getX() <= 483 && e.getY() >= 377 && e.getY() <=428){
                   //Resetting stats then sending player to game
                   character.saveStats(1, 0, 100, 0, 20);
                   character.loadStats();
                   gameManager.setState(states.getIN_GAME());
               }
               else if(e.getX() >= 526 && e.getX() <= 716 && e.getY() >= 377 && e.getY() <= 428){
                   //Starting game with players saved stats
                   gameManager.setState(states.getIN_GAME());
               }
           }
           else if (gameManager.getState() == states.getCONTROLS_ONE()){
               //Progressing through controls screen
               gameManager.setState(states.getCONTROLS_TWO());
           }
           else if (gameManager.getState() == states.getCONTROLS_TWO()){
               //Progressing through controls screen
               gameManager.setState(states.getCONTROLS_THREE());
           }
           else if (gameManager.getState() == states.getCONTROLS_THREE()){
               gameManager.setState(states.getCONTROLS_FOUR());
           }
           else if (gameManager.getState() == states.getCONTROLS_FOUR()){
               //Sending back to main menu
               gameManager.setState(states.getMAIN_MENU());
           }
       }
    }
}