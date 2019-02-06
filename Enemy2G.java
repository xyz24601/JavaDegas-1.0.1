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

/*  controls Enemy2  */
public class Enemy2G
{
  static final int SEPARATE = 0;
  static final int TRIPLE = 1;
  static final int MIX = 2;

  static private final int MAXENUM = 3;

  private boolean alive;
  private double fplace[] = new double[MAXENUM];
                                        /* firing location in radian */
  private double rd[] = new double[2];
  private boolean power;                /* flag for power enemy */
  private int maxenum;

  private Enemy2 enemy[] = new Enemy2[MAXENUM];

  public Enemy2G()
  {
    for (int i = 0; i < MAXENUM; i++)
      enemy[i] = new Enemy2();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(int type, boolean pwr, double xspeed, double yrspeed)
  /*  int type - type of wave                           *\
   *  boolean pwr - flag for power enemy                *
   *  double xspeed - horizontal speed                  *
   *  double yrspeed - vertical radian speed            *
  \*    Return: boolean - true if init, false otherwise */
  {
    if (alive)
      return(false);
    else
    {
      alive = true;
      power = pwr;
    
      switch (type)
      {
        case SEPARATE:  init_SEPARATE(xspeed, yrspeed); break;
        case TRIPLE:    init_TRIPLE(xspeed, yrspeed);   break;
        case MIX:               init_MIX(xspeed, yrspeed);      break;
        default: System.out.println("Invalid type: Enemy2G::init()");
      }
      return(true);
    }
  }

  void init_SEPARATE(double xspeed, double yrspeed)
  /*  double xspeed - horizontal speed          *\
   *  double yrspeed - vertical radian speed    *
  \*    initialize for separate waves           */
  {
    maxenum = 2;
    for (int i = 0; i < maxenum; i++)
      fplace[i] = Math.PI;              /* these fplaces looks good */

    enemy[0].reset(JavaDegas.G_HEIGHT / 2, JavaDegas.G_HEIGHT / 4,
                   0.0, xspeed, yrspeed, power);
    enemy[1].reset(JavaDegas.G_HEIGHT / 2, JavaDegas.G_HEIGHT * 3 / 4,
                   0.0, xspeed, yrspeed, power);
  }

  void init_TRIPLE(double xspeed, double yrspeed)
  /*  double xspeed - horizontal speed          *\
   *  double yrspeed - vertical radian speed    *
  \*    initialize for triple wave              */
  {
    maxenum = 3;
    for (int i = 0; i < maxenum; i++)
      fplace[i] = Math.PI;              /* these fplaces looks good */

    int cen = (int) (Math.random() * JavaDegas.G_HEIGHT * 3 / 4) +
                (JavaDegas.G_HEIGHT / 8);
    enemy[0].reset(JavaDegas.G_HEIGHT / 4, cen + Enemy2.HEIGHT * 2,
                   0.0, xspeed, yrspeed, power);
    enemy[1].reset(JavaDegas.G_HEIGHT / 4, cen,
                   0.0, xspeed, yrspeed, power);
    enemy[2].reset(JavaDegas.G_HEIGHT / 4, cen - Enemy2.HEIGHT * 2,
                   0.0, xspeed, yrspeed, power);
  }

  void init_MIX(double xspeed, double yrspeed)
  /*  double xspeed - horizontal speed          *\
   *  double yrspeed - vertical radian speed    *
  \*    initialize for mix wave                 */
  {
    maxenum = 2;
    fplace[0] = Math.PI * 0.75;         /* these fplaces looks good */
    fplace[1] = Math.PI * 1.75;

    enemy[0].reset(JavaDegas.G_HEIGHT, JavaDegas.G_HEIGHT / 2,
                   0.0, xspeed, yrspeed, power);
    enemy[1].reset(JavaDegas.G_HEIGHT, JavaDegas.G_HEIGHT / 2,
                   Math.PI, xspeed, yrspeed, power);
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
      for (int i = 0; i < maxenum; i++)
      {
        if (enemy[i].move(ship, level, efireg, scores, fplace[i], snd, rd))
          alive = true;
        else
        {
          if ((0.0 != rd[0]) && (power))
          {
            pwrcapg.place(rd[0], rd[1], false);
            rd[0] = 0.0;        /* avoid putting multiple capsules */
          }
        }
      }         /* as long as at least ONE enemy is alive, */
                /* set the entire group to be alive */
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


