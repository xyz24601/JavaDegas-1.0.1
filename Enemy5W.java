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

/*  controls enemy5g  */
public class Enemy5W
{
  static private final int EGNUM = 3;           /* num of enemy groups */

  private boolean alive;
  private int maxwave;
  private int cwave;
  private int mindelay;
  private int numfplaces;
  private int maxenum;
  private double speed;
  private int cdelay;
  private int hinc;                     /* horizontal location increment */
  private boolean power;
  private boolean half;                 /* flag for half or all the way */

  private Enemy5G enemyg1[] = new Enemy5G[EGNUM];
  private Enemy5G enemyg2[] = new Enemy5G[EGNUM];

  public Enemy5W()
  {
    for (int i = 0; i < EGNUM; i++)
    {
      enemyg1[i] = new Enemy5G();               /* allocate all enemy groups */
      enemyg2[i] = new Enemy5G();
    }
    alive = false;
  }

  public void killit()
  {
    alive = false;
    for (int i = 0; i < EGNUM; i++)
    {
      enemyg1[i].killit();
      enemyg2[i].killit();
    }
  }

  public void init(int imaxwave, int imindelay, int inumfplaces, double ispeed,
                   int imaxenum, boolean pwr, boolean ihalf)
  /*  int imaxwave - max number of waves (groups)       *\
   *  int imindelay - min delay between group           *
   *  int inumfplaces - num of firing places            *
   *  double ispeed - speed                             *
   *  int imaxenum - current max enemy num              *
   *  boolean pwr - flag for power enemy                *
  \*  boolean ihalf - flag for half or all the way      */
  {
    alive = true;
    maxwave = imaxwave;
    mindelay = imindelay;
    numfplaces = inumfplaces;
    speed = ispeed;
    maxenum = imaxenum;
    power = pwr;
    half = ihalf;
    cdelay = 0;
    if (half)
      hinc = (JavaDegas.G_HEIGHT / 2) / (maxwave + 1);
    else
      hinc = JavaDegas.G_HEIGHT / (maxwave + 1);
//    enemyg1[0].init(hinc * (cwave + 1), mindelay, power, maxenum, numfplaces,
//                    speed);
    enemyg1[0].init(hinc * cwave, mindelay, power, maxenum, numfplaces,
                    speed);
    enemyg2[0].init(JavaDegas.G_HEIGHT - hinc * (cwave + 1), mindelay, power,
                    maxenum, numfplaces, speed);
    cwave = 1;
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int level, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to pwrcapg                    *
   *  Scores scores - reference to scores                       *
   *  int level - level                                         *
   *  JDSounds snd - reference to jdsounds			*
   *    ReturnL boolean - true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo = false;
      for (int i = 0; i < EGNUM; i++)
      {
        boolean bo1 = enemyg1[i].move(ship, efireg, pwrcapg, scores, level, snd);
        boolean bo2 = enemyg2[i].move(ship, efireg, pwrcapg, scores, level, snd);
        if ((bo1) || (bo2))
          bo = true;
      }

      cdelay++;
      if ((cdelay > mindelay) && (cwave < maxwave))
      {                                 /* send in another wave */
        for (int i = 0; i < EGNUM; i++)
        {
//          if ((enemyg1[i].init(hinc * (cwave + 1), mindelay, power, maxenum,
//                               numfplaces, speed)) &&
          if ((enemyg1[i].init(hinc * cwave, mindelay, power, maxenum,
                               numfplaces, speed)) &&
              (enemyg2[i].init(JavaDegas.G_HEIGHT - hinc * (cwave + 1),
                               mindelay, power, maxenum, numfplaces, speed)))
          {
            cwave++;
            cdelay = 0;
            break;
          }
        }
      }

      if ((!bo) && (cwave >= maxwave))
        alive = false;                  /* all done */
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      for (int i = 0; i < EGNUM; i++)
      {
        enemyg1[i].paint(g);
        enemyg2[i].paint(g);
      }
    }
  }

}

