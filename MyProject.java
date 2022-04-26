// Jaimin Kirankumar Kerai (22718975)
import java.util.*;
/**
 * CITS2200 Project 1.
 *
 * @author (Jaimin Kirankumar Kerai, 22718975)
 * @version (v1.0)
 */
public class MyProject implements Project
{
    private int NoOfPixels;
    private int brightestPixelinPath;
    private int black = 0; 
    /**
     * Constructor for objects of class MyProject
     */
    public MyProject()
    {
        // zero-arguement constructor 
    }
    
    /**
     * Compute the number of pixels that change when performing a black flood-fill from the 
     * pixel at (row, col) in the given image.
     * A flood-fill operation changes the selected pixel and all contiguous pixels of the 
     * same colour to the specified colour.
     * A pixel is considered part of a contiguous region of the same colour if it is exactly 
     * one pixel up/down/left/right of another pixel in the region. 
     * @param image The greyscale image as defined above
     * @param row The row index of the pixel to flood-fill from
     * @param col The column index of the pixel to flood-fill from
     * @return The number of pixels that changed colour when performing this operation
     */
    public int floodFillCount(int[][] image, int row, int col){
       int NoOfrows = image.length;
       int NoOfcols = image[0].length;
       // Checks for boundary condiitons
        if(row >= 0 && row < NoOfrows && col >= 0 && col < NoOfcols){
           int initialbrightness = image[row][col];
           NoOfPixels = 0;
           if(initialbrightness != black){
               floodFillcheck(image, row, col, initialbrightness);
           }
           return NoOfPixels;
       }
       return 0;
    }
    
    /** 
     * Checks all surrounding pixels for colour match. If colours match, given the initial
     * colour isn't black then the pixel will turn black.
     * @param image The greyscale image as defined above
     * @param row The row index of the pixel to flood-fill from
     * @param col The column index of the pixel to flood-fill from
     * @param the initial colour of the pixel, can never be black.
     */
    private void floodFillcheck(int[][] image, int nrow, int ncol, int initialbrightness){
       int NoOfrows = image.length;
       int NoOfcols = image[0].length;
       // recursive function which checks continguious pixels, if they are of the initial 
       // brightness and non-black then change to black, else does nothing.
       if(nrow >= 0 && nrow < NoOfrows && ncol >= 0 && ncol < NoOfcols){
           if(image[nrow][ncol] == initialbrightness && image[nrow][ncol] != black){
                image[nrow][ncol] = black;
                NoOfPixels++;
                // Checks for contiguous points
                if(nrow >= 1){
                    floodFillcheck(image, nrow-1, ncol, initialbrightness);
                }
                if(ncol >= 1){
                    floodFillcheck(image, nrow, ncol-1, initialbrightness);
                }
                if(nrow < NoOfrows-1){
                    floodFillcheck(image, nrow+1, ncol, initialbrightness);
                }
                if(ncol < NoOfcols-1){
                    floodFillcheck(image, nrow, ncol+1, initialbrightness);
                }
           }
       }
    }
    
    /**
     * Compute the total brightness of the brightest exactly k*k square that appears in the 
     * given image.
     * The total brightness of a square is defined as the sum of its pixel values.
     * You may assume that k is positive, no greater than R or C, and no greater than 2048.
     * @param image The greyscale image as defined above
     * @param k the dimension of the squares to consider
     * @return The total brightness of the brightest square
     */
    public int brightestSquare(int[][] image, int k){
        int NoOfrows = image.length;
        int NoOfcols = image[0].length;
        // Checks for boundary conditions
        if(k <= NoOfrows && k <= NoOfcols && k > 0){
           int maxsquaresum = MaxSquareSum(SumAtIndex(image), k);
           return maxsquaresum;
        }
        return -1;
    }
    
