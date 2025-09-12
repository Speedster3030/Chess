import java.util.*;
public class perft
{
        private static int check=0;
        private static int capt=0;
        public static void main(String[] args)
        {
                Position p=new Position("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R",true);
                p.hasWhiteKingMoved=false;
                p.hasBlackKingMoved=true;
                p.display();
                int depth=2;
                List<Move> moves=new ArrayList<>();
                moves=p.getMoves();

                long startTime=System.currentTimeMillis();
                System.out.println(PosCount(p,depth)+" positions found, depth "+depth);
                System.out.println(check+" checks");
                System.out.println(capt+" captures");
                long endTime=System.currentTimeMillis();
                System.out.println((endTime-startTime)+" milliseconds");
        }

        public static int PosCount(Position pos,int depth)
        {
                if(depth==1)
                {
                        int n=pos.getMoves().size();
                        return n;
                }
                int count=0;
                List<Move> moves=new ArrayList<>();
                moves=pos.getMoves();
                for(Move move:moves)
                {
                        pos.applyMove(move);
                        count+=PosCount(pos,depth-1);
                        pos.undoMove();
                }
                return count;
        }
}

