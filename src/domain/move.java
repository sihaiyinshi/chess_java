package domain;
import domain.piece.*;
public class move {
    piece piece;
    String move;

    public move(piece piece, String move) {
        this.piece = piece;
        this.move = move;
    }

    public String toString() {
        return piece.toBitmap(true)+move;
    }


}
