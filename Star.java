/*
    JavaDegas v1.0.1 --- Space Shooting Game Classic
    Copyright (C) 2000  Shinji Umeki (shinji@umeki.org)

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

public class Star
{
  static private int SDELAY = 5;        /* shine delay */

  private double speed;
  private int colori;           /* index for colors array */
  private int size;
  private int numcolors;
  private int csdelay;          /* current shine delay */

  private Coord coord;

  public Star(int starsize, double ispeed)
  /*  int starsize - size of star       *\
  \*  double ispeed - speed of star     */
  {
    speed = ispeed;
    size = starsize;
    numcolors = Colors.NUMCOLOR - 1;            /* randomly get index */
    colori = (int) Math.round(Math.random() * numcolors);

    coord = new Coord();                        /* randomly place on screen */
    coord.setxy(Math.random() * JavaDegas.G_WIDTH,
                Math.random() * JavaDegas.G_HEIGHT);
    csdelay = 0;
  }

  public void move(boolean left, boolean right, boolean up, boolean down)
  /*  boolean left - flag for left movement     *\
   *  boolean right - flag for right movement   *
   *  boolean up - flag for up movement         *
  \*  boolean down - flag for down movement     */
  {
    if (!JavaDegas.sstopped)
    {
      if ((left) || (right) || (up) || (down))
      {
        double x = coord.getx();
        double y = coord.gety();

        if (left)
        {
          x -= speed;
          if (x < -size)
            x = JavaDegas.G_WIDTH;
        }
        if (right)
        {
          x += speed;
          if (x > JavaDegas.G_WIDTH)
            x = -size;
        }
        if (up)
        {
          y -= speed;
          if (y < -size)
            y = JavaDegas.G_HEIGHT;
        }
        if (down)
        {
          y += speed;
          if (y > JavaDegas.G_HEIGHT)
            y = -size;
        }
        coord.setxy(x, y);
      }
    }
    if (colori < numcolors)
      colori++;                 /* cycle colors */
    else
      colori = 0;

    if (0 == csdelay)
    {
      if (Math.random() > 0.99)
        csdelay = 1;
    }
    else if (csdelay > SDELAY)
      csdelay = 0;
    else
      csdelay++;
  }

  public void paint(Graphics g)
  {
    int cx = (int) coord.getx();
    int cy = (int) coord.gety();

    g.setColor(Colors.starColors[colori]);
    g.fillOval(cx, cy, size, size);

    if (0 != csdelay)
    {
//      g.drawLine(cx - size, cy + size / 2, cx + size * 2, cy + size / 2);
//      g.drawLine(cx + size / 2, cy - size, cx + size / 2, cy + size * 2);
      g.drawLine(cx - size / 2, cy + size / 2, cx + size * 3 / 2, cy + size / 2);
      g.drawLine(cx + size / 2, cy - size / 2, cx + size / 2, cy + size * 3 / 2);
    }
  }
}

