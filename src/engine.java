import domain.board;

import java.util.HashSet;
import java.util.Scanner;

public class engine {




    public static void main(String[] args) {

        engine e = new engine();

        board board=e.init();
        boolean gameOver=false;
        boolean isWhite=true;
        while(!gameOver) {

            if(board.underCheck(isWhite)){
                if(board.kingCanEscape(isWhite)){
                    kingEscape(isWhite, board);
                    showCurrentMoves(board);
                    isWhite=!isWhite;
                    continue;
                }
                else if(endGame(isWhite)){
                    e.init();
                    continue;
                }
                else{
                    break;
                }
            }

            board.showMeInGraph();

            String from = choosePiece2move(board, isWhite);
            String to= chooseLoc2move(board, from, isWhite);

            if(to.equals("c")){
                continue;
            }

            board.move(from, to);
            promote(board, isWhite);
            showCurrentMoves(board);
            isWhite=!isWhite;


        }



        /*
        board.showMeInText();
        board.showMeInGraph();
        */

    }


    private static void kingEscape(boolean isWhite, board board){
        Scanner in = new Scanner(System.in);
        board.showPositions2Escape(isWhite);

        System.out.println("Attention: you are under check now.");
        System.out.println("You need to deal with check first.");
        System.out.print("Enter the location you would like your king to escape:");
        String escapeLoc=in.nextLine();
        HashSet<String> escapePositions=board.returnPositions2Escape(isWhite);

        while(!escapePositions.contains(escapeLoc)) {
            System.out.println("You cannot go to that position now.");
            System.out.print("Enter the location you would like your king to escape instead:");
            escapeLoc=in.nextLine();
        }

        board.kingEscape(escapeLoc, isWhite);
    }

    private static boolean endGame(boolean isWhite){

        Scanner in = new Scanner(System.in);
        System.out.println("Game over now!");
        System.out.println("The "+(isWhite?"black":"white")+" has won!");
        System.out.print("Do you want to play again:");

        String answer=in.nextLine();
        return answer.equals("yes");
    }

    private static String choosePiece2move(board board, boolean isWhite){
        System.out.println("It is currently the turn of "+(isWhite?"white":"black")+".");
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the location of piece you would like to move:");
        String from = in.nextLine();
        while (!board.validLoc(from,isWhite)) {
            System.out.println("Invalid location! please try again.");
            System.out.print("Enter the location of piece you would like to move:");
            from = in.nextLine();
        }
        board.showPossibleMoves(from,isWhite);
        return from;
    }

    private static String chooseLoc2move(board board, String from, boolean isWhite) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the location of piece you would like to move to");
        System.out.print("or 'c' to cancel to choose another piece:");
        String to = in.nextLine();

        while (!board.isValidMove(from, to, isWhite)) {
            System.out.println("Invalid move! please try again.");
            System.out.println("Enter the location of piece you would like to move to");
            System.out.print("or 'c' to cancel to choose another piece:");
            to = in.nextLine();

            if (to.equals("c")) {
                return to;
            }
        }

        return to;
    }



    private static void promote(board board, boolean isWhite){
        if(board.canPromote(isWhite)) {

            System.out.println("You have one or more pawn who can promote.");
            System.out.println("which one would you like to promote, Q(ueen), R(ook), (k)N(ight), or B(ishop)?");
            System.out.println("Or input other than above options to give this chance up:");
            Scanner in = new Scanner(System.in);
            String piece= in.nextLine();
            if(piece.equals("Q") || piece.equals("R") || piece.equals("N") || piece.equals("B")) {
                board.promote(piece, isWhite);
            }
        }
    }

    private static void showCurrentMoves(board board){
        System.out.println("List of current moves:");
        board.showMoves();
    }




    private board init(){
        board board=new board();
        board.init();
        return board;
    }




}
