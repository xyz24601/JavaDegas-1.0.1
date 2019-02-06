/*
    JavaDegas vl.0.1 --- Space Shooting Game Classic
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

/*  controls Boss1  */
public class Boss1G
{
  private boolean alive;

  private Boss1 boss1;

  public Boss1G()
  {
    boss1 = new Boss1();
  }

  public void init(double ispeed, int ilevel)
  {
    alive = true;
    boss1.reset(ispeed, ilevel);
  }

  public boolean move(Ship ship, EFireG efireg, Scores scores, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  JDSounds snd - reference to jdsounds			*
  \*    Return: boolean - true if alive, false otherwise        */
  {
    if (alive)
    {
      if (!boss1.move(ship, efireg, scores, snd))
        alive = false;
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    boss1.paint(g);
  }

}

