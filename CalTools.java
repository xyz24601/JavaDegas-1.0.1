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


/*  static class containing misc calculations  */
public class CalTools
{

  static public void EFire_calcxy(double ox, double oy, IFO ship, double speed,
                                  double[] xyspeed)
  /*  double ox - x coordinate of firing place                          *\
   *  double oy - y coordinate of firing place                          *
   *  IFO ship - reference to ship                                      *
   *  double speed - speed of fire                                      *
   *  double[] xyspeed - calculated x & y speed                         *
   *    Calculates xspeed & yspeed, according to location of enemy,     *
  \*    ship and speed                                                  */
  {
    boolean xp, yp;                     /* keep track on direction */
    double angle;

    double tx = ship.getxc();
    double ty = ship.getyc();

    /* one of these days, I'll figure out why direction get messed up */
    double dx = ox - tx;                /* distances */
//    if (dx >= 0)
    if (dx < 0)
      xp = false;                       /* store horizontal direction */
    else
    {
      xp = true;
      dx = -dx;
    }

    double dy = oy - ty;
//    if (dy >= 0)
    if (dy < 0)
      yp = false;                       /* store vertical direction */
    else
    {
      yp = true;
      dy = -dy;
    }

    if (0 == dx)                /* protect from divide by 0 error */
      angle = Math.atan(Double.MAX_VALUE);
    else
      angle = Math.atan(dy / dx);

    xyspeed[0] = speed * Math.cos(angle);       /* needs to return these */
    xyspeed[1] = speed * Math.sin(angle);

    if (!xp)                    /* apply horizontal direction */
      xyspeed[0] = -xyspeed[0];
    if (!yp)                    /* apply vertical direction */
      xyspeed[1] = -xyspeed[1];
  }


  static public double Enemy2_calcy(double radian, int moveheight,
                                    int movecenter)
  /*  double radian - y coordinate in radian                    *\
   *  int moveheight - height of movement                       *
   *  int movecenter - center of movement                       *
  \*    Return: double - calculated y coordinate in double      */
  {
    return(Math.sin(radian) * moveheight / 2 + movecenter);
  }

  static public double Enemy3_calcy(double radian, int bounceheight)
  /*  double radian - y coordinate in radian                    *\
   *  int bounceheight - height of bounce                       *
  \*    Return: double - calculated y coordinate in double      */
  {
    return(JavaDegas.G_HEIGHT - Math.abs(Math.sin(radian) * bounceheight));
  }

  static public double CalcFP(int fplaces, int flevel)
  /*  int fplaces - number of fire places                       *\
   *  int flevel - fire level                                   *
  \*    Return double - calculated y coordinate of fire place   */
  {
//    return(JavaDegas.G_WIDTH - (JavaDegas.G_WIDTH / fplaces) * (flevel + 1));
    return(JavaDegas.G_WIDTH - (JavaDegas.G_WIDTH / fplaces) * flevel);
  }

}

