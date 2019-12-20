package controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class UtilGui {

    protected static boolean inform(JFrame gui, String mensagem, String titulo) {

        int dialogResult = JOptionPane.showConfirmDialog(gui,
                mensagem, titulo, JOptionPane.YES_NO_OPTION);
        return dialogResult == 0;
    }

    protected static void updateImage(JLabel label, String imagePath,
            String info) {
        try {
            BufferedImage imgOriginal = ImageIO.read(new File(imagePath));
            Image img = imgOriginal.getScaledInstance(
                    label.getWidth(),
                    label.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imgLabel = new ImageIcon(img);
            label.setText("");
            label.setIcon(imgLabel);
        } catch (IOException ex) {
            String str = info;
            label.setText(str);
            label.setIcon(null);
        }
    }

    protected static void updateImage(JButton button, String imagePath,
            String info) {
        try {
            BufferedImage imgOriginal = ImageIO.read(new File(imagePath));
            Image img = imgOriginal.getScaledInstance(
                    button.getWidth(),
                    button.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imgBtn = new ImageIcon(img);
            button.setText("");
            button.setIcon(imgBtn);
        } catch (IOException ex) {
            button.setText(info);
            button.setIcon(null);
        }
    }
    
    protected static String selectFile(String path, boolean action) {
        String pathFile = "";
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(path);
        fileChooser.setCurrentDirectory(workingDirectory);

        System.out.println("selected " + path);

        int userSelection;
        if (action) {
            userSelection = fileChooser.showOpenDialog(parentFrame);
        } else {
            userSelection = fileChooser.showSaveDialog(parentFrame);
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            pathFile = selectedFile.getAbsolutePath();
        }
        return pathFile;
    }
}
