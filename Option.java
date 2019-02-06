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

/*  option (multiply?)  */
public class Option extends IFO
{
  static private final int WIDTH_TEST = 8;
  static private final int HEIGHT_TEST = 5;
  static private final int WIDTH_REL = 50;
  static private final int HEIGHT_REL = 25;

  static private int WIDTH;
  static private int HEIGHT;

  public SFireG sfireg;         /* needs to be public to see from Ship */

  public Option()
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

    alive = ready = false;
    sfireg = new SFireG();
  }

  public void move(double ix, double iy, Ship ship, JDSounds snd)
  /*  double ix - x coordinate          *\
   *  double iy - y coordinate          *
  \*  Ship ship - reference to ship     */
  {
    if (alive)
    {
      setxy(ix, iy);
    }
    sfireg.move(getx() + WIDTH, gety() + HEIGHT / 2, ship, snd);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      int cx = (int) getx();
      int cy = (int) gety();

      g.setColor(Color.red);

      if (ConfigJD.DEBUG)
        g.drawRect(cx, cy, WIDTH, HEIGHT);

      g.drawArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT, 270, 90);
      g.drawArc(cx, cy, WIDTH, HEIGHT, 0, 180);

      g.drawLine(cx, cy + HEIGHT / 2, cx, cy + HEIGHT);
      g.drawLine(cx, cy + HEIGHT / 2, cx + WIDTH, cy + HEIGHT / 2);
      g.drawLine(cx + WIDTH / 2, cy, cx + WIDTH / 2, cy + HEIGHT / 2);
    }
    sfireg.paint(g);
  }

  public void add(double ix, double iy)
  /*  double ix - initial x coordinate  *\
  \*  double iy - initial y coordinate  */
  {
    alive = true;
    setxy(ix, iy);
  }

  public void delete()
  {
    alive = false;
    sfireg.reset();
  }

  public void firestat(int wtype, boolean bo)
  {
    sfireg.firestat(wtype, bo);
  }

}


