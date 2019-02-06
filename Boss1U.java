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

/*  upper part  */
public class Boss1U extends IFO
{
  static private final int WIDTH_TEST = 30;
  static private final int HEIGHT_TEST = 10;

  static private final int WIDTH_REL = 180;
  static private final int HEIGHT_REL = 60;

  static private int WIDTH;
  static private int HEIGHT;

//  static private final int OFFSET = 40;
  static private final int OFFSET = 1;

  static private final int DDELAY = 20;

  private boolean top;          /* flag for top or bottom */

  public Boss1U()
  {
    super((ConfigJD.TEST) ? WIDTH_TEST : WIDTH_REL,
          (ConfigJD.TEST) ? HEIGHT_TEST : HEIGHT_REL);

    if (ConfigJD.TEST)
    {
      WIDTH = WIDTH_TEST;
      HEIGHT = HEIGHT_TEST;
    }
    else
    {
      WIDTH = WIDTH_REL;
      HEIGHT = HEIGHT_REL;
    }
  }

  public void reset(double ix, double iy, int ih, boolean itop)
  /*  double ix - x coordinate of boss                  *\
   *  double iy - y coordinate of boss                  *
   *  double ih - height of boss                        *
  \*  boolean itop - flag for top or bottom part        */
  {
    top = itop;
    alive = true;
    dying = false;
    ddelay = 0;
    if (top)
      setxy(ix - OFFSET - WIDTH / 2, iy - ih / 2 - HEIGHT - 5);
    else
      setxy(ix - OFFSET - WIDTH / 2, iy + ih / 2 + 5);
  }

  public boolean move(double ix, double iy, Ship ship, int ih, boolean top)
  /*  double ix - x coordinate of boss                          *\
   *  double iy - y coordinate of boss                          *
   *  Ship ship - reference to ship                             *
   *  double ih - height of boss                                *
   *  boolean top - flag for top or bottom part                 *
  \*    Return: boolean - true if alive, false otherwise        */
  {
    if (alive)
    {
      if (dying)
      {
        if (ddelay > DDELAY)
          alive = false;
        else
          ddelay++;
      }
      else
        ship.ChkStatus(this);           /* crash into ship? */

      if (top)
        setxy(ix - OFFSET - WIDTH / 2, iy - ih / 2 - HEIGHT - 5);
      else
        setxy(ix - OFFSET - WIDTH / 2, iy + ih / 2 + 5);
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    int cx = (int) getx();
    int cy = (int) gety();

    g.setColor(Color.white);

    if (ConfigJD.DEBUG)
      g.drawRect(cx, cy, WIDTH, HEIGHT);

    if (dying)
    {
      int wx = WIDTH / DDELAY;
      int wy = HEIGHT / DDELAY;

      g.setColor(Color.yellow);
      g.fillOval(cx + WIDTH / 2 - wx * ddelay, cy + HEIGHT / 2 - wy * ddelay,
                 wx * ddelay * 2, wy * ddelay * 2);
    }
    else
    {
      if (top)
      {
        g.fillArc(cx + WIDTH / 4, cy, WIDTH * 3 / 2, HEIGHT / 2, 90, 180);
        g.fillArc(cx, cy + HEIGHT / 2, WIDTH * 2, HEIGHT, 90, 90);
        g.setColor(Color.black);
        g.drawLine(cx + WIDTH / 4, cy + HEIGHT / 4,
                   cx + WIDTH, cy + HEIGHT / 4);
        g.drawArc(cx + HEIGHT, cy + HEIGHT * 3 / 4, (WIDTH - HEIGHT) * 2,
                  HEIGHT / 2, 90, 90);
      }
      else
      {
        g.fillArc(cx + WIDTH / 4, cy + HEIGHT / 2,
                  WIDTH * 3 / 2, HEIGHT / 2, 90, 180);
        g.fillArc(cx, cy - HEIGHT / 2, WIDTH * 2, HEIGHT, 180, 90);
        g.setColor(Color.black);
        g.drawLine(cx + WIDTH / 4, cy + HEIGHT * 3 / 4,
                   cx + WIDTH, cy + HEIGHT * 3 / 4);
        g.drawArc(cx + HEIGHT, cy - HEIGHT / 4, (WIDTH - HEIGHT) * 2,
                  HEIGHT / 2, 180, 90);
      }
    }
  }
}


