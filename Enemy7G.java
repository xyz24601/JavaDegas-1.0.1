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

/*  controls Enemy7  */
public class Enemy7G
{
  static private final int MAXENUM = 10;

  private boolean alive;
  private double rd[] = new double[2];
  private boolean power;                /* flag for power enemy */
  private boolean dead[] = new boolean[MAXENUM];
                                        /* avoid putting multiple capsules */

  private int maxenum;

  private Enemy7 enemy[] = new Enemy7[MAXENUM];

  public Enemy7G()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy7();
    alive = false;              /* needed for use by enemy7w */
  }

  public void killit()
  {
    alive = false;
  }

//  public boolean init(boolean pwr, int imaxenum, double speed)
  public boolean init(boolean pwr, int imaxenum, double speed, int rdelay)
  /*  boolean pwr - flag for power enemy                *\
   *  int imaxenum - current max enemy num              *
   *  double speed - speed                              *
   *  int rdelay - delay before ready                   *
  \*    Return: boolean - true if init, false otherwise */
  {
    if (alive)
      return(false);
    else
    {
      alive = true;
      power = pwr;

      if (imaxenum > MAXENUM)           /* set current max */
        maxenum = MAXENUM;
      else
        maxenum = imaxenum;

      for (int i = 0; i < maxenum; i++)
      {                                 /* randomly place enemy */
        enemy[i].reset(Math.random() * (JavaDegas.G_WIDTH - Enemy7.WIDTH),
                       Math.random() * (JavaDegas.G_HEIGHT - Enemy7.HEIGHT),
                       speed, pwr, rdelay);
        dead[i] = false;
      }
      return(true);
    }
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
//                      int level, int rdelay, int fdelay)
                      int level, int fdelay, JDSounds snd)
  /*  Ship ship - reference to Ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to PwrCapG                    *
   *  Scores scores - reference to scores                       *
   *  int level - current level                                 *
//   *  int rdelay - delay before ready                         *
   *  int fdelay - delay before fire                            *
   *  JDSounds snd - reference to jdsound			*
   *    Return: boolean - true if at least one enemy is alive,  *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      alive = false;
      boolean bo;
      for (int i = 0; i < maxenum; i++)
      {
//        bo = enemy[i].move(ship, level, efireg, scores, rdelay, fdelay, rd);
        bo = enemy[i].move(ship, level, efireg, scores, fdelay, snd, rd);
        if ((!bo) && (0.0 != rd[0]) && (power) && (!dead[i]))
        {
          dead[i] = true;
          pwrcapg.place(rd[0], rd[1], false);   /* place power capsule */
        }

        if (bo)
          alive = true;
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

