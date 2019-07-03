package controller;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import model.Question;
import view.GuiTest;
import view.Login;

public class Controller {

    private final static String LOCAL = "src/";
    protected final static String PROP = LOCAL + "resources/pathTests.properties";

    private GuiTest guiTest = null;
    private Login guiLogin = null;
    private final BackGroundTextArea txtAreaHelp;
    private final BackGroundTextArea txtAreaSummary;

    private final Timer timer;
    private int countTime;

    private ArrayList<Question> currentTest;
    private static Map<String, String> groups;
    private static Map<String, String> users;
    private static Map<String, String> availableThemes;
    private static Map<String, String> availableTests;

    private String pathGroups;
    private String pathUsers;
    private String pathQuestions;
    private String pathThemes;
    private String pathAvailableTests;
    private String pathAnswers;
    private String pathImages;
    private String pathImagesGui;

    private String currentUser;
    private String currentGroup;
    private String currentTheme;
    private int currentQuestion;
    private int currentHits;
    private int currentNumberOfIssues;
    private boolean started;
    private boolean congratulations;
    private boolean answered;
    private boolean seeAll;

    private static final String TEST = "Test";
    private static final String ALERT = "Teste configurado como Avaliação!"
            + "\nResumo Bloqueado!\n";
    private static final String HELP = "Leia com atenção cada questão.\n"
            + "\nÉ possível ver as questões antes de responder clicando em "
            + "Avançar e Voltar.\n"
            + "Ao visualizar a última questão, o botão Revisar estará disponível"
            + " para retornar a primeira pergunta.\n"
            + "Ao selecionar uma alternativa clique em Avançar ou Voltar"
            + " para confirmar a escolha.\n"
            + "Para alterar uma resposta é necessário Limpar a questão primeiro.\n"
            + "\nÉ possível acompanhar as respostas pela aba Resumo.\n"
            + "O resumo estará bloqueado se o teste for uma Avaliação!\n"
            + "\nQuando for Teste de Treinamento será feita uma contagem de tempo.\n"
            + "A contagem regressiva iniciará quando escolher a primeira alternativa!\n"
            + "Se o tempo acabar não será possível alterar as respostas!\n"
            + "\nClique na aba Teste para visualizar as questões.\n"
            + "\nQuando terminar clique em Salvar e Sair para que as respostas"
            + " sejam salvas em um arquivo."
            + "\n\nBoa Sorte!";
    private static final int PLAYTIMER = 40;    // segundos por questão

