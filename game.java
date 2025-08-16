import java.util.*;
public class game extends Search
{
 public static void main(String[] args)
 {
  Position p=new Position("6k1/6p1/5pP1/8/8/5K2/8/4R3",true);
  p.hasWhiteKingMoved=true;
  p.hasBlackKingMoved=true;
  Search s=new Search();
  int eval=s.minimax(p,5,0,0,true);
  System.out.println("best eval:"+eval);
  System.out.println(s.x+" positions evaluated");
  p.display();
 }
}
