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

/*  stage 1  */
public class Stage1
{
  private AllEnemy allenemy;

  private boolean alive;
  private int level;

  private boolean b1, b2, b3, b4, b5, b6, b7;

  private JDSounds snd;

  public Stage1(AllEnemy iallenemy)
  {
    allenemy = iallenemy;
//    level = 1;
  }

  public void reset(JDSounds isnd)
  {
//    if (ConfigJD.MUSIC)
//      snd.loopSafe(snd.stage1);

    snd = isnd;

    level = 1;
    init();
  }

  public void nextStage()
  {
    level++;
    init();
  }

  private void init()
  {
//System.out.println("");       // somehow this screws up if this doesn't exist
    alive = true;

    if ((ConfigJD.MUSIC) && (JavaDegas.appletflag))
    {
      snd.stopSafe(snd.intro);
      snd.loopSafe(snd.stage1);
    }

    /*  int imaxwave - max number of waves (groups)       *\
     *  int ixc - max horizontal movement                 *
     *  int iyc - max vertical movement                   *
     *  int ienum - num of enemy in a group               *
     *  double ispeed - speed                             *
    \*  boolean ibf - flag for top or bottom              */
    allenemy.enemy1w.init(7, 70, 25, 4 + level, 2.0);

    b1 = b2 = b3 = b4 = b5 = b6 = b7 = true;
//    b1 = b2 = b3 = b4 = b5 = b6 = b7 = false;
//          allenemy.boss1w.init(1, 2.0, level);
  }


