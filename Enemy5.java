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

/*  straight to left  */
public class Enemy5 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int DDELAY = 5;
  static private final int SCORE = 10;

  private double speed;

  private int flevel;                   /* fire level */
  private double fplace;                /* location to fire */

  private boolean power;                /* flag for power enemy */
  private int numfplaces;               /* num of firing places */

  public Enemy5()
  {
    super((ConfigJD.TEST) ? WIDTH_TEST : WIDTH_REL,
          (ConfigJD.TEST) ? HEIGHT_TEST : HEIGHT_REL);

    if (ConfigJD.TEST)
    {
      WIDTH = WIDTH_TEST;
      HEIGHT = HEIGHT_TEST;
    }
    else
    {
      WIDTH = WIDTH_REL;
      HEIGHT = HEIGHT_REL;
    }
  }

  public void reset(double iy, double ispeed, boolean pwr, int inumfplaces)
  /*  double iy - initial y coordinate          *\
   *  double ispeed - speed                     *
   *  boolean pwr - flag for power enemy        *
  \*  int inumfplaces - num of firing places    */
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    speed = ispeed;
    flevel = 1;                 /* don't wanna fire on level 1 */
    power = pwr;
    numfplaces = inumfplaces;
    fplace = CalTools.CalcFP(numfplaces, flevel);
    setxy(JavaDegas.E_INITX, iy);
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      JDSounds snd, double[] rd)
  /*  Ship ship - reference to ship                             *\
   *  int glevel - level                                        *
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  JDSounds snd - reference to jdsounds			*
   *  double[] rd - returned killed coordinates                 *
  \*    Return: boolean - true if alive, false otherwise        */
  {
    if (alive)
    {
      if (dying)
      {
        if (ddelay > DDELAY)
          alive = false;
        else
          ddelay++;
      }
      else
      {
        double cx = getx();
        double cy = gety();

        if (ready)
        {
          if (ship.ChkStatus(this))     /* killed by something? */
          {
            if (ConfigJD.SFX)
              snd.playSafe(snd.deadenemy);
            dying = true;
            scores.add(SCORE);
            rd[0] = getxc();                 /* return killed coordinate */
            rd[1] = getyc();
            return(true);               /* get out of here! */
          }

          if ((flevel < glevel) && (fplace > cx))
          {
            efireg.fire(getxc(), getyc(), ship);
            flevel++;
            fplace = CalTools.CalcFP(numfplaces, flevel);
          }
        }

        if (((cx + WIDTH / 2) < JavaDegas.G_WIDTH) && (cx > -WIDTH))
          ready = true;			/* entered screen */
        else if (cx < -WIDTH)                   /* exited screen */
        {
          alive = ready = false;
          rd[0] = rd[1] = 0.0;          /* return 0.0 coordinate */
          return(false);
        }

        setxy(cx - speed, cy);
      }
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      int cx = (int) getx();
      int cy = (int) gety();
      if (dying)
        AllEnemy.kaboom(g, cx, cy, WIDTH, HEIGHT);
      else
      {
        if (power)
          g.setColor(Color.red);
        else
          g.setColor(Color.white);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);

        int ii = (int) (Math.random() * (WIDTH - 20)) + 20 + cx;
        g.drawLine(ii, cy + 13, ii, cy + HEIGHT - 13);

        g.setColor(Color.gray);
        g.fillArc(cx, cy, WIDTH * 2, HEIGHT / 2, 90, 90);
        g.fillArc(cx, cy + HEIGHT / 2 , WIDTH * 2, HEIGHT / 2, 180, 90);
      }
    }
  }

}

