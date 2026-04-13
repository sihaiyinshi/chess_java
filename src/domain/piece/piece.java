package domain.piece;

public class piece {

    final color color;
    final type type;


    public piece(color color, type type) {
        this.color = color;
        this.type = type;
    }

    public String toString(){
        return color.toString() + " " + type.toString();
    }

    public String toBitmap(boolean noColor) {
        char colorChar = noColor ? ' ' : (this.color == color.WHITE ? 'w' : 'b');
        char typeChar = switch (type) {
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case ROOK -> 'R';
            case QUEEN -> 'Q';
            case KING -> 'K';
            default -> ' ';
        };
        return "" + colorChar + typeChar;
    }

    public type getType(){
        return type;
    }

    public color getColor(){
        return color;
    }

    public boolean isWhite(){
        return color == color.WHITE;
    }

    public boolean isBishop(){
        return type==type.BISHOP;
    }

    public boolean isRook(){
        return type==type.ROOK;
    }

    public boolean isQueen(){
        return type==type.QUEEN;
    }

    public boolean isKing(){
        return type==type.KING;
    }

    public boolean isKnight(){
        return type==type.KNIGHT;
    }

    public boolean isPawn(){
        return type==type.PAWN;
    }

}
