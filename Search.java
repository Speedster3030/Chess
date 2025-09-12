import java.math.*;
import java.util.*;
public class Search
{
 public static int x=0;
 public Eval E=new Eval();

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
