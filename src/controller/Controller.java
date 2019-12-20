package controller;

import java.io.FileNotFoundException;

public class Controller {

    public final static String LOCAL = Util.userDir();
    public final static String PROP = LOCAL + "/resources/resources.properties";

    public Controller(String[] args) {

        // checar recursos
        checkBaseFiles();

        if (args.length != 1) {
            ControllerWelcome display = new ControllerWelcome();
        } else {
            switch (args[0]) {
                case "--edit":   // Editar Teste existente
                    ControllerEdit displayEdit = new ControllerEdit();
                    break;
                case "--new":   // Criar Novo Teste
                    ControllerEdit displayEditNew = new ControllerEdit(true);
                    break;
                case "--quiz":  // Abrir Quiz
                    ControllerQuiz displayQUiz = new ControllerQuiz();
                    break;
                default:        // Abrir Menu Principal
                    ControllerWelcome displayDefault = new ControllerWelcome();
            }
        }
    }

    private static void checkBaseFiles() {
        System.out.println("check base files ...");
        boolean check = true;

        try {
            check = Util.pathExists(LOCAL + Util.property(PROP, "PATHGROUPS"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHUSERS"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHQUESTIONS"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHSAVEQUESTIONS"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHIMAGES"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHIMAGESGUI"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHIMAGESBTN"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHTEST"));
            check &= Util.pathExists(LOCAL + Util.property(PROP, "PATHTHEMES"));

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }

        if (!check) {
            System.err.println("configuration directory not found!");
            System.exit(0);
        }
    }
}
