package model;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
	private static Language instance;

	private static Locale locale;
	private static ResourceBundle bundle;

	public static synchronized Language getInstance() {
		if(instance == null) {
			instance = new Language();
		}

		locale = DataManager.loadLanguage();
		bundle = ResourceBundle.getBundle("language.messages", locale);
		return instance;
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}
}
