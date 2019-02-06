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

/*  Identified Flying Object, has area for hit determination  */
public class IFO
{
  private Coord coord;
        /* I originally had this class inherited from Coord, */
        /* but member method call kept messing up, so I changed it */
  protected Rectangle area;
  public boolean alive;         /* flag for existance, usually */
  public boolean ready;         /* flag for appearing in screen, usually */
  public boolean dying;         /* flag for dying */
  public int ddelay;            /* delay for dying */

  public IFO(int w, int h)
  /*  int w - width     *\
  \*  int h - height    */
  {
    coord = new Coord();
    area = new Rectangle(w, h);
  }

  public void setxy(double ix, double iy)
  /*  int ix - new x coordinate *\
  \*  int iy - new y coordinate */
  {
    coord.setxy(ix, iy);
    area.setLocation((int) ix, (int) iy);
  }

  public void setxywh(double ix, double iy, double iw, double ih)
  {
    coord.setxy(ix, iy);

    // for Java 1.2
//    area.setRect(ix, iy, iw, ih);

    // for Java 1.1
    area.setLocation((int) ix, (int) iy);
    area.setSize((int) iw, (int) ih);
  }

  public double getx() { return(coord.getx()); }
  public double gety() { return(coord.gety()); }

  // for Java 1.2
//  public double getw() { return(area.getWidth()); }
//  public double geth() { return(area.getHeight()); }

  // for Java 1.1
  public double getw() { return((double) area.width); }
  public double geth() { return((double) area.height); }

  public double getxc() { return(getx() + (getw() / 2)); }
  public double getyc() { return(gety() + (geth() / 2)); }
}

