import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;


public class Game extends JFrame {

	private static final GameState game = new GameState();
	private static final GameStateHandler gsh = game.getStateHandler();
	
	public static void main(String[] args) {
		
		Game window = new Game();
		
		window.setTitle("Minesweeper");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setSize(new Dimension(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT));
	    window.setLayout(new FlowLayout());
	    window.setResizable(false);
	    window.add(game);
	    window.add(gsh);
	    window.setVisible(true);
	}
}
