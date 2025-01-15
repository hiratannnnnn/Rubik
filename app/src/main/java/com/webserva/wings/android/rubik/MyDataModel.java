package com.webserva.wings.android.rubik;

public class MyDataModel {
    private int imageId;
    private int correctCount;
    private int incorrectCount;
    private String solution;
    private String meanTime;

    public MyDataModel(int imageId, int correctCount, int incorrectCount, String solution, String meanTime) {
        this.imageId = imageId;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.solution = solution;
        this.meanTime = meanTime;
    }

    // 以下、ゲッターとセッター
    public int getImageId() {
        return imageId;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public float getSuccessRate() {
        int total = correctCount + incorrectCount;
        if (total == 0) return 0;  // 0で割ることを防ぐ
        return ((float) correctCount / total) * 100;
    }

    public String getSolution() {return solution;}
    public String getMeanTime() {return meanTime;}

    public int getIncorrect() {
        return this.incorrectCount;
    }
}
