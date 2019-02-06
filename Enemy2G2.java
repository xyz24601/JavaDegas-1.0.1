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

/*  controls Enemy2, take 2  */
public class Enemy2G2
{
  static final int SEPARATE = 0;
  static final int MIX = 1;

  static private final int MAXENUM = 10;

  private boolean alive;
  private double fplace;                /* firing location in radian */
  private int cdelay;
  private int cenum;                    /* current enemy num of screen */
  private double rd[] = new double[2];  /* returned x, y coordinate */
  private int maxenum;                  /* current max enemy num */

  private Enemy2 enemy[] = new Enemy2[MAXENUM];

  public Enemy2G2()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy2();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(int type, boolean top, int imaxenum, double xspeed,
                      double yrspeed)
  /*  int type - type of wave                   *\
   *  boolean top - flag for top or bottom      *
   *  double xspeed - horizontal speed          *
   *  double yrspeed - vertical radian speed    *
  \*  int imaxenum - current max enemy num      */
  {
    if (alive)
      return(false);
    else
    {
      alive = true;
      cenum = 1;                        /* only 1 enemy at the beginning */
      cdelay = 0;

      if (imaxenum > MAXENUM)                   /* set current max */
        maxenum = MAXENUM;
      else
        maxenum = imaxenum;

      switch (type)
      {
        case SEPARATE:  init_SEPARATE(xspeed, yrspeed); break;
        case MIX:       init_MIX(top, xspeed, yrspeed); break;
        default: System.out.println("Invalid type: Enemy2G2::init()");
      }
      return(true);
    }
  }

  void init_SEPARATE(double xspeed, double yrspeed)
  /*  double xspeed - horizontal speed          *\
  \*  double yrspeed - vertical radian speed    */
  {
    fplace = Math.PI;                   /* this fplace looks good */
    int cen = (int) (Math.random() * JavaDegas.G_HEIGHT * 3 / 4) +
                (JavaDegas.G_HEIGHT / 8);
    for (int i = 0; i < maxenum; i++)
      enemy[i].reset(JavaDegas.G_HEIGHT / 2, cen, 0.0, xspeed, yrspeed, false);
  }

  void init_MIX(boolean top, double xspeed, double yrspeed)
  /*  boolean top - flag for top or bottom      *\
   *  double xspeed - horizontal speed          *
  \*  double yrspeed - vertical radian speed    */
  {
    double ystart;
    if (top)
    {
      ystart = 0.0;
      fplace = Math.PI * 0.75;          /* these fplaces looks good */
    }
    else
    {
      ystart = Math.PI;
      fplace = Math.PI * 1.75;
    }

    for (int i = 0; i < maxenum; i++)
      enemy[i].reset(JavaDegas.G_HEIGHT, JavaDegas.G_HEIGHT / 2,
                     ystart, xspeed, yrspeed, false);
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
   *    Return: boolean - true if at least one enemy is alive,  *
  \*                      false otherwise                       */
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
        if ((enemy[i].move(ship, level, efireg, scores, fplace, snd, rd)) ||
            (cenum < maxenum))
        {
          alive = true;
        }       /* as long as at least ONE enemy is alive and there are */
      }         /* remaining enemy, set the entire group to be alive */

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

