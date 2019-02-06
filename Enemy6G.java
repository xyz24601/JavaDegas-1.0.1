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

/*  controls Enemy6  */
public class Enemy6G
{
  static private final int ENUM = 2;

  private boolean alive;
  private double rd[] = new double[2];

  private Enemy6 enemy[] = new Enemy6[ENUM];

  public Enemy6G()
  {
    for (int i = 0; i < ENUM; i++)
      enemy[i] = new Enemy6();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(double speed, int mwdelay)
  /*  double speed - speed                              *\
   *  int mwdelay - max walk delay                      *
  \*    Return: boolean - true if init, false otherwise */
  {
    if (alive)
      return(false);
    else
    {
      alive = true;
      enemy[0].reset(speed, (int) (Math.random() * mwdelay), true);
      enemy[1].reset(speed, (int) (Math.random() * mwdelay), false);
      return(true);
    }
  }

  public boolean move(Ship ship, EFireG efireg, Scores scores,
                      int level, int stopdelay, int maxstop, JDSounds snd)
  /*  Ship ship - reference to Ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  int level - current level                                 *
   *  int stopdelay - delay for stop                            *
   *  int maxstop - max num of stop                             *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if at least one enemy is alive,  *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo1 = enemy[0].move(ship, level, efireg, scores, stopdelay,
                                  maxstop, snd, rd);
      boolean bo2 = enemy[1].move(ship, level, efireg, scores, stopdelay,
                                  maxstop, snd, rd);
      if ((!bo1) && (!bo2))
        alive = false;                  /* both of them are dead */
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      for (int i = 0; i < ENUM; i++)
        enemy[i].paint(g);
    }
  }

}


