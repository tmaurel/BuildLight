package buildLight.views;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NumericTextField extends JTextField {

    @Override
    protected Document createDefaultModel() {
        return (Document) new NumericDocument();
    }

    private static class NumericDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (!str || str.isNumber()) {
                super.insertString(offs, str, a)
            }
        }
    }

}
