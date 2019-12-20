package controller;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Question;
import view.GuiEdit;

public class ControllerEdit {

    private final static String LOCAL = Controller.LOCAL;
    private final static String PROP = Controller.PROP;
    private final static int NUMQUESTIONS = 10;
    private final static int NUMOPTIONS = 5;

    private ArrayList<Question> currentQuestions;
    private String pathQuestions;
    private String pathSaveQuestions;
    private String pathImages;
    private String currentTheme;
    private String currentPath;
    private int currentQuestion;
    private int numQuestions;
    private boolean newTest;
    private final boolean openMenu;

    private GuiEdit guiEdit = null;

    public ControllerEdit() {
        openMenu = false;
        newTest = false;
        initialize();
    }

    public ControllerEdit(boolean newTest) {
        openMenu = false;
        this.newTest = newTest;
        initialize();
    }

    public ControllerEdit(boolean newTest, boolean openMenu) {
        this.openMenu = openMenu;
        this.newTest = newTest;
        initialize();
    }

    private void initialize() {

        // checar recursos
        checkBaseFiles();

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
        start();
    }

    private void checkBaseFiles() {
        System.out.println("open application in edit mode ...");
        try {
            pathQuestions = LOCAL + Util.property(PROP, "PATHQUESTIONS");
            pathSaveQuestions = LOCAL + Util.property(PROP, "PATHSAVEQUESTIONS");
            pathImages = LOCAL + Util.property(PROP, "PATHIMAGES");
        } catch (FileNotFoundException ex) {
            System.err.println("error loading properties ...");
        }
    }

    private String selectFile(String path, boolean action) {
        return UtilGui.selectFile(path, action);
    }

    private void exitEdit() {
        System.out.println("close application in edit mode ...");
        if (openMenu) {
            guiEdit.dispose();
        } else {
            System.exit(0);
        }
    }

    private void start() {
        numQuestions = NUMQUESTIONS;
        currentQuestion = 1;

        guiEdit.setVisible(true);

        if (!newTest) {
            // Carregar arquivo existente
            currentPath = selectFile(pathQuestions, true);
            if (currentPath.equals("")) {
                newTest = true;
            }
        }

        if (!newTest) {
            // Arquivo selecionado
            if (loadQuestions(currentPath)) { // carregar questões
                currentTheme = currentQuestions.get(currentQuestion).getTheme();
            }
        }

        if (newTest) {
            // Questão em branco
            newQuestions();
        }

        // preparar GUI 
        guiEdit.txtData.setText(currentTheme);
        guiEdit.lblData.setText(Util.filename(currentPath));
        guiEdit.lblData.setToolTipText(currentPath);
        guiEdit.btnLoad.setVisible(!newTest);

        updateQuestion(); // atualizar questão na GUI        
    }

    private void newQuestions() {
        String num;
        int minOptions = NUMOPTIONS;

        do {
            num = JOptionPane.showInputDialog("Digite o número de Questões:");
        } while (num == null);

        currentPath = "NOVO ARQUIVO";
        currentTheme = "Tema";
        currentQuestions = new ArrayList<>();
        if (!num.equals("")) {
            numQuestions = Integer.parseInt(num);
        }
        if (numQuestions < 1) {
            numQuestions = 1;
        }
        for (int i = 0; i <= numQuestions; i++) {
            Question q = new Question(i, "Nova Questão", 0, "");
            ArrayList<String> array = new ArrayList<>();
            for (int j = 0; j < minOptions; j++) {
                array.add("-");
            }
            q.setTheme(currentTheme);
            q.setOptions(array);
            currentQuestions.add(q);
        }
    }

