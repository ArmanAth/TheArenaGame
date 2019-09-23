import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.awt.Rectangle;
import java.util.Arrays;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
/**
 * Project - TheArenaGame
 * Player class
 * Extends FightingCharacter
 * Represents the active player that the user controls
 * @author Isaac Nay and Arman Atharinejad
 */
public class Player extends FightingCharacter {

    //declaring variables
    final private int STARTING_X = 466;
    final private int STARTING_Y = 235;
    final private int HEALTH_SLOT = 0;
    final private int DAMAGE_SLOT = 1;
    final private int SPEED_SLOT = 2;
    private int gold;
    private int currentExperience;
    private int maxExperience;
    private int[] itemInventory = new int[3];
    private int velocity;
    private int boostedDamage;
    private boolean isBoosted;

    /**
     * Player constructor
     * Basic constructor for player; setting level to one and assigning beginning positions
     * @param level starting level of the character
     * @param action action of the character on opening frame
     * @param direction direction character is facing on the opening frame
     */
    Player(int level, int action, int direction) {
        super(level, action, direction);
        //Loading in various stats from save file
        loadStats();
        this.setDamage(level * 2);
        this.setMaxHealth(level * 20);
        this.setCurrentHealth(getMaxHealth());
        this.setHitbox(new Rectangle(STARTING_X, STARTING_Y, getPLAYER_WIDTH(), getPLAYER_HEIGHT()));
        this.setxCoordinate(STARTING_X);
        this.setyCoordinate(STARTING_Y);
        this.setCanMoveLeft(true);
        this.setCanMoveRight(true);
        this.setCanMoveUp(true);
        this.setCanMoveDown(true);
        this.setTouchingWall(false);
        this.setAttacking(false);
        this.setShooting(false);
        this.setVelocity(3);
        this.setBoostedDamage((int) (this.getDamage() * 1.2));
        this.setBoosted(false);
        Arrays.fill(itemInventory, 0);
    }

    /**
     * changeAction method
     * Changes the players actions depending on if they are moving, shooting, etc...
     * Used to update their image within the paintcomponent
     */
    public void changeAction() {
        if (this.getxVelocity() != 0 || this.getyVelocity() != 0) {
            this.setAction(this.getWALKING());
        } else if (this.isAttacking()) {
            this.setAction(this.getATTACKING());
        } else if (this.isShooting()) {
            this.setAction(this.getSHOOTING());
        } else {
            this.setAction(this.getSTANDING());
        }
    }

    @Override
    public void move(){
        this.setxCoordinate(this.getxCoordinate() + this.getxVelocity());
        this.setyCoordinate(this.getyCoordinate() + this.getyVelocity());
        //adjusting players hitbox slightly depending on the direction they are facing to avoid getting stuck on walls
        if(this.getDirection() == this.getTOWARDS() || this.getDirection() == this.getAWAY()) {
            this.setHitbox(new Rectangle(this.getxCoordinate() + 4, this.getyCoordinate() - 4, this.getPLAYER_WIDTH() - 8, this.getPLAYER_HEIGHT() + 10));
        }
        else{
            this.setHitbox(new Rectangle(this.getxCoordinate() - 4, this.getyCoordinate() + 4, this.getPLAYER_WIDTH() + 4, this.getPLAYER_HEIGHT() - 2));
        }
    }

