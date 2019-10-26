/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;

/**
 *
 * @author gvisan
 */
import javax.swing.*;
import javax.swing.text.*;

public class SmartTextArea extends JTextArea {

    private boolean has_highlights;

    public boolean has_highlights() {
        return has_highlights;
    }

    public void remove_highlights() {
        getHighlighter().removeAllHighlights();
        has_highlights = false;

    }

    private int[] create_lsp_array(String pattern) {
        int[] array = new int[pattern.length()];

        int i = 0, j = 1;
        while (j < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                array[j] = i;
                j++;

            } else {
                if (i != 0) {
                    i = array[i - 1];
                } else {
                    array[j] = 0;
                    j++;
                }
            }
        }
        return array;
    }

    private void kmp_algorithm(String pattern) {
        remove_highlights();
        Highlighter highlighter = getHighlighter();

        String text = getText();

        if (pattern.length() > text.length()) {
            return;
        }
        int[] lsp_array = create_lsp_array(pattern);

        int i = 0, j = 0;
        int last_found = -1;

        while (j < text.length()) {
            if (pattern.charAt(i) == text.charAt(j)) {
                i++;
                j++;
            }
            if (i == pattern.length()) {

                if (last_found == -1 || j - last_found >= i) {

                    try {
                        highlighter.addHighlight(j - i, j, DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException e) {

                    }
                    last_found = j;
                }

                i = lsp_array[i - 1];

            }

            if (j < text.length() && pattern.charAt(i) != text.charAt(j)) {
                if (i != 0) {
                    i = lsp_array[i - 1];
                } else {
                    j++;
                }
            }

        }

    }

    public void highlight_pattern(String pattern) {
        kmp_algorithm(pattern);
        has_highlights = true;
    }

    public SmartTextArea() {
        has_highlights = false;
    }
}
