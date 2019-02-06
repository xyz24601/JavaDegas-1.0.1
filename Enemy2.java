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

/*  sin wave  */
public class Enemy2 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static private int WIDTH;
  static public int HEIGHT;

  static private final int DDELAY = 5;
  static private final int SCORE = 10;
  static private final int GINC = 1;    /* graphics increment */

  private double radian;                /* keep track on y radian */
  private int moveheight;               /* height of wave */
  private int movecenter;               /* center of wave */
  private double xspeed;                /* horizontal speed */
  private double yrspeed;               /* vertical radian speed */
  private boolean fired;                /* flag to avoid multiple fire */
  private int flevel;                   /* fire level */

  private int gcounter;                 /* graphics counter */
  private int ginc;                     /* graphics increment */

  private boolean power;                /* flag for power enemy */

  public Enemy2()
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

  public void reset(int ih, int ic, double ir,
                    double ixspeed, double iyrspeed, boolean pwr)
  /*  int ih - height of movement                       *\
   *  int ic - center of movement                       *
   *  double ir - initial y coordinate in radian        *
   *  double ixspeed - x speed                          *
   *  double iyrspeed - y radian speed                  *
  \*  boolean pwr - flag for power enemy                */
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    moveheight = ih;
    movecenter = ic;
    radian = ir;
    xspeed = ixspeed;
    yrspeed = iyrspeed;
    flevel = 0;
    fired = false;
    power = pwr;
    setxy(JavaDegas.E_INITX,
          CalTools.Enemy2_calcy(radian, moveheight, movecenter));
    gcounter = 0;
    ginc = GINC;
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      double fplace, JDSounds snd, double[] rd)
  /*  Ship ship - reference to ship                             *\
   *  int glevel - level                                        *
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  double fplace - firing place in radian                    *
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

          if ((!fired) && (flevel < glevel) && (radian > fplace))
          {                     /* fire at fplace */
            efireg.fire(getxc(), getyc(), ship);
            fired = true;
            flevel++;
          }
        }

        radian += yrspeed;              /* increment y radian */
        if (radian > Math.PI * 2)
        {                                       /* reset */
          radian = 0.0;
          fired = false;
        }

        if (((cx + WIDTH / 2) < JavaDegas.G_WIDTH) && (cx > -WIDTH))
          ready = true;		/* entered screen */

        if (cx < -WIDTH)                                /* exited screen */
        {
          alive = ready = false;
          rd[0] = rd[1] = 0.0;                  /* return 0.0 coordinate */
          return(false);
        }

        setxy(cx - xspeed,
              CalTools.Enemy2_calcy(radian, moveheight, movecenter));
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

        g.drawOval(cx, cy + gcounter, WIDTH, HEIGHT - gcounter * 2);
        if ((gcounter > 20) || (gcounter < 0))
          ginc = -ginc;
        gcounter += ginc;

        g.drawLine(cx + 20, cy + 10, cx + 20, cy + 30);
        g.drawLine(cx + 10, cy + 15, cx + 10, cy + 25);
        g.drawLine(cx + 30, cy + 15, cx + 30, cy + 25);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);

        g.setColor(Color.gray);
        g.fillOval(cx + 15, cy + 15, 10, 10);
      }
    }
  }

}

