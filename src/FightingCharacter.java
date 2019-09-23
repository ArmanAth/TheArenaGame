import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Project - TheArenaGame
 * FightingCharacter class
 * General class to represent all the characters
 */
public abstract class FightingCharacter implements Movable {

    //Declare Variables
    private int level;
    private int maxHealth;
    private int currentHealth;
    private int movementSpeed;
    private int damage;
    private int xCoordinate;
    private int yCoordinate;
    private int xVelocity;
    private int yVelocity;
    private int direction;
    private int currentPicNum;
    private int[] images;
    private Rectangle hitbox;
    private int action;
    private boolean canMoveLeft;
    private boolean canMoveRight;
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean isTouchingWall;
    final private int PLAYER_HEIGHT = 60;
    final private int PLAYER_WIDTH = 42;
    final private int TOWARDS = 0;
    final private int AWAY = 1;
    final private int LEFT = 2;
    final private int RIGHT = 3;
    final private int STANDING = 3;
    final private int WALKING = 0;
    final private int ATTACKING = 1;
    final private int SHOOTING = 2;
    private boolean isAttacking;
    private boolean isShooting;
    private int picNum;

     /**
     * FightingCharacter constructor
     * Basic constructor for FightingCharacters
     * @param level starting level of the player
     * @param action action player is performing on opening frame
     */
    FightingCharacter(int level, int action, int direction) {
        this.setLevel(level);
        this.setAction(action);
        this.setDirection(direction);
    }

    /**move method
     * allows the character to move
     */
    public void move(){
        this.setxCoordinate(xCoordinate + xVelocity);
        this.setyCoordinate(yCoordinate + yVelocity);
        this.setHitbox(new Rectangle(xCoordinate, yCoordinate, PLAYER_WIDTH, PLAYER_HEIGHT));
    }

    /**takeDamage method
     * changes the character's health once they take damage
     * @param enemyDamage the amount of damage the enemy dealt
     */
    public void takeDamage(int enemyDamage){
        this.setCurrentHealth(this.getCurrentHealth() - enemyDamage);
    }

