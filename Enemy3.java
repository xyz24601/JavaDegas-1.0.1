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

/*  bounce  */
public class Enemy3 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int DDELAY = 40;
  static private final int SCORE = 15;

  private double radian;                /* keep track on y radian */
  private int bounceheight;             /* height of bounce */
  private double xspeed;                /* horizontal speed */
  private double yrspeed;               /* vertical radian speed */
  private boolean fired;                /* flag to avoid multiple fire */
  private int flevel;                   /* fire level */
  private int cbounce;                  /* bounce counter */

  private boolean power;                /* flag for power enemy */

  public Enemy3()
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

  public void reset(int ih, double ir, double ixspeed,
                    double iyrspeed, boolean pwr)
  /*  int ih - height of bounce                         *\
   *  double ir - initial y coordinate in radian        *
   *  double ixspeed - x speed                          *
   *  double iyrspeed - y radian speed                  *
  \*  boolean pwr - flag for power enemy                */
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    bounceheight = ih;
    radian = ir;
    xspeed = ixspeed;
    yrspeed = iyrspeed;
    flevel = 0;
    fired = false;
    cbounce = 0;
    power = pwr;
    setxy(JavaDegas.E_INITX,
          CalTools.Enemy3_calcy(radian, bounceheight) - HEIGHT);
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      int maxbounce, int firenum, JDSounds snd, double[] rd)
  /*  Ship ship - reference to ship                             *\
   *  int glevel - level                                        *
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  int maxbounce - max num of bounce                         *
   *  int firenum - num of fires in burst                       *
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

//          if ((!fired) && (flevel < glevel) && (radian > (Math.PI / 2)) &&
//              (cbounce > 0))
          if ((!fired) && (radian > (Math.PI / 2)))
          {				/* fire at every bounce */
            efireg.firework(getxc(), getyc(), ship, firenum);
            fired = true;
            flevel++;
          }
        }

        radian += yrspeed;              /* increment y radian */
        if (radian > Math.PI)
        {
          radian = 0.0;
          fired = false;
          cbounce++;
                                        /* always bounce toward the ship */
          if ((ship.getx() > cx) && (cbounce < maxbounce))
            xspeed = -(Math.abs(xspeed));               /* bounce back */
          else
            xspeed = Math.abs(xspeed);
        }

        if (((cx + WIDTH / 2) < JavaDegas.G_WIDTH) && (cx > -WIDTH))
          ready = true;			/* entered screen */

        if (cx < -WIDTH)        /* exited screen, doesn't mean it's done */
        {
          ready = false;
          if (cbounce >= maxbounce)
          {                                     /* yep, it's done */
            alive = false;
            rd[0] = rd[1] = 0.0;
            return(false);
          }
        }
        setxy(cx - xspeed,
              CalTools.Enemy3_calcy(radian, bounceheight) - HEIGHT);
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

        g.fillArc(cx, cy, WIDTH, 50, 0, 180);
        for (int i = 0; i <= WIDTH; i += 5)
          g.drawLine(cx + i, cy + 25, cx + i, cy + HEIGHT);
      }
    }
  }

}
