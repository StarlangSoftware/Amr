package Amr.Annotation;

import Amr.Corpus.AmrConnection;
import Amr.Corpus.AmrSentence;
import Amr.Corpus.AmrWord;
import Corpus.FileDescription;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AmrDiagram {

    protected ArrayList<AmrObject> objects;
    private final AmrSentence sentence;

    public AmrDiagram(String path, String fileName) {
        sentence = new AmrSentence(path, fileName);
        constructObjects();
    }

    public AmrDiagram(FileDescription fileDescription) {
        sentence = new AmrSentence(fileDescription);
        constructObjects();
    }

    public AmrDiagram clone() {
        int i;
        AmrDiagram newDiagram = new AmrDiagram(sentence.getFileDescription());
        for (i = 0; i < objects.size(); i++) {
            newDiagram.objects.add(objects.get(i).clone());
        }
        return newDiagram;
    }

    public void constructObjects(){
        HashMap<String, AmrWordObject> wordObjects = new HashMap<>();
        objects = new ArrayList<>();
        for (int i = 0; i < sentence.wordCount(); i++){
            AmrWord word = (AmrWord) sentence.getWord(i);
            AmrWordObject amrWordObject = new AmrWordObject(word);
            objects.add(amrWordObject);
            wordObjects.put(word.getName(), amrWordObject);
        }
        for (int i = 0; i < sentence.connectionCount(); i++){
            AmrConnection connection = sentence.getConnection(i);
            objects.add(new AmrConnectionObject(wordObjects.get(connection.from().getName()), wordObjects.get(connection.to().getName())));
        }
    }

    public String getFileName(){
        return sentence.getFileName();
    }

    public String getFolder(){
        return sentence.getFolder();
    }

    public void addWord(String name, Point position) {
        objects.add(new AmrWordObject(name, position));
        sentence.addWord(new AmrWord(name, position));
    }

    public void addConnection(AmrWordObject from, AmrWordObject to) {
        objects.add(new AmrConnectionObject(from, to));
        sentence.addConnection(from.word, to.word);
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
        ArrayList<AmrObject> result = new ArrayList<>();
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
            objects.get(i).select(area.contains(objects.get(i).getCenter()));
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
        sentence.previousSentence(count);
        constructObjects();
    }

    public void nextAmr(int count){
        sentence.nextSentence(count);
        constructObjects();
    }

}
