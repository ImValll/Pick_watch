package model;

import javax.swing.*;

public class Caller extends JPanel {
	public Caller() {
		initPanel();
	}

	private void initPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void addItem(JCheckBox jCBox) {
		this.add(jCBox);
	}
}
