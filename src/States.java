/**
 * Project - TheArenaGame
 * States class
 * class holds final integer variables describing the various states the game can be in
 * @author Arman Atharinejad and Isaac Nay
 */
public class States {
    private final int MAIN_MENU = 1;
    private final int IN_GAME = 2;
    private final int IN_STORE = 3;
    private final int GAME_OVER = 4;;
    private final int CONTROLS_ONE = 5;
    private final int CONTROLS_TWO = 6;
    private final int CONTROLS_THREE = 7;
    private final int CONTROLS_FOUR = 8;
    private final int GAME_START = 9;

    /**getMainMenu method
     * returns the value of MAIN_MENU
     * @return MAIN_MENU - the number representing a section of code that runs the main menu
     */
    public int getMAIN_MENU() {
        return MAIN_MENU;
    }
    /**getMainMenu method
     * returns the value of IN_GAME
     * @return IN_GAME - the number representing a section of code that runs the game
     */
    public int getIN_GAME() {
        return IN_GAME;
    }
    /**getIN_STORE method
     * returns the value of IN_STORE
     * @return IN_STORE - the number representing a section of code that runs the store
     */
    public int getIN_STORE() {
        return IN_STORE;
    }
    /**getGAME_OVER method
     * returns the value of GAME_OVER
     * @return  - the number representing a section of code that runs the game over display
     */
    public int getGAME_OVER(){
        return GAME_OVER;
    }

    /**getCONTROLS_ONE
    * returns the value of CONTROLS_ONE
    * @return  - the number representing a section of code that runs first controls screen
     */
    public int getCONTROLS_ONE() {
        return CONTROLS_ONE;
    }
    /**getCONTROLS_TWO
     * returns the value of CONTROLS_TWO
     * @return  - the number representing a section of code that runs the second control screen
     */
    public int getCONTROLS_TWO() {
        return CONTROLS_TWO;
    }
    /**getCONTROLS_THREE
     * returns the value of CONTROLS_THREE
     * @return  - the number representing a section of code that runs the third controls screen
     */
    public int getCONTROLS_THREE() {
        return CONTROLS_THREE;
    }
    /**getCONTROLS_FOUR
     * returns the value of CONTROLS_FOUR
     * @return  - the number representing a section of code that runs the fourth controls screen
     */
    public int getCONTROLS_FOUR() {
        return CONTROLS_FOUR;
    }
    /**getGAME_START
     * returns the value of GAME_START
     * @return  - the number representing a section of code that runs the start game screen
     */
    public int getGAME_START() {
        return GAME_START;
    }
}

