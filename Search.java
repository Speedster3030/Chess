import java.math.*;
import java.util.*;
public class Search
{
 public static int x=0;
 public Eval E=new Eval();
 /*public int minimax(Position position, int depth, int alpha, int beta, boolean isMaximizing) {
    if (position.getMoves().size()==0 || depth==0) {
        x++;
        return E.eval(position);
    }

    List<Move> moves = position.getMoves();
    List<Move> orderedMoves = order(moves);
    int bestValue = isMaximizing ? -25000 : 25000;

    for (Move move : orderedMoves) {
        position.applyMove(move);
        int score = -minimax(position, depth - 1, -beta, -alpha, !isMaximizing);
        position.undoMove(); // Important!

        if (isMaximizing) {
            if (score > bestValue) {
                bestValue = score;
                if (score > alpha) {
                    alpha = score;
                }
            }
        } else {
            if (score < bestValue) {
                bestValue = score;
                if (score < beta) {
                    beta = score;
                }
            }
        }

        if (alpha >= beta) {
            break; // Beta cutoff
        }
    }
    return bestValue;
}*/

 public int minimax(Position position, int depth, int alpha, int beta, boolean isMaximizing) {
   if (depth == 0 || position.getMoves().size()==0) {
     return E.eval(position);
   }

   List<Move> legalMoves = order(position.getMoves());

   if (isMaximizing) {
     int maxEval = Integer.MIN_VALUE;
     for (Move move : legalMoves) {
       position.applyMove(move);
       int eval = minimax(position, depth - 1, alpha, beta, false);
       position.undoMove();

       maxEval = Math.max(maxEval, eval);
       alpha = Math.max(alpha, eval);
       if (beta <= alpha) {
        break;
       }
     }
     return maxEval;
   }
   else {
    int minEval = Integer.MAX_VALUE;
    for (Move move : legalMoves) {
    position.applyMove(move);
    int eval = minimax(position, depth - 1, alpha, beta, true);
    position.undoMove();

    minEval = Math.min(minEval, eval);
    beta = Math.min(beta, eval);
    if (beta <= alpha) {
     break;
    }
   }
   return minEval;
  }
 }
    
    // Quiescence search to avoid horizon effect
 /*   private int quiescenceSearch(Board board, int alpha, int beta) {
        int standPat = evaluateBoard(board);
        
        if (standPat >= beta) return beta;
        if (standPat > alpha) alpha = standPat;
        
        // Only consider captures
        List<Move> captures = board.getCaptureMoves();
        captures = orderMoves(captures, board);
        
        for (Move move : captures) {
            board.makeMove(move);
            int score = -quiescenceSearch(board, -beta, -alpha);
            board.undoMove(move);
            
            if (score >= beta) return beta;
            if (score > alpha) alpha = score;
        }
        
        return alpha;
    }
    
    // Move ordering to improve alpha-beta efficiency
    private List<Move> orderMoves(List<Move> moves, Board board) {
        moves.sort((m1, m2) -> {
            // Prioritize captures
            if (m1.isCapture() && !m2.isCapture()) return -1;
            if (!m1.isCapture() && m2.isCapture()) return 1;
            
            // Then checks
            if (m1.givesCheck(board) && !m2.givesCheck(board)) return -1;
            if (!m1.givesCheck(board) && m2.givesCheck(board)) return 1;
            
            // Then promotions
            if (m1.isPromotion() && !m2.isPromotion()) return -1;
            if (!m1.isPromotion() && m2.isPromotion()) return 1;
            
            return 0;
        });
        return moves;
    }
    
    // More sophisticated evaluation function
    private int evaluateBoard(Board board) {
        if (board.isCheckmate()) {
            return board.getTurn() == Color.WHITE ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
        }
        if (board.isDraw()) {
            return 0;
        }
        
        int score = 0;
        
        // Material
        score += materialScore(board);
        
        // Piece-square tables
        score += pieceSquareTables(board);
        
        // Mobility
        score += mobilityScore(board);
        
        // Pawn structure
        score += pawnStructureScore(board);
        
        // King safety
        score += kingSafetyScore(board);
        
        return board.getTurn() == Color.WHITE ? score : -score;
    }
    
    // Implement these evaluation components
    private int materialScore(Board board) { /* ...  }
    private int pieceSquareTables(Board board) { /* ...  }
    private int mobilityScore(Board board) { /* ...  }
    private int pawnStructureScore(Board board) { /* ...  }
    private int kingSafetyScore(Board board) { /* ...  }
}*/

 /*public static List<Move> Prune(Position p,List<Move> moves,int depth)
 {
  List<Move> pruned=new ArrayList<>();
  if(depth<2)
  {
   pruned=moves;
   return pruned;
  }
  Search s=new Search();
  if(p.isWhitesTurn)
  {
   p.applyMove(moves.get(0));
   int eval=s.minimax(p,1,false);p.undoMove();
   for(Move m:moves)
   {
    p.applyMove(m);
    int ev=s.minimax(p,1,false);
    if(eval<=ev)
    {
     pruned.add(m);eval=ev;
    }
    p.undoMove();
   }
   return pruned;
  }
  else
  {
   p.applyMove(moves.get(0));
   int eval=s.minimax(p,1,true);p.undoMove();
   for(Move m:moves)
   {
    p.applyMove(m);
    int ev=s.minimax(p,1,true);
    if(eval>=ev)
    {
     pruned.add(m);eval=ev;
    }
    p.undoMove();
   }
   return pruned;
  }
 }*/

 public static List<Move> order(List<Move> moves)
 {
  List<Move> checks=new ArrayList<>();
  List<Move> captures=new ArrayList<>();
  List<Move> regMoves=new ArrayList<>();

  for(Move move:moves)
  {
   if(move.isCheck)
   {
    checks.add(move);continue;
   }
   if(move.isCapture)
   {
    captures.add(move);continue;
   }
   regMoves.add(move);
  }

  checks.addAll(captures);
  checks.addAll(regMoves);
  return checks;
 }
}
