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

/*  controls Enemy5  */
public class Enemy5G
{
  static private final int MAXENUM = 20;

  private boolean alive;
  private int mdelay;           /* passed in delay value */
  private int cdelay;
  private int cenum;
  private boolean power;                /* flag for power enemy */

  private double rd[] = new double[2];

  private int maxenum;

  private Enemy5 enemy[] = new Enemy5[MAXENUM];

  public Enemy5G()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy5();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(int hloc, int imdelay, boolean pwr, int imaxenum,
                      int numfplaces, double speed)
  /*  int hloc - horizontal location                    *\
   *  int imdelay - delay between waves                 *
   *  boolean pwr - flag for power enemy                *
   *  int imaxenum - current max enemy num              *
   *  int numfplaces - num of firing places             *
   *  double speed - speed                              *
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
        enemy[i].reset(hloc, speed, power, numfplaces);

      cenum = 1;                        /* only 1 enemy at the beginning */
      cdelay = 0;
//      mdelay = imdelay / (maxenum + 1);
      mdelay = imdelay / maxenum;
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
      if (cdelay < mdelay)              /* add delay between */
        cdelay++;
      else if (cenum < maxenum)
      {
        cenum++;                        /* add another enemy */
        cdelay = 0;
      }

      alive = false;
      for (int i = 0; i < cenum; i++)
      {
        if ((enemy[i].move(ship, level, efireg, scores, snd, rd)) ||
            (cenum < maxenum))
          alive = true;
        else
        {
          if ((0.0 != rd[0]) && (power))
          {
            pwrcapg.place(rd[0], rd[1], false);
            rd[0] = 0.0;        /* avoid putting multiple capsules */
          }
        }
      }         /* as long as at least ONE enemy is alive and there are */
                /* remaining enemy, set the entire group to be alive */
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      for (int i = 0; i < cenum; i++)
        enemy[i].paint(g);
    }
  }

}

