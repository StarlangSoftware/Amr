package Amr.Annotation;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class AmrConnectionObject extends AmrObject {

    private final AmrWordObject from;
    private final AmrWordObject to;
    private String with;

    public AmrConnectionObject(AmrWordObject from, AmrWordObject to, String with){
        this.from = from;
        this.to = to;
        this.with = with;
        int upperLeftX, upperLeftY;
        int x1 = from.getCenter().x;
        int y1 = from.getCenter().y + from.boundingBox.height / 2;
        int x2 = to.getCenter().x;
        int y2 = to.getCenter().y - to.boundingBox.height / 2;
        upperLeftX = Math.min(x1, x2);
        upperLeftY = Math.min(y1, y2);
        this.boundingBox = new Rectangle(upperLeftX, upperLeftY, Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public String with(){
        return with;
    }

    public void setWith(String with){
        this.with = with;
    }

    public AmrConnectionObject clone(){
        return new AmrConnectionObject(from, to, with);
    }

    public void save(FileWriter outfile){
        try{
            outfile.write("<Connection from=\""+ this.from.getName() + "\" to=\"" + this.to.getName() + "\" with=\"" + this.with + "\"/>\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public boolean contains(Point p){
        int x1 = from.getCenter().x;
        int y1 = from.getCenter().y + from.boundingBox.height / 2;
        int x2 = to.getCenter().x;
        int y2 = to.getCenter().y - to.boundingBox.height / 2;
        return p.x > Math.min(x1, x2) && p.x < Math.max(x1, x2) && p.y > Math.min(y1, y2) && p.y < Math.max(y1, y2);
    }

    public void move(int deltaX, int deltaY){
        this.boundingBox = new Rectangle(from.getCenter());
        this.setBoundingBoxSize(Math.abs(from.getCenter().x - to.getCenter().x), Math.abs(from.getCenter().y - to.getCenter().y));
    }

    private void calculatePoints(int x1, int y1, int x2, int y2, int[] xpoints, int[] ypoints){
        int d = 10;
        int h = 5;
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;
        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;
        xpoints[1] = (int) xm;
        xpoints[2] = (int) xn;
        ypoints[1] = (int) ym;
        ypoints[2] = (int) yn;
    }

    private void printArrow(Graphics g, int x1, int y1, int x2, int y2){
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        xpoints[0] = x2;
        ypoints[0] = y2;
        calculatePoints(x1, y1, x2, y2, xpoints, ypoints);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        int x1 = from.getCenter().x;
        int y1 = from.getCenter().y + from.boundingBox.height / 2;
        int x2 = to.getCenter().x;
        int y2 = to.getCenter().y - to.boundingBox.height / 2;
        if (isSelected()){
            g.drawOval(x1 - 2, y1 - 2, 4, 4);
            g.drawOval(x1 - 2, y2 - 2, 4, 4);
            g.drawOval(x2 - 2, y1 - 2, 4, 4);
            g.drawOval(x2 - 2, y2 - 2, 4, 4);
        }
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, null, 0.0f));
        g.drawString(with, (x1 + x2) / 2, (y1 + y2) / 2);
        g.drawLine(x1, y1, x2, y2);
        printArrow(g, x1, y1, x2, y2);
    }

    public String toSvg(){
        String result = "";
        int x1 = from.word.getPosition().x;
        int y1 = from.word.getPosition().y + 45;
        int x2 = to.word.getPosition().x;
        int y2 = to.word.getPosition().y + 5;
        result += "<text x=\"" + ((x1 + x2) / 2) + "\" y=\"" + ((y1 + y2) / 2) + "\">" + with + "</text>\n";
        result += "<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\"/>\n";
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        xpoints[0] = x2;
        ypoints[0] = y2;
        calculatePoints(x1, y1, x2, y2, xpoints, ypoints);
        result += "<polygon points=\"" + xpoints[0] +"," + ypoints[0] + " " + xpoints[1] + "," + ypoints[1] + " " + xpoints[2] + "," + ypoints[2] + "\"/>";
        return result;
    }

    public String toString(){
        return from.getName() + " to " + to.getName();
    }
}
