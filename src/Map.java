import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project - TheArenaGame
 * Msp class
 * Class represents the map that the player interacts with
 * Holds a grid containing all the tiles that take up the map, and an arraylist
 * representing the tiles that are specifically walls, used to check for collision
 * @author Arman Atharinejad and Isaac Nay
 */
public class Map{
    private final int width = 31;
    private final int height = 16;
    private final int numImages = 15;
    private BufferedImage[] mainImages = new BufferedImage[numImages];
    private BufferedImage[] earthImages= new BufferedImage[numImages];
    private BufferedImage[] fireImages = new BufferedImage[numImages];
    private Terrain[][] grid;
    private ArrayList<Terrain> walls = new ArrayList<>();
    private int mapNum;
    private int arenaType;

    //Constructor
    public Map() {
        loadImages();
        this.grid = setGrid();
    }

    /**reset method
     * resets the map grid
     */
    public void reset(){
        loadImages();
        this.grid = setGrid();
    }

    /**
     * loadImages method
     * reads in images for all map tiles from assets
     */
    public void loadImages() {
        for (int i = 0; i < numImages; i++) {
            //Random int to pick a random map
            try {
                mainImages[i] = (ImageIO.read(new File("mainHub/" + Integer.toString(i) + ".png")));
                fireImages[i] = (ImageIO.read(new File("fireArena/" + Integer.toString(i) + ".png")));
                earthImages[i] = (ImageIO.read(new File("earthArena/" + Integer.toString(i) + ".png")));
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }

    /**
     * setGrid method
     * fills a 2d array with terrain objects based on formatting from file
     * produced from our map editor
     * @return 2d terrain array containing map
     */
    public Terrain[][] setGrid(){
        //Resetting walls so walls from previous wave dont stay
        walls.clear();
        //randomizing map grid and images
        mapNum = (int) ((Math.random() * 3) + 1);
        arenaType = (int) ((Math.random() * 3) + 1);
        BufferedImage[] images = new BufferedImage[numImages];
        if(arenaType == 1){
            images = mainImages;
        }
        else if(arenaType == 2){
            images = earthImages;
        }
        else if(arenaType == 3){
            images = fireImages;
        }
        //Variable for new grid & file with map
        File file = new File("map" + mapNum + ".txt");
        Terrain[][] newGrid = new Terrain[width][height];

        try {
            Scanner scanner = new Scanner(file);
            String num;
            int counter = 0;
            //Double loop to create the grid of terrain objects
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    num = scanner.nextLine();
                    newGrid[i][j] = new Terrain(i * 32, j * 32, images[Integer.parseInt(num)]);
                    //Adding tile to array of walls if the image is a wall
                    if(Integer.parseInt(num) >= 0 && Integer.parseInt(num) <= 12) {
                        walls.add(new Terrain(i * 32, j * 32, images[Integer.parseInt(num)]));
                        counter++;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return newGrid;
    }

    /**
     * drawMap method
     * takes the images from the grid and displays them on the screen at their proper coordinates
     * @param g
     */
    public void drawMap(Graphics g){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                g.drawImage(grid[i][j].getImage(), grid[i][j].getxCoordinate(), grid[i][j].getyCoordinate(), null);
            }
        }
    }

    /**getWidth method
     *
     * @return width - the width of the map
     */
    public int getWidth() {
        return width;
    }

    /**getHeight method
     *
     * @return - the height of the map
     */
    public int getHeight() {
        return height;
    }

    /**getNumImages method
     *
     * @return - numImages
     */
    public int getNumImages() {
        return numImages;
    }

    /**getGrid method
     *
     * @return - the terrain array storing the map
     */
    public Terrain[][] getGrid() {
        return grid;
    }

    /**setGrid method
     *
     * @param grid - the terrain array storing the map
     */
    public void setGrid(Terrain[][] grid) {
        this.grid = grid;
    }

    /**getWalls method
     *
     * @return an ArrayList of all the walls
     */
    public ArrayList<Terrain> getWalls() {
        return walls;
    }

    /**setWalls method
     *
     * @param walls - an ArrayList of all the walls
     */
    public void setWalls(ArrayList<Terrain> walls) {
        this.walls = walls;
    }

    /**getMainImages method
     *
     * @return mainImages - an array of main theme terrain images
     */
    public BufferedImage[] getMainImages() {
        return mainImages;
    }

    /**setMainImages method
     *
     * @param mainImages - an array of main theme images
     */
    public void setMainImages(BufferedImage[] mainImages) {
        this.mainImages = mainImages;
    }
    /**getEarthImages method
     *
     * @return earthImages - an array of Earth theme terrain images
     */
    public BufferedImage[] getEarthImages() {
        return earthImages;
    }

    /**setEarthImages method
     *
     * @param earthImages - an array of Earth theme terrain images
     */
    public void setEarthImages(BufferedImage[] earthImages) {
        this.earthImages = earthImages;
    }

    /**getFireImages method
     *
     * @return - an array of fire theme terrain images
     */
    public BufferedImage[] getFireImages() {
        return fireImages;
    }

    /**setFireImages method
     *
     * @param fireImages - an array of fire theme images
     */
    public void setFireImages(BufferedImage[] fireImages) {
        this.fireImages = fireImages;
    }
}