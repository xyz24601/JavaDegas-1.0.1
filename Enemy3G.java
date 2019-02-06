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

/*  controls Enemy3  */
public class Enemy3G
{
  private boolean alive;
  private double rd[] = new double[2];
  private boolean power;

  private Enemy3 enemy;

  public Enemy3G()
  {
    enemy = new Enemy3();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public boolean init(boolean pwr, double xxspeed, double xmspeed,
                      double yrspeed)
  /*  boolean pwr - flag for power enemy        *\
   *  double xxspeed - max horizontal speed     *
   *  double xmspeed - min horizontal speed     *
  \*  double yrspeed - vertical radian speed    */
  {
    if (alive)
      return(false);
    else
    {
      alive = true;
      power = pwr;

      enemy.reset((int) (Math.random() * JavaDegas.G_HEIGHT / 4 +
                         JavaDegas.G_HEIGHT / 8),
                  0.0, Math.random() * (xxspeed - xmspeed) + xmspeed,
                  yrspeed, power);
      return(true);
    }
  }

  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      int level, int maxbounce, int firenum, JDSounds snd)
  /*  Ship ship - reference to Ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  PwrCapG pwrcapg - reference to PwrCapG                    *
   *  Scores scores - reference to scores                       *
   *  int level - current level                                 *
   *  int maxbounce - max num of bounce                         *
   *  int firenum - num of fires in burst                       *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if at least one enemy is alive,  *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      alive = enemy.move(ship, level, efireg, scores, maxbounce, firenum, snd,
                         rd);
      if ((!alive) && (0.0 != rd[0]) && (power))
      {
        pwrcapg.place(rd[0], rd[1], false);     /* place power capsule */
      }
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
      enemy.paint(g);
  }
}

