package Amr.Annotation;

import AnnotatedSentence.*;
import MorphologicalAnalysis.MorphologicalTag;
import PropBank.ArgumentList;
import WordNet.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AmrSentence extends AnnotatedSentence {

    private String withTabs(int tabCount, String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tabCount; i++) {
            result.append("\t");
        }
        return result + string;
    }

    private String onlyWord(AnnotatedWord word, int i) {
        return (i + 1) + "/" + word.getParse().getWord().getName();
    }

    private boolean containsArg0(String semantic){
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getArgumentList() != null && word.getArgumentList().containsArgument("ARG0", semantic)) {
                return true;
            }
        }
        return false;
    }

    private void extraArgs(ArrayList<String> output, AnnotatedWord word, int tabCount) {
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A1SG) && !toStems().contains("ben ")) {
            output.add(withTabs(tabCount + 1, "ben:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P1SG)) {
            output.add(withTabs(tabCount + 1, "ben:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A1PL) && !toStems().contains("biz ")) {
            output.add(withTabs(tabCount + 1, "biz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P1PL)) {
            output.add(withTabs(tabCount + 1, "biz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A2SG) && !toStems().contains("sen ")) {
            output.add(withTabs(tabCount + 1, "sen:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P2SG)) {
            output.add(withTabs(tabCount + 1, "sen:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A2PL) && !toStems().contains("siz ")) {
            output.add(withTabs(tabCount + 1, "siz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P2PL)) {
            output.add(withTabs(tabCount + 1, "siz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A3SG) && !toStems().contains("o ")) {
            if (!containsArg0(word.getSemantic())){
                if (!(word.getParse().getPos().equals("NOUN") && (word.getParse().containsTag(MorphologicalTag.P1SG) || word.getParse().containsTag(MorphologicalTag.P1PL) || word.getParse().containsTag(MorphologicalTag.P2SG) || word.getParse().containsTag(MorphologicalTag.P2PL)))) {
                    output.add(withTabs(tabCount + 1, "o:ARG0"));
                }
            }
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A3PL) && !toStems().contains("onlar ")) {
            if (!containsArg0(word.getSemantic())){
                if (!(word.getParse().getPos().equals("NOUN") && (word.getParse().containsTag(MorphologicalTag.P1SG) || word.getParse().containsTag(MorphologicalTag.P1PL) || word.getParse().containsTag(MorphologicalTag.P2SG) || word.getParse().containsTag(MorphologicalTag.P2PL)))) {
                    output.add(withTabs(tabCount + 1, "onlar:ARG0"));
                }
            }
        }
    }

    private boolean containsMod(int index){
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                if (word.getUniversalDependency().toString().equals("AMOD") || word.getUniversalDependency().toString().equals("NMOD")){
                    return true;
                }
            }
        }
        return false;
    }

    private void extraPossessive(ArrayList<String> output, AnnotatedWord word, int wordIndex, int tabCount) {
        if (word.getParse().containsTag(MorphologicalTag.P1SG)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")){
                output.add(withTabs(tabCount + 1, "ben:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P1PL)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")){
                output.add(withTabs(tabCount + 1, "biz:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P2SG)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")) {
                output.add(withTabs(tabCount + 1, "sen:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P2PL)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")) {
                output.add(withTabs(tabCount + 1, "siz:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P3SG)) {
            if (!containsMod(wordIndex)){
                output.add(withTabs(tabCount + 1, "o:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P3PL)) {
            if (!containsMod(wordIndex)){
                output.add(withTabs(tabCount + 1, "onlar:poss"));
            }
        }
    }

    private boolean isMonth(String next) {
        return new ArrayList<>(Arrays.asList("ocak", "şubat", "mart", "nisan", "mayıs", "haziran", "temmuz", "ağustos",
                "eylül", "ekim", "kasım", "aralık")).contains(next);
    }

    private boolean isWeekDay(String next) {
        return new ArrayList<>(Arrays.asList("pazartesi", "salı", "çarşamba", "perşembe", "cuma", "cumartesi", "pazar")).contains(next);
    }

    private int isOrdinal(String next) {
        switch (next) {
            case "birinci":
                return 1;
            case "ikinci":
                return 2;
            case "üçüncü":
                return 3;
            case "dördüncü":
                return 4;
            case "beşinci":
                return 5;
            case "altıncı":
                return 6;
            case "yedinci":
                return 7;
            case "sekizinci":
                return 8;
            case "dokuzuncu":
                return 9;
        }
        return 0;
    }

    private boolean addArgumentList(ArrayList<String> output, AnnotatedWord current, String semantic, String currentText){
        if (current.getArgumentList() != null) {
            ArgumentList argumentList = current.getArgumentList();
            if (argumentList.containsArgument("ARG0", semantic)) {
                output.add(currentText + ":ARG0");
                return true;
            } else {
                if (argumentList.containsArgument("ARG1", semantic)) {
                    output.add(currentText + ":ARG1");
                    return true;
                } else {
                    if (argumentList.containsArgument("ARG2", semantic)) {
                        output.add(currentText + ":ARG2");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addDetails(int tabCount, ArrayList<String> output, AnnotatedWord current, int wordIndex) {
        if (current.getParse().containsTag(MorphologicalTag.NEGATIVE)) {
            output.add(withTabs(tabCount + 1, "-:polarity"));
        }
        extraArgs(output, current, tabCount);
        extraPossessive(output, current, wordIndex, tabCount);
        if (current.getParse().containsTag(MorphologicalTag.IMPERATIVE)) {
            output.add(withTabs(tabCount + 1, "imperative:mode"));
        }
    }

    private void printAmrRecursively(boolean[] done, int index, int tabCount, ArrayList<String> output, String relation, String semantic, WordNet wordNet, String extraAdded) {
        int currentWordIndex = index;
        if (done[index]){
            return;
        }
        done[index] = true;
        AnnotatedWord current = (AnnotatedWord) getWord(index);
        if (relation.equals("DET") && current.getParse().getWord().getName().equals("bir")){
            return;
        }
        if (new ArrayList<>(Arrays.asList("ve", "veya", "hem", "ama")).contains(current.getParse().getWord().getName())){
            return;
        }
        if (current.getParse().getWord().getName().equals("değil")) {
            output.add(withTabs(tabCount, "-:polarity"));
            return;
        }
        if (current.isPunctuation()){
            return;
        }
        String added = "";
        int addedIndex = -1;
        if (current.getParse().containsTag(MorphologicalTag.CONDITIONAL)){
            added = ":cond";
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                switch (word.getParse().getWord().getName()){
                    case "kadar":
                        added = ":extent";
                        addedIndex = i;
                        break;
                    case "rağmen":
                    case "karşın":
                    case "karşılık":
                        added = ":concession";
                        addedIndex = i;
                        break;
                    case "için":
                    case "sayesinde":
                    case "dolayı":
                        added = ":cause";
                        addedIndex = i;
                        break;
                }
            }
        }
        if (current.getParse().isCardinal() && index + 1 < wordCount()) {
            String next = ((AnnotatedWord) getWord(index + 1)).getParse().getWord().getName();
            if (isMonth(next)) {
                output.add(withTabs(tabCount, "date-entity:date"));
                output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":day"));
                output.add(withTabs(tabCount + 1, onlyWord(((AnnotatedWord) getWord(index + 1)), index + 1) + ":month"));
                done[index + 1] = true;
            } else {
                if (!extraAdded.isEmpty()){
                    output.add(withTabs(tabCount, onlyWord(current, index) + extraAdded));
                } else {
                    output.add(withTabs(tabCount, onlyWord(current, index) + added));
                    if (addedIndex != -1){
                        done[addedIndex] = true;
                    }
                }
            }
        } else {
            if (current.getParse().isProperNoun()) {
                String wikiType = "person";
                ArrayList<SynSet> synSets = wordNet.getSynSetsWithLiteral(current.getParse().getWord().getName());
                for (SynSet synSet : synSets) {
                    if (synSet.containsRelation(new SemanticRelation("TUR10-0820020", "INSTANCE_HYPERNYM"))){
                        wikiType = "city";
                    }
                }
                boolean argumentAdded = addArgumentList(output, current, semantic, withTabs(tabCount, wikiType));
                if (!argumentAdded) {
                    if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.INSTRUMENTAL)) {
                        output.add(withTabs(tabCount, wikiType) + ":instrument");
                    } else {
                        if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.LOCATIVE)) {
                            output.add(withTabs(tabCount, wikiType) + ":location");
                        } else {
                            if (!extraAdded.isEmpty()){
                                output.add(withTabs(tabCount, wikiType) + extraAdded);
                            } else {
                                output.add(withTabs(tabCount, wikiType) + added);
                                if (addedIndex != -1){
                                    done[addedIndex] = true;
                                }
                            }
                        }
                    }
                }
                output.add(withTabs(tabCount + 1, "name:name"));
                output.add(withTabs(tabCount + 2, onlyWord(current, index) + ":op1"));
                for (int i = 1; i <= 3; i++){
                    if (index + i < wordCount() && ((AnnotatedWord) getWord(index + i)).getSemantic() != null && ((AnnotatedWord) getWord(index + i)).getSemantic().equals(current.getSemantic())) {
                        output.add(withTabs(tabCount + 2, onlyWord((AnnotatedWord) getWord(index + i), index + 1) + ":op" + (1 + i)));
                        done[index + i] = true;
                    } else {
                        break;
                    }
                }
                if (wikiType.equals("person")) {
                    output.add(withTabs(tabCount + 1, "-:wiki"));
                } else {
                    output.add(withTabs(tabCount + 1, current.getParse().getWord().getName() + ":wiki"));
                }
            } else {
                if (isMonth(current.getParse().getWord().getName())) {
                    output.add(withTabs(tabCount, "date-entity:date"));
                    output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":month"));
                } else {
                    if (isWeekDay(current.getParse().getWord().getName())) {
                        output.add(withTabs(tabCount, "date-entity:date"));
                        output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":weekday"));
                    } else {
                        String currentWord = onlyWord(current, index);
                        for (int i = 1; i <= 3; i++) {
                            if (index > i - 1 && !done[index - i] && index - i < wordCount() && ((AnnotatedWord) getWord(index - i)).getSemantic() != null && ((AnnotatedWord) getWord(index - i)).getSemantic().equals(current.getSemantic())) {
                                currentWord = onlyWord((AnnotatedWord) getWord(index - i), index - i) + " " + currentWord;
                                done[index - i] = true;
                            } else {
                                break;
                            }
                        }
                        for (int i = 1; i <= 3; i++){
                            if (index + i < wordCount() && ((AnnotatedWord) getWord(index + i)).getSemantic() != null && ((AnnotatedWord) getWord(index + i)).getSemantic().equals(current.getSemantic())) {
                                currentWord += " " + onlyWord((AnnotatedWord) getWord(index + i), index + i);
                                done[index + i] = true;
                                current = (AnnotatedWord) getWord(index + i);
                                currentWordIndex = index + i;
                            } else {
                                break;
                            }
                        }
                        if (new ArrayList<>(Arrays.asList("çok", "gayet", "tam", "bayağı", "fazla", "hiç")).contains(current.getParse().getWord().getName())) {
                            output.add(withTabs(tabCount, currentWord) + ":degree");
                        } else {
                            if (current.getParse().getWord().getName().equals("hep") || current.getParse().getWord().getName().equals("sürekli")){
                                output.add(withTabs(tabCount, currentWord) + ":frequency");
                            } else {
                                boolean argumentAdded = addArgumentList(output, current, semantic, withTabs(tabCount, currentWord));
                                if (argumentAdded) {
                                    addDetails(tabCount, output, current, currentWordIndex);
                                } else {
                                    if (current.getParse().containsTag(MorphologicalTag.ORDINAL) || isOrdinal(current.getParse().getWord().getName()) > 0) {
                                        output.add(withTabs(tabCount, "ordinal-entity:ord"));
                                        int value = isOrdinal(current.getParse().getWord().getName());
                                        if (value > 0) {
                                            output.add(withTabs(tabCount + 1, value + ":value"));
                                        } else {
                                            output.add(withTabs(tabCount + 1, current.getParse().getWord().getName() + ":value"));
                                        }
                                    } else {
                                        if (new ArrayList<>(Arrays.asList("AMOD", "NMOD")).contains(relation)) {
                                            output.add(withTabs(tabCount, currentWord) + ":mod");
                                        } else {
                                            if (relation.equals("NUMMOD")) {
                                                output.add(withTabs(tabCount, currentWord) + ":quant");
                                            } else {
                                                if (relation.equals("ADVMOD")) {
                                                    output.add(withTabs(tabCount, currentWord) + ":manner");
                                                } else {
                                                    if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.INSTRUMENTAL)) {
                                                        output.add(withTabs(tabCount, currentWord) + ":instrument");
                                                    } else {
                                                        if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.LOCATIVE)) {
                                                            output.add(withTabs(tabCount, currentWord) + ":location");
                                                        } else {
                                                            if (!extraAdded.isEmpty()){
                                                                output.add(withTabs(tabCount, currentWord) + extraAdded);
                                                            } else {
                                                                output.add(withTabs(tabCount, currentWord) + added);
                                                                if (addedIndex != -1){
                                                                    done[addedIndex] = true;
                                                                }
                                                            }
                                                            addDetails(tabCount, output, current, currentWordIndex);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getParse().isCardinal() && i + 1 < wordCount()) {
                String next = ((AnnotatedWord) getWord(i + 1)).getParse().getWord().getName();
                if (isMonth(next)) {
                    if (((AnnotatedWord) getWord(i + 1)).getUniversalDependency().to() == index + 1) {
                        metaVerbTags(word, done, i, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
                    }
                    i++;
                    continue;
                }
            }
            int j = i;
            while (i < wordCount() - 1 && ((AnnotatedWord) getWord(i + 1)).getSemantic() != null && ((AnnotatedWord) getWord(i + 1)).getSemantic().equals(word.getSemantic())) {
                i++;
            }
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                metaVerbTags(word, done, j, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
            } else {
                if (j != i && ((AnnotatedWord) getWord(i)).getUniversalDependency() != null && ((AnnotatedWord) getWord(i)).getUniversalDependency().to() == index + 1) {
                    metaVerbTags(word, done, j, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
                }
            }
        }
    }

    private void metaVerbTags(AnnotatedWord word, boolean[] done, int index, int tabCount, ArrayList<String> output, String relation, String semantic, WordNet wordNet) {
        boolean parataxisOrConj = false;
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord connectedWord = (AnnotatedWord) getWord(i);
            if (connectedWord.getUniversalDependency() != null && (connectedWord.getUniversalDependency().toString().equals("PARATAXIS") || connectedWord.getUniversalDependency().toString().equals("CONJ")) && connectedWord.getUniversalDependency().to() == index + 1) {
                parataxisOrConj = true;
                break;
            }
        }
        if (parataxisOrConj) {
            output.add(withTabs(tabCount, "and"));
            int count = 1;
            for (int i = 0; i < wordCount(); i++) {
                AnnotatedWord connectedWord = (AnnotatedWord) getWord(i);
                if (connectedWord.getUniversalDependency() != null && (connectedWord.getUniversalDependency().toString().equals("PARATAXIS") || connectedWord.getUniversalDependency().toString().equals("CONJ")) && connectedWord.getUniversalDependency().to() == index + 1) {
                    printAmrRecursively(done, i, tabCount + 1, output, relation, semantic, wordNet, ":op" + count);
                    count++;
                }
            }
            printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, ":op" + count);
        } else {
            if (word.getParse().containsTag(MorphologicalTag.ABLE)) {
                output.add(withTabs(tabCount, "mümkün"));
                printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
            } else {
                if (word.getParse().containsTag(MorphologicalTag.CAUSATIVE)){
                    output.add(withTabs(tabCount, "yap"));
                    printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
                } else {
                    if (word.getParse().containsTag(MorphologicalTag.NECESSITY)){
                        output.add(withTabs(tabCount, "öner"));
                        printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
                    } else {
                        printAmrRecursively(done, index, tabCount, output, relation, semantic, wordNet, "");
                    }
                }
            }
        }
    }

    public ArrayList<String> constructAmr(WordNet wordNet) {
        ArrayList<String> output = new ArrayList<>();
        boolean[] done = new boolean[wordCount()];
        output.add(getFileName() + "\t" + toWords());
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("ROOT")) {
                metaVerbTags(word, done, i, 0, output, "ROOT", word.getSemantic(), wordNet);
            }
        }
        return output;
    }
}
