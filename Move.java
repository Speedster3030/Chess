import java.util.*;
public class Move
{
    public int fromRow;
    public int fromCol;
    public int toRow;
    public int toCol;
    public boolean isPromoting;
    public short piecePromotedTo;
    public boolean isCastlingKingside;
    public boolean isCastlingQueenside;
    public short piece;
    public boolean enPassant;
    public boolean isCapture;
    public boolean isCheck;
    public short pieceCaptured;

    public Move(int fr,int fc,int tr,int tc)
    {
        fromRow=fr;fromCol=fc;toRow=tr;toCol=tc;
        isPromoting=false;
        isCastlingKingside=false;
        isCastlingQueenside=false;
        enPassant=false;
        isCheck=false;isCapture=false;
        pieceCaptured=0;
    }

    public Move copy()
    {
        Move m=new Move(0,0,0,0);
        m.toRow=this.toRow;m.toCol=this.toCol;
        m.fromRow=this.fromRow;
        m.fromCol=this.fromCol;
        m.isPromoting=this.isPromoting;
        m.piecePromotedTo=this.piecePromotedTo;
        m.isCastlingKingside=this.isCastlingKingside;
        m.isCastlingQueenside=this.isCastlingQueenside;
        m.piece=this.piece;
        m.enPassant=this.enPassant;
        m.isCheck=this.isCheck;
        m.isCapture=this.isCapture;
        m.pieceCaptured=this.pieceCaptured;
        return m;
    }
}
