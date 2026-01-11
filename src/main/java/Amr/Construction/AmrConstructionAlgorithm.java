package Amr.Construction;

import AnnotatedSentence.AnnotatedSentence;
import WordNet.WordNet;

import java.util.ArrayList;

public interface AmrConstructionAlgorithm {
    ArrayList<String> constructAmr(AnnotatedSentence sentence);
}
