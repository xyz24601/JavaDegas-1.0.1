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

public class Colors
{
  static public final int NUMCOLOR = 100;       /* num of colors for star */
  static public Color starColors[] = new Color[NUMCOLOR];
                                /* array of colors for star */
        /* store only 1 instance here and let all stars access it */

  public Colors()
  {
    double d = 0.0;
    for (int i = 0; i < NUMCOLOR; i++, d += 0.01)       /* create colors */
      starColors[i] = Color.getHSBColor((float) d, (float) 1.0, (float) 1.0);
  }
}

