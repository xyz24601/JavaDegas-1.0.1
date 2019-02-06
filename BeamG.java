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

/*  This object controls group of Beam objects          */

public class BeamG extends Weapons
{
  static private final int MAXFIRE = 3;
  static private final int FIREDELAY = 5;       /* delay between beams */

  private Beam beam[] = new Beam[MAXFIRE];

  public BeamG()
  {
    for (int i = 0; i < MAXFIRE; i++)
      beam[i] = new Beam();             /* create all beams */
    fnum = 0;
  }

  public void reset()
  {
    for (int i = 0; i < MAXFIRE; i++)
      beam[i].reset();
    fnum = 0;
  }

  public void move(double ix, double iy, Ship ship, JDSounds snd)
  /*  double ix - x coordinate of firing position       *\
   *  double iy - y coordinate of firing position       *
   *  Ship ship - reference to Ship                     *
  \*  JDSounds snd - reference to jdsounds		*/
  {
    if ((ship.alive) && (ship.fbon) && (Weapons.WT_BEAM == ship.wtype))
    {
      if (cfdelay < FIREDELAY)
        cfdelay++;                      /* add delay between beams */
      else
      {
        cfdelay = 0;
        fire(ix, iy, snd);                   /* Fire! */
      }
    }

    for (int i = 0; i < MAXFIRE; i++)
    {
      if (beam[i].move())
        fnum--;                         /* beam is out of screen */
    }
  }

  void fire(double ix, double iy, JDSounds snd)
  /*  double ix - x coordinate of firing position       *\
   *  double iy - y coordinate of firing position       *
  \*  JDSounds snd - reference to jdsounds		*/
  {
    if (fnum < MAXFIRE)                 /* can not fire more than max */
    {
      int i = 0;
      while (i < MAXFIRE)               /* keep trying until valid fire */
      {
        if (beam[i].fire(ix, iy, snd))
        {
          fnum++;                       /* valid fire */
          i++;
          break;
        }
        else
        {
          i++;                          /* nope, keep trying */
        }
      }
    }
  }

  void firestat(boolean bo)
  /*  boolean bo - status of fire button        */
  {
    if (bo)
      cfdelay = FIREDELAY + 1;          /* avoid fire delay */
  }

  void paint(Graphics g)
  {
    for (int i = 0; i < MAXFIRE; i++)
      beam[i].paint(g);
  }

  public boolean ChkFire(IFO obj)
  /*  IFO obj - reference to enemy                                      *\
   *    Return boolean: true if any of the beam intersects with obj,    *
  \*                    false otherwise                                 */
  {
    for (int i = 0; i < MAXFIRE; i++)
    {
      if (beam[i].ChkFire(obj))
      {
        fnum--;                         /* reset beam */
        return(true);
      }
    }
    return(false);
  }
}