    /**
     * Finds the sum of the square or rectangle, within our image matrix, with the bottom 
     * right-most index corresponding to the square and our image.
     * @param image The greyscale image as defined above
     * @return A 2D-array with the sum of the sqaures or rectangles in the image with the 
     * bottom right-most index at each index
     * 
     */
    private int[][] SumAtIndex(int[][] image){
        int NoOfrows = image.length;
        int NoOfcols = image[0].length;
        int[][] sums = new int[NoOfrows][NoOfcols];
        // Uses geometric property of a grid to build from the left-most corner
        for(int i = 0; i < NoOfrows; i++){
            for(int j = 0; j < NoOfcols; j++){
                if(i == 0 && j == 0){
                    sums[0][0] = image[0][0];
                }
                else if(i != 0 && j == 0){
                    sums[i][0] = image[i][0] + sums[i-1][0];
                }
                else if(i == 0 && j!= 0){
                    sums[0][j] = image[0][j] + sums[0][j-1];
                }
                else if(i != 0 && j!= 0){
                    sums[i][j] = image[i][j] + sums[i-1][j] + sums[i][j-1] - sums[i-1][j-1];
                }
            }
        }
        return sums;
    }
    
    /**
     * Finds the sum (brightness) of the square k by k, within our image matrix.
     * [0][0] as its top leftmost vertex and [i][j] as its bottom rightmost index.
     * @param a preprocessed matrix which has the sums of each square or rectangle made from the image
     * @param k the size of the square of pixels
     * @return the greatest sum within the matrix of a square k*k 
    */
    private int MaxSquareSum(int[][] squaresums, int k){
        int NoOfrows = squaresums.length;
        int NoOfcols = squaresums[0].length;
        int total = 0;
        int maxsum = 0;
        // Iterates through bottom-right index of each square to calculate sum then makes comparison
        for(int i = k - 1; i < NoOfrows; i++){
            for(int j = k - 1; j < NoOfcols; j++){
                total = squaresums[i][j];
                if(i - k >= 0 && j - k >= 0){
                    total = total - squaresums[i-k][j] - squaresums[i][j-k] + squaresums[i-k][j-k];
                }
                else if(i - k >= 0){
                    total -= squaresums[i-k][j];
                }
                else if(j - k >= 0){
                    total -= squaresums[i][j-k];
                }
                if(total > maxsum){
                    maxsum = total;
                }
            }
        }
        return maxsum;
    }
   
    /**
     * Compute the maximum brightness that MUST be encountered when drawing a path from the 
     * pixel at (ur, uc) to the pixel at (vr, vc).
     * The path must start at (ur, uc) and end at (vr, vc), and may only move one pixel 
     * up/down/left/right at a time in between.
     * The brightness of a path is considered to be the value of the brightest pixel that the 
     * path ever touches.
     * This includes the start and end pixels of the path.
     * @param image The greyscale image as defined above
     * @param ur The row index of the start pixel for the path
     * @param uc The column index of the start pixel for the path
     * @param vr The row index of the end pixel for the path
     * @param vc The column index of the end pixel for the path
     * @return The minimum brightness of any path from (ur, uc) to (vr, vc)
     */
    public int darkestPath(int[][] image, int ur, int uc, int vr, int vc){ 
        int NoOfrows = image.length;
        int NoOfcols = image[0].length;
        // Checks boundary conditions
        if(ur >= 0 && ur < NoOfrows && uc >= 0 && uc < NoOfcols && vr >= 0 && vr < NoOfrows && vc >= 0 && vc < NoOfcols){
            darkestPathCheck(image, ur, uc, vr, vc);
        }
        return brightestPixelinPath;
    } 
    
