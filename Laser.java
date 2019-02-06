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

/*  laser  */
public class Laser extends IFO
{
  static private final double SPEED = 7.0;
  static private final int HEIGHT = 1;

  private double rightloc;              /* right coordinate of laser */
  private double leftloc;               /* left coordinate of laser */

  private boolean ended;                /* flag to see end of laser */
  private int cdelay;                   /* delay to extend laser */
  private int maxdelay;                 /* need to get it from LaserG */

  public Laser(int imaxdelay)
  /*  int imaxdelay - max laser delay   */
  {
    super(0, HEIGHT);

    ready = false;
    maxdelay = imaxdelay;
    reset();
  }

  public void reset()
  {
    alive = ended = false;
    cdelay = 0;
    rightloc = leftloc = 0.0;
  }

  public boolean move(double ix, double iy, Ship ship)
  /*  double ix - x coordinate of firing position               *\
   *  double iy - y coordinate of firing position               *
   *  Ship ship - reference to ship                             *
   *    Return: boolean - true when it goes out of screen,      *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      if ((cdelay < maxdelay) && (ship.fbon) &&
          (Weapons.WT_LASER == ship.wtype) && (!ended) && (ship.alive))
      {                                 /* extend laser */
        leftloc = ix;
        rightloc += SPEED;              /* extend the width */
        cdelay++;
      }
      else
      {                                 /* detach from the ship */
        ended = true;
        leftloc += SPEED;               /* move to right */
        rightloc += SPEED;

        if (leftloc > JavaDegas.G_WIDTH)
        {                               /* moved out of screen */
          reset();
          return(true);
        }
      }                         /* laser follows ship & options */
      setxywh(leftloc, iy, rightloc - leftloc, HEIGHT);
    }
    return(false);              /* false if laser is still in screen */
  }

  public boolean fire(double ix, double iy, JDSounds snd)
  /*  double ix - x coordinate of firing ship/option            *\
   *  double iy - y coordinate of firing ship/option            *
   *  JDSounds snd - reference to jdsounds			*
  \*    Return: boolean - true if fired, false otherwise        */
  {
    if (!alive)
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.laserfire);
      setxywh(ix, iy, SPEED, HEIGHT);           /* need to specify width */
      rightloc = ix + SPEED;
      alive = true;
      return(true);
    }
    return(false);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      g.setColor(Color.blue);
      g.fillRect((int) getx(), (int) gety(), (int) getw(), HEIGHT);
    }
  }

  public boolean ChkFire(IFO obj)
  /*  IFO obj - reference to enemy                      *\
   *    Return: boolean - true if intersect with obj.   *
  \*                      false otherwise               */
  {
    if ((alive) && (area.intersects(obj.area)))
    {                                   /* hit enemy */
      return(true);             /* laser goes through everything */
    }
    return(false);
  }
}

