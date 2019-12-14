
import controller.ControllerEdit;
import controller.ControllerQuiz;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            // Modo Quiz
            ControllerQuiz newController = new ControllerQuiz();
        } else {
            if (args[0].equalsIgnoreCase("--edit")) {
                // Modo Editar Questões
                System.out.println("open application in edit mode ...");
                ControllerEdit newController = new ControllerEdit();
            }
        }
    }
}
