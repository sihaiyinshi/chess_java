package domain;

/*
*
*  Each location in grid:
*
*   0 [0 1 2 3 4 5 6 7]
*   1 [0 1 2 3 4 5 6 7]
*   2 [0 1 2 3 4 5 6 7]
*   3 [0 1 2 3 4 5 6 7]
*   4 [0 1 2 3 4 5 6 7]
*   5 [0 1 2 3 4 5 6 7]
*   6 [0 1 2 3 4 5 6 7]
*   7 [0 1 2 3 4 5 6 7]
*
*
* Their corresponding location on chess:
*
*   8 [               ]
*   7 [               ]
*   6 [               ]
*   5 [               ]
*   4 [               ]
*   3 [               ]
*   2 [               ]
*   1 [               ]
*      a b c d e f g h
*
*
* i.e. b2 -> grid[6,1], which is the result of grid[8 - 2, 'b' - 'a']
*
* */


/*
* Since this function had gone too complicated, I wrote some of the functions to decouple the structure.
* All the functions used as decoupling is left at the bottom and set as private
*
*
* */

import domain.piece.*;
import engine.moveEngine;
import util.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class board {
    piece[][] grid = new piece[8][8];
    moveEngine me = new moveEngine(grid);
    util util = new util();
    List<move> moves = new ArrayList<>();

    //maintain for the check of check
    int[] locOfWhiteK=new int[]{7,4};
    int[] locOfBlackK=new int[]{0,4};

    //maintain for En Passant
    String EnPassantLoc=null;


    //maintain for castling check
    boolean whiteKHasMoved=false;
    boolean blackKHasMoved=false;

    boolean whiteRook1hasMoved=false;
    boolean blackRook1hasMoved=false;

    boolean whiteRook2hasMoved=false;
    boolean blackRook2hasMoved=false;



    public board() {

    }

    public void init() {
        grid[0][0] = new piece(color.BLACK, type.ROOK);
        grid[0][1] = new piece(color.BLACK, type.KNIGHT);
        grid[0][2] = new piece(color.BLACK, type.BISHOP);
        grid[0][3] = new piece(color.BLACK, type.QUEEN);
        grid[0][4] = new piece(color.BLACK, type.KING);
        grid[0][5] = new piece(color.BLACK, type.BISHOP);
        grid[0][6] = new piece(color.BLACK, type.KNIGHT);
        grid[0][7] = new piece(color.BLACK, type.ROOK);


        grid[1][0] = new piece(color.BLACK, type.PAWN);
        grid[1][1] = new piece(color.BLACK, type.PAWN);
        grid[1][2] = new piece(color.BLACK, type.PAWN);
        grid[1][3] = new piece(color.BLACK, type.PAWN);
        grid[1][4] = new piece(color.BLACK, type.PAWN);
        grid[1][5] = new piece(color.BLACK, type.PAWN);
        grid[1][6] = new piece(color.BLACK, type.PAWN);
        grid[1][7] = new piece(color.BLACK, type.PAWN);

        grid[6][0] = new piece(color.WHITE, type.PAWN);
        grid[6][1] = new piece(color.WHITE, type.PAWN);
        grid[6][2] = new piece(color.WHITE, type.PAWN);
        grid[6][3] = new piece(color.WHITE, type.PAWN);
        grid[6][4] = new piece(color.WHITE, type.PAWN);
        grid[6][5] = new piece(color.WHITE, type.PAWN);
        grid[6][6] = new piece(color.WHITE, type.PAWN);
        grid[6][7] = new piece(color.WHITE, type.PAWN);

        grid[7][0] = new piece(color.WHITE, type.ROOK);
        grid[7][1] = new piece(color.WHITE, type.KNIGHT);
        grid[7][2] = new piece(color.WHITE, type.BISHOP);
        grid[7][3] = new piece(color.WHITE, type.QUEEN);
        grid[7][4] = new piece(color.WHITE, type.KING);
        grid[7][5] = new piece(color.WHITE, type.BISHOP);
        grid[7][6] = new piece(color.WHITE, type.KNIGHT);
        grid[7][7] = new piece(color.WHITE, type.ROOK);

        me.updateGrid(grid);
    }

    //this method directly shows the board, while the player only wants to see the current layout
    public void showMeInGraph() {
        printBoard(null);
    }

    //this method is used to check the possible moves of one specific piece
    public void showPossibleMoves(String loc, boolean isWhite) {
        printBoard(listPossibleMoves(loc, isWhite));
    }

    //this method calls the above method with all the possible moves the king can make when trying to escape check
    public void showPositions2Escape(boolean isWhite) {
        HashSet<String> allPossibleMoves = generatePositions2Escape(isWhite);
        printBoard(allPossibleMoves);
    }

    //this method returns a list where king can escape to for the engine
    public HashSet<String> returnPositions2Escape(boolean isWhite) {
        return generatePositions2Escape(isWhite);
    }

    //check whether the inputting loc is valid
    public boolean validLoc(String loc, boolean isWhite) {
        if (loc.length() != 2 ||
                loc.charAt(0) < 'a' || loc.charAt(0) > 'h' ||
                loc.charAt(1) < '1' || loc.charAt(1) > '8') return false;

        int[] p = util.loc2grid(loc);
        piece piece = grid[p[0]][p[1]];
        return piece != null && piece.isWhite() == isWhite;
    }

    //since the loc of king has not been inputted, use one function to read the loc of king directly
    public void kingEscape(String loc, boolean isWhite) {
        if(isWhite){
            this.move(util.grid2loc(locOfWhiteK[0],locOfWhiteK[1]),loc);
        }
        else{
            this.move(util.grid2loc(locOfBlackK[0],locOfBlackK[1]),loc);
        }
    }

    public boolean canPromote(boolean isWhite) {
        int row=isWhite? 0:7;
         for(int i = 0; i < grid.length; i++){
             piece p = grid[row][i];
             if(p!=null && p.getType()==type.PAWN){
                 return true;
             }
         }
        return false;
    }

    public void promote(String pieceType, boolean isWhite) {

        int row=isWhite? 0:7;
        type t = switch(pieceType){
            case "Q" -> type.QUEEN;
            case "R" -> type.ROOK;
            case "N" -> type.KNIGHT;
            case "B"-> type.BISHOP;
            default -> null;
        };

        for(int i = 0; i < grid.length; i++){
            if(grid[row][i]!=null && grid[row][i].getType()==type.PAWN){
                grid[row][i]=new piece(grid[row][i].getColor(),t);
                return;
            }
        }
        
    }

    public boolean kingCanEscape(boolean isWhite) {

        int[][] directions= new int[][]{{-1,-1},{-1,0},{-1,1}, {0,-1},{0,1},{1,-1},{1,0},{1,1}};
        int i=isWhite?locOfWhiteK[0]:locOfBlackK[0];
        int j=isWhite?locOfWhiteK[1]:locOfBlackK[1];
        for (int[] direction : directions) {
            int x = i + direction[0], y = j + direction[1];
            if (util.onBoard(x,y)) {
                if(canIgo2ThisCell(x,y,isWhite)){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isValidMove(String from, String to, boolean isWhite) {
        int i = util.loc2grid(from)[0];
        int j = util.loc2grid(from)[1];
        piece piece = grid[i][j];
        return this.listPossibleMoves(from,isWhite).contains(to) && piece.isWhite() == isWhite;
    }

    public boolean canCastling(boolean isWhite) {

        boolean kingMoved = isWhite ? whiteKHasMoved : blackKHasMoved;
        if (kingMoved) return false;

        boolean rook1Moved = isWhite ? whiteRook1hasMoved : blackRook1hasMoved;
        boolean rook2Moved = isWhite ? whiteRook2hasMoved : blackRook2hasMoved;
        int row = isWhite ? 7 : 0;

        if (!rook1Moved && grid[row][1] == null && grid[row][2] == null && grid[row][3] == null) {



            return true;
        }

        if (!rook2Moved && grid[row][5] == null && grid[row][6] == null) {
            return true;
        }

        return false;
    }

    public void move(String from, String to) {

        int[] f = util.loc2grid(from);
        int[] t = util.loc2grid(to);

        piece p = grid[f[0]][f[1]];
        piece target = grid[t[0]][t[1]];

        moves.add(new move(p, target == null ? to : "x" + to));

        grid[t[0]][t[1]] = p;
        grid[f[0]][f[1]] = null;

        //move the rook when this move contains castling
        if (p.getType() == type.KING && Math.abs(f[1] - t[1]) == 2) {
            //long castling
            if (t[1] == 2) {
                grid[f[0]][3] = grid[f[0]][0];
                grid[f[0]][0] = null;
            //short castling
            } else if (t[1] == 6) {
                grid[f[0]][5] = grid[f[0]][7];
                grid[f[0]][7] = null;
            }
        }

        //maintain the status of king and 2 rooks for preparing the castling
        int col = f[1];
        boolean isWhite = p.getColor() == color.WHITE;

        switch (p.getType()) {
            case KING -> {
                if (isWhite) {
                    whiteKHasMoved = true;
                    locOfWhiteK = t;
                } else {
                    blackKHasMoved = true;
                    locOfBlackK = t;
                }
            }
            case ROOK -> {
                if (isWhite) {
                    if (col == 0) whiteRook1hasMoved = true;
                    else if (col == 7) whiteRook2hasMoved = true;
                } else {
                    if (col == 0) blackRook1hasMoved = true;
                    else if (col == 7) blackRook2hasMoved = true;
                }
            }
        }

        //En Passant
        EnPassantLoc = null;
        if (p.getType() == type.PAWN && Math.abs(f[0] - t[0]) == 2) {
            EnPassantLoc = to;
        }
    }

    public void showMoves() {
        for (move move : moves) {
            System.out.println(move);
        }
    }

    public boolean underCheck(boolean isWhite){
        return isWhite?!canIgo2ThisCell(locOfWhiteK[0],locOfWhiteK[1],isWhite):!canIgo2ThisCell(locOfBlackK[0],locOfBlackK[1],isWhite);
    }

    //This is designed for king, to check whether its cell is under check or the cell it gonna is under check
    private boolean canIgo2ThisCell(int i, int j, boolean isWhite) {

        if(grid[i][j]!=null && grid[i][j].isWhite() == isWhite && grid[i][j].getType()!=type.KING){
            return false;
        }

        HashSet<type> types=new HashSet<>();
        types.add(type.ROOK);
        types.add(type.QUEEN);

        if (checkInLine(i, j, isWhite, new int[][]{{-1,0},{1,0},{0,-1},{0,1}}, types)) {
            return false;
        }

        types.clear();
        types.add(type.BISHOP);
        types.add(type.QUEEN);

        if(checkInLine(i, j, isWhite, new int[][]{{-1, -1}, {1, 1}, {1, -1}, {-1, 1}}, types)){
            return false;
        }


        int[][] directions = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] direction : directions) {
            int x = i + direction[0];
            int y = j + direction[1];
            if (util.onBoard(x,y)) {
                piece p = grid[x][y];
                if (p != null && p.isKnight() && p.isWhite() != isWhite) {
                    return false;
                }
            }
        }


        directions = new int[][]{{isWhite ? -1 : 1, -1}, {isWhite ? -1 : 1, 1}};
        for (int[] direction : directions) {
            int x = i + direction[0], y = j + direction[1];
            if (util.onBoard(x,y)) {
                piece p = grid[x][y];
                if (p != null && p.isPawn() && p.isWhite() != isWhite) {
                    return false;
                }
            }
        }


        directions= new int[][]{{-1,-1},{-1,0},{-1,1}, {0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] direction : directions) {
            int x = i + direction[0], y = j + direction[1];
            if (util.onBoard(x,y)) {
                piece p = grid[x][y];
                if (p != null && p.isKing() && p.isWhite() != isWhite) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkInLine(int i, int j, boolean isWhite, int[][] directions, HashSet<type> types) {

        for (int[] d : directions) {
            int x = i + d[0];
            int y = j + d[1];

            while (util.onBoard(x, y)) {
                piece p = grid[x][y];

                if (p != null) {
                    if (types.contains(p.getType()) && p.isWhite() != isWhite) {
                        return true;
                    }
                    break;
                }

                x += d[0];
                y += d[1];
            }
        }
        return false;
    }

    //This is the general method of board printing, and below is the method calls it
    private void printBoard(HashSet<String> highlight) {
        for (int i = 0; i < grid.length; i++) {
            System.out.print(8 - i + " [ ");
            for (int j = 0; j < grid[i].length; j++) {
                piece p = grid[i][j];
                String loc = util.grid2loc(i, j);

                if (highlight != null && highlight.contains(loc)) {
                    System.out.print(p == null ? " + " : "x" + p.toBitmap(false));
                } else {
                    System.out.print(p == null ? "   " : p.toBitmap(false) + " ");
                }
            }
            System.out.println("]");
        }
        System.out.println("    a  b  c  d  e  f  g  h");
    }

    //this method generates the positions king can escape, by call each of cell around it with calling method canIgo2ThisCell
    private HashSet<String> generatePositions2Escape(boolean isWhite) {
        HashSet<String> allPossibleMoves = new HashSet<>();
        int k = isWhite ? locOfWhiteK[0] : locOfBlackK[0];
        int l = isWhite ? locOfWhiteK[1] : locOfBlackK[1];
        int[][] directions = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

        for (int[] dir : directions) {
            int x = k + dir[0], y = l + dir[1];
            if (util.onBoard(x, y) && canIgo2ThisCell(x, y, isWhite)) {
                allPossibleMoves.add(util.grid2loc(x, y));
            }
        }

        return allPossibleMoves;
    }


    private HashSet<String> listPossibleMoves(String loc, boolean isWhite){
        int[] array = util.loc2grid(loc);
        HashSet<String> moves = me.showPossibleMove(array[0], array[1]);

        if(EnPassantLoc!=null && loc.charAt(1)==EnPassantLoc.charAt(1)){
            moves.add(EnPassantLoc);
        }


        if(this.canCastling(isWhite)){
            moves.addAll(loc4castling(isWhite));
        }

        return moves;
    }

    private HashSet<String> loc4castling(boolean isWhite) {
        HashSet<String> moves = new HashSet<>();
        if(isWhite){
            if(grid[7][7].getType()==type.ROOK){
                moves.add("g1");
            }

            if(grid[7][0].getType()==type.ROOK){
                moves.add("c1");
            }

        }
        else{
            if(grid[0][7].getType()==type.ROOK){
                moves.add("g8");
            }

            if(grid[0][0].getType()==type.ROOK){
                moves.add("c8");
            }
        }

        return moves;
    }


}
