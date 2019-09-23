import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;
import java.awt.Font;

/**
 * Project - TheArenaGame
 * Database class
 * This class reads in all necessary assets for the game before starting and is used to pass it on
 * to variables in the main class
 * @author Isaac Nay and Arman Atharinejad
 */
public class Database {
    private BufferedImage[][][] playerPics = new BufferedImage[4][4][4];
    private BufferedImage[][][] meleeEnemyPics = new BufferedImage[3][4][4];
    private BufferedImage[][][] rangedEnemyPics = new BufferedImage[3][4][4];
    private BufferedImage[] projectilePics = new BufferedImage[10];
    private BufferedImage storePic;
    private BufferedImage goldPic;
    private BufferedImage healthPic;
    private BufferedImage damagePic;
    private BufferedImage speedPic;
    private BufferedImage gameOverPic;
    private BufferedImage mainMenuPic;
    private BufferedImage loadingScreen;
    private BufferedImage controlsOne;
    private BufferedImage controlsTwo;
    private BufferedImage controlsThree;
    private BufferedImage controlsFour;
    private BufferedImage gameStart;
    private Font hudFont;
    private Clip mainSong;
    private Clip easterEgg;


    /**
     * Database constructor
     * reads in all the assets required for the game when called
     */
    Database(){
        //load projectile images from files
        for (int picName = 0; picName<10; picName++){
            try {
                projectilePics[picName]= ImageIO.read(new File("projectileImages/" + Integer.toString(picName)+ ".png"));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        int picName = 0;
        //Reading in player images
        for (int i=0; i<4; i++){//sorts by action
            for (int j=0 ; j<4 ; j++) {//sorts by direction
                for(int k=0 ; k<4 ; k++) {//sorts by animation frame order
                    try {
                        playerPics [i][j][k]= ImageIO.read(new File("playerImages/" + Integer.toString(picName)+ ".png"));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    picName++;
                }
            }
        }
        picName = 0;
        //Reading in enemy images
        for (int i=0; i<3; i++){//sorts by action
            for (int j=0 ; j<4 ; j++) {//sorts by direction
                for(int k=0 ; k<4 ; k++) {//sorts by animation frame order
                    try {
                        rangedEnemyPics [i][j][k]= ImageIO.read(new File("rangedEnemyImages/" + picName+ ".png"));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    picName++;
                }
            }
        }
        picName = 0;
        for (int i=0; i<3; i++){//sorts by action
            for (int j=0 ; j<4 ; j++) {//sorts by direction
                for(int k=0 ; k<4 ; k++) {//sorts by animation frame order
                    try {
                        meleeEnemyPics [i][j][k] = ImageIO.read(new File("meleeEnemyImages/" + picName + ".png"));
                    } catch (IOException e){
                        System.out.println("yee");
                    }
                    picName++;
                }
            }
        }
        //Reading in hud/state images
        try{
            loadingScreen = ImageIO.read(new File("hudImages/loadingScreen.png"));
            gameOverPic = ImageIO.read(new File("hudImages/gameOver.png"));
            storePic = ImageIO.read(new File("hudImages/shop.png"));
            mainMenuPic = ImageIO.read(new File("hudImages/mainMenu.png"));
            goldPic = ImageIO.read(new File("hudImages/gold.png"));
            healthPic = ImageIO.read(new File("hudImages/health.png"));
            damagePic = ImageIO.read(new File("hudImages/damage.png"));
            speedPic = ImageIO.read(new File("hudImages/speed.png"));
            controlsOne = ImageIO.read(new File("hudImages/controlsOne.png"));
            controlsTwo = ImageIO.read(new File("hudImages/controlsTwo.png"));
            controlsThree = ImageIO.read(new File("hudImages/controlsThree.png"));
            controlsFour = ImageIO.read(new File("hudImages/controlsFour.png"));
            gameStart = ImageIO.read(new File("hudImages/gameStart.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        //Importing custom font for hud
        try{
            hudFont = Font.createFont(Font.TRUETYPE_FONT, new File("manaspc.ttf")).deriveFont(22f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("manaspc.ttf")));
        }catch(IOException | FontFormatException e){
            e.printStackTrace();
        }

        //Read in Main Song
        try {
            File audioFile = new File("music/mainSong.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            this.mainSong = (Clip) AudioSystem.getLine(info);
            this.mainSong.open(audioStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //Read in easter egg
        try {
            File audioFile = new File("music/sickoMode.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            this.easterEgg = (Clip) AudioSystem.getLine(info);
            this.easterEgg.open(audioStream);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**getPlayerPics method
     * returns the array storing all pictures of the player
     * @return playerPics - the array holding the player pictures
     */
    public BufferedImage[][][] getPlayerPics() {
        return playerPics;
    }
    /**getMeleeEnemyPics method
     * returns the array storing all pictures of the melee enemies
     * @return meleeEnemyPics - the array holding all the pictures of melee enemies
     */
    public BufferedImage[][][] getMeleeEnemyPics() {
        return meleeEnemyPics;
    }
    /**getRangedEnemyPics method
     * returns the array storing all pictures of the ranged enemies
     * @return rangedEnemyPics - the array holding all the pictures of ranged enemies
     */
    public BufferedImage[][][] getRangedEnemyPics() {
        return rangedEnemyPics;
    }

    /**getProjectilePics method
     * returns the array storing all the pictures of the projectiles
     * @return projectilePics - the array storing all the pictures of the projectiles
     */
    public BufferedImage[] getProjectilePics() {
        return projectilePics;
    }

    /**getStorePic method
     * returns the image for the store
     * @return - the image for tbe store
     */
    public BufferedImage getStorePic() {
        return storePic;
    }
    /**getGoldPic method
     * returns the image for gold
     * @return - the image for gold
     */
    public BufferedImage getGoldPic() {
        return goldPic;
    }
    /**getHealthPic method
     * returns the image for health
     * @return - the image for health
     */
    public BufferedImage getHealthPic() {
        return healthPic;
    }
    /**getDamagePic method
     * returns the image for Damage
     * @return - the image for Damage
     */
    public BufferedImage getDamagePic() {
        return damagePic;
    }
    /**getSpeedPic method
     * returns the image for Speed
     * @return - the image for speed
     */
    public BufferedImage getSpeedPic() {
        return speedPic;
    }
    /**getHudFont method
     * returns the font for the HUD
     * @return - the font for the HUD
     */
    public Font getHudFont() {
        return hudFont;
    }
    /**getGameOverPic method
     * returns the image for the game over screen
     * @return - the image for the game over screen
     */
    public BufferedImage getGameOverPic() {
        return gameOverPic;
    }
    /**getMainMenuPic method
     * returns the image for the main menu screen
     * @return - the image for the main menu screen
     */
    public BufferedImage getMainMenuPic() {
        return mainMenuPic;
    }

    //UNIMPLEMENTED
    //public BufferedImage getLoadingScreen() {
    //    return loadingScreen;
    //}

    /**getControlsOne method
     * returns the image for the first controls screen
     * @return - the image for the first controls screen
     */
    public BufferedImage getControlsOne() {
        return controlsOne;
    }
    /**getControlsTwo method
     * returns the image for the second controls screen
     * @return - the image for the second controls screen
     */
    public BufferedImage getControlsTwo() {
        return controlsTwo;
    }
    /**getControlsThree method
     * returns the image for the third controls screen
     * @return - the image for the third controls screen
     */
    public BufferedImage getControlsThree() {
        return controlsThree;
    }
    /**getControlsFour method
     * returns the image for the fourth controls screen
     * @return - the image for the fourth controls screen
     */
    public BufferedImage getControlsFour() {
        return controlsFour;
    }
    /**getGameStart method
     * returns the image for the game start screen
     * @return - the image for the game start screen
     */
    public BufferedImage getGameStart() {
        return gameStart;
    }

    /**getMainSong method
     * returns the main song
     * @return mainSong - the main song
     */
    public Clip getMainSong() {
        return mainSong;
    }

    /**getEasterEgg method
     * returns the song played when the enter button is pressed
     * @return easterEgg - a song
     */
    public Clip getEasterEgg() {
        return easterEgg;
    }

}