  public boolean move(Ship ship, EFireG efireg, PwrCapG pwrcapg, Scores scores,
                      JDSounds snd)
  /*  Ship ship - reference to ship                     *\
   *  EFireG efireg - reference to efireg               *
   *  PwrCapG pwrcapg - reference to pwrcapg            *
   *  Scores scores - reference to scores               *
   *  JDSounds snd- reference to jdsounds		*
   *    Return boolean: true if stage is finished,      *
  \*                    false otherwise                 */
  {
    if (alive)
    {
      if (b1)
      {
        /*  int mindelay - min delay between group                    *\
        \*  int edelay - delay between enemy                          */
        if (!allenemy.enemy1w.move(ship, efireg, pwrcapg, scores, 200, 20,
                                   level, snd))
        {
          b1 = false;
          /*  int imaxwave - max number of waves (groups)       *\
           *  int itype - type of wave                          *
           *  int ienum - num of enemy in a group               *
           *  double ixspeed - horizontal speed                 *
          \*  double iyrspeed - vertical radian speed           */
          allenemy.enemy2w2.init(3, Enemy2G2.SEPARATE, 4 + level, 1.5, 0.03);
        }
        return(true);           /* get the hell out of here */
      }

      if (b2)
      {
        /*  int mindelay - min delay between group                    *\
        \*  int idelay - delay between enemy                          */
        if (!allenemy.enemy2w2.move(ship, efireg, pwrcapg, scores, 200, 20,
                                    level, snd))
        {
          /*  int imaxwave - max number of waves (groups)       *\
           *  int type - type of wave                           *
           *  double ixspeed - horizontal speed                 *
          \*  double iyrspeed - vertical radian speed           */
          allenemy.enemy2w.init(3, Enemy2G.SEPARATE, 1.0, 0.05);
          /*  int imaxwave - max number of waves (groups)       *\
           *  int inumfplaces - num of firing places            *
           *  double ispeed - speed                             *
          \*  int imaxenum - current max enemy num              */
          allenemy.enemy4w.init(3, 3, 3.0, 2 + level);
          /*  int imaxwave - max number of waves (groups)       *\
           *  double ispeed - speed                             *
          \*  int imwdelay - max walk delay                     */
          allenemy.enemy6w.init(10, 1.0, 200);

          b2 = false;
        }
        return(true);           /* get the hell out of here */
      }

      if (b3)
      {
        /*  int mindelay - min delay between group                    */
        if (!allenemy.enemy2w.move(ship, efireg, pwrcapg, scores, 200, level,
                                   snd))
        {
          if (b5)
          {		/* keep sending them */
            allenemy.enemy2w.init(3, Enemy2G.SEPARATE, 1.0, 0.05);
          }
          else
            b3 = false;
        }
      }

      if (b4)
      {
        /*  int mindelay - min delay between group                    */
        if (!allenemy.enemy4w.move(ship, efireg, pwrcapg, scores, 150, level,
                                   snd))
        {
          if (b5)
            /*  int imaxwave - max number of waves (groups)       *\
             *  int inumfplaces - num of firing places            *
             *  double ispeed - speed                             *
            \*  int imaxenum - current max enemy num              */
            allenemy.enemy4w.init(3, 3, 1.0, 2 + level);
          else
            b4 = false;
        }
      }                         /* keep on going */

      if (b5)
      {
        /*  int mindelay - min delay between group                    *\
         *  int level - level                                         *
         *  int stopdelay - delay for stop                            *
        \*  int maxstop - max num of stop                             */
        if (!allenemy.enemy6w.move(ship, efireg, scores, 200, level, 5, 7, snd))
        {
          b5 = false;
          /*  int imaxwave - max number of waves (groups)       *\
           *  double ix - initial x coordinate                  *
           *  int imaxenum - current max enemy num              *
           *  double ispeed - speed                             *
          \*  int rdelay - delay before ready                   */
          allenemy.enemy7w2.init(5, JavaDegas.G_WIDTH * 4 / 5, 2 + level,
                                 1.5, 40);
        }
        return(true);           /* get the hell out of here */
      }

      if (b6)
      {
        /*  int mindelay - min delay between group                    *\
         *  int level - level                                         *
        \*  int fdelay - delay before fire                            */
        if (!allenemy.enemy7w2.move(ship, efireg, pwrcapg, scores, 150, level,
                                    25, snd))
        {
          if (ConfigJD.MUSIC)
          {
            snd.stopSafe(snd.stage1);
            snd.loopSafe(snd.stage1b);
          }
          b6 = false;
          JavaDegas.sstopped = true;            /* stop scroll */
          /*  int imaxwave - max number of waves (groups)       *\
          \*  int rdelay - delay before ready                   */
          allenemy.enemy7w.init(10, 3 + level, 1.0 + level * 0.5, 40);
        }
        return(true);
      }

      if (b7)
      {
        /*  int mindelay - min delay between group                    *\
         *  int level - level                                         *
        \*  int fdelay - delay before fire                            */
        if (!allenemy.enemy7w.move(ship, efireg, pwrcapg, scores, 100, level,
                                   25, snd))
        {
          b7 = false;
          /*  int imaxwave - max number of waves (groups)       *\
           *  double ispeed - speed                             *
          \*  int ilevel - level                                */
          allenemy.boss1w.init(1, 2.0, level);
        }
        return(true);
      }

      if (!allenemy.boss1w.move(ship, efireg, scores, snd))
      {
        if (ConfigJD.MUSIC)
          snd.stopSafe(snd.stage1b);

        alive = false;
        JavaDegas.sstopped = false;             /* resume scroll */
      }
    }
    return(alive);
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      if (b1)
      {
        allenemy.enemy1w.paint(g);
        return;
      }
      else if (b2)
      {
        allenemy.enemy2w2.paint(g);
        return;
      }
      else if (b6)
      {
        allenemy.enemy2w.paint(g);
        allenemy.enemy4w.paint(g);
        allenemy.enemy6w.paint(g);
        allenemy.enemy7w2.paint(g);
        return;
      }
      else if (b7)
      {
        allenemy.enemy7w.paint(g);
        return;
      }
      allenemy.boss1w.paint(g);
    }
/*
    if (alive)
    {
      allenemy.enemy1w.paint(g);
      allenemy.enemy2w2.paint(g);
      allenemy.enemy2w.paint(g);
      allenemy.enemy4w.paint(g);
      allenemy.enemy6w.paint(g);
      allenemy.enemy7w2.paint(g);
      allenemy.enemy7w.paint(g);
      allenemy.boss1w.paint(g);
    }
*/
  }

}


