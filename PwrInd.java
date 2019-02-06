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

/*  power indicator  */
public class PwrInd
{
  static private final int MAXNUM = 7;		/* num of indicators */
  static private final int SPEED_UP = 1;	/* constants */
  static private final int SLOW_DOWN = 2;
  static private final int BEAM = 3;
  static private final int LASER = 4;
  static private final int OPTION = 5;
  static private final int DOH = 6;
  static private final int SHIELD = 7;


  private final String[] label = {"SPEED UP", "SLOW DOWN", "BEAM", "LASER",
                                  "OPTION", "DOH!", "SHIELD"};
  private Font f;
  private FontMetrics fm;

  public boolean surprise;		/* flag for surprise power up */
  private int cpwr;

  public PwrInd(Component c)
  {
    cpwr = 0;
    surprise = false;
    f = new Font("Monospaced", Font.BOLD | Font.ITALIC, 12);
    fm = c.getFontMetrics(f);
  }

  public void init()
  {
    cpwr = 0;
    surprise = false;
  }

  public void reset()
  {
    if (0 != cpwr)
      cpwr = 1;         /* bring in back to 1st power */
    surprise = false;
  }

  public void pickedPower(JDSounds snd, int type)
  /*  JDSounds snd - reference to jdsounds	*\
  \*  int type - power capsule type		*/
  {
    if (!surprise)
    {
      if (Math.random() > 0.9)		/* Surprise! */
      {
        surprise = true;
        if (ConfigJD.SFX)
          snd.playSafe(snd.surprise);

        if (ConfigJD.TRACE)
          System.out.println("Surprise!");
      }
      else
      {
        surprise = false;
        if (ConfigJD.SFX)
          snd.playSafe(snd.getpower);

        if (0 == type)
          cpwr = 0;
        else
        {
          cpwr += type;
          if (cpwr < 0)
            cpwr = 0;
          else if (cpwr > MAXNUM)
            cpwr = 1;		/* back to the first one */
        }

        if (ConfigJD.TRACE)
          System.out.println(cpwr);
      }
    }
  }

  public void move(JDSounds snd)
  {
    if (surprise)
    {
      if (cpwr < MAXNUM)
        cpwr++;
      else
        cpwr = 1;

      if (ConfigJD.SFX)
        snd.playSafe(snd.annoy);
    }
  }

  public void powerUp(Ship ship, JDSounds snd)
  /*  Ship ship - reference to ship     	*\
  \*  JDSounds snd - reference to jdsounds	*/
  {
    if (surprise)
    {
      surprise = false;

      if (((OPTION == cpwr) && (ship.isMaxOption())) ||
          ((SHIELD == cpwr) && (ship.hasShield())))
      {					/* too much power, get rid of it */
        cpwr = DOH;
      }
    }

    if (SPEED_UP == cpwr)
    {
      ship.speedUp();
    }
    else if ((SLOW_DOWN == cpwr) && (!ship.isMinSpeed()))
    {
      ship.slowDown();
    }
    else if ((BEAM == cpwr) && (Weapons.WT_BEAM != ship.wtype))
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.beam);
      ship.wtype = Weapons.WT_BEAM;
    }
    else if ((LASER == cpwr) && (Weapons.WT_LASER != ship.wtype))
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.laser);
      ship.wtype = Weapons.WT_LASER;
    }
    else if ((OPTION == cpwr) && (!ship.isMaxOption()))
    {
      ship.addoption();
    }
    else if (DOH == cpwr)
    {
      ship.doh();
    }
    else if ((SHIELD == cpwr) && (!ship.hasShield()))
    {
      ship.addShield();
    }
    else
    {
      return;
    }

    cpwr = 0;
  }

  public void paint(Graphics g, Ship ship, boolean titleshowing)
  /*  Graphics g - reference to graphics        *\
   *  Ship ship - reference to ship		*
  \*  boolean titleshowing - flag for title	*/
  {
    int unit = JavaDegas.G_WIDTH / MAXNUM;
    g.setColor(Color.white);
    g.setFont(f);
    for (int i = 1; i <= MAXNUM; i++)
    {
      if ((i == cpwr) && (!titleshowing))
      {
        g.fillRect((i - 1) * unit + 1, JavaDegas.G_HEIGHT,
                   unit - 2, JavaDegas.P_HEIGHT - 2);

        if (((SLOW_DOWN == i) && (ship.isMinSpeed())) ||
            ((BEAM == i) && (Weapons.WT_BEAM == ship.wtype)) ||
            ((LASER == i) && (Weapons.WT_LASER == ship.wtype)) ||
            ((OPTION == i) && (ship.isMaxOption())) ||
            ((SHIELD == i) && (ship.hasShield())))
          continue;		/* can't power up any more */

        if (!ConfigJD.TEST)
        {
          g.setColor(Color.black);
          g.drawString(label[i - 1],
                     (i - 1) * unit + (unit - fm.stringWidth(label[i - 1])) / 2,
                     JavaDegas.G_HEIGHT + JavaDegas.P_HEIGHT * 2 / 3);
        }
        g.setColor(Color.white);
      }
      else
      {
        g.setColor(Color.white);
        g.drawRect((i - 1) * unit + 1, JavaDegas.G_HEIGHT,
                   unit - 2, JavaDegas.P_HEIGHT - 2);

        if (!titleshowing)	// display everything on title screen.
        {
          if (((SLOW_DOWN == i) && (ship.isMinSpeed())) ||
              ((BEAM == i) && (Weapons.WT_BEAM == ship.wtype)) ||
              ((LASER == i) && (Weapons.WT_LASER == ship.wtype)) ||
              ((OPTION == i) && (ship.isMaxOption())) ||
              ((SHIELD == i) && (ship.hasShield())))
            continue;		/* can't power up any more */
        }

        if (!ConfigJD.TEST)
        {
          g.drawString(label[i - 1],
                     (i - 1) * unit + (unit - fm.stringWidth(label[i - 1])) / 2,
                     JavaDegas.G_HEIGHT + JavaDegas.P_HEIGHT * 2 / 3);
        }
      }
    }
  }
}

