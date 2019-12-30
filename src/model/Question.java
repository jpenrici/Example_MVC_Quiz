package model;

import java.util.ArrayList;

public class Question {

    public int number;
    public String question;
    public String theme;
    public int correctAnswer;
    public String pathImage;
    public int currentAnswer;
    public boolean hit;
    public ArrayList<String> options;

    public Question(int number, String question, int correctAnswer,
            String pathImage) {
        this.number = number;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.pathImage = pathImage;
        theme = "";
        options = new ArrayList<>();
        hit = false;
        currentAnswer = -1;
    }
}
