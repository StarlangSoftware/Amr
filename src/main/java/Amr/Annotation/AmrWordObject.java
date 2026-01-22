package Amr.Annotation;

import Amr.Corpus.AmrWord;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class AmrWordObject extends AmrObject {

    protected AmrWord word;
    static final int OVAL_HEIGHT = 40;

    public AmrWordObject(String name, Point position){
        this.word = new AmrWord(name, position);
        this.boundingBox = new Rectangle(position);
    }

    public AmrWordObject(AmrWord word){
        this.word = word;
        this.boundingBox = new Rectangle(word.getPosition());
    }

    public String getName(){
        return word.getName();
    }

    public void setName(String name){
        word.setName(name);
    }

    public AmrWordObject clone(){
        return new AmrWordObject(word.getName(), word.getPosition());
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Word name=\""+ this.getName() + "\" positionX=\"" + this.word.getPosition().x + "\" positionY=\"" + this.word.getPosition().y + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        super.paint(g);        
        int stringSize = g.getFontMetrics().stringWidth(word.getName());
        g.drawOval(word.getPosition().x, word.getPosition().y, stringSize + 10, OVAL_HEIGHT);
        g.drawString(word.getName(), word.getPosition().x + 5, word.getPosition().y + OVAL_HEIGHT / 2 + 5);
        setBoundingBoxSize(stringSize + 10, OVAL_HEIGHT);
    }

    public void move(int deltaX, int deltaY){
        word.move(deltaX, deltaY);
        this.boundingBox.setLocation(word.getPosition());
    }

    public String toString(){
        return word.getName();
    }

}
