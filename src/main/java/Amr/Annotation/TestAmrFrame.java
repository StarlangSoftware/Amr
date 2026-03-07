package Amr.Annotation;

import Amr.Corpus.AmrCorpus;
import Amr.Corpus.AmrSentence;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestAmrFrame {

    private void gbDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        AmrCorpus corpus = new AmrCorpus(new File("../../Gb/Amr/"));
        for (int i = 0; i < corpus.sentenceCount(); i++) {
            AmrDiagram diagram = new AmrDiagram("../../Gb/Amr/", ((AmrSentence) corpus.getSentence(i)).getRawFileName());
            Rectangle box = diagram.getBoundingBox();
            output.print("<p>");
            output.print(((AmrSentence) corpus.getSentence(i)).getRawFileName());
            output.println("</p>");
            output.println(diagram.toSvg(box.y + box.height, box.x + box.width));
        }
        output.close();
    }

    private void tourismDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        AmrCorpus corpus = new AmrCorpus(new File("../../Etstur/Amr/"));
        for (int i = 0; i < corpus.sentenceCount(); i++) {
            AmrDiagram diagram = new AmrDiagram("../../Etstur/Amr/", ((AmrSentence) corpus.getSentence(i)).getRawFileName());
            Rectangle box = diagram.getBoundingBox();
            output.print("<p>");
            output.print(((AmrSentence) corpus.getSentence(i)).getRawFileName());
            output.println("</p>");
            output.println(diagram.toSvg(box.y + box.height, box.x + box.width));
        }
        output.close();
    }

    private void framenetDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        File[] listOfFiles = new File("../../FrameNet-Examples/Amr/").listFiles();
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                String fileName = file.getName();
                AmrCorpus corpus = new AmrCorpus(new File("../../FrameNet-Examples/Amr/" + fileName));
                output.println("<h2>" + fileName + "</h2>");
                for (int i = 0; i < corpus.sentenceCount(); i++) {
                    AmrDiagram diagram = new AmrDiagram("../../FrameNet-Examples/Amr/" + fileName, ((AmrSentence) corpus.getSentence(i)).getRawFileName());
                    Rectangle box = diagram.getBoundingBox();
                    output.print("<p>");
                    output.print(((AmrSentence) corpus.getSentence(i)).getRawFileName());
                    output.println("</p>");
                    output.println(diagram.toSvg(box.y + box.height, box.x + box.width));
                }
            }
        }
        output.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        AmrFrame amrFrame = new AmrFrame();
    }

}
