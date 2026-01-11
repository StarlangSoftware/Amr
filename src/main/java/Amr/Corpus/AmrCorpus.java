package Amr.Corpus;

import Corpus.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AmrCorpus extends Corpus{
    /**
     * Empty constructor for {@link AmrCorpus}.
     */
    public AmrCorpus(){
    }

    /**
     * A constructor of {@link AmrCorpus} class which reads all {@link AmrSentence} files with the file
     * name satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates an AmrSentence and puts in inside the list sentences.
     * @param folder Folder where all sentences reside.
     * @param pattern File pattern such as "." ".train" ".test".
     */
    public AmrCorpus(File folder, String pattern){
        sentences = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                String fileName = file.getName();
                if (!fileName.contains(pattern))
                    continue;
                AmrSentence sentence = new AmrSentence(folder.getAbsolutePath(), file.getName());
                sentences.add(sentence);
            }
        }
    }

    /**
     * A constructor of {@link AmrCorpus} class which reads all {@link AmrSentence} files with the file
     * name satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates an AmrSentence and puts in inside the list sentences.
     * @param folder Folder where all sentences reside.
     * @param pattern File pattern such as "." ".train" ".test".
     * @param from Index (such as 23 or 100) from which the files are read.
     * @param to Index (such as 23 or 100) to which the files are read.
     */
    public AmrCorpus(File folder, String pattern, int from, int to){
        sentences = new ArrayList<>();
        for (int i = from; i <= to; i++){
            sentences.add(new AmrSentence(folder.getAbsolutePath(), String.format("%04d", i) + pattern));
        }
    }

    /**
     * A constructor of {@link AmrCorpus} class which reads all {@link AmrSentence} files inside the given
     * folder. For each file inside that folder, the constructor creates an AmrSentence and puts in inside the
     * list sentences.
     * @param folder Folder to load annotated corpus
     */
    public AmrCorpus(File folder){
        sentences = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                if (!file.isHidden()){
                    AmrSentence sentence = new AmrSentence(folder.getAbsolutePath(), file.getName());
                    sentences.add(sentence);
                }
            }
        }
    }

}
