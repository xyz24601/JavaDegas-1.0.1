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


public class Scores
{
  private int highscore;
  private int score;
  private Font f;
  private FontMetrics fm;


  public Scores(Component c)
  {
    score = highscore = 0;
    f = new Font("Monospaced", Font.PLAIN, 16);
    fm = c.getFontMetrics(f);
  }

  public void reset()
  {
    score = 0;
  }

  public void sethigh()
  {
    if (score > highscore)
      highscore = score;
  }

  public void add(int x)
  {
    score += x;
  }

  public void paint(Graphics g)
  {
    g.setFont(f);
    g.setColor(Color.white);
//    g.drawString("S: " + score + " H: " + highscore, 0,
//                 JavaDegas.G_HEIGHT + JavaDegas.P_HEIGHT + JavaDegas.S_HEIGHT);
    g.drawString("Score: " + score, 0,
                 JavaDegas.G_HEIGHT + JavaDegas.P_HEIGHT +
                   JavaDegas.S_HEIGHT - fm.getDescent());
    g.drawString("High Score: " + highscore, JavaDegas.G_WIDTH / 2,
                 JavaDegas.G_HEIGHT + JavaDegas.P_HEIGHT +
                   JavaDegas.S_HEIGHT - fm.getDescent());

  }

}



