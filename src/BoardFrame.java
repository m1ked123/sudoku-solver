import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class <code>BoardFrame</code> represents a graphical instance of the
 * <code>SudokuBoard</code> class. It contains a 9 by 9 grid of buttons
 * that display the contents of the board. As the computer solves the
 * puzzle, the contents are updated as needed. The time it takes for the
 * computer to show that it has solved the Sudoku puzzle when using this
 * class is usually much longer than it would using the console implementation
 * of the program. The reason for this is that there is a 50ms pause 
 * between each program tick.
 * @author Michael Davis
 *
 */
public class BoardFrame extends SudokuBoard implements ChangeListener {
	private static int windowCount;
	private JPanel contentPanel, toolsPanel, mainPanel; // the content panel for this class
	private JFrame frame; // the main window
	private JLabel[][] labels; // the buttons that make up the board
	private JSlider speedSlider; // the slider to adjust the speed
	private int delay = 100; // the delay for the timer
	
	/**
	 * Constructs a new BoardFrame object with the given input stream
	 * as its source. Each new board is 500 * 500 pixels and will default
	 * position itself in the upper left-hand corner of the screen.
	 * @param input the input data stream that has already been hooked up
	 * @param fileName the name of the file linked to the input stream
	 */
	public BoardFrame(Scanner input, String fileName) {
		super(input);
		windowCount++;
		contentPanel = new JPanel(new GridLayout(9, 9));
		toolsPanel = new JPanel(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		frame = new JFrame("Sudoku Solver - " + fileName);
		labels = new JLabel[9][9];
		speedSlider = new JSlider(0, delay, delay);
		speedSlider.setPaintTicks(true);
		speedSlider.setMajorTickSpacing(delay / 10);
		speedSlider.setPaintLabels(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.addChangeListener(this);
		toolsPanel.add(speedSlider, BorderLayout.CENTER);
		mainPanel.add(toolsPanel, BorderLayout.SOUTH);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int n = super.get(i + 1, j + 1);
				labels[i][j] = new JLabel();
				labels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				labels[i][j].setVerticalAlignment(JLabel.CENTER);
				labels[i][j].setHorizontalAlignment(JLabel.CENTER);
				labels[i][j].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
				if (n == -100) {
					labels[i][j].setText("-");
				} else {
					labels[i][j].setText("" + n);
					labels[i][j].setForeground(Color.BLUE);
				}
				contentPanel.add(labels[i][j]);
			}
		}
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.setPreferredSize(new Dimension(500, 500));
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Window windowInFocus = evt.getWindow();
				if (!isComplete()) {
					System.out.println("not done!");
					// show dialog that asks to see if user would really
					// like to exit. if answer to that is "yes" then close
					// the window and kill the thread.
				}
				windowInFocus.setVisible(false);
				windowCount--;
				if (windowCount == 0) {
					System.exit(0);
				}
			}
		});
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	// pauses the simulation by the delay time
	private void pause() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			return;
		}
	}
	
	@Override
	public void remove(int col, int row) {
		labels[row - 1][col - 1].setForeground(Color.RED);
		labels[row - 1][col - 1].setText("X");
		pause();
		super.remove(col, row);
		labels[row - 1][col - 1].setText("-");
		labels[row - 1][col - 1].setForeground(Color.BLACK);
		pause();
	}
	
	@Override
	public void place(int col, int row, int n) {
		labels[row - 1][col - 1].setText("" + n);
		super.place(col, row, n);
		pause();
	}
	
	@Override
	public boolean canPlace(int col, int row, int n) {
		labels[row-1][col-1].setText("...");
		pause();
		labels[row-1][col-1].setText("-");
		return super.canPlace(col, row, n);		
	}
	
	@Override
	public void run() {
		solve();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		if (!slider.getValueIsAdjusting()) {
			if (slider.getValue() == 0) {
				this.delay = 1;
			} else {
				this.delay = slider.getValue();
			}
		}
	}
	
	// attempts to solve the given sudoku board. if there is a solution
		// it prints the original puzzle along with that solution. otherwise
		// it prints that no solution was found for the given puzzle.
		public void solve() {
			if (explore(1, 1)) {
				this.setComplete(true);
				System.out.println("Board Complete");
				this.print();
			} else {
				System.exit(0);
			}
		}
		
		// returns whether there is a solution to the current board or not
		// uncomment the print statements in order to print debug info.
		public boolean explore(int r, int c) { 
			if (r > this.size()) { 		
				return true;			
			} else {
				while (c <= 9 && this.get(r, c) != SudokuBoard.UNASSIGNED) {
					if (c == 9 && this.get(r, c) != SudokuBoard.UNASSIGNED) {
						if (r + 1 > 9) {
							return true;
						}
						r += 1;
						c = 1;
					} else {
						c++;
					}
				}
				for (int n = 1; n <= 9; n++) {
					if (this.canPlace(c,  r, n)) {
						this.place(c, r, n);
						if (c < 9) {
							if (explore(r, c + 1)) {
								return true;
							} 
						} else {
							if (explore(r + 1, 1)) {
								return true;
							}
						}
						this.remove(c, r);
					}
				}
				return false;
			}
		}
}