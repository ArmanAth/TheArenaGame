public class GameManager {
    private int roundNum;
    private int state;
    private int wavesRemaining;
    private States states = new States();

    /**
     * GameManager constructor
     */
    public GameManager(int roundNum, int state){
        this.setRoundNum(roundNum);
        this.setState(state);
        this.setWavesRemaining(3);
    }

    public void startNextRound(){
        this.setRoundNum(roundNum + 1);
        this.setWavesRemaining(3);
        this.setState(states.getIN_GAME());
    }

    public int getroundNum() {
        return roundNum;
    }

    public void setWave(int wave) {
        this.roundNum = wave;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public int getWavesRemaining() {
        return wavesRemaining;
    }

    public void setWavesRemaining(int wavesRemaining) {
        this.wavesRemaining = wavesRemaining;
    }
}
