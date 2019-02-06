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

/*  controls all power capsules  */
public class PwrCapG
{
  static private final int MAXCAP = 15;

  private PwrCap caps[] = new PwrCap[MAXCAP];
  private int cnum;

  public PwrCapG()
  {
    for (int i = 0; i < MAXCAP; i++)
      caps[i] = new PwrCap();
    cnum = 0;
  }

  public void deleteall()
  {
    for (int i = 0; i < MAXCAP; i++)
      caps[i].alive = false;
    cnum = 0;
  }

  public void move(Ship ship, Scores scores)
  /*  Ship ship - reference to ship             *\
  \*  Scores scores - reference to scores       */
  {
    for (int i = 0; i < MAXCAP; i++)
    {
      if (caps[i].move(ship, scores))
        cnum--;                 /* cap is out of screen, or picked up */
    }
  }

  public void place(double ix, double iy, boolean iop)
  /*  double ix - x coordinate                          *\
   *  double iy - y coordinate                          *
  \*  boolean iop - flag for option or normal power     */
  {
    if (cnum < MAXCAP)          /* can not place more than max cap */
    {
      int i = 0;
      while (i < MAXCAP)        /* keep trying until valid place */
      {
        if (caps[i].place(ix, iy, iop))
        {                               /* valid place */
          cnum++;
          break;
        }
        else
        {
          i++;                          /* nope, keep trying */
        }
      }
    }
  }

  public void paint(Graphics g)
  {
    for (int i = 0; i < MAXCAP; i++)
      caps[i].paint(g);
  }

}

