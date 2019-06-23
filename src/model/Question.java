package model;

import java.util.ArrayList;

public class Question {
    
    private final int number;
    private final String question;
    private final String correctAnswer; 
    private final String pathImage;
    private int currentAnswer;
    private boolean hit;
    private ArrayList<String> options;
    
    public Question(int number, String question, String answer, 
            String pathImage) {
        this.number = number;
        this.question = question;
        this.correctAnswer = answer;
        this.pathImage = pathImage;
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

    public String getAnswer() {
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
        String str = "Questão: " + number + "\nPergunta: " + question
                + "\nResposta: " + correctAnswer + "\nImagem: " + pathImage
                + "\nAlternativas:\n";
        for (String s : options) {
            str += s + "\n";            
        }
        return str;           
    }
}
