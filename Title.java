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

public class Title
{
  private final String title = "JavaDegas";
  private final String cfs1 = "verwion 1.0.1";
  private final String cfs2 = "Copyright (C)  Shinji Umeki";
  private final String[] inst = {"[K] or [Up Arrow] ---- Up",
                                 "[J] or [Down Arrow] -- Down",
                                 "[H] or [Left Arrow] -- Left",
                                 "[L] or [Right Arrow] - Right",
                                 "[Space] -------------- Fire",
                                 "            <hold down for automatic fire>",
                                 "[N] or [M] ----------- Power Up"};

  private Font tf;
  private Font cf;
  private Font sf;
  private FontMetrics tfm;
  private FontMetrics cfm;
  private FontMetrics sfm;
  private int xinst;

  public Title(Component c)
  {
    tf = new Font("Monospaced", Font.BOLD | Font.ITALIC, 30);
    tfm = c.getFontMetrics(tf);
    cf = new Font("Monospaced", Font.ITALIC, 10);
    cfm = c.getFontMetrics(cf);
    sf = new Font("Monospaced", Font.PLAIN, 14);
    sfm = c.getFontMetrics(sf);
    xinst = (JavaDegas.G_WIDTH - sfm.stringWidth(inst[5])) / 2;
  }

  public void paint(Graphics g)
  {
    g.setColor(Color.white);

    g.setFont(tf);
    g.drawString(title, (JavaDegas.G_WIDTH - tfm.stringWidth(title)) / 2,
                 JavaDegas.G_HEIGHT / 4);

    g.setFont(cf);
    g.drawString(cfs1, (JavaDegas.G_WIDTH - cfm.stringWidth(cfs1)) / 2, 
                 JavaDegas.G_HEIGHT / 4 + tfm.getHeight());
    g.drawString(cfs2, (JavaDegas.G_WIDTH - cfm.stringWidth(cfs2)) / 2,
                 JavaDegas.G_HEIGHT / 4 + tfm.getHeight() + cfm.getHeight());

    g.setFont(sf);
    for (int i = 0; i < 7; i++)
      g.drawString(inst[i], xinst, JavaDegas.G_HEIGHT / 2 + sfm.getHeight() * i);
  }

}


