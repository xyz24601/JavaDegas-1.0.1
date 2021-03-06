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

/*  controls enemy7w2  */
public class Enemy7W2
{
  static private final int EGNUM = 3;           /* num of enemy groups */

  private boolean alive;
  private int maxwave;                          /* need to get from caller */
  private int cwave;
  private int cdelay;
  private double lx;                            /* initial x coordinate */

  private int maxenum;                          /* current max enemy num */
  private double speed;

  private int rdelay;                           /* need to get from caller */

  private Enemy7G2 enemyg[] = new Enemy7G2[EGNUM];

  public Enemy7W2()
  {
    for (int i = 0; i < EGNUM; i++)
      enemyg[i] = new Enemy7G2();       /* allocate all enemy groups */
    alive = false;
  }

  public void killit()
  {
    alive = false;
    for (int i = 0; i < EGNUM; i++)
      enemyg[i].killit();
  }

  public void init(int imaxwave, double ix, int imaxenum, double ispeed,
                   int irdelay)
  /*  int imaxwave - max number of waves (groups)       *\
   *  double ix - initial x coordinate                  *
   *  int imaxenum - current max enemy num              *
   *  double ispeed - speed                             *
  \*  int rdelay - delay before ready                   */
  {
    alive = true;
    maxwave = imaxwave;
    maxenum = imaxenum;
    speed = ispeed;
    rdelay = irdelay;
    cdelay = 0;
    lx = ix;
    enemyg[0].init(lx, false, maxenum, speed, rdelay);
    cwave = 1;
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
//                      int mindelay, int level, int rdelay, int fdelay)
                      int mindelay, int level, int fdelay, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  int mindelay - min delay between group                    *
   *  int level - level                                         *
//   *  int rdelay - delay before ready                         *
   *  int fdelay - delay before fire                            *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo = false;
      for (int i = 0; i < EGNUM; i++)
      {
//        if (enemyg[i].move(ship, efireg, pwrcapg, scores, level, rdelay,
//                           fdelay))
        if (enemyg[i].move(ship, efireg, pwrcapg, scores, level, fdelay, snd))
          bo = true;
      }

      cdelay++;
      if ((cdelay > mindelay) && (cwave < maxwave))
      {
        for (int i = 0; i < EGNUM; i++)
        {
//          if (enemyg[i].init(lx, (Math.random() > 0.8) ? true : false,
//                               maxenum, speed))
          if (enemyg[i].init(lx, (Math.random() > 0.8) ? true : false,
                               maxenum, speed, rdelay))
          {
            cwave++;
            cdelay = 0;
            break;
          }
        }
      }

      if ((!bo) && (cwave >= maxwave))
        alive = false;                          /* all done */
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      for (int i = 0; i < EGNUM; i++)
        enemyg[i].paint(g);
    }
  }

}

