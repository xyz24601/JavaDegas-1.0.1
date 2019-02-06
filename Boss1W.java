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

/*  controls boss1g  */
public class Boss1W
{
  private boolean alive;
  private int maxwave;                  /* need to get from caller */
  private int cwave;
  private double speed;

  private Boss1G boss1g;
  private int level;

  public Boss1W()
  {
    boss1g = new Boss1G();
    alive = false;
  }

  public void killit()
  {
    alive = false;
  }

  public void init(int imaxwave, double ispeed, int ilevel)
  /*  int imaxwave - max number of waves (groups)       *\
   *  double ispeed - speed                             *
  \*  int ilevel - level				*/
  {
    alive = true;
    maxwave = imaxwave;
    speed = ispeed;
    level = ilevel;
    boss1g.init(speed, level);
    cwave = 1;
  }

  public boolean move(Ship ship, EFireG efireg, Scores scores, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  JDSounds snd - reference to jdsounds			*
   *    Return: boolean - true if any of enemy is still alive   *
  \*                      false otherwise                       */
  {
    if (alive)
    {
      boolean bo = boss1g.move(ship, efireg, scores, snd);

      if ((!bo) && (cwave < maxwave))
      {
        boss1g.init(speed, level);
        cwave++;
      }

      if ((!bo) && (cwave >= maxwave))
        alive = false;                  /* all done */
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    boss1g.paint(g);
  }

}

