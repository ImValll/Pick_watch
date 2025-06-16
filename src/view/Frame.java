package view;

import model.ButtonEditor;
import view.movie.MovieFrame;
import view.parameter.ParameterFrame;
import view.saga.SagaFrame;
import view.serie.SerieFrame;
import view.serie_courte.SerieCourteFrame;

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

		JButton btnMovie = ButtonEditor.createButton("FILMS", new Color(70, 130, 180));
		JButton btnSaga = ButtonEditor.createButton("SAGAS", new Color(70, 130, 180));
		JButton btnSerie = ButtonEditor.createButton("SERIES", new Color(70, 130, 180));
		JButton btnSerieCourte = ButtonEditor.createButton("SERIES COURTE", new Color(70, 130, 180));
		JButton btnParameter = ButtonEditor.createButton("PARAMETRES", new Color(220, 20, 60));
		JButton btnQuitter = ButtonEditor.createButton("QUITTER", new Color(220, 20, 60));


		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 30, 15, 30);

		add(btnMovie, gbc);
		add(btnSaga, gbc);
		add(btnSerie, gbc);
		add(btnSerieCourte, gbc);
		add(btnParameter, gbc);
		add(btnQuitter, gbc);

		btnMovie.addActionListener(e -> movieFunction());
		btnSaga.addActionListener(e -> sagaFunction());
		btnSerie.addActionListener(e -> serieFunction());
		btnSerieCourte.addActionListener(e -> serieCourteFunction());
		btnParameter.addActionListener(e -> parameterFunction());
		btnQuitter.addActionListener(e -> quitter());

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

	private void serieCourteFunction() {
		dispose();
		new SerieCourteFrame();
	}

	private void parameterFunction() {
		dispose();
		new ParameterFrame();
	}

	private void quitter() {
		dispose();
	}
}
