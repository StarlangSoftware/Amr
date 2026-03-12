package Amr.Annotation;

import java.awt.*;
import java.io.FileWriter;

public class AmrObject {

    protected boolean selected = false;
    public boolean colored = false;
    protected Rectangle boundingBox;

    public boolean isSelected(){
        return selected;
    }

    public AmrObject clone(){
        AmrObject clone = new AmrObject();
        clone.boundingBox = new Rectangle(boundingBox);
        return clone;
    }

    public void move(int deltaX, int deltaY){}

    public void save(FileWriter outfile){}

    public void select(boolean selected){
        this.selected = selected;
    }

    public void setBoundingBoxSize(int width, int height){
        boundingBox.setSize(width, height);
    }

    public boolean contains(Point p){
        return boundingBox.contains(p);
    }

    public Point getCenter(){
        return new Point(boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height / 2);
    }

    public String toSvg(){
        return "";
    }

    public void paint(Graphics g){
        if (colored){
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }        
    }

}
