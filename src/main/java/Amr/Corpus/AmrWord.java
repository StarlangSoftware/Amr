package Amr.Corpus;

import Dictionary.Word;
import java.awt.*;

public class AmrWord extends Word {
    protected Point position;

    public AmrWord(String name, Point position){
        super(name);
        this.position = position;
    }

    public Point getPosition(){
        return position;
    }

    public void move(int deltaX, int deltaY){
        position.x += deltaX;
        position.y += deltaY;
    }

    public AmrWord clone(){
        return new AmrWord(name, new Point(position));
    }

}
