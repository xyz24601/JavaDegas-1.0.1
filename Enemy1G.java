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

/*  controls Enemy1  */
public class Enemy1G
{
  static private final int MAXENUM = 10;

  private boolean alive;
  private int cdelay;
  private int cenum;                    /* current enemy num of screen */
  private double rd[] = new double[2];  /* returned x, y coordinate */
  private int maxenum;                  /* current max enemy num */

  private Enemy1 enemy[] = new Enemy1[MAXENUM];

  public Enemy1G()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy1();                  /* allocate all enemy */
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(boolean top, int ixc, int iyc, int imaxenum,
                      double ispeed)
  /*  boolean top - flag for top or bottom              *\
   *  int ixc - horizontal movement count               *
   *  int iyc - horizontal movement count               *
   *  int imaxenum - current max enemy num              *
   *  double ispeed - current speed                     *
  \*    Return: boolean - true if init, false otherwise */
  {
    if (alive)
      return(false);
    else
    {
//      double yloc = JavaDegas.G_HEIGHT / 10;     /* set y coordinate */
//      if (!top)
//        yloc = yloc * 8;
      double yloc;
      if (top)
        yloc = 0.0;
      else
        yloc = JavaDegas.G_HEIGHT - Enemy1.HEIGHT;
    
      if (imaxenum > MAXENUM)                   /* set current max */
        maxenum = MAXENUM;
      else
        maxenum = imaxenum;

      for (int i = 0; i < maxenum; i++)
        enemy[i].reset(yloc, ispeed, ixc, iyc);

      cenum = 1;                        /* only 1 enemy at the beginning */
      cdelay = 0;
      alive = true;
      return(true);
    }
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int idelay, int level, JDSounds snd)
  /*  Ship ship - reference to Ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to PwrCapG                    *
   *  Scores scores - reference to scores                       *
   *  int idelay - delay between enemy                          *
   *  int level - current level                                 *
   *  JDSounds snd - reference to jdsounds			*
   *    Return boolean - true if at least one enemy is alive,   *
  \*                     false otherwise                        */
  {
    if (alive)
    {
      if (cdelay < idelay)              /* add delay between */
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
      }         /* as long as at least ONE enemy is alive and there are */
                /* remaining enemy, set the entire group to be alive */
      if ((!alive) && (0.0 != rd[0]))   /* last enemy has died */
      {
        pwrcapg.place(rd[0], rd[1], false);     /* place power capsule */
      }
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

