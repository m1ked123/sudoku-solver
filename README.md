# Sudoku Solver

A basic text-based Sudoku solving program that examines a given 
Sudoku puzzle, solves, and prints said solution. There is a graphical viewer for the SudokuSolver that
allows the user to see each step of the solving process in a graphical manner.

## Sudoku Board Files 
There are a number of example Sudoku board files stored in the "examples"
folder. Sudoku board files are simple text files that serve as representations
of a standard Sudoku board. Below is an example of one of these files...

```
0 8 0 3 0 5 0 0 0
0 1 0 0 6 0 0 0 5
0 0 2 1 9 0 3 0 0
0 0 3 2 0 0 7 0 9
7 0 0 0 0 0 6 0 0
0 5 1 0 3 7 8 0 0
0 0 6 0 8 0 0 0 3
2 7 8 0 0 3 1 0 6
0 0 0 0 2 0 0 0 0
```

Cells in the board that are supposed to be empty should be represented
by a 0. Cells with values should have those values listed.

## Improvements
There is no version that allows the user to directly enter data for a Sudoku board on a cell by cell basis.