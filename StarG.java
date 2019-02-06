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

/*  controls all stars  */
public class StarG
{
  static private final int NUMSTARS = 20;
  static private final double MAXSPEED = 3.0;
  static private final int MAXSIZE = 3;

  private Star star[] = new Star[NUMSTARS];
  private boolean left, right, up, down;

  public StarG()
  {
    for (int i = 0; i < NUMSTARS; i++)  /* randomly initialize stars */
      star[i] = new Star((int) Math.round(Math.random() * MAXSIZE),
                         (Math.random() * MAXSPEED) + 0.1);
    left = right = up = down = false;   /* twinkle star */
  }

  public void goLeft()
  {
    left = true;
    right = up = down = false;
  }

  public void goDown()
  {
    down = true;
    left = right = up = false;
  }

  public void paint(Graphics g)
  {
    for (int i = 0; i < NUMSTARS; i++)
      star[i].paint(g);
  }

  public void move()
  {
    for (int i = 0; i < NUMSTARS; i++)
      star[i].move(left, right, up, down);
  }
}

