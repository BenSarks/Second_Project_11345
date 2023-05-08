package com.example.second_project_11345.Logic;

import java.util.Random;

public class GameManager {
    private final int rows;
    private final int cols;
    private final int[] obstacleIndexArr;
    private final int[] coinIndexArr;
    private int coinCount = 0;
    private int coinEarned = -1;
    private int timesDied = 0;
    private int currentCarIndex = 2;
    private int previousRandomIndex = 0;

    public GameManager(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.obstacleIndexArr = new int[rows];
        this.coinIndexArr = new int[rows - 1];

        for(int i = 0; i < rows; ++i) {
            this.obstacleIndexArr[i] = -1;
            if (i < rows - 1) {
                this.coinIndexArr[i] = -1;
            }
        }

    }

    public boolean setRandomCoinAndObstacle() {
        this.obstacleIndexArr[this.obstacleIndexArr.length - 1] = this.getRandomColIndex();
        if (this.randomBoolean()) {
            this.coinIndexArr[this.coinIndexArr.length - 1] = this.getRandomColIndex();
            return true;
        } else {
            this.coinIndexArr[this.coinIndexArr.length - 1] = -1;
            return false;
        }
    }

    public int disableLifeImageAtIndex() {
        return this.timesDied - 1;
    }

    public int getObstacleAtIndex(int index) {
        return this.obstacleIndexArr[index];
    }

    public int getCoinAtIndex(int index) {
        return this.coinIndexArr[index];
    }

    public int shiftGameIndexForward(int index) {
        if (this.obstacleIndexArr[index + 1] > -1) {
            this.obstacleIndexArr[index] = this.obstacleIndexArr[index + 1];
            if (index + 1 < this.coinIndexArr.length) {
                this.coinIndexArr[index] = this.coinIndexArr[index + 1];
                if (this.coinIndexArr[index + 1] > -1) {
                    return 2;
                }
            }

            return 1;
        } else {
            return 0;
        }
    }

    public Boolean updateLifeCount() {
        if (this.obstacleIndexArr[0] == this.currentCarIndex) {
            ++this.timesDied;
            return true;
        } else {
            return false;
        }
    }

    public Boolean updateCoinCount() {
        if (this.coinEarned == this.currentCarIndex) {
            ++this.coinCount;
            this.coinEarned = this.coinIndexArr[0];
            return true;
        } else {
            this.coinEarned = this.coinIndexArr[0];
            return false;
        }
    }

    public Boolean endGame() {
        int totalLives = 3;
        return this.timesDied == totalLives;
    }

    public boolean randomBoolean() {
        Random bol = new Random();
        return bol.nextBoolean();
    }

    public boolean canCarMove(int index) {
        return 0 <= index && this.cols > index;
    }

    public int getRandomColIndex() {
        Random r = new Random();

        int currentRandomIndex;
        do {
            currentRandomIndex = r.nextInt(this.cols);
        } while(this.previousRandomIndex == currentRandomIndex);

        this.previousRandomIndex = currentRandomIndex;
        return currentRandomIndex;
    }

    public int getCurrentCarIndex() {
        return this.currentCarIndex;
    }

    public void setCurrentCarIndex(int currentCarIndex) {
        this.currentCarIndex = currentCarIndex;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public int getCoinCount() {
        return this.coinCount;
    }
}
