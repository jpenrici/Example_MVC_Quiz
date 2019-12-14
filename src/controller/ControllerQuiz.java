package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import model.Question;
import view.GuiResult;
import view.GuiTest;
import view.Login;

public class ControllerQuiz {

    private final static String ANSWERS = "";
    private final static String LOCAL = Util.userDir();
    protected final static String PROP = LOCAL + "/resources/pathTests.properties";    

    private GuiTest guiTest = null;
    private Login guiLogin = null;
    private GuiResult guiResult = null;
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
    private static final String GUEST = "Visitante";
    private static final String ALERT = "Teste configurado como Avaliação!"
            + "\nResumo Bloqueado!\n";
    private static final String HELP = "Leia com atenção cada questão.\n"
            + "\nÉ possível ver as questões antes de responder clicando em "
            + "Avançar, Voltar, Primeira ou Última.\n"
            + "Ao selecionar uma alternativa clique em Avançar ou Voltar"
            + " para mudar a questão.\n"
            + "Para alterar uma resposta basta clicar em outra opção.\n"
            + "\nÉ possível acompanhar as respostas pela aba Resumo.\n"
            + "O resumo estará bloqueado se o teste for uma Avaliação!\n"
            + "\nQuando for Teste de Treinamento será feita uma contagem de tempo.\n"
            + "A contagem regressiva iniciará quando escolher a primeira alternativa!\n"
            + "Se o tempo acabar não será possível alterar as respostas!\n"
            + "\nClique na aba Teste para visualizar as questões.\n"
            + "\nQuando terminar clique em Salvar e Sair para que as respostas"
            + " sejam salvas em um arquivo."
            + "\n\nBoa Sorte!";
    private static final int PLAYTIMER = 50;    // segundos por questão

