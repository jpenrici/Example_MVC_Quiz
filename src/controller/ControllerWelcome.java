package controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import javax.swing.JButton;
import view.Welcome;

public class ControllerWelcome {

    private final static String LOCAL = Controller.LOCAL;
    private final static String PROP = Controller.PROP;

    private String pathImagesGui;
    private Welcome guiWelcome = null;

    public ControllerWelcome() {

        // checar recursos
        checkBaseFiles();

        // GUI Welcome
        guiWelcome = new Welcome();
        guiWelcome.eventGuiWelcome(new ActionsGuiWelcome());
        guiWelcome.setAlwaysOnTop(true);
        guiWelcome.setResizable(false);
        guiWelcome.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                exitWelcome();
            }
        });

        guiWelcome.lblWelcome1.setText("Editar Teste");
        guiWelcome.lblWelcome2.setText("Criar Teste");
        guiWelcome.lblWelcome3.setText("Quiz");

        updateImageButtons(guiWelcome.btnWelcome1, "btnWelcome_1.png");
        updateImageButtons(guiWelcome.btnWelcome2, "btnWelcome_2.png");
        updateImageButtons(guiWelcome.btnWelcome3, "btnWelcome_3.png");

        System.out.println("open welcome ...");
        guiWelcome.setVisible(true);
    }

    private void checkBaseFiles() {
        try {
            pathImagesGui = LOCAL + Util.property(PROP, "PATHIMAGESBTN");
        } catch (FileNotFoundException ex) {
            System.err.println("error loading properties ...");
        }
    }
    
    private void minimizeGui() {
            System.out.println("minimize welcome ...");
            guiWelcome.setState(Frame.ICONIFIED);
    }

    private void exitWelcome() {
        // saída padrão
        System.out.println("close application ...");
        System.exit(0);
    }

    class ActionsGuiWelcome implements ActionListener {
        // controle dos botões do GUI Welcome

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiWelcome.btnWelcome1) { // Editar Teste existente
                minimizeGui();
                ControllerEdit display = new ControllerEdit(false, true);
            }
            if (source == guiWelcome.btnWelcome2) { // Criar Novo Teste
                minimizeGui();
                ControllerEdit display = new ControllerEdit(true, true);
            }
            if (source == guiWelcome.btnWelcome3) { // Abrir Quiz
                minimizeGui();
                ControllerQuiz display = new ControllerQuiz();
            }
        }
    }

    private void updateImageButtons(JButton button, String image) {
        UtilGui.updateImage(button, pathImagesGui + image, "#");
    }
}
