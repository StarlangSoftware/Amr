package Amr.Annotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class AmrPanel extends DiagramPanel {

    public AmrPanel(String path, String fileName) {
        super();
        diagram = new AmrDiagram(path, fileName);
        this.filename = diagram.getFileName();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e) {
        AmrObject current;
        super.mousePressed(e);
        fromObject = null;
        if (lastCommand != null && lastCommand == EnumCommand.CONNECTION) {
            current = diagram.getNearestAmrObjectAtPos(e.getPoint());
            if (current != null) {
                if (current instanceof AmrWordObject) {
                    fromObject = current;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        AmrObject toObject;
        super.mouseReleased(e);
        dragged = false;
        moved = false;
        if (lastCommand != null && lastCommand == EnumCommand.CONNECTION && fromObject != null) {
            toObject = diagram.getNearestAmrObjectAtPos(e.getPoint());
            if (toObject instanceof AmrWordObject && !fromObject.equals(toObject)) {
                String with = JOptionPane.showInputDialog(null, "Enter Connection Name", "");
                undoList.add(diagram.clone());
                diagram.addConnection((AmrWordObject)fromObject, (AmrWordObject)toObject, with);
                save();
                this.repaint();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (lastCommand == EnumCommand.EMPTY && e.getClickCount() == 2) {
            AmrObject current = diagram.getNearestAmrObjectAtPos(e.getPoint());
            if (current instanceof AmrWordObject) {
                String word = JOptionPane.showInputDialog(null, "Enter Word Name", ((AmrWordObject) current).getName());
                if (word != null) {
                    undoList.add(diagram.clone());
                    ((AmrWordObject) current).setName(word);
                    save();
                    this.repaint();
                }
            }
            if (current instanceof AmrConnectionObject) {
                String with = JOptionPane.showInputDialog(null, "Enter Connection Name", ((AmrConnectionObject) current).with());
                if (with != null) {
                    undoList.add(diagram.clone());
                    ((AmrConnectionObject) current).setWith(with);
                    save();
                    this.repaint();
                }
            }
        }
        if (lastCommand != null && lastCommand != EnumCommand.EMPTY){
            if (lastCommand == EnumCommand.WORD) {
                String wordName = JOptionPane.showInputDialog(null, "Enter Use Case Name", "UML Editor", JOptionPane.QUESTION_MESSAGE);
                if (wordName != null) {
                    undoList.add(diagram.clone());
                    diagram.addWord(wordName, e.getPoint());
                    save();
                    this.repaint();
                }
            }
        }

    }

    public void save() {
        diagram.save();
    }

    public void undo() {
        if (!undoList.isEmpty()) {
            diagram = undoList.remove(undoList.size() - 1);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        diagram.paint(g);
    }

}
