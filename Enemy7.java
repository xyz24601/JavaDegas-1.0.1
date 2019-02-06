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

/*  appear and crash  */
public class Enemy7 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static public int WIDTH;
  static public int HEIGHT;

  static private final int DDELAY = 5;
  static private final int SCORE = 10;

  private double speed;
  private double xyspeed[] = new double[2];     /* speed */

  private int rdelay;                   /* ready delay */
  private int crdelay;                  /* current ready delay */
  private int cfdelay;                  /* current fire delay */
  private boolean power;                /* flag for power enemy */
  private int flevel;                   /* fire level */

  public Enemy7()
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

  public void reset(double ix, double iy, double ispeed, boolean pwr,
                    int irdelay)
  /*  double ix - initial x coordinate  *\
   *  double iy - initial y coordinate  *
   *  double ispeed - speed             *
  \*  int irdelay - delay before ready  */
  {
    alive = true;
    ready = dying = false;              /* not ready, yet */
    ddelay = 0;
    power = pwr;
    speed = ispeed;
    rdelay = irdelay;
    crdelay = cfdelay = 0;

    setxy(ix, iy);
    flevel = 1;                 /* don't wanna fire on level 1 */
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      int fdelay, JDSounds snd, double[] rd)
  /*  Ship ship - reference to ship                             *\
   *  int glevel - level                                        *
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  int fdelay - delay before fire                            *
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
            rd[0] = getxc();
            rd[1] = getyc();
            return(true);               /* get out of here! */
          }

          if ((cx < -WIDTH) || (cx > JavaDegas.G_WIDTH) ||
              (cy < -HEIGHT) || (cy > JavaDegas.G_HEIGHT))
          {
            alive = ready = false;
            rd[0] = rd[1] = 0.0;                /* return 0.0 coordinate */
            return(false);
          }

          setxy(cx - xyspeed[0], cy - xyspeed[1]);
          cfdelay++;
          if ((fdelay == cfdelay) && (flevel < glevel))
            efireg.fire(getxc(), getyc(), ship);
        }
        else if (crdelay < rdelay)
        {
          if (!JavaDegas.sstopped)      /* move at scroll speed */
            setxy(cx - JavaDegas.SCROLL_SPEED, cy);
          crdelay++;
        }
        else if (rdelay == crdelay)
        {                               /* this should work */
          CalTools.EFire_calcxy(getxc(), getyc(), ship, speed, xyspeed);
          ready = true;
        }
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

        if (crdelay > (rdelay / 4))
          g.drawOval(cx + WIDTH / 4, cy + HEIGHT / 4,
                     WIDTH / 2 - 1, HEIGHT / 2 - 1);

        if (crdelay > (rdelay / 2))
        {
          g.drawLine(cx + WIDTH / 2, cy, cx + WIDTH / 2, cy + HEIGHT);
          g.drawLine(cx, cy + HEIGHT / 2, cx + WIDTH, cy + HEIGHT / 2);
        }

        if (crdelay > (rdelay * 3 / 4))
        {
          g.drawLine(cx + 5, cy + 5, cx + WIDTH - 5, cy + HEIGHT - 5);
          g.drawLine(cx + 5, cy + HEIGHT - 5, cx + WIDTH - 5, cy + 5);
        }

        g.setColor(Color.gray);
        g.fillOval(cx + WIDTH / 3, cy + HEIGHT / 3, WIDTH / 3, HEIGHT / 3);
      }
    }
  }

}

