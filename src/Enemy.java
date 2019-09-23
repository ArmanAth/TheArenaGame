import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends FightingCharacter {
    //Declaring Variables
    boolean isRanged;
    int range;
    Rectangle swordHitbox;
    ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Enemy Constructor
     */
    public Enemy(int level, int action, int direction, int spawnPosition) {
        super(level, action, direction);
        this.setMaxHealth(level * 5);
        this.setCurrentHealth(getMaxHealth());
        //Spawning the at the specified spawn position
        //Melee enemies spawn top and bottom, ranged enemies spawn on sides
        //Varying stats slightly between melee and ranged enemies
        if (spawnPosition == 0) {
            this.setxCoordinate(50);
            this.setyCoordinate(266);
            this.setMovementSpeed(1);
            this.setRange(150);
            this.setRanged(true);
            this.setDamage(level * 2);
        } else if (spawnPosition == 1) {
            this.setxCoordinate(477);
            this.setyCoordinate(50);
            this.setMovementSpeed(2);
            this.setRange(40);
            this.setRanged(false);
            this.setDamage(level);
        } else if (spawnPosition == 2) {
            this.setxCoordinate(900);
            this.setyCoordinate(186);
            this.setMovementSpeed(1);
            this.setRange(150);
            this.setRanged(true);
            this.setDamage(level * 2);
        } else if (spawnPosition == 3) {
            this.setxCoordinate(477);
            this.setyCoordinate(412);
            this.setMovementSpeed(2);
            this.setRange(40);
            this.setRanged(false);
            this.setDamage(level);
        }
        this.setHitbox(new Rectangle(this.getxCoordinate(), this.getyCoordinate(), this.getPLAYER_WIDTH(), this.getPLAYER_HEIGHT()));
        this.setCanMoveLeft(true);
        this.setCanMoveRight(true);
        this.setCanMoveUp(true);
        this.setCanMoveDown(true);
        this.setTouchingWall(false);
        this.setAttacking(false);
        this.setShooting(false);
        //Changing the enemies range if they are melee vs ranged
    }

    /**chooseAction method
     * calculates if the player is within the enemy's range, and chooses if the enemy should attack or move
     * @param playerX - the player's x coordinate
     * @param playerY - the player's y coordinate
     * @param frameCount - the frame count of the game
     * @param projectilePics - the array of the projectile pictures
     */
    public void chooseAction(int playerX, int playerY, int frameCount, BufferedImage[] projectilePics) {

        //Declare Variables
        int rise;
        int run;

        //Calculate slope values
        rise = playerY - this.getyCoordinate();
        run = playerX - this.getxCoordinate();

        //
        if (run != 0 && (Math.abs(rise / run)) >= 1) {
            if (rise >= 0) {//if the player is below the enemy
                this.setDirection(this.getTOWARDS());
            } else {
                this.setDirection(this.getAWAY());
            }

        } else {
            if (run >= 0) {//if the player is right the enemy
                this.setDirection(this.getRIGHT());
            } else {
                this.setDirection(this.getLEFT());
            }
        }

        //If enemy is out of range, move them, if not, attack
        //Using distance formula
        if (Math.sqrt((Math.pow(playerX - this.getxCoordinate(), 2) + (Math.pow(playerY - this.getyCoordinate(), 2)))) > range) {
            this.move(frameCount);
        } else if (!this.isAttacking()) {
            this.attack(projectilePics);
        }
    }

    /**
     * attack method
     * allows enemies to attack the player
     * @param projectilePics projectile images to assign to projectile object
     */
    public void attack(BufferedImage[] projectilePics) {

        //Set action to attacking
        this.setAction(this.getATTACKING());
        this.setAttacking(true);

        //Ensure animation begins from right frame
        this.setPicNum(0);

        //Shoot Projectile
        if (isRanged) {
            if (this.getDirection() == this.getTOWARDS()) {
                projectiles.add(new Projectile((this.getxCoordinate() + 3), (this.getyCoordinate() + 60), 0, 8, this.getDamage(), projectilePics[4], 17, 9));
            } else if (this.getDirection() == this.getAWAY()) {
                projectiles.add(new Projectile((this.getxCoordinate() + 39), this.getyCoordinate(), 0, -8, this.getDamage(), projectilePics[4], 17, 9));
            } else if (this.getDirection() == this.getLEFT()) {
                projectiles.add(new Projectile((this.getxCoordinate() + 15), (this.getyCoordinate() + 36), -8, 0, this.getDamage(), projectilePics[5], 9, 15));
            } else {
                projectiles.add(new Projectile((this.getxCoordinate() + 42), (this.getyCoordinate() + 36), 8, 0, this.getDamage(), projectilePics[5], 9, 15));
            }
        }
        else{
            this.setSwordHitbox(this.getHitbox());
        }
    }

    /**
     * animate method
     * determines which picture is used to display the Enemy
     * @param frameCount
     * @param frameDelay
     */
    public void animate(int frameCount, int frameDelay) {
        if (frameCount % frameDelay == 0) {
            if (this.getPicNum() + 1 > 3) {
                this.setPicNum(0);
                if (this.isAttacking()) {
                    this.setAttacking(false);
                    this.setSwordHitbox(null);
                }
            } else {
                this.setPicNum(this.getPicNum() + 1);
            }
        }
    }

    /**moveMethod
     * moves the enemy towards the player
     * @param frameCount - the frame count to prevent awkward diagonal movement
     */
    public void move(int frameCount) {

        //Set action to walking
        this.setAction(this.getWALKING());

        //Prevent diagonal movement
        if (frameCount % 10 == 0) {
            //Set Velocity
            if (this.getDirection() == this.getTOWARDS()) {
                if (this.canMoveDown()) {
                    this.setyVelocity(this.getMovementSpeed());
                    this.setxVelocity(0);
                } else {
                    //moving character right instead so they dont constantly walk into walls
                    this.setxVelocity(this.getMovementSpeed());
                    this.setyVelocity(0);
                }
            } else if (this.getDirection() == this.getAWAY()) {
                if (this.canMoveUp()) {
                    this.setyVelocity(-(this.getMovementSpeed()));
                    this.setxVelocity(0);
                } else {
                    //Moving the character left instead so they dont constantly walk into walls
                    this.setxVelocity(-(this.getMovementSpeed()));
                    this.setyVelocity(0);
                }
            } else if (this.getDirection() == this.getRIGHT()) {
                if (this.canMoveRight()) {
                    this.setyVelocity(0);
                    this.setxVelocity((this.getMovementSpeed()));
                } else {
                    //Moving the character up instead so they dont constantly walk into walls
                    this.setyVelocity(-(this.getMovementSpeed()));
                    this.setxVelocity(0);
                }
            } else {
                if (this.canMoveLeft()) {
                    this.setyVelocity(0);
                    this.setxVelocity(-(this.getMovementSpeed()));
                } else {
                    //Moving character down instead so they dont constantly walk into walls
                    this.setxVelocity(0);
                    this.setyVelocity(this.getMovementSpeed());
                }
            }
        }
        //Actually move the enemy
        this.setxCoordinate(this.getxCoordinate() + this.getxVelocity());
        this.setyCoordinate(this.getyCoordinate() + this.getyVelocity());
        if (this.getDirection() == this.getTOWARDS() || this.getDirection() == this.getAWAY()) {
            this.setHitbox(new Rectangle(this.getxCoordinate() + 4, this.getyCoordinate() - 4, this.getPLAYER_WIDTH() - 8, this.getPLAYER_HEIGHT() + 10));
        } else {
            this.setHitbox(new Rectangle(this.getxCoordinate() - 4, this.getyCoordinate() + 4, this.getPLAYER_WIDTH() + 4, this.getPLAYER_HEIGHT() - 2));
        }
    }

    /**
     * isRanged method
     * @return if the enemy is ranged
     */
    public boolean isRanged() {
        return isRanged;
    }

    /**
     * setRanged method
     * @param ranged - if the enemy is ranged
     */
    public void setRanged(boolean ranged) {
        isRanged = ranged;
    }

    /**
     * getRange method
     * @return range - the distance the enemy can attack from
     */
    public int getRange() {
        return range;
    }

    /**setRange method
     *
     * @param range - the distance the enemy can attack from
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**getProjectiles method
     *
     * @return projectiles - the ArrayList storing all the projectiles
     */
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    /**setProjectiles method
     *
     * @param projectiles - the ArrayList holding all the projectiles
     */
    public void setProjectiles(ArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    /**getSwordHitbox
     *
     * @return - the rectangle representing the player's sword
     */
    public Rectangle getSwordHitbox() {
        return swordHitbox;
    }

    /**setSwordHitbox method
     *
     * @param swordHitbox - the Rectangle representing the player's sword
     */
    public void setSwordHitbox(Rectangle swordHitbox) {
        this.swordHitbox = swordHitbox;
    }
}