    public ControllerQuiz() {
        // dados
        groups = new HashMap<>();
        users = new HashMap<>();
        availableTests = new HashMap<>();

        // GUI Login
        guiLogin = new Login();
        guiLogin.eventLogin(new ActionsGuiLogin());
        guiLogin.setAlwaysOnTop(true);
        guiLogin.setResizable(false);

        // GUI Result
        guiResult = new GuiResult();
        guiResult.eventResult(new ActionsGuiResult());
        guiResult.setAlwaysOnTop(false);
        guiResult.setPreferredSize(new Dimension(800, 500));        
        guiResult.txtAreaSummary.setText("");

        // GUI Teste
        guiTest = new GuiTest();
        guiTest.eventTest(new ActionsGuiTest());
        guiTest.eventOptions(new ActionsGuiTestTable());
        guiTest.setAlwaysOnTop(false);
        guiTest.setResizable(false);
        guiTest.setPreferredSize(new Dimension(850, 600));
        guiTest.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.out.println("pause application ...");
                if (inform("Teste será salvo com as respostas atuais.\nDeseja encerrar?",
                        "Fechar Teste")) {
                    finalizeTest();
                } else {
                    System.out.println("continue ...");
                }
            }
        });
        
        // tabela de alternativas
        guiTest.tbOptions.setFont(new java.awt.Font("Arial", Font.BOLD, 14));

        // resumo
        txtAreaSummary = new BackGroundTextArea();
        txtAreaSummary.setText("");
        txtAreaSummary.setEditable(false);
        txtAreaSummary.setColumns(20);
        txtAreaSummary.setFont(new java.awt.Font("Arial", 0, 18));
        txtAreaSummary.setForeground(Color.white);
        txtAreaSummary.setBackground(Color.black);
        txtAreaSummary.setRows(5);
        txtAreaSummary.setMargin(new java.awt.Insets(10, 10, 10, 10));

        JScrollPane jScrollPaneSummary = new JScrollPane();
        jScrollPaneSummary.setViewportView(txtAreaSummary);
        guiTest.tablePanel.addTab("RESUMO", jScrollPaneSummary);

        // informe (help)
        txtAreaHelp = new BackGroundTextArea();
        txtAreaHelp.setText(HELP);
        txtAreaHelp.setEditable(false);
        txtAreaHelp.setColumns(20);
        txtAreaHelp.setFont(new java.awt.Font("Arial", 0, 18));
        txtAreaHelp.setForeground(Color.white);
        txtAreaHelp.setRows(5);
        txtAreaHelp.setMargin(new java.awt.Insets(10, 10, 10, 10));

        JScrollPane jScrollPaneHelp = new JScrollPane();
        jScrollPaneHelp.setViewportView(txtAreaHelp);
        guiTest.tablePanel.addTab("INFORMAÇÕES GERAIS", jScrollPaneHelp);

        // contagem de tempo
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
                started = false;
            }
        });

        // iniciar
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
            pathImages = LOCAL + Util.property(PROP, "PATHIMAGES");
            pathImagesGui = LOCAL + Util.property(PROP, "PATHIMAGESGUI");
            pathThemes = LOCAL + Util.property(PROP, "PATHTHEMES");
            // respostas salvas em diretório individual
            pathAnswers = ANSWERS + Util.property(PROP, "PATHANSWER");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControllerQuiz.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (Util.createDirectory(pathAnswers)) {
            System.out.println(pathAnswers + " ok ...");
        } else {
            System.out.println("new directory ... " + pathAnswers + " ...");
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
            guiLogin.cboxUser.addItem("Visitante");
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
        System.out.println("theme ... " + selectedGroup + " ...");

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
        // nova rodada
        currentQuestion = 1;
        currentGroup = "Grupo";
        currentUser = "Usuário";
        currentTheme = "";
        started = false;            // contagem de tempo liberada = falso
        congratulations = false;    // acertou todas as respostas = falso
        countTime = PLAYTIMER;      // tempo mínimo por questão em segundos
        timer.setInitialDelay(0);
        timer.start();              // iniciar objeto Timer
        guiLogin.setVisible(true);
        guiLogin.cboxUser.setSelectedIndex(0);
        guiTest.setVisible(false);
        guiTest.lblUser.setText(currentUser + " : " + currentGroup);
        guiResult.setVisible(false);
        guiResult.txtAreaSummary.setText("");
    }

    private void closeLogin() {
        currentGroup = String.valueOf(guiLogin.cboxGroup.getSelectedItem());
        if (guiLogin.cboxUser.isEnabled()) {
            currentUser = String.valueOf(guiLogin.cboxUser.getSelectedItem());
        } else {
            currentUser = "usuário";
        }

        // checar testes disponíveis para o grupo escolhido
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
            startTest(pathQuestions + file);
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

    private void startTest(String file) {
        if (loadQuestions(file)) { // carregar questões
            String pathBg = pathImagesGui + "background.png";
            try { // alterar background da aba resumo e informe (help)
                Image image = ImageIO.read(new File(pathBg));
                if (image != null) {
                    txtAreaSummary.setBackgroundImage(image);
                    txtAreaHelp.setBackgroundImage(image);
                }
            } catch (IOException ex) {
            }

            // atualizar o tempo para contagem regressiva
            if (!currentTest.get(0).getQuestion().equals(TEST)) {
                System.out.println("hidden time counter ...");
                guiTest.lblTimer.setVisible(false);
                //guiTest.lblTimerText.setVisible(false);
                guiTest.lblTimerText.setText("Leia com atenção!");
                // tempo para tipo avaliação
                countTime = 60 * 60 * 24;
            } else {
                guiTest.lblTimer.setVisible(true);
                guiTest.lblTimerText.setVisible(true);
                guiTest.lblTimerText.setText("Tempo Restante:");
                // tempo para tipo treinamento, jogo
                countTime = PLAYTIMER * (currentTest.size() - 1);
            }
            txtAreaSummary.setText(ALERT);
            System.out.println("updated time ... " + countTime + " seconds");

            // preparar GUI Teste
            guiTest.tablePanel.setSelectedIndex(2); // abrir em help
            guiLogin.setVisible(false);
            guiTest.setVisible(true);
            answered = false;

            // visitante, solicitar nome
            if (currentUser.equalsIgnoreCase(GUEST)) {
                do {
                    currentUser = JOptionPane.showInputDialog("Digite seu nome:");
                } while (currentUser == null);
                if (currentUser.isEmpty()) {
                    currentUser = "Usuário Visitante";
                }
            }
            guiTest.lblUser.setText(currentUser + " : " + currentGroup);

            // atualizar questão
            updateQuestion();

        } else {
            String str = "Não há teste para:\n" + currentGroup
                    + "\nTema: " + currentTheme;
            if (inform(str + "\nDeseja encerrar?", "Atenção!")) {
                exitLogin();
            }
        }
    }

    private boolean loadQuestions(String file) {
        System.out.println("load questions ...");
        boolean emptyQuestions = true;
        currentTest = new ArrayList<>();
        ArrayList<String> questions = Util.loadFile(file);

        // embaralhar questões
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 1; i < questions.size(); i++) {
            temp.add(questions.get(i));
        }
        Collections.shuffle(temp);
        System.out.println("shuffle list ...");
        for (int i = 0; i < temp.size(); i++) {
            questions.set(i + 1, temp.get(i));
        }

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
        }

        // atualizar questão
        String str = "Questão " + currentTest.get(currentQuestion).getNumber()
                + " de " + Integer.toString(currentTest.size() - 1) + ":\n"
                + currentTest.get(currentQuestion).getQuestion();
        guiTest.txtAreaQuestion.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        guiTest.txtAreaQuestion.setText(str);

        // atualizar alternativas na tabela
        updateOptions(currentTest.get(currentQuestion).getCurrentAnswer());

        // atualizar informe (help)
        updateReports();

        // atualizar imagem da questão
        updateImageQuestion();

        // atualizar o resumo com as respostas
        updateSummary();
    }

    private void updateSavedResponses(int row) {
        // salvar a resposta escolhida pelo usuário
        int optionSaved = currentTest.get(currentQuestion).getCurrentAnswer();
        if (optionSaved == -1) { // se questão sem resposta, atualizar
            currentTest.get(currentQuestion).setCurrentAnswer(row);
        } else {
            if (optionSaved != row) {
                if (inform("Deseja trocar a alternativa?", "Atenção")) {
                    currentTest.get(currentQuestion).setCurrentAnswer(row);
                }
            }
        }
        // checar se a resposta escolhida está certa
        if (currentTest.get(currentQuestion).getAnswer().equals(String.valueOf(row))) {
            currentTest.get(currentQuestion).setHit(true);
        } else {
            currentTest.get(currentQuestion).setHit(false);
        }
        updateOptions(currentTest.get(currentQuestion).getCurrentAnswer());
        updateSummary();
        updateReports();
    }

    private void updateOptions(int chosenOption) {
        // atualizar alternativas no JTable
        ArrayList<String> options = currentTest.get(currentQuestion).getOptions();
        String[][] elements = new String[options.size()][1];
        for (int i = 0; i < options.size(); i++) {
            if (chosenOption == i) {
                elements[i][0] = String.valueOf(i + 1) + ") " + options.get(i).toUpperCase();
            } else {
                elements[i][0] = String.valueOf(i + 1) + ") " + options.get(i);
            }
        }

        guiTest.tbOptions.setModel(new DefaultTableModel(elements, new String[]{
            "Alternativas para questão "
            + String.valueOf(currentTest.get(currentQuestion).getNumber())
        }
        ) {
            boolean[] canEdit = new boolean[]{false};
        });

        // atualizar cor das linhas
        Enumeration<TableColumn> en = guiTest.tbOptions.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = en.nextElement();
            tc.setCellRenderer(new OptionsColor());
        }
    }

    private void updateReports() {
        // atualizar informe sobre a questão
        String info;
        int optionSaved = currentTest.get(currentQuestion).getCurrentAnswer();
        if (optionSaved == -1) {
            info = "Questão sem resposta!";
            if (!congratulations && (countTime >= 0)) {
                info += " Selecione uma alternativa.";
            }
        } else {
            info = "Alternativa Selecionada [ ";
            info += String.valueOf(currentTest.get(currentQuestion).getCurrentAnswer() + 1);
            info += " ]. ";
            if (!congratulations && (countTime >= 0)) {
                info += "Para alterar selecione outra alternativa.";
            }
        }
        guiTest.lblInform.setText(info);
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

    private String formatAnswers(int output) {
        String delim;
        switch (output) {
            case 0: // txtAreaSummary
            case 1: // export txt
                delim = "\n";
                break;
            default: // export csv
                delim = Util.DELIM;
        }

        String summary = ""; // cabeçalho padrão
        summary += "Usuário: " + currentUser + "\n";
        summary += "Grupo  : " + currentGroup + "\n";
        summary += "Tema   : " + currentTheme + "\n\n";

        switch (output) {
            case 0: // txtAreaSummary
            case 1: // export txt
                break;
            default: // export csv
                summary += "Questão" + delim;
                summary += "Resposta Esperada" + delim;
                summary += "Resposta Usuário" + delim;
                summary += "Lógica" + delim;
                summary += "Situação" + delim + delim;
                summary += "Questão" + delim;
                summary += "Alternativas\n";
        }

        String str = "";
        currentHits = 0;
        currentNumberOfIssues = currentTest.size() - 1;
        for (int i = 1; i < currentTest.size(); i++) {
            switch (output) {
                case 0: // txtAreaSummary
                    str += currentTest.get(i).getNumber() + ") ";
                    break;
                case 1: // export txt
                    str += delim + currentTest.get(i).getQuestion() + delim;
                    str += "Opções:" + delim;
                    for (int j = 0; j < currentTest.get(i).getOptions().size(); j++) {
                        str += j + ") " + currentTest.get(i).getOptions().get(j) + "\n";
                    }
                    str += "Opção Esperada: " + currentTest.get(i).getAnswer();
                    str += ") " + currentTest.get(i).getOptions().get(Integer.parseInt(currentTest.get(i).getAnswer())) + delim;
                    if (currentTest.get(i).getCurrentAnswer() != -1) {
                        str += "Opção Escolhida: " + currentTest.get(i).getCurrentAnswer();
                        str += ") " + currentTest.get(i).getOptions().get(currentTest.get(i).getCurrentAnswer()) + delim;
                    }
                    str += "Situação: ";
                    break;
                default: // export csv
                    str += currentTest.get(i).getNumber() + delim;
                    str += currentTest.get(i).getAnswer() + delim;
                    str += currentTest.get(i).getCurrentAnswer() + delim;
                    str += "= (" + currentTest.get(i).getAnswer() + "="
                            + currentTest.get(i).getCurrentAnswer() + ")+0"
                            + delim;
            }

            if (currentTest.get(i).getCurrentAnswer() != -1) {
                answered = true;
                if (currentTest.get(i).getHit()) {
                    str += " acertou" + delim;
                    currentHits++;
                } else {
                    str += " resposta incorreta" + delim;
                }
            } else {
                str += " sem resposta" + delim;
            }

            if (output == 2) {
                str += delim + currentTest.get(i).getQuestion() + delim;
                str = currentTest.get(i).getOptions().stream().map((s)
                        -> s + delim).reduce(str, String::concat);
                str += "\n";
            }
        }

        if (answered) {
            summary += str + "\nAcertos: " + String.valueOf(currentHits)
                    + " de " + String.valueOf(currentNumberOfIssues);
        } else {
            summary = "Nenhuma resposta foi preenchida!";
        }

        return summary;
    }

    private void updateSummary() {
        // atualizar resumo
        if (!currentTest.get(0).getQuestion().equals(TEST)) {
            txtAreaSummary.setText(ALERT);
        } else {
            String summary = formatAnswers(0);
            txtAreaSummary.setText(summary);

            if (currentHits == currentNumberOfIssues && started) {
                //guiTest.tablePanel.setSelectedIndex(1);
                started = false;
                congratulations = true;
                guiTest.tablePanel.setSelectedIndex(1); // abrir em resumo               
                if (inform("Acertou todas as perguntas.\nDeseja salvar e encerrar?",
                        "PARABÉNS")) {
                    finalizeTest();
                }
            }
        }
    }

    private void finalizeTest() {
        started = false; // parar contagem regressiva

        // salvar respostas no arquivo de saída
        String summaryTXT = formatAnswers(1);
        String summaryCSV = formatAnswers(2);

        String path = pathAnswers + currentUser.replace(" ", "-")
                + "-" + currentTheme.replace(" ", "-");
        path += "_" + Util.timeNow();
        Util.export(summaryCSV, path + ".csv");
        Util.export(summaryTXT, path + ".txt");

        // exibir resumo (resultados)
        if (!currentTest.get(0).getQuestion().equals(TEST)) {
            guiResult.txtAreaSummary.setText(formatAnswers(1));
            guiTest.setVisible(false);
            guiResult.setVisible(true);
        } else {
            login();
        }
    }

    private boolean inform(String mensagem, String titulo) {
        int dialogResult = JOptionPane.showConfirmDialog(guiTest,
                mensagem, titulo, JOptionPane.YES_NO_OPTION);
        return dialogResult == 0;

    }

    class ActionsGuiLogin implements ActionListener {
        // controle dos botões do GUI Login

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
    
    class ActionsGuiResult implements ActionListener {
        // controle dos botões do GUI Resumo

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiResult.btnClose) {
                login();
            }
        }
    }    

    class ActionsGuiTest implements ActionListener {
        // controle dos botões do GUI Teste

        @Override
        public void actionPerformed(ActionEvent ev) {
            Object source = ev.getSource();
            if (source == guiTest.btnFirst) {
                currentQuestion = 1;
                updateQuestion();
            }
            if (source == guiTest.btnPrevious) {
                currentQuestion--;
                updateQuestion();
            }
            if (source == guiTest.btnNext) {
                currentQuestion++;
                updateQuestion();
            }
            if (source == guiTest.btnLast) {
                currentQuestion = currentTest.size() - 1;
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
        // controle de seleção de alternativas

        @Override
        public void mouseClicked(MouseEvent ev) {
            started = true && !congratulations && (countTime >= 0);
            System.out.print("count status ... ");
            if (!guiTest.lblTimer.isVisible()) {
                System.out.println("irrelevant ...");
            } else {
                System.out.println(started + " ...");
            }
            if (started) {
                updateSavedResponses(guiTest.tbOptions.getSelectedRow());
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

    private class BackGroundTextArea extends JTextArea {
        // alterar cor de JTextArea

        private Image backgroundImage;

        public BackGroundTextArea() {
            super();
        }

        public void setBackgroundImage(Image image) {

            image = image.getScaledInstance(
                    guiTest.tablePanel.getWidth() - 10,
                    guiTest.tablePanel.getHeight() - 10,
                    Image.SCALE_SMOOTH);
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

    private class OptionsColor extends DefaultTableCellRenderer
            implements TableCellRenderer {
        // alterar cor das alternativas na JTable

        Color optionColor = new Color(0, 152, 152);     // azul

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground(null);
            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            setText(String.valueOf(value));

            // checar se questão tem alternativa salva
            if (row == currentTest.get(currentQuestion).getCurrentAnswer()) {
                setBackground(optionColor);
            } else {
                setBackground(null);
            }
            return this;
        }
    }

    private String convert(int count) {
        // contagem de tempo em minutos:segundos
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

        // alertar em cor quando chegar a 1:30 do final
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
