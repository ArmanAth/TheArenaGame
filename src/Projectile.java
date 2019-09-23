import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Project - TheArenaGame
 * Projectile class
 * class represents all projectiles on the map, both enemies and players
 * Each projectile has an assigned damage and hitbox, disjointed from all the FightingCharacters when shot
 * @author Arman Atharinejad and Isaac Nay
 */
public class Projectile implements Movable{
    private int xCoordinate;
    private int yCoordinate;
    private int xVelocity;
    private int yVelocity;
    private int damage;
    private int height;
    private int width;
    private Rectangle hitbox;
    private BufferedImage image;

    /**Projectile Constructor
     * Basic constructor for projectiles
     * @param xCoordinate
     * @param yCoordinate
     * @param xVelocity
     * @param yVelocity
     * @param image
     */
    Projectile (int xCoordinate, int yCoordinate, int xVelocity, int yVelocity, int Damage, BufferedImage image, int height, int width){
        this.setxCoordinate(xCoordinate);
        this.setyCoordinate(yCoordinate);
        this.setxVelocity(xVelocity);
        this.setyVelocity(yVelocity);
        this.setDamage(Damage);
        this.setImage(image);
        this.setHeight(height);
        this.setWidth(width);
        this.setHitbox(new Rectangle(xCoordinate, yCoordinate, width, height));
    }

    /**
     * checkWallCollision
     * Takes in arraylist of walls and projectiles on the map and checks if the projectile hits any of them
     * Deletes the projectile from arraylist if wall is hit
     * @param walls arraylist of Terrain objects representing the walls in the map
     */
    public ArrayList<Projectile> checkWallCollision(ArrayList<Terrain> walls, ArrayList<Projectile> projectiles, int num){
        boolean notCompleted = true;
        for(int i = 0; i < walls.size() && notCompleted; i++){
            if(walls.get(i).getHitbox().intersects(projectiles.get(num).getHitbox())){
                projectiles.remove(num);
                notCompleted = false;
            }
        }
        return projectiles;
    }
    /**move method
     * allows the projectile to move
     */
    public void move() {
        this.setxCoordinate(this.xCoordinate + this.xVelocity);
        this.setyCoordinate(this.yCoordinate + this.yVelocity);
        this.setHitbox(new Rectangle(this.getxCoordinate(), this.getyCoordinate(), this.getWidth(), this.getHeight()));
    }
    /**getxCoordinate method
     * returns the value of the private variable xCoordinate
     * @return xCoordinate - int
     */
    public int getxCoordinate() {
        return xCoordinate;
    }
    /**setxCoordinate method
     * changes the private variable xCoordinate
     * @param xCoordinate
     */
    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    /**getyCoordinate method
     * returns the value of the private variable xCoordinate
     * @return YCoordinate - int
     */
    public int getyCoordinate() {
        return yCoordinate;
    }
    /**setyCoordinate method
     * changes the private variable yCoordinate
     * @param yCoordinate
     */
    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    /**getxVelocity method
     * returns the value of the private variable xVelocity
     * @return xVelocity - int
     */
    public int getxVelocity() {
        return xVelocity;
    }
    /**setxVelocity method
     * changes the private variable xVelocity
     * @param xVelocity
     */
    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }
    /**getyVelocity method
     * returns the value of the private variable yVelocity
     * @return yVelocity - int
     */
    public int getyVelocity() {
        return yVelocity;
    }
    /**setyVelocity method
     * changes the private variable yVelocity
     * @param yVelocity
     */
    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }
    /**getDamage method
     * returns the value of the private variable damage
     * @return damage
     */
    public int getDamage() {
        return damage;
    }
    /**setDamage method
     * changes the value of the private variable damage
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }
    /**getHitbox method
     * returns the Rectangle representing the hitbox of the projectile
     * @return hitbox - Rectangle
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
    /**setHitbox method
     * changes the private Rectangle hitbox
     * @param hitbox
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
    /**getImage method
     * returns the image used to display the projectile
     * @return image - BufferedImage
     */
    public BufferedImage getImage() {
        return image;
    }
    /**setImage method
     * returns the image representing the projectile
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
