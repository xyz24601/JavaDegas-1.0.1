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

import java.applet.*;
import java.awt.*;

public class JavaDegasSA
{
  public static void main(String args[])
  {
    Applet applet = new JavaDegas();
    Frame frame = new JavaDegasFrame(applet);
  }
}

class JavaDegasFrame extends Frame
{
  public JavaDegasFrame(Applet applet)
  {
    super("JavaDegas");

    this.add("Center", applet);
    this.setSize(510, 400); 
//    this.setSize(130, 110); 
    this.show();

    applet.init();
    applet.start();
  }
}


