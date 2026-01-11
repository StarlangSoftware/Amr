package Amr.Corpus;

public class AmrConnection {
    private final AmrWord from;
    private final AmrWord to;

    public AmrConnection(AmrWord from, AmrWord to){
        this.from = from;
        this.to = to;
    }

    public AmrWord from(){
        return from;
    }

    public AmrWord to(){
        return to;
    }

}
