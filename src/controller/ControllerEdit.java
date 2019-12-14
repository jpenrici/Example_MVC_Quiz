package controller;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Question;
import view.GuiEdit;

public class ControllerEdit {

    private final static String LOCAL = Util.userDir();
    protected final static String PROP = LOCAL + "/resources/pathTests.properties";

    private ArrayList<Question> currentQuestions;
    private String pathQuestions;
    private String pathSaveQuestions;
    private String pathImages;
    private String currentTheme;
    private String currentPath;
    private int currentQuestion;
    private final boolean newTest;

    private GuiEdit guiEdit = null;

    public ControllerEdit() {
        newTest = false;
        initialize();
    }

    public ControllerEdit(boolean newTest) {
        this.newTest = newTest;
        initialize();
    }

    private void initialize() {
        
        // GUI Edit
        guiEdit = new GuiEdit();
        guiEdit.eventTest(new ActionsGuiTest());
        guiEdit.setAlwaysOnTop(false);
        guiEdit.setResizable(false);
        guiEdit.setPreferredSize(new Dimension(850, 600));
        guiEdit.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.out.println("pause application ...");
                if (inform("As alterações não serão salvas.\nDeseja encerrar?",
                        "Fechar Edição")) {
                    exitEdit();
                } else {
                    System.out.println("continue ...");
                }
            }
        });

        // tabela de alternativas
        guiEdit.tbOptions.setFont(new java.awt.Font("Arial", Font.BOLD, 14));

        // iniciar
        checkBaseFiles();
        start();
    }

    private void checkBaseFiles() {
        try {
            pathQuestions = LOCAL + Util.property(PROP, "PATHQUESTIONS");
            pathSaveQuestions = LOCAL + Util.property(PROP, "PATHSAVEQUESTIONS");
            pathImages = LOCAL + Util.property(PROP, "PATHIMAGES");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControllerEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String read(String path, boolean action) {
        
        // checar entrada
        if (path.charAt(0) != '/') {
            path += "/" + path;
        }
        
        String output = "";
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(path);
        fileChooser.setCurrentDirectory(workingDirectory);

        System.err.println(path);

        int userSelection;
        if (action) {
            userSelection = fileChooser.showOpenDialog(parentFrame);
            fileChooser.setDialogTitle("Abrir arquivo:");
        } else {
            userSelection = fileChooser.showSaveDialog(parentFrame);
            fileChooser.setDialogTitle("Salvar como:");
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            output = fileToSave.getAbsolutePath();
        }
        return output;
    }

    private void exitEdit() {
        // saída padrão
        System.out.println("close application in edit mode ...");
        System.exit(0);
    }

    private void start() {
        currentQuestion = 1;
        currentTheme = "Tema a definir ...";
        int numQuestions = 1;

        if (newTest) {
            String temp = JOptionPane.showInputDialog("Digite o número de Questões:");
            if (temp.isEmpty()) {
                numQuestions = Integer.parseInt(temp);
                currentPath = "NOVO";
            }
        } else {
            currentPath = read(pathQuestions, true);
            if (currentPath.equals("")) {
                exitEdit();
                System.err.println("Corrigir iniciar...");
            }

            if (loadQuestions(currentPath)) { // carregar questões
                // atualizar questão
                updateQuestion();
                currentTheme = currentQuestions.get(currentQuestion).getTheme();
            }
        }

        // preparar GUI 
        guiEdit.setVisible(true);
        guiEdit.txtData.setText(currentTheme);
        guiEdit.lblData.setText(Util.filename(currentPath));
        guiEdit.lblData.setToolTipText(currentPath);
    }

    private boolean loadQuestions(String file) {
        System.out.println("load questions ...");
        boolean emptyQuestions = true;
        currentQuestions = new ArrayList<>();
        ArrayList<String> questions = Util.loadFile(file);

        String[] str = questions.get(0).split(Util.DELIM);
        Question q = new Question(0, str[0], null, null);
        currentQuestions.add(q);

        int number = 1;
        for (int i = 1; i < questions.size(); i++) {
            str = questions.get(i).split(Util.DELIM);
            ArrayList<String> options = new ArrayList<>();

            if (str.length > 4) {
                // Question(numero da questão, questao, resposta, pathImagem)          
                q = new Question(number++, str[3], str[1], str[0]);
                for (int j = 4; j < str.length; j++) {
                    options.add(str[j]);
                }
                q.setTheme(str[2]);
                q.setOptions(options);
                currentQuestions.add(q);
                emptyQuestions = false;
            }
        }
        return !emptyQuestions;
    }

    private void updateQuestion() {
        if (currentQuestion < 1) {
            currentQuestion = 1;
        }
        if (currentQuestion >= currentQuestions.size() - 1) {
            currentQuestion = currentQuestions.size() - 1;
        }

        // atualizar questão
        updateReports("Editando questão " + currentQuestions.get(currentQuestion).getNumber()
                + " de " + Integer.toString(currentQuestions.size() - 1) + " ...");

        String str = currentQuestions.get(currentQuestion).getQuestion();
        guiEdit.txtAreaQuestion.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        guiEdit.txtAreaQuestion.setText(str);
        guiEdit.txtData.setText(currentQuestions.get(currentQuestion).getTheme());

        // atualizar alternativas na tabela
        updateOptions(currentQuestions.get(currentQuestion).getCurrentAnswer(), false);

        // atualizar imagem da questão
        updateImageQuestion();
    }

    private void changeQuestion() {
        // registra modificações na questão, sem salvar!
        currentQuestions.get(currentQuestion).setQuestion(guiEdit.txtAreaQuestion.getText());
        currentQuestions.get(currentQuestion).setTheme(guiEdit.txtData.getText());
        currentQuestions.get(currentQuestion).setCorrectAnswer("1");

        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < guiEdit.tbOptions.getRowCount(); i++) {
            String value = guiEdit.tbOptions.getValueAt(i, 1).toString();
            options.add(value);
        }
        currentQuestions.get(currentQuestion).setOptions(options);
    }

    private void updateOptions(int chosenOption, boolean clear) {
        // atualizar alternativas no JTable
        ArrayList<String> options = currentQuestions.get(currentQuestion).getOptions();
        String[][] elements = new String[options.size()][2];
        for (int i = 0; i < options.size(); i++) {
            if (clear) {
                options.set(i, "");
            }
            if (chosenOption == i) {
                elements[i][0] = String.valueOf(i + 1);
                elements[i][1] = options.get(i).toUpperCase();
            } else {
                elements[i][0] = String.valueOf(i + 1);
                elements[i][1] = options.get(i);
            }
        }

        guiEdit.tbOptions.setModel(new DefaultTableModel(elements, new String[]{
            "Nº", "Alternativas para questão "
            + String.valueOf(currentQuestions.get(currentQuestion).getNumber())
        }
        ) {
            boolean[] canEdit = new boolean[]{false, false};
        });

        // ajustando colunas do JTable
        guiEdit.tbOptions.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        guiEdit.tbOptions.getColumnModel().getColumn(0).setPreferredWidth(3);
        guiEdit.tbOptions.getColumnModel().getColumn(1).setPreferredWidth(750);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        guiEdit.tbOptions.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
    }

    private void updateReports(String info) {
        // atualizar informe
        guiEdit.lblInform.setText(info);
    }

    private void updateImageQuestion() {
        String imagePath = pathImages + currentQuestions.get(currentQuestion).getPathImage();
        //System.out.println(imagePath);
        try {
            BufferedImage imgOriginal = ImageIO.read(new File(imagePath));
            Image img = imgOriginal.getScaledInstance(
                    guiEdit.lblImage.getWidth(),
                    guiEdit.lblImage.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imgLabel = new ImageIcon(img);
            guiEdit.lblImage.setText("");
            guiEdit.lblImage.setIcon(imgLabel);
        } catch (IOException ex) {
            String str = "Questão sem imagem disponível!";
            guiEdit.lblImage.setText(str);
            guiEdit.lblImage.setIcon(null);
        }
    }

    private String formatOutput() {
        System.out.println("prepare text ...");
        String delim = Util.DELIM;
        String output = "";

        for (Question q : currentQuestions) {
            output += q.getPathImage() + delim + "?" + delim
                    + q.getTheme() + delim + q.getAnswer();
            output = q.getOptions().stream().map(
                    (option) -> option + delim).reduce(output, String::concat);
            output += delim + delim + delim + delim + "\n";
        }

        return output;
    }

    private void finalizeEdit() {

        String text = formatOutput();

        String output = read(pathSaveQuestions, false);
        if (!output.equals("")) {
            System.out.println("saving file ...");
            Util.export(text, output);
        }
    }

    private boolean inform(String mensagem, String titulo) {
        int dialogResult = JOptionPane.showConfirmDialog(guiEdit,
                mensagem, titulo, JOptionPane.YES_NO_OPTION);
        return dialogResult == 0;

    }

    class ActionsGuiTest implements ActionListener {
        // controle dos botões do GUI Edit

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiEdit.btnFirst) {
                currentQuestion = 1;
                updateQuestion();
            }
            if (source == guiEdit.btnPrevious) {
                currentQuestion--;
                updateQuestion();
            }
            if (source == guiEdit.btnNext) {
                currentQuestion++;
                updateQuestion();
            }
            if (source == guiEdit.btnLast) {
                currentQuestion = currentQuestions.size() - 1;
                updateQuestion();
            }
            if (source == guiEdit.btnFinish) {
                finalizeEdit();
            }

            if (source == guiEdit.btnLoad) {
                start();
            }

            if (source == guiEdit.btnClearTheme) {
                String temp = currentTheme;
                currentTheme = JOptionPane.showInputDialog("Digite o novo tema:");
                if (currentTheme.isEmpty()) {
                    currentTheme = temp;
                }
                guiEdit.txtData.setText(currentTheme);
            }

            if (source == guiEdit.btnSaveQuestion) {
                changeQuestion();
            }

            if (source == guiEdit.btnClearOptions) {
                updateOptions(currentQuestions.get(currentQuestion).getCurrentAnswer(), true);
            }

            if (source == guiEdit.btnClearQuestion) {
                guiEdit.txtAreaQuestion.setText("");
            }
        }
    }
}
