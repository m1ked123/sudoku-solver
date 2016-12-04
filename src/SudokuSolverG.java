import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Program SudokuSolverG uses recursive backtracking to solve a
// standard game of Sudoku. This is a graphical version of the
// console utility
public class SudokuSolverG {
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length > 0) {
			Thread[] solvers = new Thread[args.length];
			for (int i = 0; i < args.length; i++) {
				solvers[i] = new Thread(constructBoard(args[i]));
				solvers[i].start();
			}
			for (int i = 0; i < solvers.length; i++) {
				try {
					solvers[i].join();
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	// constructs and returns the board that is to be solved from the given file name.
	// If the given file is not valid or not found, will throw a FileNotFoundException
	public static BoardFrame constructBoard(String fileName) throws FileNotFoundException {
		if (fileName.lastIndexOf('.') < 0) {
			// means the file name was not entered properly, attempt
			// to insert file extension
			System.out.println("missing file extension, inserting...");
			fileName += ".txt";
		}
		Scanner input = new Scanner(new File(fileName));
		return new BoardFrame(input, fileName);
	}

	// attempts to solve the given sudoku board. if there is a solution
	// it prints the original puzzle along with that solution. otherwise
	// it prints that no solution was found for the given puzzle.
	public static void solve(SudokuBoard b) {
		if (explore(b, 1, 1)) {
			b.setComplete(true);
		} else {
			System.exit(0);
		}
	}
	
	// returns whether there is a solution to the current board or not
	// uncomment the print statements in order to print debug info.
	public static boolean explore(SudokuBoard b, int r, int c) { 
		if (r > b.size()) { 		
			return true;			
		} else {
			while (c <= 9 && b.get(r, c) != SudokuBoard.UNASSIGNED) {
				if (c == 9 && b.get(r, c) != SudokuBoard.UNASSIGNED) {
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
				if (b.canPlace(c,  r, n)) {
					b.place(c, r, n);
					if (c < 9) {
						if (explore(b, r, c + 1)) {
							return true;
						} 
					} else {
						if (explore(b, r + 1, 1)) {
							return true;
						}
					}
					b.remove(c, r);
				}
			}
			return false;
		}
	}
}