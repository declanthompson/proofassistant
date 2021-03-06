package proofassistant.dialogs;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;


/**
 * A class to support highlighting of parenthesis.  To use it, add it as a
 * caret listener to your text component.
 * 
 * It listens for the location of the dot.  If the character before the dot
 * is a close paren, it finds the matching start paren and highlights both
 * of them.  Otherwise it clears the highlighting.
 *
 * This object can be shared among multiple components.  It will only
 * highlight one at a time.
 * 
 * from http://www.informit.com/articles/article.aspx?p=31204&seqNum=4
 **/
public class BracketMatcher implements CaretListener
{
    /** The tags returned from the highlighter, used for clearing the
        current highlight. */
    Object start, end;

    /** The last highlighter used */
    Highlighter highlighter;

    /** Used to paint good parenthesis matches */
    Highlighter.HighlightPainter goodPainter;

    /** Used to paint bad parenthesis matches */
    Highlighter.HighlightPainter badPainter;

    /** Highlights using a good painter for matched parens, and a bad
        painter for unmatched parens */
    BracketMatcher(Highlighter.HighlightPainter goodHighlightPainter,
		   Highlighter.HighlightPainter badHighlightPainter)
    {
	this.goodPainter = goodHighlightPainter;
	this.badPainter = badHighlightPainter;
    }

    /** A BracketMatcher with the default highlighters (cyan and magenta) */
    BracketMatcher()
    {
	this(new DefaultHighlighter.DefaultHighlightPainter(Color.cyan),
	     new DefaultHighlighter.DefaultHighlightPainter(Color.magenta));
    }

    public void clearHighlights()
    {
	if(highlighter != null) {
	    if(start != null)
		highlighter.removeHighlight(start);
	    if(end != null)
		highlighter.removeHighlight(end);
	    start = end = null;
	    highlighter = null;
	}
    }

    /** Returns the character at position p in the document*/
    public static char getCharAt(Document doc, int p) 
	throws BadLocationException
    {
	return doc.getText(p, 1).charAt(0);
    }

    /** Returns the position of the matching parenthesis (bracket,
     * whatever) for the character at paren.  It counts all kinds of
     * brackets, so the "matching" parenthesis might be a bad one.  For
     * this demo, we're not going to take quotes or comments into account
     * since that's not the point.
     * 
     * It's assumed that paren is the position of some parenthesis
     * character
     * 
     * @return the position of the matching paren, or -1 if none is found 
     **/
    public static int findMatchingParen(Document d, int paren) 
	throws BadLocationException
    {
	int parenCount = 1;
	int i = paren-1;
	for(; i >= 0; i--) {
	    char c = getCharAt(d, i);
	    switch(c) {
	    case ')':
	    case '}':
	    case ']':
		parenCount++;
		break;
	    case '(':
	    case '{':
	    case '[':
		parenCount--;
		break;
	    }
	    if(parenCount == 0)
		break;
	}
	return i;
    }

    /** Called whenever the caret moves, it updates the highlights */
    public void caretUpdate(CaretEvent e)
    {
	clearHighlights();
	JTextComponent source = (JTextComponent) e.getSource();
	highlighter = source.getHighlighter();
	Document doc = source.getDocument();
	if(e.getDot() == 0) {
	    return;
	}
		
	// The character we want is the one before the current position
	int closeParen = e.getDot()-1;
	try {
	    char c = getCharAt(doc, closeParen);
	    if(c == ')' ||
	       c == ']' ||
	       c == '}') {
		int openParen = findMatchingParen(doc, closeParen);
		if(openParen >= 0) {
		    char c2 = getCharAt(doc, openParen);
		    if((c2 == '(' && c == ')') ||
		       (c2 == '{' && c == '}') ||
		       (c2 == '[' && c == ']')) {
			start = highlighter.addHighlight(openParen,
							 openParen+1,
							 goodPainter);
			end = highlighter.addHighlight(closeParen,
						       closeParen+1,
						       goodPainter);
		    }
		    else {
			start = highlighter.addHighlight(openParen,
							 openParen+1,
							 badPainter);
			end = highlighter.addHighlight(closeParen,
						       closeParen+1,
						       badPainter);
		    }
		}
		else {
		    end = highlighter.addHighlight(closeParen,
						   closeParen+1,
						   badPainter);		
		}
		    
	    }
	}
	catch(BadLocationException ex) {
	    throw new Error(ex);
	}
    }

    /** A demo main */
    public static void main(String a[])
    {
	JFrame f = new JFrame();
	JEditorPane p = new JEditorPane();
	f.getContentPane().add(new JScrollPane(p));
	p.setFont(new Font("Monospaced", 0, 12));
	p.addCaretListener(new BracketMatcher());
	f.setSize(400, 400);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setVisible(true);
    }
}

