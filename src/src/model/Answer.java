package src.model;

public class Answer {
    public String id;
    public String answer;
    public long timeStamp;
    public String type;

    public Answer(String a, String b, long c, String type){
        this.id = a;
        this.answer = b;
        timeStamp = c;
        this.type = type;
    }
}
