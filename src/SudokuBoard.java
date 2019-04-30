import java.io.PrintStream;
import java.util.Scanner;

/**
 * Class <code>SudokuBoard</code> represents a normal 9 by 9 board of 
 * Sudoku. By "normal" it is meant that this class represents a 
 * standard board of Sudoku that is defined as a 9 by 9 grid of squares
 * in which some of the squares are already filled in and the role of
 * the player is to fill in each box with a number between 1 and 9.
 * @author Michael Davis
 *
 */
public class SudokuBoard implements Runnable {
	public static final int UNASSIGNED = -100; // the unassigned value. it can be arbitrarily low or high, should not be 1-9
	private int[][] board; // the internal structure of the board
	private boolean complete = false; // whether this board has been completed
	
	/**
	 * This is the default constructor for the SudokuBoard. It creates
	 * an empty 9 by 9 board;
	 */
	public SudokuBoard() {
		board = new int[9][9];
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				board[r][c] = UNASSIGNED;
			}
		}
	}

	/**
	 * Constructs a new board from the given input. The input is
	 * assumed to be delimited by whitespace of some kind. There is no
	 * guarantee that this class will be properly formatted otherwise
	 * and may produce a false positive or negative result. This class
	 * will not add more than 9 lines of input when constructing itself.
	 * The input is assumed to be open for reading and tied to a valid
	 * file.
	 * <br />
	 * A properly formatted text input file will have each row
	 * represented as a single line of text with numbers from 1 to 9
	 * separated by some sort of whitespace that is not a newline.
	 * Spaces that do not have values should be denoted with a 0.
	 * @param input the input file that will build this board
	 * @throws IllegalArgumentException if for some reason the input
	 * is pointing to nothing. This is just a precaution in case the
	 * input stream is not valid.
	 */
	public SudokuBoard(Scanner input) {
		if (input == null) {
			throw new IllegalArgumentException("improper input");
		}
		int line = 0;
		board = new int[9][9];
		Scanner lineParser = null; // parses through the given line
		while (line < 9 && input.hasNextLine()) { 
			int col = 0;
			lineParser = new Scanner(input.nextLine());
			while (col < 9 && lineParser.hasNextInt()) {
				int n = lineParser.nextInt();
				if (n == 0) {
					board[line][col] = UNASSIGNED;
				} else {
					board[line][col] = n;
				}
				col++;
			}
			line++;
		}
		lineParser.close();
	}
	
	public SudokuBoard(int[][] board) {
		this.board = board;
	}
		
	/**
	 * Inserts the given number into the given row and column. If the
	 * number is 0 or greater than 9, the space will be given
	 * the value of UNASSIGNED.
	 * @param row the row in which the given number is to be added
	 * @param col the column in which the given number is to be added
	 * @param n the number to be added to the board. No value will be
	 * assigned if the number is less than or equal to 0 or greater
	 * than 9
	 */
	public void insert(int row, int col, int n) {
		validateRowAndColumn(row, col);
		if (n > 0 && n < 9 ) {
			board[row][col] = n;
		} else {
			board[row][col] = UNASSIGNED;
		}
	}

	/**
	 * Gets the size of this board which is defined as the length of
	 * the board and should always be 9 for a normal Sudoku board
	 * @return the size of the board
	 */
	public int size() {
		return board.length;
	}

	/**
	 * Returns whether it is valid to put the given number at the given
	 * location on the board. A location is considered valid if it does
	 * not already occur in the same row, column, and 3 by 3 sub-square
	 * in the board. This method will throw and exception if the number
	 * being explored is greater than 9 or less than 0. It will also
	 * throw an exception if the row or column being explored is greater
	 * than 9 or less than or equal to 0.
	 * @param col the column in which the given number is being placed
	 * @param row the row that in which the given number is being paced
	 * @param n the number that is to be placed at the given locations
	 * @return true if the location is valid, false otherwise.
	 * @throws IllegalArgumentException if 0 <= row < 9 or 0 <= col < 9
	 * or 0 <= 0 < 9
	 */
	public boolean canPlace(int col, int row, int n) {
		if (n <= 0 || n > 9) {
			throw new IllegalArgumentException("incorrect target number."
					+ " please enter a number that is between 1 and 9."
					+ "\n\tn=" + n);
		}
		validateRowAndColumn(row, col);
		// assumes that the client program does not take 0-based indexing
		// into account and resets the values of row and col
		row--;
		col--;
		if (board[row][col] == UNASSIGNED) {
			if (checkColumn(col, n) && checkRow(row, n) && checkSquare(row, col, n)) {
				return true;
			}
		} 
		return false;
	}

	/**
	 * Places the given number at the given location in the board. The
	 * location and the number must be legal.
	 * @param row the row in which the given number is being added
	 * @param col the column in which the given number is being added
	 * @param n the number being added to the board
	 * @throws IllegalArgumentException if 0 <= row < 9 or 0 <= col < 9
	 * or 0 <= n < 9
	 */
	public void place(int col, int row, int n) {
		if (n <= 0 || n > 9) {
			throw new IllegalArgumentException("incorrect target number."
					+ " please enter a number that is between 1 and 9."
					+ "\n\tn=" + n);
		}
		validateRowAndColumn(row, col);
		board[row - 1][col - 1] = n;
	}

	/**
	 * Removes whatever is at the given location in the board.
	 * @param row the row of the location that is to be emptied
	 * @param col the column of the location that is to be emptied
	 * @throws IllegalArgumentException if 0 <= row < 9 or 0 <= col < 9
	 */
	public void remove(int col, int row) {
		validateRowAndColumn(row, col);
		board[row - 1][col - 1] = UNASSIGNED;
	}

	/**
	 * Prints the board for console and debug versions of this
	 * program. Empty spaces are denoted with a hyphen ("-").
	 */
	public void print() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				if (board[row][col] == UNASSIGNED) {
					System.out.print(" - ");
				} else {
					System.out.print(" " + board[row][col] + " ");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints this board to the given output. It is assumed that this
	 * output is suitable and open to be written to.
	 * @param output the stream to which output text will be written
	 */
	public void print(PrintStream output) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				if (board[row][col] == UNASSIGNED) {
					output.print(" - ");
				} else {
					output.print(" " + board[row][col] + " ");
				}
			}
			output.println();
		}
	}
	
	/**
	 * Gets the number at the given row and column.
	 * @param row the row from which the number is being retrieved
	 * @param col the column from which the number is being retrieved
	 * @throws IllegalArgumentException if 0 <= row < 9 or 0 <= col < 9
	 * @return the number at the given location in the board
	 */
	public int get(int row, int col) {
		validateRowAndColumn(row, col);
		row--;
		col--;
		return board[row][col];
	}
	
	/**
	 * Gets whether or not this board is completed.
	 * @return true if the board has been solved, false otherwise
	 */
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Sets the completeness of this board to the given value.
	 * @param bool the completeness of this SudokuBoard
	 */
	public void setComplete(boolean bool) {
		complete = bool;
	}

	@Override
	public void run() {
		solve();
	}
	
	/*
	 * validates the given row and column. Throws an
	 * IllegalArgumentException if 9 < row <= 0 || 9 < col <= 0
	 */
	private void validateRowAndColumn(int row, int col) {
		if (row > 9 || row <= 0) {
			throw new IllegalArgumentException("incorrect target row."
					+ " please enter a row that is between 1 and 9."
					+ "\n\trow=" + row);
		} else if (col > 9 || col <= 0) {
			throw new IllegalArgumentException("incorrect target column."
					+ " please enter a column that is between 1 and 9."
					+ "\n\tcol=" + row);
		}
	}
	
	/* BOARD CHECK METHODS */
	
	// checks the given column and returns false if the given number
	// is at all present in that column
	private boolean checkColumn(int col, int n) {
		for (int currRow = 0; currRow < board.length; currRow++) {
			if (board[currRow][col] != UNASSIGNED) {
				if (board[currRow][col] == n) {
					return false;
				}
			}
		}
		return true;
	}
	
	// checks the given row to see if the given number is present in it.
	// returns false if the number exists in the present row
	private boolean checkRow(int row, int n) {
		for (int currCol = 0; currCol < board.length; currCol++) {
			if (board[row][currCol] != UNASSIGNED) {
				if (board[row][currCol] == n) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Checks the 3 by 3 subsquare that the given number is in. returns
	// false if there is another instance of that number in the subsquare
	private boolean checkSquare(int row, int col, int n) {
		for(int currRow = row / 3 * 3; currRow < row / 3 * 3 + 3; currRow++){
			for (int currCol = col / 3 * 3; currCol < col / 3 * 3 + 3; currCol++){
				if (board[currRow][currCol] == n) {
					return false;
				}
			}
		}
		return true;
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
		if (r > board.length) { 		
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