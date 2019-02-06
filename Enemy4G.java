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

/*  controls Enemy4  */
public class Enemy4G
{
  static private final int MAXENUM = 5;

  private boolean alive;

  private double rd[] = new double[2];
  private boolean power;                /* flag for power enemy */
  private boolean dead[] = new boolean[MAXENUM];
                                        /* avoid putting multiple capsules */
  private int maxenum;

  private Enemy4 enemy[] = new Enemy4[MAXENUM];

  public Enemy4G()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy4();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(boolean pwr, int imaxenum, int numfplaces, double speed)
  /*  boolean pwr - flag for power enemy                *\
   *  int imaxenum - current max enemy num              *
   *  int numfplaces - num of firing places             *
   *  double speed - speed                              *
  \*    Return: boolean - true if init, false otherwise */
  {
    if (alive)
      return(false);
    else
    {
      if (imaxenum > MAXENUM)                   /* set current max */
        maxenum = MAXENUM;
      else
        maxenum = imaxenum;

      alive = true;
      power = pwr;

      for (int i = 0; i < maxenum; i++)         /* place them evenly */
      {
        enemy[i].reset((i + 1) * (JavaDegas.G_HEIGHT / (maxenum + 2)),
                       speed, power, numfplaces);
        dead[i] = false;
      }
      return(true);
    }
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int level, JDSounds snd)
  /*  Ship ship - reference to Ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to PwrCapG                    *
   *  Scores scores - reference to scores                       *
   *  int level - current level                                 *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if at least one enemy is alive,  *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      alive = false;
      boolean bo;
      for (int i = 0; i < maxenum; i++)
      {
        bo = enemy[i].move(ship, level, efireg, scores, snd, rd);
        if ((!bo) && (0.0 != rd[0]) && (power) && (!dead[i]))
        {                                       /* shot down */
          dead[i] = true;
          pwrcapg.place(rd[0], rd[1], false);   /* place power capsule */
        }

        if (bo)
          alive = true;                 /* true as long as at least 1 alive */
      }
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      for (int i = 0; i < maxenum; i++)
        enemy[i].paint(g);
    }
  }

}

