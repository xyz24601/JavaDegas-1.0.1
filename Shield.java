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

public class Shield extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 10;

  static private final int WIDTH_REL = 15;
  static private final int HEIGHT_REL = 35;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int MAXHIT = 5;

  private int chit;

  public Shield()
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

    alive = ready = dying = false;
  }

  public void add(double ix, double iy)
  {
    alive = true;
    dying = false;
    chit = 0;
    setxy(ix, iy);
  }

  public void hit()
  {
    if (chit > MAXHIT)
      alive = false;
    else if (chit == MAXHIT)
      dying = true;
    chit++;
  }

  public void delete()
  {
    alive = false;
  }

  public void move(double ix, double iy)
  {
    if (alive)
    {
      setxy(ix, iy);
    }
  }

  public boolean hasShield()
  {
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      int cx = (int) getx();
      int cy = (int) gety();

      if (dying)
        g.setColor(Color.orange);
      else
        g.setColor(Color.white);

      g.drawArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT / 2, 0, 90);
      g.drawArc(cx - WIDTH, cy + HEIGHT / 2, WIDTH * 2, HEIGHT / 2, 270, 90);
      g.drawLine(cx + WIDTH, cy + HEIGHT / 4, cx + WIDTH, cy + HEIGHT * 3 / 4);

      g.drawArc(cx - WIDTH / 2, cy + HEIGHT / 4, WIDTH, HEIGHT / 2, -90, 180);

      if (ConfigJD.DEBUG)
        g.drawRect(cx, cy, WIDTH, HEIGHT);
    }
  }

}

