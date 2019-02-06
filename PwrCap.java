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

/*  power capsule  */
public class PwrCap extends IFO
{
  static private final int WIDTH_TEST = 8;
  static private final int HEIGHT_TEST = 3;

  static private final int WIDTH_REL = 20;
  static private final int HEIGHT_REL = 20;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int SCORE = 5;
  static private final int GINC = 1;	/* graphics increment */

  private boolean option;
  public int type;

  private int gcounter;			/* graphics counter */
  private int ginc;			/* graphics increment */

  public PwrCap()
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

    alive = ready = option = false;
  }

  public boolean move(Ship ship, Scores scores)
  /*  Ship ship - reference to ship                             *\
   *  Scores scores - reference to scores                       *
  \*    Return: boolean - true if alive, false otherwise        */
  {
    if (alive)
    {
      double cx = getx();

      if (ship.ChkPower(this))
      {                                 /* picked up by ship */
        alive = false;
        scores.add(SCORE);
        return(true);
      }

      if (cx < -WIDTH)
      {                                 /* moved out of screen */
        alive = false;
        return(true);
      }

      if (!JavaDegas.sstopped)
        setxy(cx - JavaDegas.SCROLL_SPEED, gety());
    }
    return(false);
  }

  public boolean place(double ix, double iy, boolean iop)
  /*  double ix - x coordinate                          *\
   *  double iy - y coordinate                          *
  \*  boolean iop - flag for option or normal power     */
  {
    if (!alive)
    {
      setxy(ix - WIDTH / 2, iy - HEIGHT / 2);
      alive = true;
      option = iop;
      gcounter = 0;
      ginc = GINC;

      if (!option)
      {
        double rval = Math.random();
        if (rval > 0.8)
          type = 2;
        else if (rval > 0.3)
          type = 1;
        else if (rval > 0.1)
          type = -1;
        else
          type = 0;
      }

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

      if (ConfigJD.DEBUG)
        g.drawRect(cx, cy, WIDTH, HEIGHT);

      if (option)
      {
        g.setColor(Color.green);

        g.drawArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT, 270, 90);
        g.drawArc(cx, cy, WIDTH, HEIGHT, 0, 180);

        g.drawLine(cx, cy + HEIGHT / 2, cx, cy + HEIGHT);
        g.drawLine(cx, cy + HEIGHT / 2, cx + WIDTH, cy + HEIGHT / 2);
        g.drawLine(cx + WIDTH / 2, cy, cx + WIDTH / 2, cy + HEIGHT / 2);
      }
      else
      {
        if (2 == type)
          g.setColor(Color.green);
        else if (1 == type)
          g.setColor(Color.blue);
        else if (0 == type)
          g.setColor(Color.yellow);
        else
          g.setColor(Color.red);

        g.drawOval(cx, cy, WIDTH, HEIGHT);
        g.fillOval(cx + WIDTH / 2 - gcounter, cy + HEIGHT /2 - gcounter,
                   gcounter * 2, gcounter * 2);
        if ((gcounter > (HEIGHT / 2)) || (gcounter < 0))
          ginc = -ginc;
        gcounter += ginc; 
      }
    }
  }

  public boolean isOption()
  {
    return(option);
  }

}



