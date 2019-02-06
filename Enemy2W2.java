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

/*  controls enemy2g, take 2  */
public class Enemy2W2
{
  static private final int EGNUM = 3;           /* num of enemy groups */

  private boolean alive;
  private int maxwave;                          /* need to get from caller */
  private int cwave;
  private int cdelay;
  private int type;                             /* type of wave */
  private double xspeed;                        /* horizontal speed */
  private double yrspeed;                       /* vertical radian speed */
  private int enum;                             /* num of enemy in a group */

  private Enemy2G2 enemyg[] = new Enemy2G2[EGNUM];

  public Enemy2W2()
  {
    for (int i = 0; i < EGNUM; i++)
      enemyg[i] = new Enemy2G2();               /* allocate all enemy groups */
    alive = false;
  }

  public void killit()
  {
    alive = false;
    for (int i = 0; i < EGNUM; i++)
      enemyg[i].killit();
  }

  public void init(int imaxwave, int itype, int ienum, double ixspeed, 
                   double iyrspeed)
  /*  int imaxwave - max number of waves (groups)       *\
   *  int itype - type of wave                          *
   *  int ienum - num of enemy in a group               *
   *  double ixspeed - horizontal speed                 *
  \*  double iyrspeed - vertical radian speed           */
  {
    alive = true;
    type = itype;
    maxwave = imaxwave;
    enum = ienum;
    xspeed = ixspeed;
    yrspeed = iyrspeed;
    cdelay = 0;
    if (Enemy2G2.SEPARATE == type)
    {
      enemyg[0].init(type, true, enum, xspeed, yrspeed);
      cwave = 1;
    }
    else if (Enemy2G2.MIX == type)
    {
      enemyg[0].init(type, true, enum, xspeed, yrspeed);
      enemyg[1].init(type, false, enum, xspeed, yrspeed);
      cwave = 2;
    }
    else
      System.out.println("Invalid type: Enemy2W2:init()");
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int mindelay, int idelay, int level, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - referenct to pwrcapg                    *
   *  Scores scores - reference to scores                       *
   *  int mindelay - min delay between group                    *
   *  int idelay - delay between enemy                          *
   *  int level - current level                                 *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean = true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      if (Enemy2G2.SEPARATE == type)
      {
        boolean bo = false;
        for (int i = 0; i < EGNUM; i++)
        {
          if (enemyg[i].move(ship, efireg, pwrcapg, scores, idelay, level, snd))
            bo = true;
        }

        cdelay++;
        if ((cdelay > mindelay) && (cwave < maxwave))
        {                                       /* send in another wave */
          for (int i = 0; i < EGNUM; i++)
          {                                     /* keep trying until valid */
            if (enemyg[i].init(type, true, enum, xspeed, yrspeed))
            {
              cwave++;
              cdelay = 0;
              break;
            }
          }
        }

        if ((!bo) && (cwave >= maxwave))
          alive = false;                        /* all done */
      }
      else if (Enemy2G2.MIX == type)
      {
        boolean b1, b2;
        b1 = b2 = true;
        b1 = enemyg[0].move(ship, efireg, pwrcapg, scores, idelay, level, snd);
        b2 = enemyg[1].move(ship, efireg, pwrcapg, scores, idelay, level, snd);
        cdelay++;

        if ((!b1) && (!b2) && (cdelay > mindelay) && (cwave < maxwave))
        {                               /* start BOTH at once */
          enemyg[0].init(type, true, enum, xspeed, yrspeed);
          enemyg[1].init(type, false, enum, xspeed, yrspeed);
          cwave++;
          cdelay = 0;
        }
        if ((!b1) && (!b2) && (cwave >= maxwave))
          alive = false;                        /* all done */
      }
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

