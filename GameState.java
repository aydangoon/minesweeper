import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import java.util.*;
public class GameState extends JPanel implements MouseListener {

	private GameStateHandler gsh;
	private Space[][] board;
	
	public GameState() {
		this.gsh = new GameStateHandler(this);
		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
		this.setFocusable(true);
		this.setVisible(true);
		this.setBackground(Color.GRAY);

		board = new Space[Constants.COLUMNS][Constants.ROWS];
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				board[i][j] = new Space();
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Rectangles
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				if(board[i][j].isHidden()) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i*Constants.SPACE_HEIGHT, j*Constants.SPACE_WIDTH, 
							   Constants.SPACE_WIDTH, Constants.SPACE_HEIGHT);
					
					if(board[i][j].isFlagged()) {
						g.setColor(Color.RED);
						g.drawLine(i*Constants.SPACE_HEIGHT, j*Constants.SPACE_WIDTH, 
								   (i+1)*Constants.SPACE_HEIGHT, (j+1)*Constants.SPACE_WIDTH);
						g.drawLine((i+1)*Constants.SPACE_HEIGHT, j*Constants.SPACE_WIDTH, 
								   i*Constants.SPACE_HEIGHT, (j+1)*Constants.SPACE_WIDTH);
					}
				}else {
					
					if(board[i][j].hasBomb()) {
						g.setColor(Color.BLACK);
						g.fillOval(i*Constants.SPACE_HEIGHT, j*Constants.SPACE_WIDTH, 
								   Constants.SPACE_WIDTH, Constants.SPACE_HEIGHT);
					}else {
						g.setColor(Color.WHITE);
						g.drawString(""+board[i][j].getNumber(), i*Constants.SPACE_HEIGHT + 7, (j+1)*Constants.SPACE_WIDTH - 5);
					}
				}
			}
		}
		
		//White Lines
		g.setColor(Color.WHITE);
		for(int i = 1; i<Constants.COLUMNS; i++) {
			g.drawLine(i*Constants.SPACE_WIDTH, 0, i*Constants.SPACE_WIDTH, Constants.GAME_HEIGHT);
		}
		for(int i = 1; i<Constants.ROWS; i++) {
			g.drawLine(0,i*Constants.SPACE_HEIGHT,Constants.GAME_WIDTH,i*Constants.SPACE_HEIGHT);
		}
	}
	
	
	private void calculateNumber(int c, int r) {
		int counter = 0;
		for(int i = c - 1; i <= c + 1; i++) {
			for(int j = r - 1; j <= r + 1; j++) {
				if(i >= 0 && j >= 0 && j < Constants.ROWS && i < Constants.COLUMNS && board[i][j].hasBomb()) {
					counter++;
				}
			}
		}
		board[c][r].setNumber(counter);
	}
	private void explosion(int c, int r) {
		int[][] positions = new int[][] {new int[] {c - 1, r},
										 new int[] {c - 1, r - 1},
										 new int[] {c - 1, r + 1},
										 new int[] {c + 1, r},
										 new int[] {c + 1, r - 1},
										 new int[] {c + 1, r + 1},
										 new int[] {c, r - 1}, 
										 new int[] {c, r + 1}};
		
		for(int[] p : positions) {
			if(p[0] >= 0 && p[1] >= 0 && p[1] < Constants.ROWS && p[0] < Constants.COLUMNS && 
			   !(p[0] == c && p[1] == r) && board[p[0]][p[1]].isHidden()) {
				board[p[0]][p[1]].doAction();
				if(board[p[0]][p[1]].getNumber() == 0) {
					explosion(p[0], p[1]);
				}
			}
		}
	}
	private boolean hasWon() {
		int spacesLeft = 0;
		for(int i = 0; i<board.length;i++) {
			for(int j = 0; j<board[i].length; j++) {
				spacesLeft += board[i][j].isHidden() ? 1 : 0;
			}
		}
		return spacesLeft == Constants.NUMBER_OF_BOMBS;
	}
	
	public void endGame() {
		this.removeMouseListener(this);
	}
	public void startGame() {
		board = new Space[Constants.COLUMNS][Constants.ROWS];
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				board[i][j] = new Space();
			}
		}
		List<int[]> bombLocations = new LinkedList<int[]>();
		while(bombLocations.size() < Constants.NUMBER_OF_BOMBS) {
			Random r = new Random();
			int[] toAdd = new int[] {r.nextInt(Constants.COLUMNS), r.nextInt(Constants.ROWS)};
			boolean add = true;
			for(int[] xyPos : bombLocations) {
				if(xyPos[0] == toAdd[0] && xyPos[1] == toAdd[1]) {
					add = false;
				}
			}
			if(add) {
				bombLocations.add(toAdd);
			}
		}
		for(int[] xyPos : bombLocations) {
			board[xyPos[0]][xyPos[1]].setBomb();
		}
		
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				calculateNumber(i, j);
			}
		}
		this.addMouseListener(this);
		this.repaint();
	}

	public void mousePressed(MouseEvent e) {
		int c = (int) e.getPoint().getX() / Constants.SPACE_HEIGHT;
		int r = (int) e.getPoint().getY() / Constants.SPACE_WIDTH; 
		
		if(SwingUtilities.isLeftMouseButton(e)) {
			String broadcast = board[c][r].doAction();
			if(broadcast.equals("game over")) {
				endGame();
				gsh.setState(State.LOST);
			}
			if(board[c][r].getNumber() == 0) {
				explosion(c, r);
			}
			if(hasWon()) {
				endGame();
				gsh.setState(State.WON);
			}
		}else {
			board[c][r].setFlag();
		}
		
		this.repaint();
		
	}
	public GameStateHandler getStateHandler() {
		return gsh;
	}

	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}