    /**
     * cycleImage method
     * Uses a framecounter in the gameloop to cycle the players image on certain intervals
     * @param frameDelay number of frames to pass before cycling image
     * @param frameCount number of frames that have passed since last cycle
     */
    public void cycleImage(int frameDelay, int frameCount){
        if (frameCount%frameDelay== 0) {
            if(this.getPicNum() + 1 > 3) {
                this.setPicNum(0);
                if(this.getAction() == this.getSHOOTING()){
                    this.setShooting(false);
                }else if (this.getAction() == this.getATTACKING()){
                    this.setAttacking(false);

                }
            }else {
                this.setPicNum(this.getPicNum() + 1);
            }
        }
    }
    /**
     * saveStats method
     * method to write players experience, level and gold information
     * to a text file automatically after every round
     * @param level players level
     * @param currentExperience players current experience
     * @param maxExperience player max experience before level up
     * @param gold current gold the player has
     */
    public void saveStats(int level, int currentExperience, int maxExperience, int gold, int maxHealth){
        //Creating players save file
        File file = new File("save.txt");
        PrintWriter pw;
        try{
            pw = new PrintWriter(file);
            //Writing stats into file
            pw.println(level);
            pw.println(currentExperience);
            pw.println(maxExperience);
            pw.println(gold);
            pw.println(maxHealth);
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * loadStats method
     * method to read in players saved stats
     */
    public void loadStats(){
        //Getting players save file
        File file = new File("save.txt");
        try {
            Scanner scanner = new Scanner(file);
            this.setLevel(Integer.parseInt(scanner.nextLine()));
            this.setCurrentExperience(Integer.parseInt(scanner.nextLine()));
            this.setMaxExperience(Integer.parseInt(scanner.nextLine()));
            this.setGold(Integer.parseInt(scanner.nextLine()));
            this.setMaxHealth(Integer.parseInt(scanner.nextLine()));
            scanner.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**levelUp method
     * changes the player's stats upon leveling up
     */

    public void levelUp() {
        this.setLevel(this.getLevel() + 1);
        this.setDamage(this.getLevel() * 2);
        this.setMaxHealth(this.getLevel() * 20);
        this.setCurrentExperience(this.getCurrentExperience() - this.getMaxExperience());
        this.setMaxExperience(this.getMaxExperience() + 50);
        //Play level up sounds
        try {
            File audioFile = new File("music/levelUp.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            Clip levelUp = (Clip) AudioSystem.getLine(info);
            levelUp.open(audioStream);
            levelUp.start();
        }catch (Exception f) {
            f.printStackTrace();
        }
    }

    /**getGold method
     * returns the amount of gold the player holds
     * @return gold - the amount of gold the player holds
     */
    public int getGold() {
        return gold;
    }

    /**setGold method
     * changes the amount of gold the player has
     * @param gold - the current amount of gold the player has
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**getCurrentExperience method
     * returns the amount of experience the player currently has
     * @return experience - the amount of experience the player has
     */
    public int getCurrentExperience() {
        return currentExperience;
    }
    /**setCurrentExperience method
     * changes the amount of experience the player has
     * @param currentExperience - the current amount of experience the player has
     */
    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }
    /**getMaxExperience method
     * returns the amount of experience the player needs to level up
     * @return maxExperience - the amount of experience the player needs to level up
     */
    public int getMaxExperience() {
        return maxExperience;
    }
    /**setMaxExperience method
     * changes the amount of experience before the player levels up
     * @param maxExperience - the amount of experience before the player levels up
     */
    public void setMaxExperience(int maxExperience) {
        this.maxExperience = maxExperience;
    }
    /**getHealthPotions method
     * returns the amount of health potions the player has
     * @return the amount of health potions the player has
     */
    public int getHealthPotions() {
        return itemInventory[this.getHEALTH_SLOT()];
    }

    /**setHealthPotions method
     * changes the number of the health potions the character has
     * @param num - the new number of potions
     */
    public void setHealthPotions(int num) {
        this.itemInventory[this.getHEALTH_SLOT()] = num;
    }
    /**getDamagePotions method
     * returns the amount of damage potions the player has
     * @return the amount of damage potions the player has
     */
    public int getDamagePotions() {
        return itemInventory[this.getDAMAGE_SLOT()];
    }
    /**setDamagePotions method
     * changes the number of the Damage potions the character has
     * @param num - the new number of potions
     */
    public void setDamagePotions(int num) {
        this.itemInventory[this.getDAMAGE_SLOT()] = num;
    }
    /**getSpeedPotions method
     * returns the amount of speed potions the player has
     * @return the amount of speed potions the player has
     */
    public int getSpeedPotions() {
        return itemInventory[this.getSPEED_SLOT()];
    }
    /**setSpeedPotions method
     * changes the number of the speed potions the character has
     * @param num - the new number of potions
     */
    public void setSpeedPotions(int num) {
        this.itemInventory[this.getSPEED_SLOT()] = num;
    }
    /**getHEALTH_SLOT method
     * returns the value of the private final variable HEALTH_SLOT
     * @return HEALTH_SLOT
     */
    public int getHEALTH_SLOT() {
        return HEALTH_SLOT;
    }
    /**getDAMAGE_SLOT method
     * returns the value of the private final variable DAMAGE_SLOT
     * @return DAMAGE_SLOT
     */
    public int getDAMAGE_SLOT() {
        return DAMAGE_SLOT;
    }
    /**getSPEED_SLOT method
     * returns the value of the private final variable SPEED_SLOT
     * @return SPEED_SLOT
     */
    public int getSPEED_SLOT() {
        return SPEED_SLOT;
    }
    /**getSTARTING_X method
     * returns the value of the private final variable STARTING_X
     * @return STARTING_X
     */
    public int getSTARTING_X() {
        return STARTING_X;
    }
    /**getSTARTING_Y method
     * returns the value of the private final variable STARTING_Y
     * @return STARTING_Y
     */
    public int getSTARTING_Y() {
        return STARTING_Y;
    }

    /**getVelocity method
     * returns the velocity of the player
     * @return velocity- the velocity of the player
     */
    public int getVelocity() {
        return velocity;
    }

    /**setVelocity method
     * changes the value of the player's velocity
     * @param velocity - the player's new velocity
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    /**getBoostedDamage method
     * returns the amount of damage the player does after consuming a damage potion
     * @return boostedDamage - the damage the player does after consuming a damage potion
     */
    public int getBoostedDamage() {
        return boostedDamage;
    }

    /**setBoostedDamage method
     * changes the amount of damage the player does when boosted on a damage potion
     * @param boostedDamage - the new amount of boosted damage
     */
    public void setBoostedDamage(int boostedDamage) {
        this.boostedDamage = boostedDamage;
    }

    /**isBoosted method
     * returns whether or not the player is boosted by a potion
     * @return isBoosted - whether or not the player is boosted by a potion
     */
    public boolean isBoosted() {
        return isBoosted;
    }

    /**setBoosted method
     * changes whether or not the player is boosted
     * @param boosted whether or not the player is boosted
     */
    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }
}