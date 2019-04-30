import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Program SudokuSolverG uses recursive backtracking to solve a
// standard game of Sudoku. This is a graphical version of the
// console utility
public class SudokuSolverG {
	public static void main(String[] args) throws FileNotFoundException {
		Thread[] solvers = null;
		if (args.length > 0) {
			solvers = new Thread[args.length];
			for (int i = 0; i < args.length; i++) {
				solvers[i] = new Thread(constructBoard(args[i]));
				solvers[i].start();
			}
			joinThreads(solvers);
		} else {
			Scanner console = new Scanner(System.in);
			Queue<String> fileNames = new LinkedList<String>();
			System.out.print("file name (return to exit): ");
			String fileName = console.nextLine();
			while (fileName.length() > 0) {
				fileNames.add(fileName);
				System.out.println(fileNames);
				System.out.print("file name (return to exit):");
				fileName = console.nextLine();
			}
			if (fileNames.size() > 0) {
				System.out.println("...solving puzzles...");
				solvers = new Thread[fileNames.size()];
				int i = 0;
				while (!fileNames.isEmpty()) {
					String str = fileNames.remove();
					if (str != "") {
						solvers[i] = new Thread(constructBoard(str));
						solvers[i].start();
						i++;
					}
				}
					
				System.out.println();
				joinThreads(solvers);
			} else {
				System.out.println("no files entered.");
				System.exit(0);
			}
			console.close();
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
	
	private static void joinThreads(Thread[] solvers) {
		for (int i = 0; i < solvers.length; i++) {
			try {
				solvers[i].join();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}