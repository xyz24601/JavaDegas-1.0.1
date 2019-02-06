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

/*  enemy missile  */
public class EMissile extends IFO
{
  static private final int WIDTH = 30;
  static private final int HEIGHT = 8;

  private double speed;

  public EMissile()
  {
    super(WIDTH, HEIGHT);

    alive = ready = false;
  }

  public boolean move(Ship ship)
  /*  Ship ship - reference to ship                             *\
   *    Return: boolean - true when it goes out of screen,      *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      double cx = getx();

      if ((ship.ChkCrash(this)) || (ship.ChkShield(this)) || (cx < -WIDTH))
      {                 /* hit ship?. or moved out of screen? */
        ship.shield.hit();      /* add extra damage */
        alive = false;
        return(true);
      }
      setxy(cx - speed, gety());
    }
    return(false);
  }

  public boolean fire(double ix, double iy, double ispeed)
  /*  double ix - initial x coordinate                          *\
   *  double iy - initial y coordinate                          *
   *  double ispeed - speed                                     *
  \*    Return: boolean = true if fired, false otherwise        */
  {
    if (!alive)
    {
      alive = true;
      speed = ispeed;
      setxy(ix, iy - HEIGHT / 2);
      return(true);
    }
    return(false);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      int cx = (int) getx();
      int cy = (int) gety();

      g.setColor(Color.white);

      if (ConfigJD.DEBUG)
        g.drawRect(cx, cy, WIDTH, HEIGHT);

      g.fillArc(cx, cy, WIDTH * 3 / 2, HEIGHT, 90, 180);
      g.fillRect(cx + WIDTH - 5, cy, 5, HEIGHT);
    }
  }

}

