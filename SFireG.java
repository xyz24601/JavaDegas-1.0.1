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

/*  controls all ship's fires  */
public class SFireG
{
  private BeamG beamg;
  private LaserG laserg;

  public SFireG()
  {
    beamg = new BeamG();
    laserg = new LaserG();
  }

  public void reset()
  {
    beamg.reset();
    laserg.reset();
  }

  public void move(double ix, double iy, Ship ship, JDSounds snd)
  /*  double ix - x coordinate of firing position       *\
   *  double iy - y coordinate of firing position       *
   *  Ship ship - reference to Ship                     *
  \*  JDSounds snd - reference to jdsounds		*/
  {
    /* has to move all, because all of them can exist on screen at same time */
    beamg.move(ix, iy, ship, snd);
    laserg.move(ix, iy, ship, snd);
  }

  public void firestat(int wtype, boolean bo)
  /*  int wtype - weapon type                                           *\
   *  boolean bo - true if fire button is pressed, false otherwise      *
  \*    change status of fire button.                                   */
  {
    switch (wtype)
    {
      case Weapons.WT_BEAM: beamg.firestat(bo); break;
      case Weapons.WT_LASER: laserg.firestat(bo); break;
    }
  }

  public void paint(Graphics g)
  {
    beamg.paint(g);
    laserg.paint(g);
  }

  public boolean ChkFire(IFO obj)
  /*  IFO obj - reference to enemy                                      *\
   *    Return boolean: true if any of weapon intersects with obj,      *
  \*                    false otherwise                                 */
  {
    if ((beamg.ChkFire(obj)) || (laserg.ChkFire(obj)))
      return(true);
    else
      return(false);
  }
}