    private boolean loadQuestions(String file) {
        System.out.println("load questions ...");
        boolean emptyQuestions = true;
        currentQuestions = new ArrayList<>();
        ArrayList<String> questions = Util.loadFile(file);

        String[] str = questions.get(0).split(Util.DELIM);
        Question q = new Question(0, str[0], 0, null);
        currentQuestions.add(q);

        int number = 1;
        for (int i = 1; i < questions.size(); i++) {
            str = questions.get(i).split(Util.DELIM);
            ArrayList<String> options = new ArrayList<>();

            if (str.length > 4) {
                // Question(numero da questão, questão, resposta, pathImagem)          
                q = new Question(number++, str[3], Integer.parseInt(str[1]), str[0]);
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
        updateReports("Editando questão "
                + currentQuestions.get(currentQuestion).getNumber() + " de "
                + Integer.toString(currentQuestions.size() - 1) + " ...");

        guiEdit.txtAreaQuestion.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        guiEdit.txtAreaQuestion.setText(currentQuestions.get(currentQuestion).getQuestion());
        guiEdit.txtData.setText(currentQuestions.get(currentQuestion).getTheme());

        // atualizar alternativas na tabela
        updateOptions(currentQuestions.get(currentQuestion).getCorrectAnswer(), false);

        // atualizar alternativa correta
        if (!currentQuestions.get(currentQuestion).getOptions().isEmpty()) {
            guiEdit.cboxCorrectAnswer.removeAllItems();
            for (int i = 1; i <= currentQuestions.get(currentQuestion).getOptions().size(); i++) {
                guiEdit.cboxCorrectAnswer.addItem(String.valueOf(i));
            }
            guiEdit.cboxCorrectAnswer.setSelectedIndex(
                    currentQuestions.get(currentQuestion).getCorrectAnswer());
        }

        // atualizar imagem da questão
        updateImageQuestion();
    }

    private void changeQuestion() {
        // registra modificações na questão, sem salvar!
        currentQuestions.get(currentQuestion).setQuestion(guiEdit.txtAreaQuestion.getText());
        currentQuestions.get(currentQuestion).setTheme(guiEdit.txtData.getText());

        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < guiEdit.tbOptions.getRowCount(); i++) {
            String value = guiEdit.tbOptions.getValueAt(i, 1).toString();
            options.add(value);
        }
        currentQuestions.get(currentQuestion).setOptions(options);

        if (guiEdit.cboxCorrectAnswer.getSelectedItem() != null) {
            currentQuestions.get(currentQuestion).setCorrectAnswer(
                    guiEdit.cboxCorrectAnswer.getSelectedIndex());
        }
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
                elements[i][0] = "*" + String.valueOf(i + 1) + "*";
                elements[i][1] = options.get(i).toUpperCase();
            } else {
                elements[i][0] = String.valueOf(i + 1);
                elements[i][1] = options.get(i).toLowerCase();
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
        UtilGui.updateImage(guiEdit.lblImage, imagePath, "imagem indisponível!");
    }

    private String format() {
        System.out.println("prepare text ...");
        String delim = Util.DELIM;
        String output;

        output = "Test;local image;answer number;theme;question;options...\n";
        for (int i = 1; i < currentQuestions.size(); i++) {
            Question q = currentQuestions.get(i);
            output += q.getPathImage() + delim + q.getCorrectAnswer() + delim
                    + q.getTheme() + delim + q.getQuestion() + delim;
            for (int j = 0; j < q.getOptions().size(); j++) {
                output += q.getOptions().get(j) + delim;
            }
            output += "\n" + delim + delim + delim + "\n";
        }
        return output;
    }

    private void finalizeEdit() {

        String text = format();
        String output = selectFile(pathSaveQuestions, false);
        if (!output.equals("")) {
            Util.export(text, output);
        }
    }

    private boolean inform(String mensagem, String titulo) {
        return UtilGui.inform(guiEdit, mensagem, titulo);
    }

    class ActionsGuiTest implements ActionListener {
        // controle dos botões do GUI Edit

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();

            if (source == guiEdit.btnFirst) {
                changeQuestion();
                currentQuestion = 1;
                updateQuestion();
            }
            if (source == guiEdit.btnPrevious) {
                changeQuestion();
                currentQuestion--;
                updateQuestion();
            }
            if (source == guiEdit.btnNext) {
                changeQuestion();
                currentQuestion++;
                updateQuestion();
            }
            if (source == guiEdit.btnLast) {
                changeQuestion();
                currentQuestion = currentQuestions.size() - 1;
                updateQuestion();
            }
            if (source == guiEdit.btnFinish) {
                finalizeEdit();
            }

            if (source == guiEdit.btnLoad) {
                start();
            }

            if (source == guiEdit.btnImage) {
                System.out.println("change image ...");
                String imagePath = selectFile(pathImages, true);
                if (!imagePath.equals("")) {
                    String[] str = imagePath.split(pathImages);
                    if (str.length > 1) {
                        imagePath = str[str.length - 1];
                    }
                    currentQuestions.get(currentQuestion).setPathImage(imagePath);
                    changeQuestion();
                    updateImageQuestion();
                } else {
                    inform("Erro ao carregar imagem!", "Atenção");
                }
            }

            if (source == guiEdit.btnClearOptions) {
                updateOptions(currentQuestions.get(currentQuestion).getCurrentAnswer(), true);
            }

            if (source == guiEdit.btnClearQuestion) {
                guiEdit.txtAreaQuestion.setText("");
            }

            if (source == guiEdit.btnClearTheme) {
                guiEdit.txtData.setText("");
            }

            if (source == guiEdit.cboxCorrectAnswer) {
                changeQuestion();
                updateQuestion();
            }
        }
    }
}
