package Amr.Annotation;

import java.awt.*;
import java.io.FileWriter;

public class AmrSimpleObject extends AmrObject{

    protected String name;
    protected Point position;

    public AmrSimpleObject(String name, Point position){
        this.name = name;
        this.position = position;
        this.boundingBox = new Rectangle(position);
    }

    public AmrSimpleObject clone(){
        return new AmrSimpleObject(name, new Point(position));
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void save(FileWriter outfile){}

    public void move(int deltaX, int deltaY){
        position.x += deltaX;
        position.y += deltaY;
        this.boundingBox.setLocation(position);
    }

    public String toString(){
        return name;
    }

}
