package Amr.Construction;

import AnnotatedSentence.AnnotatedSentence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public abstract class AmrConstructionAlgorithm {

    abstract ArrayList<String> constructExcelAmr(AnnotatedSentence sentence);

    public void saveAmr(AnnotatedSentence sentence, String fileName){
        ArrayList<String> items = constructExcelAmr(sentence);
        items.remove(0);
        if (items.size() > 0){
            saveAmr(items, fileName);
        }
    }

    public void saveAmr(ArrayList<String> items, String fileName){
        int startX = 750, startY = 100;
        String[] lastParent = new String[50];
        int[] lastX = new int[50];
        int[] childCount = new int[50];
        HashSet<String> words = new HashSet<>();
        try {
            PrintWriter output = new PrintWriter(fileName);
            output.println("<Amr>");
            String line = items.get(0);
            output.println("<Word name=\"" + line + "\" positionX=\"" + startX + "\" positionY=\"" + startY + "\"/>");
            words.add(line);
            lastParent[0] = line;
            childCount[0] = 0;
            lastX[0] = startX;
            for (int j = 1; j < items.size(); j++){
                line = items.get(j);
                int tabCount = 0, i = 0;
                while (line.charAt(i) == '\t'){
                    tabCount++;
                    i++;
                }
                line = line.substring(tabCount);
                if (line.contains(":")){
                    lastParent[tabCount] = line.substring(0, line.lastIndexOf(":"));
                } else {
                    lastParent[tabCount] = line;
                }
                childCount[tabCount] = 0;
                lastX[tabCount] = lastX[tabCount - 1] + (childCount[tabCount - 1] - 1) * 100;
                if (line.contains(":")){
                    if (!words.contains(line.substring(0, line.lastIndexOf(":")))){
                        output.println("<Word name=\"" + line.substring(0, line.lastIndexOf(":")) + "\" positionX=\"" + lastX[tabCount] + "\" positionY=\"" + (startY + 100 * tabCount) + "\"/>");
                        words.add(line.substring(0, line.lastIndexOf(":")));
                    }
                    output.println("<Connection from=\"" + lastParent[tabCount - 1] + "\" to=\"" + line.substring(0, line.lastIndexOf(":")) + "\" with=\"" + line.substring(line.lastIndexOf(":") + 1) + "\"/>");
                } else {
                    if (!words.contains(line)){
                        output.println("<Word name=\"" + line + "\" positionX=\"" + lastX[tabCount] + "\" positionY=\"" + (startY + 100 * tabCount) + "\"/>");
                        words.add(line);
                    }
                    output.println("<Connection from=\"" + lastParent[tabCount - 1] + "\" to=\"" + line + "\"/>");
                }
                childCount[tabCount - 1]++;
            }
            output.println("</Amr>");
            output.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAmrOutputFileName(String line){
        String folder = ".";
        if (line.contains("/")){
            folder = line.substring(0, line.indexOf("/"));
            File file = new File("./" + folder);
            if (!file.exists()){
                file.mkdir();
            }
            line = line.substring(line.indexOf("/") + 1);
        }
        return folder + "/" + line;
    }

    public void readAndSaveMultipleAmr(String fileName) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        String outputFileName = null;
        boolean firstLine = false, skipFile = false;
        Scanner input = new Scanner(new File(fileName));
        while (input.hasNext()){
            String line = input.nextLine();
            if (line.matches(".*[0-9]+\\.(train|test|dev)")){
                firstLine = true;
                skipFile = false;
                outputFileName = getAmrOutputFileName(line);
                lines = new ArrayList<>();
            } else {
                if (line.isEmpty()){
                    if (!skipFile){
                        saveAmr(lines, outputFileName);
                    }
                } else {
                    if (firstLine){
                        if (line.startsWith("\t")){
                            skipFile = true;
                        } else {
                            lines.add(line);
                        }
                        firstLine = false;
                    } else {
                        if (!skipFile){
                            lines.add(line);
                        }
                    }
                }
            }
        }
        saveAmr(lines, outputFileName);
    }

}
