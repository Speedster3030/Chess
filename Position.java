import java.util.*;
public class Position
{
    public short[][] board= new short[8][8];
    public boolean hasWhiteKingMoved;
    public boolean hasBlackKingMoved;
    private boolean hasWhiteKingRookMoved;
    private boolean hasWhiteQueenRookMoved;
    private boolean hasBlackKingRookMoved;
    private boolean hasBlackQueenRookMoved;
    public boolean isWhitesTurn;
    public Move lastMove;
    public square[][] squares=new square[8][8];
    public boolean whiteInCheck;
    public boolean blackInCheck;
    public Location WhiteKing;
    public Location BlackKing;
    public List<save> history=new ArrayList<>();

    public Position copy()
    {
        Position pos = new Position();
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                pos.board[i][j] = this.board[i][j];
                pos.squares[i][j].pieces = this.squares[i][j].pieces;
            }
        }
        pos.hasWhiteKingMoved=this.hasWhiteKingMoved;pos.hasBlackKingMoved=this.hasBlackKingMoved;
        pos.hasWhiteKingRookMoved=this.hasWhiteKingRookMoved;pos.hasWhiteQueenRookMoved=this.hasWhiteQueenRookMoved;
        pos.hasBlackKingRookMoved=this.hasBlackKingRookMoved;pos.hasBlackQueenRookMoved=this.hasBlackQueenRookMoved;
        pos.isWhitesTurn=this.isWhitesTurn;pos.lastMove=this.lastMove.copy();
        pos.whiteInCheck=this.whiteInCheck;pos.blackInCheck=this.blackInCheck;
        pos.WhiteKing.Row=this.WhiteKing.Row;pos.WhiteKing.Col=this.WhiteKing.Col;
        pos.BlackKing.Row=this.BlackKing.Row;pos.BlackKing.Col=this.BlackKing.Col;
        return pos;
    }

    public class save
    {
        public short[][] previous=new short[8][8];
        public Move move;
        public boolean[] backup=new boolean[9];
        public int[] loc=new int[4];

        public save(short[][] arr,boolean[] b,int[] l,Move m)
        {
            for(int i=0;i<=7;i++)
            {
                for(int j=0;j<=7;j++)
                {
                    previous[i][j]=arr[i][j];
                }
            }
            for(int i=0;i<=8;i++)
            {
                backup[i]=b[i];
            }
            for(int i=0;i<=3;i++)
            {
                loc[i]=l[i];
            }
            move=m.copy();
        }
    }

    public void applyMove(Move move)
    {
        Move bm=lastMove.copy();
        boolean[] back=new boolean[9];
        int[] loc=new int[4];
        back[0]=whiteInCheck;back[1]=blackInCheck;back[2]=hasWhiteKingMoved;
        back[3]=hasBlackKingMoved;back[4]=hasWhiteKingRookMoved;
        back[5]=hasWhiteQueenRookMoved;back[6]=hasBlackKingRookMoved;
        back[7]=hasBlackQueenRookMoved;back[8]=isWhitesTurn;loc[0]=WhiteKing.Row;
        loc[1]=WhiteKing.Col;loc[2]=BlackKing.Row;loc[3]=BlackKing.Col;
        save s=new save(board,back,loc,bm);
        history.add(s);
        whiteInCheck = false; blackInCheck = false;
        if(!hasWhiteKingMoved && board[7][4]!=100){hasWhiteKingMoved = true;}
        if(!hasWhiteKingRookMoved && board[7][7]!=50){hasWhiteKingRookMoved = true;}
        if(!hasWhiteQueenRookMoved && board[7][0]!=50){hasWhiteQueenRookMoved = true;}
        if(!hasBlackKingMoved && board[0][4]!=-100){hasBlackKingMoved = true;}
        if(!hasBlackKingRookMoved && board[0][7]!=-50){hasBlackKingRookMoved = true;}
        if(!hasBlackQueenRookMoved && board[0][0]!=-50){hasBlackQueenRookMoved = true;}
        move.pieceCaptured=board[move.toRow-1][move.toCol-1];
        //moving the piece
        board[move.toRow-1][move.toCol-1]=board[move.fromRow-1][move.fromCol-1];
        //vacating the previous square
        board[move.fromRow-1][move.fromCol-1]=0;
        //Rook movement in Castling
        if(move.isCastlingKingside)
        {
            if(isWhitesTurn) { board[7][5]=50;board[7][7]=0;}if(!isWhitesTurn){ board[0][5]=-50;board[0][7]=0; }
        }
        if(move.isCastlingQueenside)
        {
            if(isWhitesTurn) { board[7][3]=50;board[7][0]=0; }if(!isWhitesTurn){ board[0][3]=-50;board[0][0]=0; }
        }
        //switching the turn
        isWhitesTurn = !isWhitesTurn;
        //Pawn Promotion
        if(move.isPromoting == true)
        {
            board[move.toRow-1][move.toCol-1]=move.piecePromotedTo;
        }
        //en passant
        if(move.enPassant)
        {
            move.pieceCaptured=board[lastMove.toRow-1][lastMove.toCol-1];
            board[lastMove.toRow-1][lastMove.toCol-1]=0;
        }
        //update king locations
        if(move.piece==100) { WhiteKing.Row=move.toRow;WhiteKing.Col=move.toCol; }
        if(move.piece==-100) { BlackKing.Row=move.toRow;BlackKing.Col=move.toCol; }
        //updating the squares array for legal king moves
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                squares[i][j]=new square(i+1,j+1);
            }
        }
        //Check variables
        if(squares[WhiteKing.Row-1][WhiteKing.Col-1].attackedByOtherColor((short)(10))){whiteInCheck = true;}
        if(squares[BlackKing.Row-1][BlackKing.Col-1].attackedByOtherColor((short)(-10))){blackInCheck = true;}
        //storing last move
        lastMove=move.copy();
    }

    public void undoMove()
    {
        save s=history.get(history.size()-1);
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                board[i][j]=s.previous[i][j];
                squares[i][j]=new square(i+1,j+1);
            }
        }
        whiteInCheck=s.backup[0];blackInCheck=s.backup[1];
        hasWhiteKingMoved=s.backup[2];hasBlackKingMoved=s.backup[3];
        hasWhiteKingRookMoved=s.backup[4];hasWhiteQueenRookMoved=s.backup[5];
        hasBlackKingRookMoved=s.backup[6];hasBlackQueenRookMoved=s.backup[7];
        isWhitesTurn=s.backup[8];lastMove=s.move.copy();WhiteKing.Row=s.loc[0];
        WhiteKing.Col=s.loc[1];BlackKing.Row=s.loc[2];BlackKing.Col=s.loc[3];

        history.remove(history.size()-1);
    }

    public List<Move> getMoves()
    {
        List<Move> moves = new ArrayList<>();
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                short v=board[i][j];
                if(v>0 && isWhitesTurn)
                {
                    if(v==10)   { moves.addAll(Pawn(i+1,j+1));   }if(v==30)   { moves.addAll(Nite(i+1,j+1));   }
                    if(v==32) { moves.addAll(Bishop(i+1,j+1)); }if(v==50)   { moves.addAll(Rook(i+1,j+1));   }
                    if(v==90)   { moves.addAll(Queen(i+1,j+1));  }if(v==100)  { moves.addAll(King(i+1,j+1));   }
                }
                if(v<0 && !isWhitesTurn)
                {
                    if(v==-10)   { moves.addAll(Pawn(i+1,j+1));  }if(v==-30)   { moves.addAll(Nite(i+1,j+1));  }
                    if(v==-32) { moves.addAll(Bishop(i+1,j+1));}if(v==-50)   { moves.addAll(Rook(i+1,j+1));  }
                    if(v==-90)   { moves.addAll(Queen(i+1,j+1)); }if(v==-100)  { moves.addAll(King(i+1,j+1));  }
                }
            }
        }
        List<Move> checkMoves= new ArrayList<>();
        //Legal moves when King is in Check
        if(whiteInCheck||blackInCheck)
        {
            for(Move move:moves)
            {
                this.applyMove(move);save s=history.get(history.size()-1);
                if(s.backup[0] && !isWhitesTurn && !whiteInCheck)
                {
                    if(blackInCheck){move.isCheck=true;}
                    checkMoves.add(move);
                }
                if(s.backup[1] && isWhitesTurn && !blackInCheck)
                {
                    if(whiteInCheck){move.isCheck=true;}
                    checkMoves.add(move);
                }
                this.undoMove();
            }
            return checkMoves;
        }

        //filter out pinned pieces moving(and save the king from attacking defended enemy pieces;)
        List<Move> finalMoves = new ArrayList<>();
        for(Move move:moves)
        {
            this.applyMove(move);save s=history.get(history.size()-1);
            if(s.backup[8] && whiteInCheck)
            {
                this.undoMove();
                continue;
            }
            if(!s.backup[8] && blackInCheck)
            {
                this.undoMove();
                continue;
            }
            if(whiteInCheck||blackInCheck)
            {
                move.isCheck=true;
            }
            finalMoves.add(move);
            this.undoMove();
        }
        return finalMoves;
    }

    /*The fakeKing function seems to be necessary
    to avoid bugs while finding legal moves
    when a king would face another king.*/

     public List<Move> fakeKing(int r,int c)
     {
         List<Move> moves= new ArrayList<>();
         int[][] dir={{1,0},{0,1},{-1,0},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
         for(int[] i:dir)
         {
             int row=r+i[0];int col= c+i[1];
             if(isValidSquare(row,col) && (squareEmpty(row,col) || (!squareEmpty(row,col) && !ofSameColor(r,c,row,col))))
             {
                 Move move=new Move(r,c,row,col);move.piece=board[r-1][c-1];
                 if(board[row-1][col-1]!=0){move.isCapture=true;}moves.add(move);
             }
         }
         return moves;
     }

     public List<Move> King(int r,int c)
     {
         List<Move> moves= new ArrayList<>();
         //call fakeKing for moves, then pick legal moves
         List<Move> temp= new ArrayList<>();temp.addAll(fakeKing(r,c));
         for(Move m:temp)
         {
             if(squares[m.toRow-1][m.toCol-1].attackedByOtherColor(board[r-1][c-1]))
             {
                 continue;
             }
             moves.add(m);
         }
         //Castling
         if(isWhitesTurn && !whiteInCheck && !hasWhiteKingMoved && !hasWhiteKingRookMoved && squareEmpty(8,6) && squareEmpty(8,7) && !squares[7][5].attackedByOtherColor((short)(10)) && !squares[7][6].attackedByOtherColor((short)(10)) && board[7][7]==50)
         {
             Move castle= new Move(8,5,8,7);castle.piece=100;castle.isCastlingKingside=true;moves.add(castle);
         }
         if(isWhitesTurn && !whiteInCheck && !hasWhiteKingMoved && !hasWhiteQueenRookMoved && squareEmpty(8,4) && squareEmpty(8,3) && squareEmpty(8,2) && !squares[7][3].attackedByOtherColor((short)(10)) && !squares[7][2].attackedByOtherColor((short)(10)) && board[7][0]==50)
         {
             Move castle=new Move(8,5,8,3);castle.piece=100;castle.isCastlingQueenside=true;moves.add(castle);
         }
         if(!isWhitesTurn && !blackInCheck && !hasBlackKingMoved && !hasBlackKingRookMoved && squareEmpty(1,6) && squareEmpty(1,7) && !squares[0][5].attackedByOtherColor((short)(-10)) && !squares[0][6].attackedByOtherColor((short)(-10)) && board[0][7]==-50)
         {
             Move castle=new Move(1,5,1,7);castle.piece=-100;castle.isCastlingKingside=true;moves.add(castle);
         }
         if(!isWhitesTurn && !blackInCheck && !hasBlackKingMoved && !hasBlackQueenRookMoved && squareEmpty(1,4) && squareEmpty(1,3) && squareEmpty(1,2) && !squares[0][3].attackedByOtherColor((short)(-10)) && !squares[0][2].attackedByOtherColor((short)(-10)) && board[0][0]==-50)
         {
             Move castle=new Move(1,5,1,3);castle.piece=-100;castle.isCastlingQueenside=true;moves.add(castle);
         }
         return moves;
     }

     public List<Move> Pawn(int r,int c)
     {
         List<Move> moves= new ArrayList<>(); short val;
         if(board[r-1][c-1]>0) { val=1; } else { val=-1; }
         Move move;
         //Normal Pawn Push
         if(val==1 && squareEmpty(r-1,c))
         {
             move = new Move(r,c,r-1,c);
             if(r==2) { move.isPromoting = true;move.piecePromotedTo=90; }
             moves.add(move);
         }
         if(val==-1 && squareEmpty(r+1,c))
         {
             move = new Move(r,c,r+1,c);
             if(r==7) { move.isPromoting = true;move.piecePromotedTo=-90; }
             moves.add(move);
         }
         //Double Pawn Push at the Start
         if(r==2 && val==-1 && squareEmpty(r+2,c) && squareEmpty(r+1,c))
         {
             move = new Move(r,c,r+2,c);moves.add(move);
         }
         if(r==7 && val==1 && squareEmpty(r-2,c) && squareEmpty(r-1,c))
         {
             move = new Move(r,c,r-2,c);moves.add(move);
         }
         //Pawn Captures
         if(val==1 && isValidSquare(r-1,c+1) && !squareEmpty(r-1,c+1) && !ofSameColor(r,c,r-1,c+1))
         {
             move = new Move(r,c,r-1,c+1);if(r==2) { move.isPromoting = true;move.piecePromotedTo=90; }
             move.isCapture=true;moves.add(move);
         }
         if(val==1 && isValidSquare(r-1,c-1) && !squareEmpty(r-1,c-1) && !ofSameColor(r,c,r-1,c-1))
         {
             move = new Move(r,c,r-1,c-1);if(r==2) { move.isPromoting = true;move.piecePromotedTo=90; }
             move.isCapture=true;moves.add(move);
         }
         if(val==-1 && isValidSquare(r+1,c+1) && !squareEmpty(r+1,c+1) && !ofSameColor(r,c,r+1,c+1))
         {
             move = new Move(r,c,r+1,c+1);if(r==7) { move.isPromoting = true;move.piecePromotedTo=-90; }
             move.isCapture=true;moves.add(move);
         }
         if(val==-1 && isValidSquare(r+1,c-1) && !squareEmpty(r+1,c-1) && !ofSameColor(r,c,r+1,c-1))
         {
             move = new Move(r,c,r+1,c-1);if(r==7) { move.isPromoting = true;move.piecePromotedTo=-90; }
             move.isCapture=true;moves.add(move);
         }
         //En Passant Moves
         if(val==1 && r==4 && lastMove.piece==-10 && lastMove.fromRow==2 && lastMove.fromCol==c+1 && lastMove.toRow==4)
         {
             move = new Move(r,c,r-1,c+1);move.enPassant=true;move.isCapture=true;moves.add(move);
         }
         if(val==1 && r==4 && lastMove.piece==-10 && lastMove.fromRow==2 && lastMove.fromCol==c-1 && lastMove.toRow==4)
         {
             move = new Move(r,c,r-1,c-1);move.enPassant=true;move.isCapture=true;moves.add(move);
         }
         if(val==-1 && r==5 && lastMove.piece==10 && lastMove.fromRow==7 && lastMove.fromCol==c-1 && lastMove.toRow==5)
         {
             move = new Move(r,c,r+1,c-1);move.enPassant=true;move.isCapture=true;moves.add(move);
         }
         if(val==-1 && r==5 && lastMove.piece==10 && lastMove.fromRow==7 && lastMove.fromCol==c+1 && lastMove.toRow==5)
         {
             move = new Move(r,c,r+1,c+1);move.enPassant=true;move.isCapture=true;moves.add(move);
         }
         for(Move m:moves)
         {
             m.piece=board[r-1][c-1];
         }
         //Promotion to knight, bishop, and rook added
         List<Move> prom = new ArrayList<>();
         for(Move m:moves)
         {
             if(m.isPromoting)
             {
                 if(m.piecePromotedTo>0) { val=1; } else { val=-1; }
                 Move mov=m.copy(); mov.piecePromotedTo=(short)(val*30);  prom.add(mov);
                 mov=m.copy(); mov.piecePromotedTo=(short)(val*32); prom.add(mov);
                 mov=m.copy(); mov.piecePromotedTo=(short)(val*50); prom.add(mov);
             }
         }
         moves.addAll(prom);
         return moves;
     }

     public List<Move> Queen(int r,int c)
     {
         List<Move> moves=new ArrayList<>();
         moves.addAll(Rook(r,c));
         moves.addAll(Bishop(r,c));
         return moves;
     }

    public List<Move> Rook(int r,int c)
    {
        List<Move> moves=new ArrayList<>();
        int[][] dir={{0,1},{1,0},{-1,0},{0,-1}};
        for(int[]i:dir)
        {
            int row=r+i[0];int col=c+i[1];
            while(isValidSquare(row,col))
            {
                if(!squareEmpty(row,col) && ofSameColor(r,c,row,col)) { break; }
                Move move= new Move(r,c,row,col);
                if(board[row-1][col-1]!=0){move.isCapture=true;}moves.add(move);
                if(!squareEmpty(row,col) && !ofSameColor(r,c,row,col)) { break; }
                row=row+i[0];col=col+i[1];
            }
        }
        for(Move m:moves){m.piece=board[r-1][c-1];}
        return moves;
    }

    public List<Move> Bishop(int r,int c)
    {
        List<Move> moves= new ArrayList<>();
        int[][] dir={{1,1},{1,-1},{-1,1},{-1,-1}};
        for(int[] i:dir)
        {
            int row = r+i[0]; int col = c+i[1];
            while(isValidSquare(row,col))
            {
                if(!squareEmpty(row,col) && ofSameColor(r,c,row,col)) { break; }
                Move move=new Move(r,c,row,col);
                if(board[row-1][col-1]!=0){move.isCapture=true;}moves.add(move);
                if(!squareEmpty(row,col) && !ofSameColor(r,c,row,col)) { break; }
                row=row+i[0];col=col+i[1];
            }
        }
        for(Move m:moves){m.piece=board[r-1][c-1];}
        return moves;
    }

    public List<Move> Nite(int r,int c)
    {
        List<Move> moves= new ArrayList<>();
        int[][] dir={{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2},{1,-2},{-1,-2}};
        for(int[] i : dir)
        {
            int row = r + i[0]; int col = c + i[1];
            if(isValidSquare(row,col))
            {
                Move move = new Move(r,c,row,col);
                if(squareEmpty(row,col)||!ofSameColor(r,c,row,col))
                {
                    if(board[row-1][col-1]!=0){move.isCapture=true;}moves.add(move);
                }
            }
        }
        for(Move m:moves){m.piece=board[r-1][c-1];}
        return moves;
    }

    public boolean isValidSquare(int row,int col)
    {
        if(0<row && row<9 && 0<col && col<9) { return true; }
        return false;
    }

    public boolean squareEmpty(int r,int c)
    {
        if(board[r-1][c-1]==0) { return true; }
        return false;
    }

    public boolean ofSameColor(int r1,int c1,int r2,int c2)
    {
        if(board[r1-1][c1-1]*board[r2-1][c2-1]<0) { return false; }
        return true;
    }

    public Position(String fen,boolean turn)
    {
        int l=fen.length();int t=0,x=0;char ch;
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                board[i][j]=0;
            }
        }
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                ch=fen.charAt(x);t=0;
                if(ch=='/'){x++;j--;continue;}if(ch=='r'){board[i][j]=-50;t=1;}if(ch=='n'){board[i][j]=-30;t=1;}
                if(ch=='b'){board[i][j]=-32;t=1;}if(ch=='p'){board[i][j]=-10;t=1;}if(ch=='k'){board[i][j]=-100;t=1;BlackKing=new Location(i+1,j+1);}
                if(ch=='q'){board[i][j]=-90;t=1;}if(ch=='R'){board[i][j]=50;t=1;}if(ch=='N'){board[i][j]=30;t=1;}
                if(ch=='B'){board[i][j]=32;t=1;}if(ch=='P'){board[i][j]=10;t=1;}
                if(ch=='K'){board[i][j]=100;WhiteKing=new Location(i+1,j+1);t=1;}
                if(ch=='Q'){board[i][j]=90;t=1;}if(t==0){int n=Character.getNumericValue(ch);j=j+n-1;}
                x++;
            }
        }
        hasWhiteKingMoved=false;     hasBlackKingMoved=false;hasWhiteKingRookMoved=false; hasBlackKingRookMoved=false;
        hasWhiteQueenRookMoved=false;hasBlackQueenRookMoved=false;

        if(board[7][0]!=50){hasWhiteQueenRookMoved=true;}if(board[7][7]!=50){hasWhiteKingRookMoved=true;}
        if(board[0][0]!=-50){hasBlackQueenRookMoved=true;}if(board[0][7]!=-50){hasBlackKingRookMoved=true;}

        isWhitesTurn=turn;lastMove=new Move(0,0,0,0);lastMove.piece=0;lastMove.piecePromotedTo=0;
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                squares[i][j]=new square(i+1,j+1);
            }
        }
        if(squares[WhiteKing.Row-1][WhiteKing.Col-1].attackedByOtherColor((short)(10)))
        {
            whiteInCheck=true;}else{whiteInCheck=false;
        }
        if(squares[BlackKing.Row-1][BlackKing.Col-1].attackedByOtherColor((short)(-10)))
        {
            blackInCheck=true;}else{blackInCheck=false;
        }
    }

    public Position()
    {
        isWhitesTurn = true;
        hasWhiteKingMoved = false;hasBlackKingMoved = false;hasWhiteKingRookMoved = false;
        hasWhiteQueenRookMoved = false;hasBlackKingRookMoved = false;hasBlackQueenRookMoved = false;

        for(int i=1;i<=8;i++)
        {
            for(int j=1;j<=8;j++)
            {
                board[i-1][j-1]=0;
            }
        }
        for(int i=1;i<=8;i++)
        {
            board[2-1][i-1] = -10; board[7-1][i-1] = 10;
        }

        board[0][0] = -50;board[0][7] = -50;board[0][1] = -30;board[0][6] =-30;board[0][2] = -32;
        board[0][5] = -32;board[0][3] = -90;board[0][4] = -100;board[7][0] = 50;
        board[7][7] = 50;board[7][1] = 30; board[7][6] = 30;board[7][2] = 32;
        board[7][5] = 32;board[7][3] = 90;board[7][4] = 100;

        WhiteKing = new Location(8,5);BlackKing = new Location(1,5);lastMove = new Move(1,1,1,1);
        lastMove.piecePromotedTo=0;lastMove.piece=0;

        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                squares[i][j]=new square(i+1,j+1);
            }
        }
    }

    public List<Move> PawnAttack(int r,int c)
    {
        List<Move> moves=new ArrayList<>();
        if(board[r-1][c-1]==10)
        {
            if(isValidSquare(r-1,c+1)){Move m=new Move(r,c,r-1,c+1);moves.add(m);}
            if(isValidSquare(r-1,c-1)){Move m=new Move(r,c,r-1,c-1);moves.add(m);}
        }
        else
        {
            if(isValidSquare(r+1,c+1)){Move m=new Move(r,c,r+1,c+1);moves.add(m);}
            if(isValidSquare(r+1,c-1)){Move m=new Move(r,c,r+1,c-1);moves.add(m);}
        }
        for(Move move:moves)
        {
            move.piece=board[r-1][c-1];
        }
        return moves;
    }

    //square class to help with king legal moves
    public class square
    {
        public List<Short> pieces=new ArrayList<>();Move m;
        public square(int r,int c)
        {
            List<Move> moves=new ArrayList<>();int i,j;
            for(i=0;i<=7;i++)
            {
                for(j=0;j<=7;j++)
                {
                    //call both color pieces
                    short val=board[i][j];
                    if(val==10||val==-10)    { moves.addAll(PawnAttack(i+1,j+1)); }if(val==30||val==-30)    { moves.addAll(Nite(i+1,j+1)); }
                    if(val==32||val==-32){ moves.addAll(Bishop(i+1,j+1)); }if(val==50||val==-50)    { moves.addAll(Rook(i+1,j+1)); }
                    if(val==90||val==-90)    { moves.addAll(Queen(i+1,j+1)); }if(val==100||val==-100)  { moves.addAll(fakeKing(i+1,j+1)); }
                }
            }
            /*This doesnt cover defended pieces for the king!!That is handled by the same loop that filters out pinned pieces moving*/
            for(Move move:moves)
            {
                if(move.toRow==r && move.toCol==c)
                {
                    pieces.add(move.piece);
                }
            }
        }
        //to check if a square is attacked by an enemy piece
        public boolean attackedByOtherColor(short p)
        {
            for(short piece:pieces)
            {
                if(piece*p<0){return true;}
            }
            return false;
        }
    }

    public void display()
    {
        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                short s=board[i][j];
                if(s==-10){System.out.print("♟ ");}
                if(s==-50){System.out.print("♜ ");}
                if(s==-30){System.out.print("♞ ");}
                if(s==-32){System.out.print("♝ ");}
                if(s==-90){System.out.print("♛ ");}
                if(s==-100){System.out.print("♚ ");}
                if(s==0){System.out.print(". ");}
                if(s==10){System.out.print("♙ ");}
                if(s==50){System.out.print("♖ ");}
                if(s==30){System.out.print("♘ ");}
                if(s==32){System.out.print("♗ ");}
                if(s==90){System.out.print("♕ ");}
                if(s==100){System.out.print("♔ ");}
            }
            System.out.println("");
        }
    }
}
