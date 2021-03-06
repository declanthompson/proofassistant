/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proofassistant.justification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import proofassistant.Globals;
import proofassistant.Globals;
import proofassistant.core.NDJust;
import proofassistant.core.NDJust;
import proofassistant.core.NDLine;
import proofassistant.core.NDLine;

/**
 * The JustBoxElim class implements NDJust for box elimination
 * 
 * @since Proof Assistant 1.3
 * @version 2.0
 * @author Declan Thompson
 */
public class JustBoxElim implements NDJust {
    private NDLine box;
    private NDLine ant;
    
    public JustBoxElim(NDLine boxLine, NDLine antecedent) {
        this.box = boxLine;
        this.ant = antecedent;
    }
    
    @Override
    public String getJava() {
        return box.getLineNumOutput() + ", " + ant.getLineNumOutput() + ", " 
                + Globals.operators.get("LEFTBOX") + box.getArgAsString(1) 
                + Globals.operators.get("RIGHTBOX") + "E";
    }
    
    @Override
    public String getTeX() {
        return box.getLineNumOutput() + ", " + ant.getLineNumOutput() 
                + ", $[ " + box.getArgAsString(1) + "]\\rulename{E}$";
    }
    
    public boolean getBlank() {
        return false;
    }
    
    public ArrayList<NDLine> getDependentNDLines() {
        return new ArrayList<NDLine>(Arrays.asList(box, ant));
    }
    
}
