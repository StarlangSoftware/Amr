package Amr.Annotation;

import Amr.Construction.RuleBasedConstructionAlgorithm;
import Amr.Corpus.AmrCorpus;
import Amr.Corpus.AmrSentence;
import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.AnnotatedSentence;
import WordNet.WordNet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestAmrFrame {

    public static void main(String[] args) throws FileNotFoundException {
        AmrFrame amrFrame = new AmrFrame();
        /*PrintWriter output;
        AmrCorpus corpus = new AmrCorpus(new File("../../Etstur/Amr/"));
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        WordNet turkish = new WordNet();
        int i = 0, j = 0;
        RuleBasedConstructionAlgorithm algorithm = new RuleBasedConstructionAlgorithm(turkish);
        while (i < corpus.sentenceCount() && j < corpus2.sentenceCount()){
            if (((AnnotatedSentence)corpus2.getSentence(j)).getFileName().compareTo(((AmrSentence) corpus.getSentence(i)).getRawFileName()) < 0){
                AnnotatedSentence sentence = (AnnotatedSentence)corpus2.getSentence(j);
                System.out.println(((AnnotatedSentence)corpus2.getSentence(j)).getFileName());
                algorithm.saveAmr(sentence, ((AnnotatedSentence)corpus2.getSentence(j)).getFileName());
                j++;
            } else {
                if (((AnnotatedSentence)corpus2.getSentence(j)).getFileName().compareTo(((AmrSentence) corpus.getSentence(i)).getRawFileName()) > 0){
                    i++;
                } else {
                    i++;
                    j++;
                }
            }
        }*/
    }

}
