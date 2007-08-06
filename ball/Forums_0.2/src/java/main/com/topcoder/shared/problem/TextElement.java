package com.topcoder.shared.problem;

import com.topcoder.shared.netCommon.CSReader;
import com.topcoder.shared.netCommon.CSWriter;

import java.io.IOException;
import java.io.ObjectStreamException;

/**
 * The simplest element is a <code>TextElement</code>, which corresponds to <code>CDATA</code> (plain,
 * unstructured text).
 *
 * @see Element
 * @author Logan Hanks
 */
public class TextElement extends BaseElement
        implements Element {
    private String text = "";
    private boolean escapedText = false;

    public TextElement() {
    }

    public TextElement(String text) {
        this.text = text;
    }

    public TextElement(boolean escapedText, String text) {
        this.escapedText = escapedText;
        this.text = text;
    }

    public void customWriteObject(CSWriter writer)
            throws IOException {
        writer.writeString(text);
        writer.writeBoolean(escapedText);
    }

    public void customReadObject(CSReader reader)
            throws IOException, ObjectStreamException {
        text = reader.readString();
        escapedText = reader.readBoolean();
    }


    public String getEditableText() {
        return text;
    }

    public void setEditableText(String text) {
        this.text = text;
    }

    public String toXML() {
        return ProblemComponent.encodeHTML(text);
    }

    public String toString() {
        if (escapedText) {
            return text;
        }
        return ProblemComponent.encodeHTML(text);
    }

    /**
     * @return Returns the escapedText.
     */
    public boolean isEscapedText() {
        return escapedText;
    }

    /**
     * @param escapedText The escapedText to set.
     */
    public void setEscapedText(boolean escapedText) {
        this.escapedText = escapedText;
    }

}


