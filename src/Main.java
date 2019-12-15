
import controller.ControllerEdit;
import controller.ControllerQuiz;
import controller.ControllerWelcome;
import controller.Util;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static String LOCAL = Util.userDir();
    private final static String PROP = LOCAL + "/resources/resources.properties";

    public static void main(String[] args) {

        // checar recursos
        checkBaseFiles();

        // inicializar
        Object display = null;
        if (args.length != 1) {
            display = new ControllerWelcome();
        } else {
            switch (args[0]) {
                case "--edit":   // Editar Teste existente
                    display = new ControllerEdit(false);
                    break;
                case "--new":   // Criar Novo Teste
                    display = new ControllerEdit(true);
                    break;
                case "--quiz":  // Abrir Quiz
                    display = new ControllerQuiz();
                    break;
                default:        // Abrir Menu Principal
                    display = new ControllerWelcome();
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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!check) {
            System.err.println("configuration directory not found!");
            System.exit(0);
        }
    }
}
