import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Program SudokuSolver uses recursive backtracking to solve a
// standard game of Sudoku. The user can either enter files as
// command line arguments or enter them in as input. Currently
// only the programmer has the ability to toggle debug mode. This
// program is a thread safe implementation and allows for solving
// multiple boards at once.
public class SudokuSolver {
	
	/**
	 * Gets a thread that will solve the Sudoku board defined by the
	 * text file at the given string file path. The thread will be
	 * ready to be started by the client. The thred will be null if
	 * the provided path is empty.
	 * @param boardFilePath the path to the file holding the String representation 
	 * of a sudoku board to solve.
	 * @return a Thread with a SudokuBoard that will be solved. The
	 * thread will be null if the file path provided is empty
	 * @throws FileNotFoundException if the file defined by this 
	 */
	public static Thread getSolver(String boardFilePath) 
			throws FileNotFoundException {
		Thread solver = null;
		if (boardFilePath != null && boardFilePath.length() > 0) {
			solver = new Thread(constructBoard(boardFilePath));
			System.out.println();
		}
		return solver;
	}
	
	/**
	 * Gets a collection of threads that will solve the Sudoku boards
	 * defined by the given list of file paths. The threads will
	 * be ready to start when they are returned. If the list of paths
	 * is empty or null, the returned collection will be null.
	 * @param boardFilePaths a list of paths to sudoku board 
	 * representations
	 * @return a collection of threads if the list of paths is neither
	 * empty nor null. Will return null otherwise
	 * @throws FileNotFoundException if any of the file paths provided
	 * cannot be found on disk
	 */
	public static Thread[] getSolvers(String[] boardFilePaths) 
			throws FileNotFoundException {
		int numPaths = boardFilePaths.length;
		if (boardFilePaths != null && numPaths > 0) {
			Thread[] solvers = null;
			solvers = new Thread[numPaths];
			for (int i = 0; i < numPaths; i++) {
				solvers[i] = new Thread(constructBoard(boardFilePaths[i]));
			}
			return solvers;
		} else {
			return null;
		}
	}
	
	/**
	 * Joins all the solver threads in the provided list and waits for
	 * them to complete/die. If the thread is interrupted at any point,
	 * the function will return with false.
	 * @param solvers a list of solver threads
	 */
	public static boolean joinThreads(Thread[] solvers) {
		for (int i = 0; i < solvers.length; i++) {
			try {
				solvers[i].join();
			} catch (InterruptedException e) {
				return false;
			}
		}
		return true;
	}
	
	// constructs and returns the board that is to be solved from the given file name.
	// If the given file is not valid or not found, will throw a FileNotFoundException
	public static SudokuBoard constructBoard(String fileName) throws FileNotFoundException {
		if (fileName.lastIndexOf('.') < 0) {
			// means the file name was not entered properly, attempt
			// to insert file extension
			System.out.println("missing file extension, inserting...");
			fileName += ".txt";
		}
		Scanner input = new Scanner(new File(fileName));
		SudokuBoard b = new SudokuBoard(input);
		return b;
	}
}