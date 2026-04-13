package engine;

import domain.piece.color;
import domain.piece.piece;
import domain.piece.type;
import util.util;
import java.util.HashSet;

public class moveEngine {

    piece[][] grid;
    util util=new util();

    public moveEngine(piece[][] grid) {
        this.grid=grid;
    }

    public void updateGrid(piece[][] grid) {
        this.grid=grid;
    }

    public HashSet<String> showPossibleMove(int i, int j){
        type type=grid[i][j].getType();
        return switch (type) {
            case PAWN -> pawnMove(i, j);
            case KNIGHT -> knightMove(i, j);
            case BISHOP -> bishopMove(i, j);
            case ROOK -> rookMove(i, j);
            case QUEEN -> queenMove(i, j);
            case KING -> kingMove(i, j);
        };
    }



    private HashSet<String> bishopMove(int i, int j){
        int[][] direction= new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        return lineMove(i,j,direction);
    }

    private HashSet<String> rookMove(int i, int j){
        int[][] direction= new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        return lineMove(i, j, direction);
    }

    private HashSet<String> queenMove(int i, int j){
        int[][] direction= new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        return lineMove(i, j, direction);
    }

    private HashSet<String> kingMove(int i, int j){
        int[][] direction= new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        return cellMove(i,j,direction);
    }

    private HashSet<String> knightMove(int i, int j){
        int[][] direction= new int[][]{{-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}};
        return cellMove(i,j,direction);
    }

    private HashSet<String> lineMove(int i, int j, int[][] direction){
        HashSet<String> possibleMoves=new HashSet<>();
        color color=grid[i][j].getColor();
        for(int[] d : direction){
            int x=i+d[0];
            int y=j+d[1];

            while(x>=0 && y>=0 && x<grid.length && y<grid[0].length){
                piece p = grid[x][y];

                if (p == null || p.getColor() != color) {
                    possibleMoves.add(util.grid2loc(x, y));
                }

                if (p != null) {
                    break;
                }

                x+=d[0];
                y+=d[1];
            }

        }
        return possibleMoves;
    }


    private HashSet<String> cellMove(int i, int j, int[][] direction){
        HashSet<String> possibleMoves=new HashSet<>();
        color color=grid[i][j].getColor();

        for(int[] d : direction){
            int x=i+d[0];
            int y=j+d[1];

            if(x<0 || y<0 || x>=grid.length || y>=grid[0].length){
                continue;
            }

            piece p = grid[x][y];
            if (p == null || p.getColor() != color) {
                possibleMoves.add(util.grid2loc(x, y));
            }

        }
        return possibleMoves;
    }

    private HashSet<String> pawnMove(int i, int j) {
        HashSet<String> moves = new HashSet<>();

        piece p = grid[i][j];
        int dir = p.getColor() == color.WHITE ? -1 : 1;
        int startRow = p.getColor() == color.WHITE ? 6 : 1;

        int x = i + dir;
        if (util.onBoard(x, j) && grid[x][j] == null) {
            moves.add(util.grid2loc(x, j));

            int x2 = i + 2 * dir;
            if (i == startRow && grid[x2][j] == null) {
                moves.add(util.grid2loc(x2, j));
            }
        }

        for (int dj : new int[]{-1, 1}) {
            int y = j + dj;
            if (util.onBoard(x, y)) {
                piece target = grid[x][y];
                if (target != null && target.getColor() != p.getColor()) {
                    moves.add(util.grid2loc(x, y));
                }
            }
        }

        return moves;
    }


}
