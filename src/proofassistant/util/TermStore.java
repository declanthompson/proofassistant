/*
 * The MIT License
 *
 * Copyright 2014 Declan Thompson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package proofassistant.util;

import proofassistant.core.NDLine;
import java.util.*;
import proofassistant.Globals;
import proofassistant.exception.MissingArityException;
import proofassistant.core.NDAtom;

/** The TermStore class provides methods for dealing with terms.
 *
 * @since ProofAssistant 1.0
 * @version 2.0
 * @author Declan Thompson
 */
public class TermStore {
    private ArrayList<String> listOfUsedTerms;
    private final String[] termAlphabet = "a b c d e".split(" ");
    private final String[] contextAlphabet = "s t u".split(" ");
    private final String[] propositionAlphabet = "p q r".split(" ");
    private HashMap<String, Integer> arities;
    
    public static String aritiyS = "a0, b0, c0, d0, e0, f1, g1, h2, S1, s0, t0, u0";
    
    /**
     * Default constructor.
     */
    public TermStore() {
        listOfUsedTerms = new ArrayList<>();
        arities = new HashMap<>();
        startArities();
    }
    
    /**
     * Get the arity of a term.
     * @param term A String, the term to find the arity of.
     * @return An integer, the arity of the term.
     * @throws MissingArityException if the term isn't known to this TermStore.
     */
    public int getArity(String term) throws MissingArityException {
        if (arities.containsKey(term)) {
            return arities.get(term);
        } else {
            throw new MissingArityException("Cannot determine arity of " + term);
        }
    }
    
    public String getNewTermString() { // Find a term from termAlphabet that is not in listOfUsedTerms
        String termToReturn = "";
        boolean foundATerm = false;
        
        int k = 0; // k tells us the number of ' to put on the end of the term (in case there are more than alphabet.length terms
        while (!foundATerm) {
            String suffix = "";
            for (int i = 0; i < k; i++) { // Find the suffix to use
                suffix = suffix + "'";
            }
            for (int i = 0; i < termAlphabet.length && !foundATerm; i++) { // Check through the alphabet until we find a term not in the listOfUsedTerms
                if (!listOfUsedTerms.contains(termAlphabet[i] + suffix)) {
                    termToReturn = termAlphabet[i] + suffix;
                    foundATerm = true;
                }
            }
            k++;
        }
        Globals.arity.put(termToReturn, 0);
        return termToReturn;
    }
    
    public NDAtom getNewTerm() {
        return new NDAtom(getNewTermString(), new ArrayList<NDAtom>(), NDAtom.TERM);
    }
    
    public String getNewContext() { // Find a term from conextAlphabet that is not in listOfUsedTerms
        String termToReturn = "";
        boolean foundATerm = false;
        
        int k = 0; // k tells us the number of ' to put on the end of the term (in case there are more than alphabet.length terms
        while (!foundATerm) {
            String suffix = "";
            for (int i = 0; i < k; i++) { // Find the suffix to use
                suffix = suffix + "'";
            }
            for (int i = 0; i < contextAlphabet.length && !foundATerm; i++) { // Check through the alphabet until we find a term not in the listOfUsedTerms
                if (!listOfUsedTerms.contains(contextAlphabet[i] + suffix)) {
                    termToReturn = contextAlphabet[i] + suffix;
                    foundATerm = true;
                }
            }
            k++;
        }
        Globals.arity.put(termToReturn, 0);
        return termToReturn;
    }
    
    public String getNewProposition() { // Find a term from propositionAlphabet that is not in listOfUsedTerms
        String termToReturn = "";
        boolean foundATerm = false;
        
        int k = 0; // k tells us the number of ' to put on the end of the term (in case there are more than alphabet.length terms
        while (!foundATerm) {
            String suffix = "";
            for (int i = 0; i < k; i++) { // Find the suffix to use
                suffix = suffix + "'";
            }
            for (int i = 0; i < propositionAlphabet.length && !foundATerm; i++) { // Check through the alphabet until we find a term not in the listOfUsedTerms
                if (!listOfUsedTerms.contains(propositionAlphabet[i] + suffix)) {
                    termToReturn = propositionAlphabet[i] + suffix;
                    foundATerm = true;
                }
            }
            k++;
        }
        return termToReturn;
    }
    
    public void processLine(String macro) {
        String[] arrayOfLine = macro.split("");
        boolean inCommand = false;
        
        
        
        for (int i = 0; i < arrayOfLine.length; i++) {
            if (arrayOfLine[i].equals("\\") || arrayOfLine[i].equals("/")) { // We're going into a tex command
                inCommand = true;
            } else if (arrayOfLine[i].equals("{") || arrayOfLine[i].equals("[")) { // We've come out of a tex command
                inCommand = false;
            } else if (arrayOfLine[i].equals("}") || arrayOfLine[i].equals("]")){ // Ignore }, they're everywhere
                
            } else if (!inCommand){ // If we're not in a tex command
                String termToCheck = arrayOfLine[i]; // Prepare a term to check
                boolean checkingForPrimes = true;
                while (checkingForPrimes && i < arrayOfLine.length - 1) { // Check to see if this term is followed by any '. If so, add them to the term
                    if (arrayOfLine[i+1].equals("'")) {
                        termToCheck = termToCheck + "'";
                        i++;
                    } else {
                        checkingForPrimes = false;
                    }
                }
                
                if (!listOfUsedTerms.contains(termToCheck)) {
                    listOfUsedTerms.add(termToCheck);
//                    System.out.println(Thread.currentThread().getStackTrace()[6]);
//                    System.out.println("Added " + termToCheck + " from " + macro);
//                    System.out.println("added " + termToCheck);
//                    System.out.println("termstore says " + listOfUsedTerms.contains("s"));
                }
            }
        }
        
        Collections.sort(listOfUsedTerms);
    }
    
    public boolean containsTerm(String term) {
        return listOfUsedTerms.contains(term);
    }
    
    public void empty() {
//        System.out.println("EMPTY");
        listOfUsedTerms = new ArrayList<>();
    }
    
    public boolean isEmpty() {
        return listOfUsedTerms.isEmpty();
    }
    
    public void processNDLineArray(NDLine[] proofArray) {
        for (int i = 0; i < proofArray.length; i++) {
            processLine(proofArray[i].getLine());
        }
    }
    
    public ArrayList<String> getListOfUsedTerms() {
        return listOfUsedTerms;
    }
    
    private void startArities() {
//        System.out.println("arityS is " + arityS);
        String currentLetter = "";
        String currentArity = "";
        for (int i = 0; i < aritiyS.length(); i++) {
            char c = aritiyS.charAt(i);
            if (c == ',' && !currentLetter.equals("")) {
                if (currentArity.equals("")) {
                    arities.remove(currentLetter);
                } else {
//                System.out.println(currentLetter + Integer.parseInt(currentArity));
                    arities.put(currentLetter, Integer.parseInt(currentArity));
                }
                currentLetter = "";
                currentArity = "";
            } else if ((c > 64 && c < 91) || (c > 96 && c < 123) || c == '\'') {
                currentLetter = currentLetter + c;
            } else if (c > 47 && c < 58) {
                currentArity = currentArity + c;
            }
        }
        if (!currentLetter.equals("")){
            if (currentArity.equals("")) {
                arities.remove(currentLetter);
            } else {
//            System.out.println(currentLetter + Integer.parseInt(currentArity));
                arities.put(currentLetter, Integer.parseInt(currentArity));
            }
        }
    }
}