    /**isAlive method
     * determines if the character is still alive
     * @return wether or not the character is alive
     */
    public boolean isAlive(){
        if(this.getCurrentHealth() <= 0){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * detectWallCollision method
     * Accepts an arraylist of terrain objects holding the walls,
     * and checks to see if the player is against any of them to prevent them from moving into it
     * @param walls arraylist of walls (terrain objects) on the map
     */
    public void detectWallCollision(ArrayList<Terrain> walls){
        //Loop to go through the list of walls to see if the player is touching them
        for(int i = 0; i < walls.size() && this.isTouchingWall() == false; i++){
            //If-else statement to check for two conditions: if the player is intersecting the wall, and the direction they are facing
            if (walls.get(i).getHitbox().intersects(this.getHitbox()) && this.getDirection() == this.getLEFT()){
                //Setting a condition to stop the keylistener from moving character; istouching true to break from the loop
                this.setCanMoveLeft(false);
                this.setxVelocity(0);
                this.setTouchingWall(true);
            }
            else if(walls.get(i).getHitbox().intersects(this.getHitbox()) && this.getDirection() == this.getRIGHT()){
                this.setCanMoveRight(false);
                this.setxVelocity(0);
                this.setTouchingWall(true);
            }
            else if((walls.get(i).getHitbox().intersects(this.getHitbox()) && this.getDirection() == this.getAWAY())){
                this.setCanMoveUp(false);
                this.setyVelocity(0);
                this.setTouchingWall(true);
            }
            else if((walls.get(i).getHitbox().intersects(this.getHitbox()) && this.getDirection() == this.getTOWARDS())){
                this.setCanMoveDown(false);
                this.setyVelocity(0);
                this.setTouchingWall(true);
            }
            //Resetting all conditions if the player is not touching a wall
            else{
                this.setCanMoveDown(true);
                this.setCanMoveUp(true);
                this.setCanMoveRight(true);
                this.setCanMoveLeft(true);
            }
        }
        //Resetting loop condition so collision will be checked again on next loop of the game loop
        this.setTouchingWall(false);
    }

    /**getLevel method
     *
     * @return the character's level
     */
    public int getLevel() {
        return level;
    }

    /**setLevel method
     *
     * @param level - the characters level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**getMaxHealth method
     *
     * @return the character's maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**setMaxHealth method
     *
     * @param maxHealth - the character's maximum health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**getCurrentHealth method
     *
     * @return the character's current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**setCurrentHealth method
     *
     * @param currentHealth - the character's current health
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**getMovementSpeed method
     *
     * @return the character's movement speed
     */
    public int getMovementSpeed() {
        return movementSpeed;
    }

    /**setMovementSpeed method
     *
     * @param movementSpeed the character's movement speed
     */
    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    /**getDamage method
     *
     * @return - the character's damage per attack
     */
    public int getDamage() {
        return damage;
    }

    /**setDamage method
     *
     * @param damage - the character's damage per attack
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**getxCoordinate method
     *
     * @return - the character's x coordinate
     */
    public int getxCoordinate() {
        return xCoordinate;
    }

    /**setxCoordinate method
     *
     * @param xCoordinate - the character's x coordinate
     */
    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**getyCoordinate method
     *
     * @return the character's y coordinate
     */
    public int getyCoordinate() {
        return yCoordinate;
    }

    /**setyCoordinate method
     *
     * @param yCoordinate the character's y coordinate
     */
    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**getxVelocity method
     *
     * @return the character's x velocity
     */
    public int getxVelocity() {
        return xVelocity;
    }

    /**setxVelocity method
     *
     * @param xVelocity - the character's x velocity
     */
    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**getyVelocity method
     *
     * @return the character's y velocity
     */
    public int getyVelocity() {
        return yVelocity;
    }

    /**setyVelocity method
     *
     * @param yVelocity - the character's y velocity
     */
    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    /**getDirection method
     *
     * @return the direction the character is facing
     */
    public int getDirection() {
        return direction;
    }

    /**setDirection method
     *
     * @param direction - the direction the character is facing
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**getCurrentPicNum method
     *
     * @return - the current number of the picture displaying the character
     */
    public int getcurrentPicNum() {
        return currentPicNum;
    }

    /**setCurrentPicNum method
     *
     * @param picNum - the current number of the picture displaying the character
     */
    public void setcurrentPicNum(int picNum) {
        this.currentPicNum = picNum;
    }

    /**getHitbox method
     *
     * @return - the hitbox representing the character
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**setHitbox method
     *
     * @param hitbox - the hitbox representing the character
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    /**getAction method
     *
     * @return - int representing the character's action
     */
    public int getAction() {
        return action;
    }

    /**setAction method
     *
     * @param action - int representing the character's action
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**canMoveLeft method
     *
     * @return true if the character can move left
     */
    public boolean canMoveLeft() {
        return canMoveLeft;
    }

    /**setCanMoveLeft method
     *
     * @param canMoveLeft true if the character can move left
     */
    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }
    /**canMoveLeft method
     *
     * @return true if the character can move left
     */
    public boolean canMoveRight() {
        return canMoveRight;
    }
    /**setCanMoveRight method
     *
     * @param canMoveRight true if the character can move Right
     */
    public void setCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }
    /**canMoveUp method
     *
     * @return true if the character can move up
     */
    public boolean canMoveUp() {
        return canMoveUp;
    }
    /**setCanMoveUp method
     *
     * @param canMoveUp true if the character can move up
     */
    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }
    /**canMoveDown method
     *
     * @return true if the character can move down
     */
    public boolean canMoveDown() {
        return canMoveDown;
    }
    /**setCanMoveDown method
     *
     * @param canMoveDown true if the character can move down
     */
    public void setCanMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }

    /**isTouchingWall method
     *
     * @return true if the character is touching a wall
     */
    public boolean isTouchingWall() {
        return isTouchingWall;
    }

    /**setTouchingWall method
     *
     * @param touchingWall true if the character is touching a wall
     */
    public void setTouchingWall(boolean touchingWall) {
        isTouchingWall = touchingWall;
    }

    /**getPLAYER_HEIGHT method
     *
     * @return the player's height
     */
    public int getPLAYER_HEIGHT() {
        return PLAYER_HEIGHT;
    }
    /**getPLAYER_WIDTH method
     *
     * @return the player's width
     */
    public int getPLAYER_WIDTH() {
        return PLAYER_WIDTH;
    }

    /**getTOWARDS method
     *
     * @return the int representing the towards direction
     */
    public int getTOWARDS() {
        return TOWARDS;
    }
    /**getAWAY method
     *
     * @return the int representing the away direction
     */
    public int getAWAY() {
        return AWAY;
    }
    /**getLeft method
     *
     * @return the int representing the left direction
     */
    public int getLEFT() {
        return LEFT;
    }
    /**getRIGHT method
     *
     * @return the int representing the right direction
     */
    public int getRIGHT() {
        return RIGHT;
    }
    /**getSTANDING method
     *
     * @return the int representing the standing action
     */
    public int getSTANDING() {
        return STANDING;
    }
    /**getWALKING method
     *
     * @return the int representing the walking action
     */
    public int getWALKING() {
        return WALKING;
    }

    /**getATTACKING method
     *
     * @return the int representing the attacking action
     */
    public int getATTACKING() {
        return ATTACKING;
    }

    /**getSHOOTING method
     *
     * @return the int representing the attacking action
     */
    public int getSHOOTING() {
        return SHOOTING;
    }

    /**isAttacking method
     *
     * @return true if the character is attacking
     */
    public boolean isAttacking() {
        return isAttacking;
    }

    /**setAttacking method
     *
     * @param attacking - true if the character is attacking
     */
    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    /**isShooting method
     *
     * @return true if the character is shooting
     */
    public boolean isShooting() {
        return isShooting;
    }

    /**setShooting method
     *
     * @param shooting - true if the character is shooting
     */
    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    /**getPicNum method
     *
     * @return - the number representing the picture used to display the character
     */
    public int getPicNum() {
        return picNum;
    }

    /**setPicNum method
     *
     * @param picNum - the number representing the picture used to display the character
     */
    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }
}
