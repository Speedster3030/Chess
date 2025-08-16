import java.math.*;
public class Eval
{
 public static int eval(Position p)
 {
  int ev=0;int n=p.getMoves().size();
  if(p.whiteInCheck && n==0)
  {
   //p.display();
   //System.out.println(-25000);
   return -25000;
  }
  if(p.blackInCheck && n==0)
  {
   //p.display();
   //System.out.println(25000);
   return 25000;
  }
  if(n==0 && p.isWhitesTurn && !p.whiteInCheck)
  {
   //p.display();
   return 0;
  }
  if(n==0 && !p.isWhitesTurn && !p.blackInCheck)
  {
   //p.display();
   return 0;
  }
  /*if(n>11 && p.history.get(n-1)==p.history.get(n-5) && p.history.get(n-5)==p.history.get(n-9))
  {
   return 0;
  }*/
  for(int i=0;i<=7;i++)
  {
   for(int j=0;j<=7;j++)
   {
    int t=(int)(p.board[i][j]);
    ev=ev+t;
    /*if(t>0)
    {
     ev+=(8-i)+(5-Math.abs(5-j));
    }
    if(t<0)
    {
     ev-=i+1+(5-Math.abs(5-j));
    }*/
   }
  }
  /*if(p.whiteInCheck)
  {
   ev-=5;
  }
  if(p.blackInCheck)
  {
   ev+=5;
  }*/
  //System.out.println(ev);
  return ev;
 }
}
