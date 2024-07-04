package view;

import view.movie.MovieFrame;
import view.saga.SagaFrame;
import view.serie.SerieFrame;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
	public Frame() {

		super("MENU");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700); // Taille augmentée
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(50, 50, 50)); // Fond sombre pour le contraste
		setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un meilleur contrôle

		JButton btnMovie = createButton("FILMS", new Color(70, 130, 180));
		JButton btnSaga = createButton("SAGAS", new Color(70, 130, 180));
		JButton btnSerie = createButton("SERIES", new Color(70, 130, 180));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnMovie, gbc);
		add(btnSaga, gbc);
		add(btnSerie, gbc);

		btnMovie.addActionListener(e -> movieFunction());
		btnSaga.addActionListener(e -> sagaFunction());
		btnSerie.addActionListener(e -> serieFunction());

		this.setVisible(true);
	}

	private void movieFunction() {
		dispose();
		new MovieFrame();
	}

	private void sagaFunction() {
		dispose();
		new SagaFrame();
	}

	private void serieFunction() {
		dispose();
		new SerieFrame();
	}

	public JButton createButton(String title, Color color) {
		JButton button = new JButton(title);

		button.setBackground(color); // Bleu foncé
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

		return button;
	}
}