    /**
     * Finds brightest pixel that must be encountered to g from point (ur, uc) to (vr, vc)
     * @param image The greyscale image as defined above
     * @param ur The row index of the start pixel for the path
     * @param uc The column index of the start pixel for the path
     * @param vr The row index of the end pixel for the path
     * @param vc The column index of the end pixel for the path
     * @alters The variable brightestPixelinPath
     */
    private void darkestPathCheck(int[][] image, int currow, int curcol, int vr, int vc){
       int NoOfrows = image.length;
       int NoOfcols = image[0].length;
       int[][] directions = new int[][] {{1,0}, {-1,0}, {0,1}, {0,-1}};
       boolean[][] visited = new boolean[NoOfrows][NoOfcols];
       Queue<int[]> pointq = new PriorityQueue<int[]>((r, c) -> r[0] - c[0]);
       pointq.add(new int[] {image[currow][curcol], currow, curcol});
       brightestPixelinPath = -1; 
       // Looks for the darkest contiguous pixel and adds it to queue, queue is then processed
       while(!pointq.isEmpty()){
           int[] pointinfo = pointq.poll();
           brightestPixelinPath = Math.max(brightestPixelinPath, pointinfo[0]);
           int row = pointinfo[1];
           int col = pointinfo[2];
           // Checks if we've reached the target
           if (row == vr && col == vc){ 
               break;
           }
           for (int[] dir : directions) {
                int temprow = row + dir[0];
                int tempcol = col + dir[1];
                // Checks boundary conditions and visited state
                if (temprow < 0 || temprow >= NoOfrows || tempcol < 0 || tempcol >= NoOfcols || visited[temprow][tempcol]){
                    continue;
                }
                visited[temprow][tempcol] = true;
                pointq.add(new int[] {image[temprow][tempcol], temprow, tempcol});
           }
       }
    }
    
    /**
     * Compute the results of a list of queries on the given image.
     * Each query will be a three-element int array {r, l, u} defining a row segment. You may 
     * assume l < u.
     * A row segment is a set of pixels (r, c) such that r is as defined, l <= c, and c < u.
     * For each query, find the value of the brightest pixel in the specified row segment.
     * Return the query results in the same order as the queries are given.
     * @param image The greyscale image as defined above
     * @param queries The list of query row segments
     * @return The list of brightest pixels for each query row segment
     */
    public int[] brightestPixelsInRowSegments(int[][] image, int[][] queries){
       int NoOfrows = image.length;
       int NoOfcols = image[0].length;
       int NoOfqueries = queries.length;
       int[] brightestInSegments = new int[NoOfqueries];
       int P = (int) (Math.log(NoOfcols)/Math.log(2));
       int[] logbase = new int[NoOfcols+1];
       int[][][] sparsetable = new int[NoOfrows][P+1][NoOfcols];
       int count = 0;
       // Initalising and developing the sparse table 
       for(int i = 0; i < NoOfrows; i++){
           for(int j = 0; j < NoOfcols; j++){
               sparsetable[i][0][j] = image[i][j];
           }
       }
       for(int i = 2; i <= NoOfcols; i++){
           logbase[i] = logbase[i/2] + 1;
       }
       for(int i = 0; i < NoOfrows; i++){
           for(int j = 1; j <= P; j++){
               for(int k = 0; k + (1 << j) <= NoOfcols; k++){
                   int leftinterval = sparsetable[i][j-1][k];
                   int rightinterval = sparsetable[i][j-1][k+(1<<(j-1))];
                   sparsetable[i][j][k] = Math.max(leftinterval, rightinterval);
                }
            }
       }
       // Iterating over the queries
       for(int[] q: queries){
           int r = q[0];
           int l = q[1];
           int u = q[2]-1;
           brightestInSegments[count] = querySegment(sparsetable, logbase, r, l, u);
           count++;
       }
       return brightestInSegments;
    }
    
    /**
     * A query method to find the largest in the row segement that checks our calculated 
     * sparse table created by the method that calls this
     * @param sparsetable preprocessed 3D array with largest row segments store
     * @param queries The list of query row segments
     * @return The list of brightest pixels for each query row segment
     */
    private int querySegment(int[][][] sparsetable, int[] logbase, int r, int l, int u){
        int segmentlength = u - l + 1;
        int x = logbase[segmentlength];
        int k = 1 << x;
        return Math.max(sparsetable[r][x][l], sparsetable[r][x][u-k+1]);
    }
}
    
