import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainProgram {
	public static void main(String[] args) throws FileNotFoundException {
		giveIntro();
		Thread[] solvers = null;
		if (args.length > 0) {
			solvers = SudokuSolver.getSolvers(args);
		} else {
			Scanner console = new Scanner(System.in);
			Queue<String> fileNames = new LinkedList<String>();
			System.out.print("file name (return to exit): ");
			String fileName = console.nextLine();
			while (fileName.length() > 0) {
				fileNames.add(fileName);
				System.out.println(fileNames);
				System.out.print("file name (return to exit): ");
				fileName = console.nextLine();
			}
			if (fileNames.size() > 0) {
				String[] filePaths = new String[fileNames.size()];
				int i = 0;
				while (!fileNames.isEmpty()) {
					filePaths[i] = fileNames.remove();
					i++;
				}
				solvers = SudokuSolver.getSolvers((filePaths));
			} else {
				System.out.println("no files entered.");
				System.exit(0);
			}
			console.close();
		}
		
		for (int i = 0; i < solvers.length; i++) {
			solvers[i].start();
		}
		System.out.println();
		SudokuSolver.joinThreads(solvers);
	}
	
	// introduces the user to the program.
	public static void giveIntro() {
		System.out.println("This program takes a text representation of a Sudoku board");
		System.out.println("and prints out its solution.");
		System.out.println();
	}
}
