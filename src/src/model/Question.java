package src.model;

public class Question {
    public String ques;
    public String answer;
    public int firstIndex = 0;

    public Question(String ques, String ans, int firstIndex){
        this.ques  = ques;
        this.answer = ans;
        this.firstIndex = firstIndex;
    }
}
