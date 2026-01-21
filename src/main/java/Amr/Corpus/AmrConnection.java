package Amr.Corpus;

public class AmrConnection {
    private final AmrWord from;
    private final AmrWord to;
    private final String with;

    public AmrConnection(AmrWord from, AmrWord to, String with){
        this.from = from;
        this.to = to;
        this.with = with;
    }

    public AmrWord from(){
        return from;
    }

    public AmrWord to(){
        return to;
    }

    public String with(){
        return with;
    }

}
