import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GameStateHandler extends JPanel{

	private State state;
	private JButton toggleStart;
	private JLabel timeDisplay;
	private JTextArea highScoreDisplay;
	private JTextField nameInput;
	private GameState g;
	private Timer timer;
	private int time;
	private List<Score> highScores;
	
	public GameStateHandler(GameState g) {
		state = State.NOT_PLAYING;
		this.g = g;
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTime();
			}
		});
		
		JTextArea instructionText = new JTextArea();
		
		instructionText.setPreferredSize(new Dimension(Constants.GSH_WIDTH, Constants.GSH_HEIGHT / 4));
		instructionText.setLineWrap(true);
		instructionText.setWrapStyleWord(true);
		instructionText.setBorder(BorderFactory.createTitledBorder("Instructions:"));
		String text = "Welcome to classic Minesweeper. Use the left click to open a space. Use the right click to flag it.";
		text += " Enter your name in the Name display to save your score should you win. High Scores are displayed below.";
		text += "Press start to begin.";
		instructionText.setText(text);
		
		this.add(instructionText);
		
		toggleStart = new JButton("start");
		toggleStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				state = state == State.PLAYING ? State.NOT_PLAYING : State.PLAYING;
				String newText = state == State.PLAYING ? "end" : "start";
				toggleStart.setText(newText);
				
				if(state == State.PLAYING) {
					time = 0;
					timeDisplay.setText("Time: " + time);
					g.startGame();
					timer.start();
					
				}else {
					g.endGame();
					timer.stop();
				}
				
			}
		});
		this.add(toggleStart);
		
		timeDisplay = new JLabel("Time: " + time);
		this.add(timeDisplay);
		
		this.setPreferredSize(new Dimension(Constants.GSH_WIDTH, Constants.GSH_HEIGHT));
		this.setFocusable(true);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		
		nameInput = new JTextField("unknown");
		nameInput.setPreferredSize(new Dimension(Constants.GSH_WIDTH, 40));
		nameInput.setBorder(BorderFactory.createTitledBorder("Name:"));
		this.add(nameInput);
		
		highScores = getHighScores();
		highScoreDisplay = new JTextArea();
		updateHighScoreText();
		highScoreDisplay.setPreferredSize(new Dimension(Constants.GSH_WIDTH, Constants.GSH_HEIGHT / 3));
		highScoreDisplay.setBorder(BorderFactory.createTitledBorder("High Scores:"));
		this.add(highScoreDisplay);
		
	}
	/**
	 * Sets the state of the GameStateHandler object
	 * state is determined by whether the player won
	 * or lost.
	 * */
	public void setState(State s) {
		state = s;
		switch(state) {
			case WON:
				updateHighScores();
				updateHighScoreText();
			case LOST:
			
				timer.stop();
				toggleStart.setText("restart");
				
			break;
		
		}
	}
	/***
	 * Updates the time variable and time display with
	 * the appropriate text
	 */
	private void updateTime() {
		time+=1;
		timeDisplay.setText("Time: " + time);
	}
	/***
	 * Adds a new score entry to the high scores list if
	 * that score falls within the top ten range.
	 * It also writes this high score list to the high
	 * scores text file
	 */
	private void updateHighScores() {
		Score toAdd = new Score(nameInput.getText(), time);
		int index = 0;
		while(index < highScores.size() && highScores.get(index).getRight() < toAdd.getRight()) {
			index++;
		}
		highScores.add(index, toAdd);
		
		while(highScores.size() > 10) {
			highScores.remove(highScores.size() - 1);
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.HIGH_SCORE_PATH, false));
			for(Score s : highScores) {
				bw.write(s.getLeft() + ":" + s.getRight() + "\n");
			}
			bw.close();
		} catch (IOException e) {
		}
		
	}
	/***
	 * Updates the high scores text every time a new entry is added
	 */
	private void updateHighScoreText() {
		String text =  "";
		for(int i = 0; i < highScores.size(); i++) {
			text += (i+1) + ") " + highScores.get(i).getLeft() + " : " + highScores.get(i).getRight() + "\n";
		}
		highScoreDisplay.setText(text);
		
	}
	/***
	 * -this returns a list of scores. It reads from the -high scores text file. 
	 */
	private List<Score> getHighScores() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Constants.HIGH_SCORE_PATH));
	
			List<Score> highScores = new LinkedList<Score>();
			try {
				while(br.ready() && highScores.size() < Constants.MAX_HIGH_SCORE_SIZE) {
					String[] pair = br.readLine().split(":");
					highScores.add(new Score(pair[0], Integer.parseInt(pair[1])));
				}
				br.close();
				return highScores;
			} 
			catch (NumberFormatException e) {} 
			catch (IOException e) {}
			
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		return null;
	}
}
