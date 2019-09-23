import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Project - TheArenaGame
 * Terrain Class
 * class represents an individual tile that makes up the map the characters fight on
 * Each tile has a hitbox, however the hitbox is only utilized in the game if the tile
 * is a wall to check for collision
 * @author Isaac Nay and Arman Atharinejad
 */
public class Terrain {

    //Declare Variables
    private int xCoordinate;
    private int yCoordinate;
    private BufferedImage image;
    private Rectangle hitbox;
    final private int TILE_SIZE = 32;


    /**Terrain Constructor
     * basic constructor for terrain superclass
     * @param xCoordinate xCoordinate of the tile
     * @param yCoordinate yCoordinate of the tile
     * @param image image file that the tile holds
     */
    Terrain(int xCoordinate, int yCoordinate, BufferedImage image){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.image = image;
        this.hitbox = new Rectangle(xCoordinate, yCoordinate, TILE_SIZE, TILE_SIZE);
    }

    /**getxCoordinate method
     * returns the value of the private variable xCoordinate
     * @return xCoordinate - int
     */
    public int getxCoordinate() {
        return xCoordinate;
    }
    /**setxCoordinate method
     * changes the value of the private variable xCoordinate
     * @param xCoordinate - int
     */
    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    /**getyCoordinate method
     * returns the value of the private variable yCoordinate
     * @return yCoordinate - int
     */
    public int getyCoordinate() {
        return yCoordinate;
    }
    /**setyCoordinate method
     * changes the value of the private variable yCoordinate
     * @param yCoordinate
     */
    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    /**getImage method
     * returns the image to display the terrain
     * @return image - BufferedImage
     */
    public BufferedImage getImage() {
        return image;
    }
    /**setImage method
     * changes the image of the terrain
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}