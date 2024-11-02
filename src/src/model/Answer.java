package src.model;

public class Answer {
    public String id;
    public String answer;
    public long timeStamp;

    public Answer(String a, String b, long c){
        this.id = a;
        this.answer = b;
        timeStamp = c;
    }
}
