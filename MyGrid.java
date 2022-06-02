// Written by Drew Brock; brock450

import java.awt.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Updated by Yuha Yoo and Austin Franzen 3.3.2022
 * Written by Cormac Pearce on 11.10.2021
 * Significant portions of code taken from Noah Park's Spring 2021 solution
 */
public class MyGrid {

    Cell[][] grid;
    int rows, cols, startRow, endRow, moves;
    int[][] packages;

    /**
     * Default constructor for MyMaze object.
     *
     * @param rows     total rows for the maze.
     * @param cols     total columns for the maze.
     * @param startRow row index of maze entrance
     * @param endRow   row index of maze exit
     */
    public MyGrid(int rows, int cols, int startRow, int endRow) {
        grid = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.startRow = startRow;
        this.endRow = endRow;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Draws grid and displays to the user.
     *
     */
    public void drawGrid() {
        Canvas c = new Canvas();

        int xTracker = 250;
        int yTracker = 100;
        Color gridColor = Color.RED;

        //Will be final, can only be changed here
        int WIDTH = 20;
        int HEIGHT = 20;

        // Just go row by row and build it down
        
        for (int i = 0; i < grid.length; i++){
            xTracker = 250 - (grid[0].length / 2 * 25);
            for (int j = 0; j < grid[0].length; j++){
                // check top, bottom, and sides to create implicit boundary
                if(i == 0 && j == 0){
                    c.drawShape(new Square(xTracker - WIDTH - 5, yTracker - HEIGHT - 5, WIDTH, HEIGHT, Color.BLACK));
                }
                if(i == 0){
                    c.drawShape(new Square(xTracker, yTracker - HEIGHT - 5, WIDTH, HEIGHT, Color.BLACK));
                    c.drawShape(new Square(xTracker + WIDTH + 5, yTracker - HEIGHT - 5, WIDTH, HEIGHT, Color.BLACK));
                }
                if(j == 0){
                    if(i != startRow)
                        c.drawShape(new Square(xTracker - WIDTH - 5, yTracker, WIDTH, HEIGHT, Color.BLACK));
                    c.drawShape(new Square(xTracker - WIDTH - 5, yTracker + HEIGHT + 5, WIDTH, HEIGHT, Color.BLACK));
                }
                if(i == grid.length - 1){
                    c.drawShape(new Square(xTracker, yTracker + HEIGHT + 5, WIDTH, HEIGHT, Color.BLACK));
                    c.drawShape(new Square(xTracker + WIDTH + 5, yTracker + HEIGHT + 5, WIDTH, HEIGHT, Color.BLACK));
                }
                if(j == grid[0].length - 1){
                    if(i != endRow)
                        c.drawShape(new Square(xTracker + WIDTH + 5, yTracker, WIDTH, HEIGHT, Color.BLACK));
                    c.drawShape(new Square(xTracker + WIDTH + 5, yTracker + HEIGHT + 5, WIDTH, HEIGHT, Color.BLACK));
                }
                // Now do the actual cells
                // visited
                if(grid[i][j].getVisited()){
                    gridColor = Color.BLUE;
                }
                //open right and open bottom
                if(!grid[i][j].getBottom() && !grid[i][j].getRight()){
                    c.drawShape(new Square(xTracker, yTracker, WIDTH, HEIGHT, gridColor));
                    inBetween(c, xTracker, yTracker, WIDTH, HEIGHT, i, j); 
                    if(j != grid[0].length - 1)
                        c.drawShape(new Square(xTracker + WIDTH + 5, yTracker, WIDTH, HEIGHT, gridColor));
                    if(i != grid.length - 1)
                        c.drawShape(new Square(xTracker, yTracker + HEIGHT + 5, WIDTH, HEIGHT, gridColor));
                }
                //open bottom, closed right
                else if(!grid[i][j].getBottom()){
                    c.drawShape(new Square(xTracker, yTracker, WIDTH, HEIGHT, gridColor));
                    inBetween(c, xTracker, yTracker, WIDTH, HEIGHT, i, j); 
                    if(j != grid[0].length - 1)
                        c.drawShape(new Square(xTracker + WIDTH + 5, yTracker, WIDTH, HEIGHT, Color.GRAY));
                    if(i != grid.length - 1)
                        c.drawShape(new Square(xTracker, yTracker + WIDTH + 5, WIDTH, HEIGHT, gridColor));
                }
                //open right, closed bottom
                else if(!grid[i][j].getRight()){
                    c.drawShape(new Square(xTracker, yTracker, WIDTH, HEIGHT, gridColor));
                    inBetween(c, xTracker, yTracker, WIDTH, HEIGHT, i, j);
                    if(j != grid[0].length - 1)
                        c.drawShape(new Square(xTracker + WIDTH + 5, yTracker, WIDTH, HEIGHT, gridColor));
                    if(i != grid.length - 1)
                        c.drawShape(new Square(xTracker, yTracker + WIDTH + 5, WIDTH, HEIGHT, Color.GRAY));
                }
                //closed bottom, closed right
                else{
                    c.drawShape(new Square(xTracker, yTracker, WIDTH, HEIGHT, gridColor));
                    inBetween(c, xTracker, yTracker, WIDTH, HEIGHT, i, j); 
                    if(j != grid[0].length - 1)
                        c.drawShape(new Square(xTracker + WIDTH + 5, yTracker, WIDTH, HEIGHT, Color.GRAY));
                    if(i != grid.length - 1)
                        c.drawShape(new Square(xTracker, yTracker + WIDTH + 5, WIDTH, HEIGHT, Color.GRAY));
                }
                xTracker = xTracker + WIDTH + WIDTH + 10;
                gridColor = Color.RED;
            }
            yTracker += HEIGHT + HEIGHT + 10;
        }
    }
    // basic method to fill in gaps, used VSCode refactoring tool
    private void inBetween(Canvas c, int xTracker, int yTracker, int WIDTH, int HEIGHT, int i, int j) {
        if(j != grid[0].length - 1 && i != grid.length - 1)
            c.drawShape(new Square(xTracker + WIDTH + 5, yTracker + WIDTH + 5, WIDTH, HEIGHT, Color.GRAY));
    }

    /**
     * Generates a random maze using the algorithm from the write up.
     *
     * @param level difficulty level for maze (1-3) that decides maze dimensions
     *              level 1 -> 5x5, level 2 -> 5x20, level 3 -> 20x20
     * @return MyMaze object fully generated.
     */
    public static MyGrid makeGrid(int level){
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        Random rand = new Random();
        MyGrid maze;

        if(level == 1){
            maze = new MyGrid(5, 5, rand.nextInt(5), rand.nextInt(5));
        }
        else if(level == 2){
            maze = new MyGrid(5, 10, rand.nextInt(5), rand.nextInt(5));
        }
        //Assume any other option is level 3 as we will make sure input is correct in main!
        else{
            maze = new MyGrid(12, 12, rand.nextInt(12), rand.nextInt(12));
        }

        stack.push(new int[]{maze.startRow, 0});

        while(!stack.isEmpty()){
            int row = stack.top()[0];
            int col = stack.top()[1];
            maze.grid[row][col].setVisited(true);
            
            //Set up for checking visited/directions
            boolean up = false;
            boolean right = false;
            boolean down = false;
            boolean left = false;
            boolean next = false;
            // Long conditional checks incoming
            //Check up for valid range and visitation rights
            if(row > 0){
                if(!maze.grid[row - 1][col].getVisited())
                    up = true;
            }
            //Check right
            if(col < maze.grid[0].length - 1){
                if(!maze.grid[row][col + 1].getVisited())
                    right = true;
            }
            //Check down
            if(row < maze.grid.length - 1){
                if(!maze.grid[row + 1][col].getVisited())
                    down = true;
            }
            //Check left
            if(col > 0){
                if(!maze.grid[row][col - 1].getVisited())
                    left = true;
            }
            if(!(up || right || down || left)){
                stack.pop();
                next = true;
            }
            /*
            0
         3  +  1
            2
            */
            // while there is not a next direction and not popped
            while(!next){
                int direction = rand.nextInt(4);
                if(direction == 0 && up){
                    stack.push(new int[]{row - 1, col});
                    maze.grid[row - 1][col].setBottom(false);
                    next = true;
                }
                else if(direction == 1 && right){
                    stack.push(new int[]{row, col + 1});
                    maze.grid[row][col].setRight(false);
                    next = true;
                }
                else if(direction == 2 && down){
                    stack.push(new int[]{row + 1, col});
                    maze.grid[row][col].setBottom(false);
                    next = true;
                }
                else if(direction == 3 && left){
                    stack.push(new int[]{row, col - 1});
                    maze.grid[row][col - 1].setRight(false);
                    next = true;
                }
            }
        }
        for(int i = 0; i < maze.grid.length; i++){
            for(int j = 0; j< maze.grid[0].length; j++){
                maze.grid[i][j].setVisited(false);
            }
        }
        return maze;
    }

    /**
     * Solves the maze using the algorithm from the writeup.
     */
    public void solveGrid() {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{startRow, 0});
        while(queue.length() > 0){
            int[] cell = queue.remove();
            int row = cell[0];
            int col = cell[1];
            grid[row][col].setVisited(true);

            if(row == endRow && col == grid[0].length - 1) break;

            // left direction (cell to the left is open right)
            if(col != 0 && !grid[row][col - 1].getVisited() && !grid[row][col - 1].getRight()){
                queue.add(new int[]{row, col - 1});
            }
            // right direction (current cell has open right)
            if(col != grid[0].length - 1 && !grid[row][col].getRight() && !grid[row][col + 1].getVisited()){
                queue.add(new int[]{row, col + 1});
            }
            // below direction (current cell has open bottom)
            if(row != grid.length - 1 && !grid[row][col].getBottom() && !grid[row + 1][col].getVisited()){
                queue.add(new int[]{row + 1, col});
            }
            // above direction (cell above has open bottom)
            if(row != 0 && !grid[row - 1][col].getVisited() && !grid[row - 1][col].getBottom()){
                queue.add(new int[]{row - 1, col});
            }
        }
    }

    public static <T> void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int level = 0;
        // IO for choosing level
        while(level != 1 && level != 2 && level != 3){
            System.out.println("Time to make a maze. Would you like level 1, 2, or 3?");
            level = s.nextInt();
        }
        s.close();
        MyGrid test = makeGrid(level);
        test.solveGrid();
        test.drawGrid();
    }
}