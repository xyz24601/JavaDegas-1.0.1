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

/*  One Beam Object     */

public class Beam extends IFO
{
  static private final double SPEED = 7.0;

  static private final int WIDTH_TEST = 2;
  static private final int HEIGHT_TEST = 2;

  static private final int WIDTH_REL = 9;
  static private final int HEIGHT_REL = 4;

  static private int WIDTH;
  static private int HEIGHT;

  public Beam()
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

    ready = false;
    reset();
  }

  public void reset()
  {
    alive = false;
  }

  public boolean move()
  /*    Return: boolean - true when it goes out of screen,      *\
  \*                      false otherwise                       */
  {
    if (alive)
    {
      double x = getx();
      if (x > JavaDegas.G_WIDTH)
      {
        reset();                /* moved out of screen */
        return(true);
      }
      else
      {
        x += SPEED;
        setxy(x, gety());       /* moves straight to the right */
      }
    }
    return(false);
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
        snd.playSafe(snd.beamfire);

      setxy(ix, iy);
      alive = true;
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

      g.fillOval(cx, cy, WIDTH / 2, HEIGHT);
      g.fillOval(cx + WIDTH / 2, cy, WIDTH / 2, HEIGHT);
    }
  }

  public boolean ChkFire(IFO obj)
  /*  IFO obj - reference to enemy                      *\
   *    Return boolean: true if intersects with obj,    *
   *                    false otherwise                 *
  \*  Check to see if this Beam object hit IFO object   */
  {
    if ((alive) && (area.intersects(obj.area)))
    {                                   /* hit enemy */
      reset();                          /* kill itself */
      return(true);
    }
    return(false);
  }
}


