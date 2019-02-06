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

/*  Traditional(?) Boss Object                                  *\
 *  Also controls upper & lower part of Boss that can not be    *
\*  destroyed.                                                  */

public class Boss1 extends IFO
{
  static private final int WIDTH_TEST = 20;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 170;
  static private final int HEIGHT_REL = 15;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int DDELAY = 20;
  static private final int SCORE = 500;

  static private final int MARGIN = 20;
  static private final int WALKDELAY = 75;
  static private final int MAXSTOP = 20;        /* max num of stop */
  static private final int MAXHIT = 10;         /* how many hits can take */

                                /* horizontal distance between missiles */
  static private final double HDIST = 55.0;
                                /* vertical distance between missiles */
  static private final double VDIST = 40.0;
                                /* horizontal firing offset */
//  static private final double HOFFSET = 10.0;
  static private final double HOFFSET = 90.0;

  static private final int LASERRESIST = 5;	/* resistance to laser */

  private int claserr;

  private int flevel;                   /* fire level */
  private int maxhit;
  private int level;

  private int cwdelay;                  /* current walk delay */
  private int cstop;                    /* stop counter */
  private int chit;                     /* current hit */
  private double speed;

  private int gunit;                    /* graphics unit */

  private Boss1U boss1t, boss1b;        /* top & bottom part */

  public Boss1()
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

    boss1t = new Boss1U();              /* create necessary objects */
    boss1b = new Boss1U();
  }

  public void reset(double ispeed, int ilevel)
  /*  double ispeed - current speed	*\
  \*  int ilevel - level		*/
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    speed = ispeed;
    level = ilevel;
    flevel = 0;
    cwdelay = cstop = chit = 0;
    setxy(JavaDegas.E_INITX, JavaDegas.G_HEIGHT / 2 - HEIGHT / 2);
    boss1t.reset(getxc(), getyc(), HEIGHT, true);
    boss1b.reset(getxc(), getyc(), HEIGHT, false);
    maxhit = MAXHIT + 5 * level;
    gunit = WIDTH / maxhit;
    claserr = 0;
  }

  public boolean move(Ship ship, EFireG efireg, Scores scores, JDSounds snd)
  /*  Ship ship - reference to ship                             *\
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  JDSounds snd - reference to jdsounds			*
  \*    Return: boolean - true if alive, false otherwise        */
  {
    if (alive)
    {
      double cxc = getxc();
      double cyc = getyc();
      if (dying)
      {
        boolean b1 = boss1t.move(cxc, cyc - speed * ddelay, ship, HEIGHT, true);
        boolean b2 = boss1b.move(cxc, cyc + speed * ddelay, ship,
                                 HEIGHT, false);
        if ((ddelay > DDELAY) && (!b1) && (!b2))
          alive = false;
        else
        {
          ddelay++;
          if ((!boss1t.dying) && (ddelay > (DDELAY / 2)))
            boss1t.dying = boss1b.dying = true;
        }
      }
      else
      {
        double cx = getx();
        double cy = gety();

        if (ready)
        {
          if (ship.ChkStatus(this))             /* killed by something? */
          {
            if (chit < maxhit)                  /* nope, not dead, yet */
            {
              if (Weapons.WT_LASER == ship.wtype)
              {			/* add resistance to laser */
                if (claserr < LASERRESIST)
                  claserr++;
                else
                {
                  claserr = 0;
                  chit++;

                  if (ConfigJD.SFX)
                    snd.playSafe(snd.hitboss);
                }
              }
              else
              {
                if (ConfigJD.SFX)
                  snd.playSafe(snd.hitboss);
                chit++;
              }
            }
            else
            {                                   /* yep, it's dead */
              if (ConfigJD.SFX)
                snd.playSafe(snd.deadboss);
              efireg.firearc(getx() + WIDTH - 5, getyc(), ship, 15);
              dying = true;
              scores.add(SCORE);
              return(true);             /* get out of here! */
            }
          }

          if (cstop < MAXSTOP)
          {
            if (cwdelay < WALKDELAY)
            {
              if (!((cyc < 0) && (speed < 0)) &&
                  !((cyc > JavaDegas.G_HEIGHT) && (speed > 0)))
              {
                setxy(cx, cy + speed);
                boss1t.move(cxc, cyc + speed, ship, HEIGHT, true);
                boss1b.move(cxc, cyc + speed, ship, HEIGHT, false);
              }
              cwdelay++;
            }
            else
            {                           /* always move toward the ship */
              efireg.fire(getx() + WIDTH, getyc() - 25, ship);
              efireg.fire(getx() + WIDTH, getyc() + 25, ship);
              efireg.fireM4(this, HOFFSET, HDIST, VDIST, snd);
              if (level > 1)
                efireg.firearc(getx() + WIDTH - 5, getyc(), ship, 5 + level);

              if (ship.gety() < cy)
                speed = -(Math.abs(speed));             /* move up */
              else
                speed = Math.abs(speed);

              cwdelay = 0;
              cstop++;
            }
          }
          else
          {
            ready = false;
            speed = Math.abs(speed);
          }
        }
        else
        {
          if (((cx + WIDTH + MARGIN) > JavaDegas.G_WIDTH) || (cstop >= MAXSTOP))
          {
            if (cx < -WIDTH)            /* exited screen */
            {
              alive = ready = false;
              return(false);
            }
            else
            {
              setxy(cx - speed, cy);    /* move to the left */
              boss1t.move(getxc(), getyc(), ship, HEIGHT, true);
              boss1b.move(getxc(), getyc(), ship, HEIGHT, false);
            }
          }
          else
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
      if ((dying) && (ddelay < DDELAY))
      {
        int wx = WIDTH / DDELAY;
        int wy = HEIGHT / DDELAY;

        g.setColor(Color.yellow);
        g.fillOval(cx + WIDTH / 2 - wx * ddelay,
                   cy + HEIGHT / 2 - wy * ddelay,
                   wx * ddelay * 2, ddelay * 2);
      }
      else if (!dying)
      {
        g.setColor(Color.gray);
        g.fillRect(cx + gunit * chit, cy, WIDTH - gunit * chit, HEIGHT);

        if (ready)
          g.setColor(Color.blue);
        else
          g.setColor(Color.red);
        g.fillOval(cx + WIDTH - HEIGHT, cy, HEIGHT, HEIGHT);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);
      }
      boss1t.paint(g);
      boss1b.paint(g);
    }
  }

}


