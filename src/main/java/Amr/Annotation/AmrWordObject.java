package Amr.Annotation;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class AmrWordObject extends AmrSimpleObject {

    static final int OVAL_HEIGHT = 40;

    public AmrWordObject(String name, Point position){
        super(name, position);
    }

    public AmrWordObject clone(){
        return new AmrWordObject(name, new Point(position));
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Word name=\""+ this.name + "\" positionX=\"" + this.position.x + "\" positionY=\"" + this.position.y + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void paint(Graphics g){
        super.paint(g);        
        int stringSize = g.getFontMetrics().stringWidth(name);
        g.drawOval(position.x, position.y, stringSize + 10, OVAL_HEIGHT);
        g.drawString(name, position.x + 5, position.y + OVAL_HEIGHT / 2 + 5);
        setBoundingBoxSize(stringSize + 10, OVAL_HEIGHT);
    }

}
