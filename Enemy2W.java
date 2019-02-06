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

/*  controls enemy2g  */
public class Enemy2W
{
  static private final int EGNUM = 3;           /* num of enemy groups */

  private boolean alive;
  private int maxwave;                          /* need to get from caller */
  private int cwave;
  private int cdelay;
  private int type;                             /* type of wave */
  private double xspeed;                        /* horizontal speed */
  private double yrspeed;                       /* vertical radian speed */
  private boolean bf;                           /* alternate boolean flag */

  private Enemy2G enemyg[] = new Enemy2G[EGNUM];

  public Enemy2W()
  {
    for (int i = 0; i < EGNUM; i++)
      enemyg[i] = new Enemy2G();                /* allocate all enemy groups */
//    bf = false;
    alive = false;
  }

  public void killit()
  {
    alive = false;
    for (int i = 0; i < EGNUM; i++)
      enemyg[i].killit();
  }

  public void init(int imaxwave, int itype, double ixspeed, double iyrspeed)
  /*  int imaxwave - max number of waves (groups)       *\
   *  int type - type of wave                           *
   *  double ixspeed - horizontal speed                 *
  \*  double iyrspeed - vertical radian speed           */
  {
    alive = true;
    bf = false;
    type = itype;
    maxwave = imaxwave;
    xspeed = ixspeed;
    yrspeed = iyrspeed;
    cdelay = 0;
    enemyg[0].init(type, bf, xspeed, yrspeed);
    cwave = 1;
    bf = !bf;
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int mindelay, int level, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to pwrcapg                    *
   *  Scores scores - reference to scores                       *
   *  int mindelay - min delay between group                    *
   *  int level - level                                         *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo = false;
      for (int i = 0; i < EGNUM; i++)
      {
        if (enemyg[i].move(ship, efireg, pwrcapg, scores, level, snd))
          bo = true;
      }

      cdelay++;
      if ((cdelay > mindelay) && (cwave < maxwave))
      {                                 /* send in another wave */
        for (int i = 0; i < EGNUM; i++)
        {                               /* keep trying until valid */
          if (enemyg[i].init(type, bf, xspeed, yrspeed))
          {
            cwave++;
            cdelay = 0;
            bf = !bf;                   /* alternate */
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
        enemyg[i].paint(g);
    }
  }

}

