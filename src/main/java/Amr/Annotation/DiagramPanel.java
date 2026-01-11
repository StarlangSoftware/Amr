package Amr.Annotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class DiagramPanel extends JPanel  implements MouseListener, MouseMotionListener {

    protected AmrDiagram diagram;
    protected ArrayList<AmrDiagram> undoList;
    protected AmrObject fromObject = null;
    protected boolean dragged = false;
    protected boolean moved = false;

    protected EnumCommand lastCommand;
    private AmrObject previousColored = null;
    private Point fromPoint;
    private Rectangle selectedArea = null;
    protected String filename = null;

    public DiagramPanel(){
        undoList = new ArrayList<AmrDiagram>();
    }

    public void mousePressed(MouseEvent e){
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            fromPoint = e.getPoint();
        }
    }

    public void mouseReleased(MouseEvent e){
        selectedArea = null;
        this.repaint();
    }

    public AmrDiagram getDiagram(){
        return diagram;
    }

    public String getFileName(){
        return filename;
    }

    public String getFolder(){
        return diagram.getFolder();
    }

    public void setFileName(String filename){
        this.filename = filename;
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseDragged(MouseEvent e){
        dragged = true;
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            Point toPoint = e.getPoint();
            if (diagram.getAmrObjectAtPos(fromPoint) == null){
                selectedArea = new Rectangle(fromPoint.x, fromPoint.y, Math.abs(toPoint.x - fromPoint.x), Math.abs(toPoint.y - fromPoint.y));
                diagram.selectArea(selectedArea);
            } else {
                moved = true;
                diagram.moveSelected(toPoint.x - fromPoint.x, toPoint.y - fromPoint.y);
                save();
                fromPoint = toPoint;
            }
            this.repaint();
        }
    }
    
    public void mouseClicked(MouseEvent e){
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            AmrObject current;
            current = diagram.getAmrObjectAtPos(e.getPoint());
            if (current != null){
                current.select(!current.isSelected());
            } else {
                diagram.deselectAll();
            }
            this.repaint();            
        }
    }

    public void mouseMoved(MouseEvent e){
        AmrObject current;
        if (lastCommand != null && lastCommand == EnumCommand.EMPTY){
            current = diagram.getAmrObjectAtPos(e.getPoint());
            if (current == null){
                if (previousColored != null){
                    previousColored.colored = false;
                }
                previousColored = null;
            } else {
                if (current != previousColored){
                    current.colored = true;
                    if (previousColored != null){
                        previousColored.colored = false;
                    }
                    previousColored = current;
                }
            }
            this.repaint();
        }
    }

    public boolean canUndo(){
        return (!undoList.isEmpty());
    }

    public void save(){
    }

    public void undo(){
    }

    public void setCommand(EnumCommand lastCommand){
        this.lastCommand = lastCommand;
    }

    public void previousAmr(int count){
        diagram.previousAmr(count);
        filename = diagram.getFileName();
    }

    public void nextAmr(int count){
        diagram.nextAmr(count);
        filename = diagram.getFileName();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (selectedArea != null){
            g.drawRect(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
        }
    }

}