    public Controller() {
        groups = new HashMap<>();
        users = new HashMap<>();
        availableTests = new HashMap<>();
        guiLogin = new Login();
        guiLogin.ouvirLogin(new ActionsGuiLogin());
        guiTest = new GuiTest();
        guiTest.eventTest(new ActionsGuiTest());
        guiTest.eventOptions(new ActionsGuiTestTable());
        guiTest.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                finalizeTest();
            }
        });

        txtAreaSummary = new BackGroundTextArea();
        txtAreaSummary.setText("");
        txtAreaSummary.setEditable(false);
        txtAreaSummary.setColumns(20);
        txtAreaSummary.setFont(new java.awt.Font("Arial", 0, 18));
        txtAreaSummary.setRows(5);
        txtAreaSummary.setMargin(new java.awt.Insets(10, 10, 10, 10));

        JScrollPane jScrollPaneSummary = new JScrollPane();
        jScrollPaneSummary.setViewportView(txtAreaSummary);
        guiTest.tablePanel.addTab("RESUMO", jScrollPaneSummary);

        txtAreaHelp = new BackGroundTextArea();
        txtAreaHelp.setText(HELP);
        txtAreaHelp.setEditable(false);
        txtAreaHelp.setColumns(20);
        txtAreaHelp.setFont(new java.awt.Font("Arial", 0, 18));
        txtAreaHelp.setRows(5);
        txtAreaHelp.setMargin(new java.awt.Insets(10, 10, 10, 10));

        JScrollPane jScrollPaneHelp = new JScrollPane();
        jScrollPaneHelp.setViewportView(txtAreaHelp);
        guiTest.tablePanel.addTab("INFORMAÇÕES GERAIS", jScrollPaneHelp);

        // controlador de tempo
        countTime = PLAYTIMER;
        timer = new Timer(1000, (ActionEvent e) -> {
            if (started) {
                countTime--;
            }
            if (countTime >= 0) {
                guiTest.lblTimer.setText(convert(countTime));
            } else {
                JOptionPane.showMessageDialog(null,
                        "Não é possível alterar as respostas!",
                        "Tempo Esgotado!",
                        JOptionPane.PLAIN_MESSAGE);
                ((Timer) (e.getSource())).stop();
                guiTest.btnClean.setEnabled(false);
                guiTest.btnNext.setEnabled(true);
                guiTest.btnPrevious.setEnabled(true);
                guiTest.btnReview.setEnabled(true);
            }
        });

        checkBaseFiles();
        loadData();
        login();
    }

    private void checkBaseFiles() {
        System.out.println("open application ...");
        try {
            pathGroups = LOCAL + Util.property(PROP, "PATHGROUPS");
            pathUsers = LOCAL + Util.property(PROP, "PATHUSERS");
            pathQuestions = LOCAL + Util.property(PROP, "PATHQUESTIONS");
            pathAvailableTests = LOCAL + Util.property(PROP, "PATHTEST");
            pathAnswers = LOCAL + Util.property(PROP, "PATHANSWER");
            pathImages = LOCAL + Util.property(PROP, "PATHIMAGES");
            pathImagesGui = LOCAL + Util.property(PROP, "PATHIMAGESGUI");
            pathThemes = LOCAL + Util.property(PROP, "PATHTHEMES");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        updateAvailableTests();
        updateGroups();
        updateUsers();
        updateAvaliableThemes();
    }

    private void updateAvailableTests() {
        availableTests = Util.loadMap(pathAvailableTests);
    }

    private void updateGroups() {
        groups = Util.loadMap(pathGroups);

        System.out.println("check groups and available tests ...");
        ArrayList<String> array = new ArrayList<>();
        groups.keySet().stream().filter((k) -> (k != null)).forEachOrdered((k) -> {
            System.out.print(k);
            if (availableTests.containsKey(k)) {
                array.add(k);
                System.out.println(" Ok ...");
            } else {
                System.out.println(" don't found ...");
            }
        });

        Collections.sort(array);
        guiLogin.cboxGroup.removeAllItems();
        array.forEach((s) -> {
            guiLogin.cboxGroup.addItem(s);
        });
    }

    private void updateUsers() {
        String selectedGroup = String.valueOf(guiLogin.cboxGroup.getSelectedItem());
        System.out.println("Usuários de " + selectedGroup + " ...");

        if (selectedGroup == null) {
            return;
        }

        String file = (String) groups.get(selectedGroup);
        if (file == null) {
            return;
        } else {
            file = pathUsers + file;
        }
        users = Util.loadMap(file);
        System.out.println(file + " ...");
        if (Util.fileExists(file)) {
            guiLogin.cboxUser.setEnabled(true);
            guiLogin.lblUser.setEnabled(true);
            ArrayList<String> array = new ArrayList<>();
            users.keySet().stream().filter((k) -> (k != null)).forEachOrdered((k) -> {
                array.add(users.get(k));
            });

            Collections.sort(array);
            guiLogin.cboxUser.removeAllItems();
            array.forEach((s) -> {
                guiLogin.cboxUser.addItem(s);
            });
        } else {
            guiLogin.cboxUser.setEnabled(false);
            guiLogin.lblUser.setEnabled(false);
        }
    }

    private void updateAvaliableThemes() {
        String selectedGroup = String.valueOf(guiLogin.cboxGroup.getSelectedItem());
        System.out.println("Temas de " + selectedGroup + " ...");

        if (selectedGroup == null) {
            return;
        }

        availableThemes = Util.loadMap(pathThemes);
        System.out.println(pathThemes + " ...");

        String file = (String) availableThemes.get(selectedGroup);
        System.out.println(file + " ...");

        guiLogin.cboxTheme.removeAllItems();
        if (file == null) {
            guiLogin.cboxTheme.setEnabled(false);
            guiLogin.lblTheme.setEnabled(false);
            currentTheme = "Não disponível";
            seeAll = true;
            return;
        } else {
            guiLogin.cboxTheme.setEnabled(true);
            guiLogin.lblTheme.setEnabled(true);
            seeAll = false;
        }

        ArrayList<String> array = Util.loadFile(pathQuestions + file);
        if (array != null) {
            Collections.sort(array);
            array.forEach((s) -> {
                guiLogin.cboxTheme.addItem(s);
            });
        }
    }

    private void login() {
        currentQuestion = 1;
        currentTheme = "";
        started = false;
        congratulations = false;
        countTime = PLAYTIMER;
        timer.setInitialDelay(0);
        timer.start();
        guiTest.btnClean.setEnabled(true);
        guiTest.btnNext.setEnabled(true);
        guiTest.btnPrevious.setEnabled(true);
        guiTest.btnReview.setEnabled(false);
        guiLogin.setVisible(true);
        guiTest.setVisible(false);
    }

    private void closeLogin() {
        currentGroup = String.valueOf(guiLogin.cboxGroup.getSelectedItem());
        if (guiLogin.cboxUser.isEnabled()) {
            currentUser = String.valueOf(guiLogin.cboxUser.getSelectedItem());
        } else {
            currentUser = "usuário";
        }

        String file = "";
        if (Util.fileExists(pathAvailableTests)) {
            ArrayList<String> array = Util.loadFile(pathAvailableTests);
            for (String s : array) {
                String[] str = s.split(Util.DELIM);
                if (str[0].equals(currentGroup)) {
                    if (str.length > 1) {
                        file = str[1];
                        break;
                    }
                }
            }
        }

        System.out.println("check questions ... " + file + "...");
        if (Util.fileExists(pathQuestions + file)) {
            if (guiLogin.cboxTheme.isEnabled()) {
                currentTheme = String.valueOf(guiLogin.cboxTheme.getSelectedItem());
            }
            System.out.println(currentUser + " : " + currentGroup + " : "
                    + currentTheme + " : " + pathQuestions + file);
            startTest(currentGroup, currentUser, pathQuestions + file);
        } else {
            if (inform("Não há teste para:\n" + currentGroup
                    + "\nDeseja encerrar?", "Atenção!")) {
                exitLogin();
            }
        }
    }

    private void exitLogin() {
        System.out.println("close application ...");
        System.exit(0);
    }

    private void startTest(String currentGroup, String currentUser,
            String file) {
        if (loadQuestions(file)) {
            String pathBg = pathImagesGui + "background.png";
            try {
                Image image = ImageIO.read(new File(pathBg));
                if (image != null) {
                    txtAreaSummary.setBackgroundImage(image);
                    txtAreaHelp.setBackgroundImage(image);
                }
            } catch (IOException ex) {
            }

            // atualiza o tempo
            countTime = PLAYTIMER * (currentTest.size() - 1);
            System.out.println(countTime);

            guiTest.tablePanel.setSelectedIndex(2);
            guiTest.lblUser.setText(currentUser + " : " + currentGroup);
            guiLogin.setVisible(false);
            guiTest.setVisible(true);
            answered = false;
            updateQuestion();
        } else {
            String str = "Não há teste para:\n" + currentGroup
                    + "\nTema: " + currentTheme;
            if (inform(str + "\nDeseja encerrar?", "Atenção!")) {
                exitLogin();
            }
        }
    }

    private boolean loadQuestions(String arquivo) {
        System.out.println("Carregar questões ...");
        boolean emptyQuestions = true;
        currentTest = new ArrayList<>();
        ArrayList<String> questions = Util.loadFile(arquivo);

        String[] str = questions.get(0).split(Util.DELIM);
        Question q = new Question(0, str[0], null, null);
        currentTest.add(q);

        int number = 1;
        for (int i = 1; i < questions.size(); i++) {
            str = questions.get(i).split(Util.DELIM);
            ArrayList<String> options = new ArrayList<>();
            if (str.length > 4) {
                if (currentTheme.equals(str[2]) || seeAll) {
                    // Question(numero da questão, questao, resposta, pathImagem)               
                    q = new Question(number++, str[3], str[1], str[0]);
                    for (int j = 4; j < str.length; j++) {
                        options.add(str[j]);
                    }
                    q.setOptions(options);
                    currentTest.add(q);
                    emptyQuestions = false;
                }
            }
        }

        return !emptyQuestions;
    }

    private void updateQuestion() {
        if (currentQuestion < 1) {
            currentQuestion = 1;
        }
        if (currentQuestion >= currentTest.size() - 1) {
            currentQuestion = currentTest.size() - 1;
            guiTest.btnReview.setEnabled(true);
        } else {
            guiTest.btnReview.setEnabled(false);
        }

        // atualiza questão
        String str = "Questão " + currentTest.get(currentQuestion).getNumber()
                + ") " + currentTest.get(currentQuestion).getQuestion();
        guiTest.txtAreaQuestion.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        guiTest.txtAreaQuestion.setText(str);

        // atualiza alternativas na tabela
        updateOptions(currentTest.get(currentQuestion).getCurrentAnswer());

        // atualiza informe
        updateReports();

        // atualiza imagem da questão
        updateImageQuestion();

        // atualiza o resumo com as respostas
        updateSummary();
    }

    private void updateSavedResponses() {
        int optionSaved = currentTest.get(currentQuestion).getCurrentAnswer();
        int row = guiTest.tbOptions.getSelectedRow();
        if (optionSaved == -1) {
            currentTest.get(currentQuestion).setCurrentAnswer(row);
            if (currentTest.get(currentQuestion).getAnswer().equals(String.valueOf(row))) {
                currentTest.get(currentQuestion).setHit(true);
            } else {
                currentTest.get(currentQuestion).setHit(false);
            }
        }
    }

    private void updateOptions(int chosenOption) {
        ArrayList<String> options = currentTest.get(currentQuestion).getOptions();
        String[][] elements = new String[options.size()][1];
        for (int i = 0; i < options.size(); i++) {
            if (chosenOption == i) {
                elements[i][0] = String.valueOf(i + 1) + ") " + options.get(i).toUpperCase();
            } else {
                elements[i][0] = String.valueOf(i + 1) + ") " + options.get(i);
            }
        }

        guiTest.tbOptions.setModel(new javax.swing.table.DefaultTableModel(
                elements, new String[]{
                    "Alternativas para questão "
                    + String.valueOf(currentTest.get(currentQuestion).getNumber())
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false
            };
        });
    }

    private void updateReports() {
        int optionSaved = currentTest.get(currentQuestion).getCurrentAnswer();
        if (optionSaved == -1) {
            guiTest.lblInform.setText("Questão sem resposta!"
                    + " Selecione uma alternativa ou"
                    + " Avançar (Voltar) para ver outra questão.");
        } else {
            String info = "Alternativa Selecionada [ ";
            info += String.valueOf(currentTest.get(currentQuestion).getCurrentAnswer() + 1);
            info += " ]. Para alterar clique em LIMPAR ";
            info += "e selecione outra alternativa.";
            guiTest.lblInform.setText(info);
        }
    }

    private void updateImageQuestion() {
        String imagePath = pathImages + currentTest.get(currentQuestion).getPathImage();
        //System.out.println(imagePath);
        try {
            BufferedImage imgOriginal = ImageIO.read(new File(imagePath));
            Image img = imgOriginal.getScaledInstance(
                    guiTest.lblImage.getWidth(),
                    guiTest.lblImage.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imgLabel = new ImageIcon(img);
            guiTest.lblImage.setText("");
            guiTest.lblImage.setIcon(imgLabel);
        } catch (IOException ex) {
            String str = "Questão sem imagem disponível!";
            guiTest.lblImage.setText(str);
            guiTest.lblImage.setIcon(null);
        }
    }

    private void updateSummary() {
        if (!currentTest.get(0).getQuestion().equals(TEST)) {
            txtAreaSummary.setText(ALERT);
        } else {
            currentHits = 0;
            currentNumberOfIssues = currentTest.size() - 1;
            String str = "";
            String summary = currentUser + " : " + currentGroup + "\n\n";

            for (int i = 1; i < currentTest.size(); i++) {
                str += currentTest.get(i).getNumber() + ") ";
                if (currentTest.get(i).getCurrentAnswer() != -1) {
                    answered = true;
                    if (currentTest.get(i).getHit()) {
                        str += " acertou\n";
                        currentHits++;
                    } else {
                        str += " resposta incorreta\n";
                    }
                } else {
                    str += " sem resposta\n";
                }
            }
            if (answered) {
                summary += "Acertos: " + String.valueOf(currentHits)
                        + " de " + String.valueOf(currentNumberOfIssues)
                        + "\n\n" + str;
            } else {
                summary = "Nenhuma resposta foi preenchida!";
            }

            txtAreaSummary.setText(summary);

            if (currentHits == currentNumberOfIssues && started) {
                guiTest.tablePanel.setSelectedIndex(1);
                started = false;
                congratulations = true;
                guiTest.btnClean.setEnabled(false);
                guiTest.btnNext.setEnabled(true);
                guiTest.btnPrevious.setEnabled(true);
                guiTest.btnReview.setEnabled(true);
                if (inform("Acertou todas as perguntas.\nDeseja salvar e encerrar?",
                        "PARABÉNS")) {
                    finalizeTest();
                }
            }
        }
    }

    private void finalizeTest() {
        started = false;

        ArrayList answers = new ArrayList<>();
        answers.add(currentUser + Util.DELIM + currentGroup
                + Util.DELIM + currentTheme);
        answers.add("Questão" + Util.DELIM + "Resposta Esperada"
                + Util.DELIM + "Resposta Usuário" + Util.DELIM + "Situação");
        for (int i = 1; i < currentTest.size(); i++) {
            Question q = currentTest.get(i);
            String output = q.getNumber() + Util.DELIM;
            output += q.getAnswer() + Util.DELIM;
            if (q.getCurrentAnswer() == -1) {
                output += "nulo" + Util.DELIM;
            } else {
                output += q.getCurrentAnswer() + Util.DELIM;
            }
            if (q.getHit()) {
                output += "acertou";
            } else {
                output += "errou";
            }
            answers.add(output);

            try {
                String path = pathAnswers + currentUser.replace(" ", "-")
                        + "-" + currentTheme.replace(" ", "-") + ".csv";
                Util.export(answers, path);
                System.out.println("exported " + path + " ...");
                login();
            } catch (IOException ex) {
                System.out.println("error while exporting file ...");
            }
        }
    }

    private boolean inform(String mensagem, String titulo) {
        int dialogResult = JOptionPane.showConfirmDialog(guiTest,
                mensagem, titulo, JOptionPane.YES_NO_OPTION);
        return dialogResult == 0;
    }

    class ActionsGuiLogin implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiLogin.btnLogin) {
                closeLogin();
            }
            if (source == guiLogin.btnQuit) {
                exitLogin();
            }
            if (source == guiLogin.cboxGroup) {
                updateUsers();
                updateAvaliableThemes();
            }
        }
    }

    class ActionsGuiTest implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiTest.btnClean) {
                currentTest.get(currentQuestion).setHit(false);
                currentTest.get(currentQuestion).setCurrentAnswer(-1);
                updateQuestion();
            }
            if (source == guiTest.btnPrevious) {
                updateSavedResponses();
                currentQuestion--;
                updateQuestion();
            }
            if (source == guiTest.btnNext) {
                updateSavedResponses();
                currentQuestion++;
                updateQuestion();
            }
            if (source == guiTest.btnReview) {
                currentQuestion = 1;
                updateQuestion();
            }
            if (source == guiTest.btnFinish) {
                started = false;
                if (inform("Teste será salvo com as respostas atuais.\nDeseja encerrar?",
                        "Fechar Teste")) {
                    finalizeTest();
                } else {
                    started = true && !congratulations;
                }
            }
            if (source == guiTest.btnUser) {
                started = false;
                if (inform("As respostas não serão salvas!"
                        + "\nDeseja trocar o usuário?", "Trocar Usuário")) {
                    login();
                } else {
                    started = true && !congratulations;
                }
            }
        }
    }

    class ActionsGuiTestTable implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent ev) {
            int optionSaved = currentTest.get(currentQuestion).getCurrentAnswer();
            if (optionSaved == -1) {
                System.out.println("Contagem regressiva liberada...");
                started = true;
            }
        }

        @Override
        public void mousePressed(MouseEvent ev) {
        }

        @Override
        public void mouseReleased(MouseEvent ev) {
        }

        @Override
        public void mouseEntered(MouseEvent ev) {
        }

        @Override
        public void mouseExited(MouseEvent ev) {
        }
    }

    static class BackGroundTextArea extends JTextArea {

        private Image backgroundImage;

        public BackGroundTextArea() {
            super();
        }

        public void setBackgroundImage(Image image) {
            this.backgroundImage = image;
            this.repaint();
            this.setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this);
            }
            super.paintComponent(g);
        }
    }

    private String convert(int count) {
        String str = "";
        int minutos = count / 60;
        int segundos = count % 60;
        String strMinutos = Integer.toString(minutos);
        String strSegundos = Integer.toString(segundos);

        if (minutos < 10) {
            strMinutos = "0" + strMinutos;
        }

        if (segundos < 10) {
            strSegundos = "0" + strSegundos;
        }

        if (count > 90) {
            str += strMinutos + " : " + strSegundos;
            guiTest.lblTimer.setForeground(Color.black);
        } else {
            if (count > 60) {
                str += strMinutos + " : " + strSegundos;
            } else {
                str += "00 : " + strSegundos;
            }
            if ((count % 2) == 0) {
                guiTest.lblTimer.setForeground(Color.black);
            } else {
                guiTest.lblTimer.setForeground(Color.red);
            }
        }
        return str;
    }
}
