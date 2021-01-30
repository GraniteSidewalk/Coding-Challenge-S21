package com.thomassymalla;
import ca.ualberta.stothard.cgview.*;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.location.template.AbstractLocation;
import org.biojava.nbio.core.sequence.location.template.Point;
import org.biojava.nbio.core.sequence.template.AbstractSequence;


import static ca.ualberta.stothard.cgview.CgviewConstants.*;
import java.io.*;
import java.util.*;


public class Main {
    //Code created using https://paulstothard.github.io/cgview/api_overview.html
    public static void main( String args[] ) throws IOException {
        BufferedReader genebankIn = null;
        try {
            genebankIn = new BufferedReader(new FileReader("Genome.gb"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        GenbankSequenceParser parser = new GenbankSequenceParser();
        String sequence = parser.getSequence(genebankIn, 0);
        int length = sequence.length();

        HashMap<String, ArrayList<AbstractFeature>> features = parser.getFeatures();
        Object[] keywords = features.keySet().toArray();

        /*for(int i = 0;i < features.size();i++){
            System.out.println(keywords[i]+":::");
            for(AbstractFeature abs : features.get(keywords[i])){
                System.out.println("  "+ abs.getSource());

                Object[] qualifiers = abs.getQualifiers().keySet().toArray();
                for(int j = 0;j < abs.getQualifiers().size();j++){
                    System.out.println("    "+ abs.getQualifiers().get(qualifiers[j]).toString());
                }
            }
        }*/

        Cgview cgview = new Cgview(length);
        cgview.setWidth(1000);
        cgview.setHeight(1000);
        cgview.setBackboneRadius(160.0f);
        cgview.setLabelPlacementQuality(10);
        cgview.setLabelLineLength(8.0d);
        cgview.setLabelLineThickness(0.5f);
        FeatureSlot directSlot = new FeatureSlot(cgview, DIRECT_STRAND);
        FeatureSlot reverseSlot = new FeatureSlot(cgview, CgviewConstants.REVERSE_STRAND);

        /*
        Feature featureA = new Feature(directSlot, "TESTA");
        FeatureRange featureRangeA = new FeatureRange (featureA, 200, 500);
        featureRangeA.setDecoration(DECORATION_CLOCKWISE_ARROW);
        Feature featureB = new Feature(reverseSlot, "TESTB");
        FeatureRange featureRangeB = new FeatureRange (featureB, 300, 600);
        featureRangeB.setDecoration(DECORATION_CLOCKWISE_ARROW);
        */
        for(int i = 0;i < features.size();i++){
            if(keywords[i].equals("CDS")) {
                int x = 0;
                for (AbstractFeature abs : features.get(keywords[i])) {
                    AbstractLocation location = abs.getLocations();
                    Point start = location.getStart();
                    Point end = location.getEnd();

                    String locus_tag = "ERROR";
                    String protein_id = "ERROR";
                    String product = "ERROR";
                    List<String> qualifierKeys = new ArrayList<String>(abs.getQualifiers().keySet());
                    for (int j = 0; j < qualifierKeys.size(); j++) {
                        Map<String, List<Qualifier>> qualifierMap = abs.getQualifiers();
                        List<Qualifier> qualifiers = qualifierMap.get(qualifierKeys.get(j));
                        for(int k = 0;k < qualifiers.size();k++){
                            Qualifier q = qualifiers.get(k);
                            if(q.getName().equals("locus_tag")){
                                locus_tag = q.getValue();
                            }
                            if(q.getName().equals("protein_id")){
                                protein_id = q.getValue();
                            }
                            if(q.getName().equals("product")){
                                product = q.getValue();
                            }
                        }
                    }
                    FeatureSlot activeSlot = null;
                    if(x % 2 == 0)
                        activeSlot = directSlot;
                    else
                        activeSlot = reverseSlot;
                    x++;
                    Feature feature = new Feature(activeSlot, locus_tag + " - " + product + " : " + protein_id);
                    FeatureRange featureRange = new FeatureRange (feature, start.getPosition(), end.getPosition());
                    featureRange.setDecoration(DECORATION_CLOCKWISE_ARROW);
                }

            }
            else if(keywords[i].equals("gene")) {

            }
            else if(keywords[i].equals("source")){
                for (AbstractFeature abs : features.get(keywords[i])) {
                    AbstractLocation location = abs.getLocations();
                    Point start = location.getStart();
                    Point end = location.getEnd();


                    List<String> qualifierKeys = new ArrayList<String>(abs.getQualifiers().keySet());
                    for (int j = 0; j < qualifierKeys.size(); j++) {
                        Map<String, List<Qualifier>> qualifierMap = abs.getQualifiers();
                        List<Qualifier> qualifiers = qualifierMap.get(qualifierKeys.get(j));
                        for (int k = 0; k < qualifiers.size(); k++) {
                            Qualifier q = qualifiers.get(k);
                            if (q.getName().equals("organism")) {
                                cgview.setTitle(q.getValue());
                            }
                        }
                    }
                }
            }
        }




        try {
            CgviewIO.writeToPNGFile(cgview, "GenomeMap.png");
            CgviewIO.writeToSVGFile(cgview, "Genomemap.svg", false);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }


}
