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

/*  controls enemy3g  */
public class Enemy3W
{
  static private final int EGNUM = 3;           /* num of enemy groups */

  private boolean alive;
  private int maxwave;                          /* need to get from caller */
  private int cwave;
  private int cdelay;
  private double xxspeed;                       /* max horizontal speed */
  private double xmspeed;                       /* min horizontal speed */
  private double yrspeed;                       /* vertical radian speed */

  private Enemy3G enemyg[] = new Enemy3G[EGNUM];

  public Enemy3W()
  {
    for (int i = 0; i < EGNUM; i++)
      enemyg[i] = new Enemy3G();        /* allocate all enemy groups */
    alive = false;
  }

  public void killit()
  {
    alive = false;
    for (int i = 0; i < EGNUM; i++)
      enemyg[i].killit();
  }

  public void init(int imaxwave, double ixxspeed, double ixmspeed,
                   double iyrspeed)
  /*  int imaxwave - max number of waves (groups)       *\
   *  double ixxspeed - max horizontal speed            *
   *  double ixmspeed - min horizontal speed            *
  \*  double iyrspeed - vertical radian speed           */
  {
    alive = true;
    maxwave = imaxwave;
    xxspeed = ixxspeed;
    xmspeed = ixmspeed;
    yrspeed = iyrspeed;
    cdelay = 0;
    enemyg[0].init(false, xxspeed, xmspeed, yrspeed);
    cwave = 1;
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int mindelay, int level, int maxbounce, int firenum,
                      JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to pwrcapg                    *
   *  Scores scores - reference to scores                       *
   *  int mindelay - min delay between group                    *
   *  int level - level                                         *
   *  int maxbounce - max num of bounce				*
   *  int firenum - num of fires in burst                       *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo = false;
      for (int i = 0; i < EGNUM; i++)
      {
        if (enemyg[i].move(ship, efireg, pwrcapg, scores, level, maxbounce,
                           firenum, snd))
          bo = true;
      }

      cdelay++;
      if ((cdelay > mindelay) && (cwave < maxwave))
      {                                 /* send in another wave */
        for (int i = 0; i < EGNUM; i++)
        {                               /* keep trying until valid */
          if (enemyg[i].init(((Math.random() > 0.8) ? true : false),
                             xxspeed, xmspeed, yrspeed))
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
        enemyg[i].paint(g);
    }
  }

}

