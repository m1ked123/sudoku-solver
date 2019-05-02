import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainProgram {
	public static void main(String[] args) throws FileNotFoundException {
		giveIntro();
		Thread[] solvers = null;
		if (args.length > 0) {
			solvers = new Thread[args.length];
			for (int i = 0; i < args.length; i++) {
				solvers[i] = new Thread(constructBoard(args[i]));
				solvers[i].start();
			}
			System.out.println();
			for (int i = 0; i < solvers.length; i++) {
				try {
					solvers[i].join();
				} catch (InterruptedException e) {
					return;
				}
			}
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
				String str = fileNames.remove();
				if (str != "") {
					solvers[i] = new Thread(constructBoard(str));
					solvers[i].start();
					i++;
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
	
	private static void joinThreads(Thread[] solvers) {
		for (int i = 0; i < solvers.length; i++) {
			try {
				solvers[i].join();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	// introduces the user to the program.
	public static void giveIntro() {
		System.out.println("This program takes a text representation of a Sudoku board");
		System.out.println("and prints out its solution.");
		System.out.println();
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

	// attempts to solve the given sudoku board. if there is a solution
	// it prints the original puzzle along with that solution. otherwise
	// it prints that no solution was found for the given puzzle.
	public static void solve(SudokuBoard b) {
		if (explore(b, 1, 1)) {
			b.setComplete(true);
			System.out.println("SOLUTION");
			b.print();
		} else {
			System.out.println("NO SOLUTION FOUND");
		}
	}
	
	// returns whether there is a solution to the current board or not
	// uncomment the print statements in order to print debug info.
	public static boolean explore(SudokuBoard b, int row, int col) { 
		if (row > b.size()) { 		
			return true;			
		} else {
			while (col <= 9 && b.get(row, col) != SudokuBoard.UNASSIGNED) {
				if (col == 9 && b.get(row, col) != SudokuBoard.UNASSIGNED) {
					if (row + 1 > 9) {
						return true;
					}
					row += 1;
					col = 1;
				} else {
					col++;
				}
			}
			for (int n = 1; n <= 9; n++) {
				if (b.canPlace(col,  row, n)) {
					b.place(col, row, n);
					if (col < 9) {
						if (explore(b, row, col + 1)) {
							return true;
						} 
					} else {
						if (explore(b, row + 1, 1)) {
							return true;
						}
					}
					b.remove(col, row);
				}
			}
			return false;
		}
	}
}
