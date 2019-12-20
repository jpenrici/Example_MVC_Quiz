package model;

import java.util.ArrayList;

public class Question {

    private final int number;
    private String question;
    private String theme;
    private int correctAnswer;
    private String pathImage;
    private int currentAnswer;
    private boolean hit;
    private ArrayList<String> options;

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

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }
    
    public String getTheme() {
        return theme;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean getHit() {
        return hit;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getPathImage() {
        return pathImage;
    }

    public int getCurrentAnswer() {
        return currentAnswer;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }
    
    public void setTheme(String newTheme) {
        theme = newTheme;
    }

    public void setHit(boolean state) {
        hit = state;
    }

    public void setCurrentAnswer(int currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public void setOptions(ArrayList<String> option) {
        this.options = option;
    }

    @Override
    public String toString() {
        String str = "Tema: " + theme 
                + "\nQuestão: " + number + "\nPergunta: " + question
                + "\nResposta Correta: " + correctAnswer + "\nImagem: " + pathImage
                + "\nAlternativas:\n";
        str = options.stream().map((s) -> s + "\n").reduce(str, String::concat);
        return str;
    }
}
