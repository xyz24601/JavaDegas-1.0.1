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

/*  follow the leader  */
public class Enemy1 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;
  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static private int WIDTH;
  static public int HEIGHT;

  static private final int DDELAY = 5;
  static private final int SCORE = 10;
  static private final double GINC = 0.1;       /* graphics increment */
  static private int HW;			/* a half width */
  static private int HH;			/* a half height */

  private int xcount;                   /* horizontal movement count */
  private int ycount;                   /* vertical movement count */
  private int xc;       /* horizontal counter before changing direction */
  private int yc;       /* vertical counter before changing direction */
  private double xspeed;        /* horizontal speed */
  private double yspeed;        /* vertical speed */
  private int flevel;           /* fire level */
  private double gcounter;      /* graphics counter */

  public Enemy1()
  {
    super((ConfigJD.TEST) ? WIDTH_TEST : WIDTH_REL,
          (ConfigJD.TEST) ? HEIGHT_TEST : WIDTH_REL);

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

    HW = WIDTH / 2;
    HH = HEIGHT / 2;
  }

  public void reset(double iy, double speed, int ixc, int iyc)
  /*  double iy - initial y coordinate          *\
   *  double speed - speed                      *
   *  int ixc - horizontal movement count       *
  \*  int iyc - vertical movement count         */
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    setxy(JavaDegas.E_INITX, iy);
    xspeed = speed;
    xc = yc = 0;
    xcount = ixc;
    ycount = iyc;
    flevel = 1;                 /* don't wanna fire on level 1 */
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      JDSounds snd, double[] rd)
  /*  Ship ship - reference to Ship                     *\
   *  int glevel - level                                *
   *  EFireG efireg - reference to efireg               *
   *  Scores scores - reference to scores               *
   *  JDSounds snd - reference to jdsounds		*
   *  double[] rd - returned killed coordinates         *
  \*    Return boolean - true if alive, false otherwise */
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
        double cx = getx();             /* current x coordinate */
        double cy = gety();             /* current y coordinate */

        if (ready)
        {
          if (ship.ChkStatus(this))     /* killed by something? */
          {
            if (ConfigJD.SFX)
              snd.playSafe(snd.deadenemy);
            dying = true;
            scores.add(SCORE);
            rd[0] = getxc();                 /* need to return coordinate */
            rd[1] = getyc();
            return(true);               /* get out of here! */
          }
        }

        if ((xc < xcount) && (0 == yc))
        {                                       /* horizontal movement */
          if (((cx + WIDTH / 2) < JavaDegas.G_WIDTH) && (cx > -WIDTH))
            ready = true;		/* entered screen */

          if (cx < -WIDTH)                      /* exited screen */
          {
            ready = alive = false;
            rd[0] = rd[1] = 0.0;                /* return 0.0 coordinate */
            return(false);
          }
          setxy(cx - xspeed, cy);               /* move horizontal */
          xc++;

          if (xcount == xc)
          {             /* check direction right before vertical move */
            if (ship.gety() < gety())   /* set y to move closer to ship */
              yspeed = -xspeed;
            else
              yspeed = xspeed;
          }
        }
        else
        {
          if ((xcount == xc) && (flevel < glevel))
          {                             /* fire when direction changes */
            efireg.fire(getxc(), getyc(), ship);
            flevel++;
            xc++;                               /* very important */
          }

          if (yc < ycount)
          {                             /* move diagonal */
            setxy(cx + xspeed, cy + yspeed);
            yc++;
          }
          else
          {                             /* reset movement counters */
            xc = yc = 0;
          }
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
        g.setColor(Color.gray);
        g.fillOval(cx + HW - 5, cy + HH - 5, 10, 10);

        g.fillOval((int) (HW - (15 * Math.cos(gcounter)) - 5) + cx,
                   (int) (HH - (15 * Math.sin(gcounter)) - 5) + cy,
                   10, 10);
        g.fillOval(
          (int) (HW - (15 * Math.cos(gcounter - 2 * Math.PI / 3)) - 5) + cx,
          (int) (HH - (15 * Math.sin(gcounter - 2 * Math.PI / 3)) - 5) + cy,
          10, 10);
        g.fillOval(
          (int) (HW - (15 * Math.cos(gcounter + 2 * Math.PI / 3)) - 5) + cx,
          (int) (HH - (15 * Math.sin(gcounter + 2 * Math.PI / 3)) - 5) + cy,
          10, 10);

        if (gcounter > Math.PI * 2)
          gcounter = 0.0;
        else
          gcounter += GINC;

        g.setColor(Color.white);
        g.drawOval(cx + 12, cy + 12, 16, 16);
        g.drawOval(cx, cy, WIDTH, HEIGHT);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);
      }
    }
  }
}

