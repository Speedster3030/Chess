import java.math.*;
public class Eval
{
    public static int eval(Position p)
    {
        int ev=0;int n=p.getMoves().size();
        if(p.whiteInCheck && n==0)
        {
            return -25000;
        }
        if(p.blackInCheck && n==0)
        {
            return 25000;
        }
        if(n==0 && p.isWhitesTurn && !p.whiteInCheck)
        {
            return 0;
        }
        if(n==0 && !p.isWhitesTurn && !p.blackInCheck)
        {
            return 0;
        }

        for(int i=0;i<=7;i++)
        {
            for(int j=0;j<=7;j++)
            {
                int t=(int)(p.board[i][j]);
                ev=ev+t;
            }
        }
        return ev;
    }
}
