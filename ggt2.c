int ggt2(int x, int y)
{
  int r;
  do {
    r = x % y;
    x = y;
    y = r;
   } while(y!=0);
   return x;}
