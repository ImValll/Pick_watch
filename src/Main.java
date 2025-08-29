import model.DataManager;
import model.Language;
import view.Frame;

public class Main {
	public static void main(String[] args) {
		Language.getInstance();

		new Frame();
	}
}