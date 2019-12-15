
import controller.ControllerEdit;
import controller.ControllerQuiz;
import controller.ControllerWelcome;

public class Main {

    public static void main(String[] args) {

        Object display;

        if (args.length != 1) {
            display = new ControllerWelcome();
        } else {
            switch (args[0]) {
                case "--edit":
                    display = new ControllerEdit();
                    break;
                case "--quiz":
                    display = new ControllerQuiz();
                    break;
                default:
                    display = new ControllerWelcome();
            }
        }
    }
}
