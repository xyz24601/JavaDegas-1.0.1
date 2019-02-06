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

/*  walk on ground  */
public class Enemy6 extends IFO
{
  static private final int WIDTH_TEST = 5;
  static private final int HEIGHT_TEST = 5;

  static private final int WIDTH_REL = 40;
  static private final int HEIGHT_REL = 40;

  static private int WIDTH;
  static private int HEIGHT;

  static private final int DDELAY = 5;
  static private final int SCORE = 10;

  static private final int GINC = 1;    /* graphics increment */

  private int walkdelay;
  private double speed;

  private int flevel;                   /* fire level */

  private int cwdelay;                  /* current walk delay */
  private int csdelay;                  /* current stop delay */
  private int cstop;                    /* stop counter */

  private boolean top;                  /* flag for top or bottom */
  private int gcounter;                 /* graphics counter */
  private int ginc;                     /* graphics increment */

  public Enemy6()
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

  public void reset(double ispeed, int iwalkdelay, boolean itop)
  /*  double ispeed - speed                     *\
   *  int iwalkdelay - delay for walking        *
  \*  boolean itop - flag for top or bottom     */
  {
    alive = true;
    ready = dying = false;      /* not ready, not in the screen, yet */
    ddelay = 0;
    speed = ispeed;
    walkdelay = iwalkdelay;
    top = itop;
    flevel = 0;
    cwdelay = csdelay = cstop = 0;
    if (top)
    {
      setxy(JavaDegas.E_INITX, 0.0);
      gcounter = HEIGHT / 2;
    }
    else
    {
      setxy(JavaDegas.E_INITX, JavaDegas.G_HEIGHT - HEIGHT);
      gcounter = 5;
    }
    ginc = GINC;
  }

  public boolean move(Ship ship, int glevel, EFireG efireg, Scores scores,
                      int stopdelay, int maxstop, JDSounds snd, double[] rd)
  /*  Ship ship - reference to ship                             *\
   *  int glevel - level                                        *
   *  EFireG efireg - reference to efireg                       *
   *  Scores scores - reference to scores                       *
   *  int stopdelay - delay for stop                            *
   *  int maxstop - max num of stop                             *
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
        }

        if (((cx + WIDTH / 2) < JavaDegas.G_WIDTH) && (cx > -WIDTH))
          ready = true;			/* entered screen */
        else if (cx < -WIDTH)                   /* exited screen */
        {
          alive = ready = false;
          rd[0] = rd[1] = 0.0;          /* return 0.0 coordinate */
          return(false);
        }

        if (cstop < maxstop)
        {
          if (cwdelay < walkdelay)
          {
            setxy(cx - speed, cy);
            cwdelay++;
          }
          else if (csdelay < stopdelay)
          {
            if ((flevel < glevel) && ((stopdelay / 2)  == csdelay))
            {
              efireg.fire(getxc(), getyc(), ship);
              flevel++;
            }
            csdelay++;                  /* stop for a while */
          }
          else
          {                             /* always move toward the ship */
            if (ship.getx() > cx)
              speed = -(Math.abs(speed));               /* move back */
            else
              speed = Math.abs(speed);

            cwdelay = csdelay = 0;
            cstop++;
          }
        }
        else
        {
          setxy(cx - Math.abs(speed), cy);      /* just keep moving to left */
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
        g.setColor(Color.white);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);

        if (top)
        {
          g.fillArc(cx, cy - HEIGHT / 4, WIDTH, HEIGHT / 2, 180, 180);
          g.drawLine(cx + WIDTH / 2, cy + HEIGHT,
                     cx + WIDTH / 2, cy + HEIGHT / 4);

          g.fillOval(cx + WIDTH / 4, cy + gcounter, WIDTH / 2, HEIGHT / 6);
          if ((gcounter > (HEIGHT - 10)) || (gcounter < (HEIGHT / 2) - 5))
            ginc = -ginc;
        }
        else
        {
          g.fillArc(cx, cy + HEIGHT * 3 / 4, WIDTH, HEIGHT / 2, 0, 180);
          g.drawLine(cx + WIDTH / 2, cy, cx + WIDTH / 2, cy + HEIGHT * 3 / 4);

          g.fillOval(cx + WIDTH / 4, cy + gcounter, WIDTH / 2, HEIGHT / 6);
          if ((gcounter > (HEIGHT / 2)) || (gcounter < 5))
            ginc = -ginc;
        }
        gcounter += ginc;
      }
    }
  }

}

