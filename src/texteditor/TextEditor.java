// Java Program to create a text TextEditor using java 
package texteditor;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.*;

class TextEditor implements ActionListener, KeyListener, DocumentListener {

    SmartTextArea text_area;//text area component
    JFrame frame;
    JMenuItem item_opened_file;
    File opened_file;//file that is currently oppened inside the editor
    boolean has_been_saved;

    private void set_theme() {

        UIManager.put("control", new Color(128, 128, 128));
        UIManager.put("info", new Color(128, 128, 128));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115, 164, 209));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", new Color(0, 230, 0));
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("could not set theme");
        }
    }

    private void init() {
        has_been_saved = true;
        frame = new JFrame("editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        set_theme();
        text_area = new SmartTextArea();
        JScrollPane scroll = new JScrollPane(text_area);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(scroll, BorderLayout.CENTER);

        JMenuBar menu_bar = new JMenuBar();
        JMenu file_menu = new JMenu("File");
        JMenu find_menu = new JMenu("Find");

        JMenuItem item_new = new JMenuItem("Open");
        JMenuItem item_save = new JMenuItem("Save");
        JMenuItem item_find = new JMenuItem("Find");

        JMenuItem item_new_document = new JMenuItem("New Document");

        item_opened_file = new JMenuItem("Empty Document");

        item_new.addActionListener(this);
        item_save.addActionListener(this);
        item_new_document.addActionListener(this);
        item_find.addActionListener(this);

        file_menu.add(item_new);
        file_menu.add(item_save);
        file_menu.add(item_new_document);

        find_menu.add(item_find);

        menu_bar.add(file_menu);
        menu_bar.add(find_menu);
        menu_bar.add(item_opened_file);

        frame.setJMenuBar(menu_bar);
        frame.add(panel);

        text_area.addKeyListener(this);
        text_area.getDocument().addDocumentListener(this);
        text_area.setFocusable(true);
        text_area.requestFocus();

        frame.setSize(500, 500);
        frame.show();

    }

    void show_dialogue_error(String error) {
        JOptionPane.showMessageDialog(frame, error);
    }

    void set_opened_file(File file) {

        opened_file = file;
        if (file != null) {
            item_opened_file.setText(FileUtility.trim_text(file.getAbsolutePath(), 40));
        } else {
            item_opened_file.setText("Empty Document");
        }
        has_been_saved = true;

    }

    void dialogue_open_file() {
        JFileChooser chooser = new JFileChooser("home/");
        int return_value = chooser.showOpenDialog(null);
        if (return_value == JFileChooser.APPROVE_OPTION) {
            File selected_file = chooser.getSelectedFile();

            try {
                text_area.setText(FileUtility.get_string_from_file(selected_file));
                set_opened_file(selected_file);

            } catch (IOException exception) {
                show_dialogue_error("Could not load file " + selected_file.getAbsolutePath());
            }
        }
    }

    void dialogue_save_file() {
        if (opened_file == null) {

            JFileChooser chooser = new JFileChooser("home/");
            int return_value = chooser.showSaveDialog(null);

            if (return_value == JFileChooser.APPROVE_OPTION) {
                File selected_file = chooser.getSelectedFile();
                try {
                    FileUtility.write_string_to_file(selected_file, text_area.getText());
                    set_opened_file(selected_file);

                } catch (IOException exception) {
                    show_dialogue_error("Could not save file " + selected_file.getAbsolutePath());

                }
            }
        } else {
            try {
                FileUtility.write_string_to_file(opened_file, text_area.getText());
                set_opened_file(opened_file);

            } catch (IOException exception) {
                show_dialogue_error("Could not save file " + opened_file.getAbsolutePath());

            }
        }

    }

    void dialogue_find_pattern() {

        String pattern = JOptionPane.showInputDialog("Please input string to search in this file");

        if (pattern != null) {
            text_area.highlight_pattern(pattern);
        }

    }

    void set_empty_document() {
        text_area.setText("");
        set_opened_file(null);
    }

    void do_action(String action) {

        if (action.equals("Open")) {
            dialogue_open_file();
        } else if (action.equals("Save")) {
            dialogue_save_file();
        } else if (action.equals("New Document")) {
            set_empty_document();
        } else if (action.equals("Find")) {
            dialogue_find_pattern();
        }

    }

    // If a button is pressed 
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        do_action(s);
    }

    private boolean is_save_key(KeyEvent event) {
        return event.isControlDown() && event.getKeyCode() == KeyEvent.VK_S;
    }

    private boolean is_find_key(KeyEvent event) {
        return event.isControlDown() && event.getKeyCode() == KeyEvent.VK_F;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (is_save_key(event)) {
            dialogue_save_file();
        } else if (is_find_key(event)) {
            dialogue_find_pattern();
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }

    private void update_states() {

        if (text_area.has_highlights()) {
            text_area.remove_highlights();
        }

        if (has_been_saved) {
            item_opened_file.setText(item_opened_file.getText() + " -not saved");
            has_been_saved = false;
        }
    }

    @Override
    public void changedUpdate(DocumentEvent docevent) {

    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        update_states();
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        update_states();
    }

    TextEditor() {

        init();
    }

    // Main class 
    public static void main(String args[]) {
        TextEditor e = new TextEditor();
    }
}
