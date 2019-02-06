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

/*  controls all efires  */
public class EFireG
{
  static private final int MAXFIRE = 50;
  static private final int MAXMISSILE = 16;
  static private final double INITSPEED = 2.0;
  static private final double INCSPEED = 1.0;

  private EFire efire[] = new EFire[MAXFIRE];
  private EMissile emissile[] = new EMissile[MAXMISSILE];
  private double speed;
  private int fnum;
  private int mnum;

  public EFireG()
  {
//    speed = INITSPEED;
    for (int i = 0; i < MAXFIRE; i++)
      efire[i] = new EFire();
    for (int i = 0; i < MAXMISSILE; i++)
      emissile[i] = new EMissile();
    fnum = mnum = 0;
  }

  public void reset()
  {
    speed = INITSPEED;
    for (int i = 0; i < MAXFIRE; i++)
      efire[i].alive = false;
    for (int i = 0; i < MAXMISSILE; i++)
      emissile[i].alive = false;
  }

  public void nextStage()
  {
    speed += INCSPEED;
  }

  public void move(Ship ship)
  {
    for (int i = 0; i < MAXFIRE; i++)
    {
      if (efire[i].move(ship))
        fnum--;                 /* fire is out of screen */
    }
    for (int i = 0; i < MAXMISSILE; i++)
    {
      if (emissile[i].move(ship))
        mnum--;                 /* missile is out of screen */
    }
  }

  public void fire(double ix, double iy, IFO ship)
  /*  double ix - x coordinate of firing place  *\
   *  double iy - y coordinate of firing place  *
  \*  IFO ship - reference to ship              */
  {
    if ((ship.alive) && (fnum < MAXFIRE))       /* ship has to be alive */
    {                           /* can not fire more than maxfire */
      int i = 0;
      while (i < MAXFIRE)       /* keep trying until valid fire */
      {
        if (efire[i].fire(ix, iy, ship, speed))
        {                                       /* valid fire */
          fnum++;
          break;
        }
        else
        {
          i++;                          /* nope, keep trying */
        }
      }
    }
  }

  public void firework(double ix, double iy, IFO ship, int num)
  /*  double ix - x coordinate of firing place  *\
   *  double iy - y coordinate of firing place  *
   *  IFO ship - reference to ship              *
  \*  int num - num of fires                    */
  {
    double rad = 0.0;
    int i = 0;
    int j = 0;                  /* fire counter */
    while ((ship.alive) && (fnum < MAXFIRE) && (j < num))
    {
      while (i < MAXFIRE)               /* keep trying until valid fire */
      {
        if (efire[i].fireRadian(ix, iy, rad, speed))
        {                               /* valid fire */
          fnum++;
          i++;
          j++;
          rad += Math.PI * 2 / num;     /* change angle */
          break;
        }
        else
        {
          i++;                          /* nope, keep trying */
        }
      }
      if (i >= MAXFIRE)
        break;
    }
  }

  public void firearc(double ix, double iy, IFO ship, int num)
  /*  double ix - x coordinate of firing place  *\
   *  double iy - y coordinate of firing place  *
   *  IFO ship - reference to ship              *
  \*  int num - num of fires                    */
  {
    double rad = Math.PI / 2;
//    double rad = Math.PI;
    int i = 0;
    int j = 0;                  /* fire counter */
    while ((ship.alive) && (fnum < MAXFIRE) && (j < num))
    {
      while (i < MAXFIRE)
      {                         /* keep trying until valid fire */
        if (efire[i].fireRadian(ix, iy, rad, speed))
        {                               /* valid fire */
          fnum++;
          i++;
          j++;
          rad += Math.PI / (num - 1);           /* change angle */
          break;
        }
        else
        {
          i++;                          /* nope, keep trying */
        }
      }
      if (i >= MAXFIRE)
        break;
    }
  }

  int tryFireM(double ix, double iy, int ii)
  /*  double ix - x coordinate of firing place  *\
   *  double iy - y coordinate of firing place  *
   *  int ii - fire counter                     *
  \*    Return: int - current fire counter      */
  {
    while ((mnum < MAXMISSILE) && (ii < MAXMISSILE))
    {
      if (emissile[ii].fire(ix, iy, speed))
      {                                 /* valid fire */
        mnum++;
        ii++;
        break;
      }
      else
      {                                 /* nope, keep trying */
        ii++;
      }
    }
    return(ii);
  }

  public void fireM(IFO me)
  /*  IFO me - reference to self (enemy)        */
  {
    tryFireM(me.getxc(), me.getyc(), 0);
  }

  public void fireM4(IFO me, double hoffset, double hdist, double vdist,
                     JDSounds snd)
  /*  IFO me - reference to self (enemy)                        *\
   *  double hdist - horizontal distance between missiles       *
   *  double vdist - vertical distance between missiles         *
  \*  JDSounds snd - reference to jdsounds			*/
  {
    int i = 0;
    double cx = me.getxc();
    double cy = me.getyc();

    if (ConfigJD.SFX)
      snd.playSafe(snd.emissfire);

    i = tryFireM(cx - hoffset - hdist, cy - vdist / 2, i);
    i = tryFireM(cx - hoffset - hdist, cy + vdist / 2, i);

    i = tryFireM(cx - hoffset, cy - vdist * 3 / 2, i);
    i = tryFireM(cx - hoffset, cy + vdist * 3 / 2, i);
  }

  public void paint(Graphics g)
  {
    for (int i = 0; i < MAXFIRE; i++)
      efire[i].paint(g);
    for (int i = 0; i < MAXMISSILE; i++)
      emissile[i].paint(g);
  }
}

