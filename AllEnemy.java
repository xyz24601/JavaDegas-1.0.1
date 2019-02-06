/*
    JavaDegas v1.0.1 --- Space Shooting Game Classic
    Copyright (C) 2000  Shinji Umeki (shinji@umeki.ord)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    See the file, COPYING, for more details.
*/

import java.awt.*;


/*  This class is responsible to allocating all enemy objects.          *\
 *  All the other classes can access all enemy objects thru this        *
\*  class.                                                              */

public class AllEnemy
{
  public Enemy1W enemy1w;
  public Enemy2W enemy2w;
  public Enemy2W2 enemy2w2;
  public Enemy3W enemy3w;
  public Enemy4W enemy4w;
  public Enemy5W enemy5w;
  public Enemy6W enemy6w;
  public Enemy7W enemy7w;
  public Enemy7W2 enemy7w2;

  public Boss1W boss1w;

  public AllEnemy()
  {
    enemy1w = new Enemy1W();            /* allocate all enemy objects */
    enemy2w = new Enemy2W();
    enemy2w2 = new Enemy2W2();
    enemy3w = new Enemy3W();
    enemy4w = new Enemy4W();
    enemy5w = new Enemy5W();
    enemy6w = new Enemy6W();
    enemy7w = new Enemy7W();
    enemy7w2 = new Enemy7W2();

    boss1w = new Boss1W();
  }

  public void killthemall()
  {
    enemy1w.killit();
    enemy2w.killit();
    enemy2w2.killit();
    enemy3w.killit();
    enemy4w.killit();
    enemy5w.killit();
    enemy6w.killit();
    enemy7w.killit();
    enemy7w2.killit();

    boss1w.killit();
  }

  static public void kaboom(Graphics g, int x, int y, int w, int h)
  /*  Graphics g - reference to graphics        *\
   *  int x - x cooreinate                      *
   *  int y - y coordinate                      *
   *  int w - width of enemy                    *
  \*  int h - height of enemy                   */
  {
    int hw = w / 2;
    int hh = h / 2;
    g.setColor(Color.white);
    g.drawLine(x + hw, y, x + hw, y + 10);
    g.drawLine(x + hw, y + h, x + hw, y + h - 10);
    g.drawLine(x, y + hh, x + 10, y + hh);
    g.drawLine(x + w, y + hh, x + w - 10, y + hh);

    g.drawLine(x + 5, y + 5, x + 15, y + 15);
    g.drawLine(x + 5, y + h - 5, x + 15, y + h - 15);

    g.drawLine(x + w - 5, y + 5, x + w - 15, y + 15);
    g.drawLine(x + w - 5, y + h - 5, x + w - 15, y + h - 15);
  }
}

