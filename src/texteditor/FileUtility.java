/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;

/**
 *
 * @author gvisan
 */
public class FileUtility {

    static String get_string_from_file(File file) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
        return text;
    }

    static void write_string_to_file(File file, String to_write) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(to_write);
        writer.close();
    }

    static String trim_text(String to_trim, int max_length) {
        String result = to_trim;

        if (result.length() > max_length) {
            result = result.substring(0, max_length - 3) + "...";
        }
        return result;

    }

}
