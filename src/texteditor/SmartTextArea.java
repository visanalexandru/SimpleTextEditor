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
import java.util.ArrayList;
import javafx.util.Pair;
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

    private ArrayList<Integer> kmp_algorithm(String pattern) {
        remove_highlights();
        Highlighter highlighter = getHighlighter();

        String text = getText();
        ArrayList<Integer> to_return = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return to_return;//return empty arraylist
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

                    to_return.add(j - i);
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
        return to_return;

    }

    private void add_highlights(ArrayList<Integer> intervals, int pattern_length) {

        for (int i = 0; i < intervals.size(); i++) {
            try {
                int a = intervals.get(i);
                int b = a + pattern_length;
                getHighlighter().addHighlight(a, b, DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException e) {

            }
        }
    }

    private void replace(ArrayList<Integer> intervals, int pattern_length, String to_replace) {
        int offset = 0;
        int offset_add = pattern_length - to_replace.length();
        for (int i = 0; i < intervals.size(); i++) {
            int here = intervals.get(i) - offset;
            String text_here = getText().substring(0, here);
            String text_after = getText().substring(here + pattern_length);
            setText(text_here + to_replace + text_after);
            offset += offset_add;
        }
    }

    public void highlight_pattern(String pattern) {
        add_highlights(kmp_algorithm(pattern), pattern.length());
        has_highlights = true;
    }

    public void replace_pattern(String pattern, String replaced) {
        replace(kmp_algorithm(pattern), pattern.length(), replaced);
    }

    public SmartTextArea() {
        has_highlights = false;
    }
}
