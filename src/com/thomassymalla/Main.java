package com.thomassymalla;
import ca.ualberta.stothard.cgview.*;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;

import static ca.ualberta.stothard.cgview.CgviewConstants.*;
import java.io.*;
import java.util.*;


public class Main {

    public static void main( String args[] ) {
        File genebank = new File("Genome.gb");
        try {
            LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(genebank);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //try {
            //LinkedHashMap<String, DNASequence> dnaSequence = reader.readGenbankDNASequence(genebank);
        //} catch (Exception e) {
            //e.printStackTrace();
        //}


        //Code altered from https://paulstothard.github.io/cgview/api_overview.html
        int length = 2781;
        Cgview cgview = new Cgview(length);

        cgview.setWidth(600);
        cgview.setHeight(600);
        cgview.setBackboneRadius(160.0f);
        cgview.setLabelPlacementQuality(10);
        cgview.setLabelLineLength(8.0d);
        cgview.setLabelLineThickness(0.5f);

        cgview.setTitle("A");


        //create a FeatureSlot to hold sequence features
        FeatureSlot featureSlot = new FeatureSlot(cgview, DIRECT_STRAND);

        /*//create random sequence features
        for (int i = 1; i <= 100; i = i + 1) {

            int j = Math.round((float)((float)(length - 2) * Math.random())) + 1;

            //a Feature to add to our FeatureSlot
            Feature feature = new Feature(featureSlot, "label");

            //a single FeatureRange to add the Feature
            FeatureRange featureRange = new FeatureRange (feature, j, j + 1);
            featureRange.setDecoration(DECORATION_CLOCKWISE_ARROW);

        }*/

        try {
            CgviewIO.writeToPNGFile(cgview, "GenomeMap.png");
            CgviewIO.writeToSVGFile(cgview, "Genomemap.svg", false);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }





    /*
    DataNode fileData = ReadDataFromFile("Genome.gb");


    for (DataNode node : fileData.children) {
        System.out.println(node.GetName() + ":" + node.GetValue());
        if(node.children.size() > 0){
            for (DataNode node2 : node.children) {
                if(node2.IsSpecial())
                    System.out.print("     ");
                else
                    System.out.print("  ");
                System.out.println(node2.GetName() + ":" + node2.GetValue());
            }
        }
    }

    */



    //Reads .gb file and organizes data into node-based hierarchy.
    //A bit messy due to the inconsistent rules of a .gb file.
    private static DataNode ReadDataFromFile(String fileName) {
        DataNode headNode = new DataNode();
        headNode.SetToHead();
        final int spaceMax = 10; //The .gd file format is weird, since our files only have two layers (4 spaces) and
        //the data typically starts at 12 spaces, let's just put 10 as a divider.
        final int specialSpaces = 5;
        final int normalSpaces = 2;

        try {
            File genomeData = new File(fileName);
            Scanner genomeReader = new Scanner(genomeData);

            //This list keeps track of the path in the tree we are currently on.
            List<DataNode> nodeStack = new ArrayList<DataNode>(Collections.singletonList(headNode));
            String currentLine;
            String currentNodeData = null;
            boolean readingOrigin = false; //origin for whatever reason is just completely different than the rest of
            //the file, so we need a special cast for it.
            int spaceMarker = -1;

            while (genomeReader.hasNextLine()) {
                currentLine = genomeReader.nextLine(); //For each line...
                int spaceCount = SpacesBeforeFirstLetter(currentLine); //Get amount of spaces until data entry.
                char firstCharacter = currentLine.charAt(0);

                if(currentLine.equals("//")){ //Exit case.
                    break;
                }
                else if(readingOrigin){
                    DataNode newNode = new DataNode();
                    nodeStack.get(nodeStack.size()-1).children.add(newNode);
                    String firstWord = GetFirstWord(currentLine);
                    if(firstWord == null){
                        //Error!
                    }
                    newNode.SetName(firstWord);
                    newNode.SetValue(currentLine.substring(firstWord.length()+spaceCount));
                }
                else if(spaceMarker == -1){ //Special first entry case.
                    String firstWord = GetFirstWord(currentLine);
                    if(firstWord == null){
                        //Error!
                    }

                    DataNode newNode = new DataNode();
                    nodeStack.get(nodeStack.size()-1).children.add(newNode);
                    nodeStack.add(newNode);
                    newNode.SetName(firstWord);
                    currentNodeData = currentLine.substring(firstWord.length());
                    spaceMarker = 0;
                }
                else if (spaceCount < spaceMarker){ //Move backwards in the tree if the space is behind our current position.
                    if(currentNodeData == null){
                        //Error
                    }
                    nodeStack.get(nodeStack.size()-1).SetValue(currentNodeData);

                    while(true) {
                        if(nodeStack.get(nodeStack.size()-1).IsSpecial())
                            spaceMarker -= specialSpaces;
                        else
                            spaceMarker -= normalSpaces;
                        nodeStack.remove(nodeStack.size() - 1);
                        if(spaceMarker == spaceCount)
                            break;
                    }
                    nodeStack.remove(nodeStack.size()-1);

                    String firstWord = GetFirstWord(currentLine);
                    if(firstWord == null){
                        //Error!
                    }

                    DataNode newNode = new DataNode();
                    nodeStack.get(nodeStack.size()-1).children.add(newNode);
                    nodeStack.add(newNode);
                    newNode.SetName(firstWord);
                    if(!firstWord.equals("ORIGIN")) {
                        currentNodeData = currentLine.substring(firstWord.length()+spaceCount);
                    }
                    else{
                        readingOrigin = true;
                        newNode.SetValue("");
                    }
                }
                else if(spaceCount == spaceMarker) { //0 spaces indicates a new node on the same line
                    if(currentNodeData == null){
                        //Error
                    }
                    boolean special = nodeStack.get(nodeStack.size()-1).IsSpecial();
                    nodeStack.get(nodeStack.size()-1).SetValue(currentNodeData);
                    nodeStack.remove(nodeStack.size()-1);

                    String firstWord = GetFirstWord(currentLine);
                    if(firstWord == null){
                        //Error!
                    }

                    DataNode newNode = new DataNode();
                    if(special)
                        newNode.SetSpecial();
                    nodeStack.get(nodeStack.size()-1).children.add(newNode);
                    nodeStack.add(newNode);
                    newNode.SetName(firstWord);
                    currentNodeData = currentLine.substring(firstWord.length()+spaceCount);
                }
                else if(spaceCount == spaceMarker+normalSpaces || spaceCount == spaceMarker+specialSpaces) { //2 spaces indicates a new child node.
                    if(currentNodeData == null){
                        //Error
                    }
                    nodeStack.get(nodeStack.size()-1).SetValue(currentNodeData);

                    String firstWord = GetFirstWord(currentLine);
                    if(firstWord == null){
                        //Error!
                    }

                    DataNode newNode = new DataNode();
                    nodeStack.get(nodeStack.size()-1).children.add(newNode);
                    nodeStack.add(newNode);
                    newNode.SetName(firstWord);
                    if(spaceCount == spaceMarker+specialSpaces) {
                        newNode.SetSpecial();
                        spaceMarker += specialSpaces;
                    }
                    else{
                        spaceMarker += normalSpaces;
                    }
                    currentNodeData = currentLine.substring(firstWord.length() + spaceCount);

                }
                else if(spaceCount > spaceMax){
                    currentNodeData += "\n";
                    currentNodeData += currentLine;
                }

            }
            genomeReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return headNode;
    }

    //Returns the number of spaces before the first non-space character in the given string.
    //Returns -1 if there is only spaces or the string is empty.
    private static int SpacesBeforeFirstLetter(String input) {
        for(int i = 0;i < input.length();i++){
            if(input.charAt(i) != ' ')
                return i;
        }
        return -1;
    }

    //Returns the first word in the given input. Returns a null string if the string is empty or
    //all whitespace.
    private static String GetFirstWord(String input){
        for(int i = 0;i < input.length();i++){
            if(input.charAt(i) != ' ') {
                for(int j = i;j < input.length();j++){
                    if(input.charAt(j) == ' ') {
                        return input.substring(i, j);
                    }
                }
            }
        }
        return null;
    }
}
