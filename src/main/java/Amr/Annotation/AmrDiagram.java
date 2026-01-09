package Amr.Annotation;

import Corpus.FileDescription;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AmrDiagram {

    protected ArrayList<AmrObject> objects;
    private final FileDescription fileDescription;

    public AmrDiagram(String path, String fileName) {
        fileDescription = new FileDescription(path, fileName);
        objects = new ArrayList<AmrObject>();
    }

    public AmrDiagram clone() {
        int i;
        AmrDiagram newDiagram = new AmrDiagram(fileDescription.getPath(), fileDescription.getRawFileName());
        for (i = 0; i < objects.size(); i++) {
            newDiagram.objects.add(objects.get(i).clone());
        }
        return newDiagram;
    }

    public void addWord(String name, Point position) {
        objects.add(new AmrWordObject(name, position));
    }

    public void addConnection(AmrObject from, AmrObject to) {
        objects.add(new AmrConnection((AmrSimpleObject) from, (AmrSimpleObject) to));
    }

    public void save(String filename) {
        int i;
        FileWriter outfile;
        try {
            outfile = new FileWriter(filename);
            outfile.write("<Amr>\n");
            for (i = 0; i < objects.size(); i++) {
                objects.get(i).save(outfile);
            }
            outfile.write("</Amr>\n");
            outfile.close();
        } catch (IOException ioException) {
            System.out.println("Output file can not be opened");
        }
    }

    public AmrObject getAmrObjectAtPos(Point p) {
        int i;
        for (i = 0; i < objects.size(); i++) {
            if (objects.get(i).contains(p)) {
                return objects.get(i);
            }
        }
        return null;
    }

    public AmrObject getObject(String name) {
        int i;
        for (i = 0; i < objects.size(); i++) {
            if (objects.get(i).getClass().getName().equalsIgnoreCase("Amr.Annotation.AmrWordObject")) {
                if (((AmrSimpleObject) objects.get(i)).getName().equalsIgnoreCase(name)) {
                    return objects.get(i);
                }
            }
        }
        return null;
    }

    private void reload(){
        objects.clear();
        DOMParser parser = new DOMParser();
        Document doc;
        try {
            parser.parse(fileDescription.getFileName());
        } catch (SAXException | IOException ignored) {
        }
        doc = parser.getDocument();
        loadFromXml(doc.getFirstChild());
    }

    public void loadFromXml(Node rootNode) {
        Node objectNode;
        NamedNodeMap attributes;
        String objectName;
        AmrObject from, to;
        objectNode = rootNode.getFirstChild();
        while (objectNode != null) {
            if (objectNode.hasAttributes()) {
                objectName = objectNode.getNodeName();
                attributes = objectNode.getAttributes();
                if (objectName.equalsIgnoreCase("Word")) {
                    addWord(attributes.getNamedItem("name").getNodeValue(), new Point(Integer.parseInt(attributes.getNamedItem("positionX").getNodeValue()), Integer.parseInt(attributes.getNamedItem("positionY").getNodeValue())));
                } else {
                    if (objectName.equalsIgnoreCase("Connection")) {
                        from = getObject(attributes.getNamedItem("from").getNodeValue());
                        to = getObject(attributes.getNamedItem("to").getNodeValue());
                        addConnection(from, to);
                    }
                }

            }
            objectNode = objectNode.getNextSibling();
        }
    }

    public void paint(Graphics graphics) {
        int i;
        for (i = 0; i < objects.size(); i++) {
            objects.get(i).paint(graphics);
        }
    }

    public void selectAll() {
        int i;
        for (i = 0; i < objects.size(); i++) {
            objects.get(i).select(true);
        }
    }

    public ArrayList<AmrObject> copyAll() {
        int i;
        ArrayList<AmrObject> result = new ArrayList<AmrObject>();
        for (i = 0; i < objects.size(); i++) {
            if (objects.get(i).isSelected()) {
                result.add(objects.get(i).clone());
            }
        }
        return result;
    }

    public void moveSelected(int deltaX, int deltaY) {
        int i;
        for (i = 0; i < objects.size(); i++) {
            if (objects.get(i).isSelected()) {
                objects.get(i).move(deltaX, deltaY);
            }
        }
    }

    public void selectArea(Rectangle area) {
        int i;
        for (i = 0; i < objects.size(); i++) {
            if (area.contains(objects.get(i).getCenter())) {
                objects.get(i).select(true);
            } else {
                objects.get(i).select(false);
            }
        }
    }

    public void pasteObjects(ArrayList<AmrObject> copyList) {
        int i;
        for (i = 0; i < copyList.size(); i++) {
            objects.add(copyList.get(i));
        }
    }

    public void deselectAll() {
        int i;
        for (i = 0; i < objects.size(); i++) {
            objects.get(i).select(false);
        }
    }

    public void deleteSelected() {
        int i;
        for (i = 0; i < objects.size(); i++) {
            if (objects.get(i).isSelected()) {
                objects.remove(i);
                i--;
            }
        }
    }

    public void previousAmr(int count){
        if (fileDescription.previousFileExists(count)){
            fileDescription.addToIndex(-count);
            reload();
        }
    }

    public void nextAmr(int count){
        if (fileDescription.nextFileExists(count)){
            fileDescription.addToIndex(count);
            reload();
        }
    }

}
