package com.thomassymalla;

import java.util.ArrayList;
import java.util.List;

//This class allows us to organize a .gb file into nodes.
//For example, we might have this:
/*
SOURCE      Tomato curly stunt virus
  ORGANISM  Tomato curly stunt virus
            Viruses; Monodnaviria; Shotokuvirae; Cressdnaviricota;
            Repensiviricetes; Geplafuvirales; Geminiviridae; Begomovirus.
*/
// The top DataNode would have a name of SOURCE, value of Tomato curly stunt virus, and 1 child.
// The 1 child would have a name of ORGANISM, value of the following 3 lines, and no children

public class DataNode {
    private String name;
    private String value;
    private boolean isHead = false;
    private boolean isSpecial = false;

    public List<DataNode> children = new ArrayList<DataNode>();

    public DataNode GetChildWithName(String name){
        for(DataNode node : children){
            if(node.GetName().equals(name))
                return node;
        }
        return null;
    }

    public void SetName(String newName){ name = newName; }
    public String GetName() { return name; }

    public void SetValue(String newValue){ value = newValue; }
    public String GetValue() { return value; }

    public void SetToHead(){ isHead = true; }
    public boolean IsHead(){
        return isHead;
    }

    public void SetSpecial(){
        isSpecial = true;
    }
    public boolean IsSpecial(){
        return isSpecial;
    }


}
