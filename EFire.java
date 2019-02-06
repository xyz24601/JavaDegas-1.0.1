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

/*  normal enemy fire  */
public class EFire extends IFO
{
  static private final int WIDTH = 5;
  static private final int HEIGHT = 5;

  private double xyspeed[] = new double[2];

  public EFire()
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
      double cy = gety();

      if ((ship.ChkCrash(this)) || (ship.ChkShield(this)) ||
          (cx < -WIDTH) || (cx > JavaDegas.G_WIDTH) ||
          (cy < -HEIGHT) || (cy > JavaDegas.G_HEIGHT))
      {                         /* hit ship?, or moved out of screen? */
        alive = false;
        return(true);
      }
      setxy(cx - xyspeed[0], cy - xyspeed[1]);
    }
    return(false);
  }

  public boolean fire(double ix, double iy, IFO ship, double speed)
  /*  double ix - x coordinate of firing place          *\
   *  double iy - y coordinate of firing place          *
   *  IFO ship - reference to ship                      *
   *  double speed - speed of fire                      *
   *    Return boolean: true if fired, false otherwise  *
  \*            fire direct shot                        */
  {
    if (!alive)
    {                                   /* calculate xspeed & yspeed */
      CalTools.EFire_calcxy(ix, iy, ship, speed, xyspeed);
      alive = true;
      setxy(ix, iy);
      return(true);
    }
    return(false);
  }

  public boolean fireRadian(double ix, double iy, double rad, double speed)
  /*  double ix - x coordinate of firing place                  *\
   *  double iy - y coordinate of firing place                  *
   *  double rad - degree in radian                             *
   *  double speed - speed of fire                              *
   *    Return: boolean - true if fired, false otherwise        *
  \*            fire dump shot in rad angle                     */
  {
    if (!alive)
    {
//      xyspeed[0] = speed * Math.cos(rad);
//      xyspeed[1] = speed * Math.sin(rad);
      xyspeed[0] = -speed * Math.cos(rad);
      xyspeed[1] = -speed * Math.sin(rad);
      alive = true;
      setxy(ix, iy);
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

      g.fillOval(cx, cy, WIDTH, HEIGHT);
    }
  }

}

