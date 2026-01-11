package Amr.Annotation;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class AmrConnectionObject extends AmrObject {

    private final AmrWordObject from;
    private final AmrWordObject to;

    public AmrConnectionObject(AmrWordObject from, AmrWordObject to){
        this.from = from;
        this.to = to;
        this.boundingBox = new Rectangle(from.getCenter().x, from.getCenter().y, Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    public AmrConnectionObject clone(){
        return new AmrConnectionObject(from, to);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Connection from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void move(int deltaX, int deltaY){
        this.boundingBox = new Rectangle(from.getCenter());
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    private void printArrow(Graphics g, int x1, int y1, int x2, int y2){
        int d = 10;
        int h = 5;
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;
        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;
        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
        g.fillPolygon(xpoints, ypoints, 3);
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, null, 0.0f));
        int x1 = from.getCenter().x;
        int y1 = from.getCenter().y + from.boundingBox.height / 2;
        int x2 = to.getCenter().x;
        int y2 = to.getCenter().y - to.boundingBox.height / 2;
        g.drawLine(x1, y1, x2, y2);
        printArrow(g, x1, y1, x2, y2);
    }

    public String toString(){
        return from.getName() + " to " + to.getName();
    }
}
