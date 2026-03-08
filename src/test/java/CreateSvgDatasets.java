import Amr.Annotation.AmrDiagram;
import Amr.Corpus.AmrCorpus;
import Amr.Corpus.AmrSentence;
import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.AnnotatedSentence;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CreateSvgDatasets {

    private void outputDataSet(AmrCorpus corpus, AnnotatedCorpus corpus2, PrintWriter output, String path){
        int j = 0;
        for (int i = 0; i < corpus.sentenceCount(); i++) {
            AmrDiagram diagram = new AmrDiagram(path, ((AmrSentence) corpus.getSentence(i)).getRawFileName());
            while (j < corpus2.sentenceCount() && !((AnnotatedSentence)corpus2.getSentence(j)).getFileName().equals(((AmrSentence) corpus.getSentence(i)).getRawFileName())){
                j++;
            }
            Rectangle box = diagram.getBoundingBox();
            output.print("<h3>");
            output.println(((AmrSentence) corpus.getSentence(i)).getRawFileName());
            output.println("</h3>");
            output.print("<h4>");
            output.println(((AnnotatedSentence) corpus2.getSentence(j)).toWords());
            output.println("</h4>");
            output.println(diagram.toSvg(box.y + box.height, box.x + box.width));
        }
    }

    public void gbDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        AmrCorpus corpus = new AmrCorpus(new File("../../Gb/Amr/"));
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Gb/Turkish-Phrase/"));
        outputDataSet(corpus, corpus2, output, "../../Gb/Amr/");
        output.close();
    }

    public void tourismDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        AmrCorpus corpus = new AmrCorpus(new File("../../Etstur/Amr/"));
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        outputDataSet(corpus, corpus2, output, "../../Etstur/Amr/");
        output.close();
    }

    public void framenetDataset() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        File[] listOfFiles = new File("../../FrameNet-Examples/Amr/").listFiles();
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                String fileName = file.getName();
                AmrCorpus corpus = new AmrCorpus(new File("../../FrameNet-Examples/Amr/" + fileName));
                AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName));
                output.println("<h2>" + fileName + "</h2>");
                outputDataSet(corpus, corpus2, output, "../../FrameNet-Examples/Amr/" + fileName);
            }
        }
        output.close();
    }


}
