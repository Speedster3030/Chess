import java.util.*;
import java.io.*;
public class play extends Search
{
    public static void main(String[] args)throws IOException
    {
        Search s=new Search();
        List<Move> moves=new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        Position p=new Position();int n=0;boolean turn=true;

        while(true)
        {
            p.display();moves=s.order(p.getMoves());n=0;
            System.out.println("");
            if(p.getMoves().size()==0)
            {
                break;
            }
            if(p.isWhitesTurn)
            {
                for(Move move:moves)
                {
                    n++;
                    System.out.println(n+"- "+"row: "+move.toRow+" col: "+move.toCol+ " piece: "+move.piece);
                }
                int inp=Integer.parseInt(br.readLine());
                p.applyMove(moves.get(inp-1));
            }
            else
            {
                int bestev=25000;Move bestmove=moves.get(0);

                for(Move move:moves)
                {
                    p.applyMove(move);
                    int eval=s.minimax(p,2,0,0,true);
                    if(eval<bestev)
                    {
                        bestev=eval;bestmove=move;
                    }
                    p.undoMove();
                }
                p.applyMove(bestmove);
            }
        }
    }
}
