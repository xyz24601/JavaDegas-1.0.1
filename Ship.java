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

public class Ship extends IFO
{
  static private final int WIDTH_TEST = 10;
  static private final int HEIGHT_TEST = 10;

  static private final int WIDTH_REL = 50;
  static private final int HEIGHT_REL = 25;

  static private int WIDTH;
  static private int HEIGHT;

  static private final double INITSPEED = 3.0;  /* initial speed */
  static private final double MINSPEED = 1.0;	/* minimum speed */
  static private final double SPEEDINC = 2.0;   /* speed increment */
  static private final double SPEEDDEC = 1.0;	/* speed decrement */

  static private final int DDELAY = 10;
  static private final int MAXSHIP = 3;
  static private final int DEADDELAY = 30;
  static private final int READYDELAY = 45;
  static private final int MARGIN = 5;          /* between ship & border */
  static private final int NUMSHIP = 3;
  static private final int MAXOPTION = 4;       /* max number of options */
  static private final int OPTDISTANCE = 10;     /* distance between options */

  static private int WX;	/* need this for explosion */
  static private int WY;

        /* these array and other stuff are necessary for option */
  static private final int maxarray = (MAXOPTION + 1) * OPTDISTANCE;
                                        /* size of movement track array */
  static private double xyarray[][] = new double[maxarray][2];
                                        /* array to track ship movements */
  private int sindex;                   /* array index for ship */
  private int oindex[] = new int[MAXOPTION];
                                        /* array indexes for options */

  private int cship;                            /* current number of ship */
  private int coption;                          /* current number of options */
  private int initx;                            /* initial x coordinate */
  private int inity;                            /* initial y coordinate */
  private double speed;                         /* current speed */
  private boolean up, down, left, right;        /* current movement flags */
  private int crdelay;                          /* current ready delay */
  private int cddelay;                          /* current dead delay */
  private int movedir;                          /* moving up or down */

  private SFireG sfireg;
  private PwrInd pwrind;
  public Shield shield;

  private PwrCapG pwrcapg;                      /* reference to PwrCapG */

  private JDSounds snd;				/* reference to sounds */

  public boolean fbon;                          /* flag for fire button */
  public int wtype;                             /* current weapon type */

  public boolean god;                           /* flag for god mode */

  private Option option[] = new Option[MAXOPTION];

  public Ship(PwrCapG ipwrcapg, PwrInd ipwrind, JDSounds isnd)
  /*  PwrCapG ipwrcapg - reference to PwrCapG   */
  {				/* change the size according to ConfigJD */
    super((ConfigJD.TEST) ? WIDTH_TEST : WIDTH_REL,
          (ConfigJD.TEST) ? HEIGHT_TEST : HEIGHT_REL);

    if (ConfigJD.TEST)		/* bummer, super has to be called first */
    {
      WIDTH = WIDTH_TEST;
      HEIGHT = HEIGHT_TEST;
    }
    else
    {
      WIDTH = WIDTH_REL;
      HEIGHT = HEIGHT_REL;
    }

    initx = JavaDegas.G_WIDTH / 5;
    inity = JavaDegas.G_HEIGHT / 2;

    WX = WIDTH / DDELAY;
    WY = HEIGHT / DDELAY;

    sfireg = new SFireG();

    pwrcapg = ipwrcapg;
    pwrind = ipwrind;

    snd = isnd;

    shield = new Shield();

    for (int i = 0; i < MAXOPTION; i++)
      option[i] = new Option();

    god = false;
//    god = true;

    init();
    reset();
  }

  public void init()
  {
    cship = 1;
    up = down = left = right = false;
    movedir = 0;
    fbon = false;                               /* fire button is off */
  }

  void reset()
  {
    speed = INITSPEED;
    setxy(initx, inity);
    alive = true;
    ready = dying = false;
    ddelay = 0;
    crdelay = 0;
    cddelay = 0;
    wtype = Weapons.WT_BEAM;                    /* beam is default weapon */

    if (ConfigJD.SFX)
      snd.playSafe(snd.letsgo);

    int i;
    coption = 0;
    for (i = 0; i < MAXOPTION; i++)             /* hide all options */
      option[i].delete();
    sfireg.reset();

    for (i = 0; i < maxarray; i++)
    {
      xyarray[i][0] = initx;
      xyarray[i][1] = inity;
    }
    sindex = 0;
    for (i = 0; i < MAXOPTION; i++)     /* set array index for all options */
      oindex[i] = maxarray - ((i + 1) * OPTDISTANCE) - 1;

    pwrind.reset();
    shield.delete();
  }

  public boolean move()
  /*    Return: boolean - true if all ships are dead    *\
  \*                      false otherwise               */
  {
    int i;
    if (crdelay < READYDELAY)           /* wait before getting hit */
      crdelay++;
    else
      ready = true;                     /* Now, it's ready */

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
        if ((left) || (right) || (up) || (down))
        {
          double x = getx();
          double y = gety();

          if ((left) && (x > MARGIN))
            x -= speed;
          if ((right) && ((x + WIDTH + MARGIN) < JavaDegas.G_WIDTH))
            x += speed;
          if ((up) && (y > MARGIN))
          {
            y -= speed;
            movedir++;
          }
          if ((down) && (y < (JavaDegas.G_HEIGHT - MARGIN - HEIGHT)))
          {
            y += speed;
            movedir--;
          }

          if (sindex < (maxarray - 1))  /* move array index for ship */
            sindex++;
          else
            sindex = 0;

          xyarray[sindex][0] = x;       /* store ship's new coordinate */
          xyarray[sindex][1] = y;

          for (i = 0; i < MAXOPTION; i++)       /* move indexes for options */
          {
            if (oindex[i] < (maxarray - 1))
              oindex[i]++;
            else
              oindex[i] = 0;
          }

          shield.move(x + WIDTH, y - 5);

          setxy(x, y);
        }
      }
    }
    else
    {
      if (cddelay < DEADDELAY)
        cddelay++;
      else if (cship < NUMSHIP)
      {
        cship++;
        reset();
      }
      else
        return(true);
    }                           /* move sfireg, always */
    sfireg.move(getx() + WIDTH, gety() + HEIGHT / 2, this, snd);
    for (i = 0; i < coption; i++)       /* update current options */
    {           /* need to call this even ship to dead to update sfireg */
      option[i].move(xyarray[oindex[i]][0], xyarray[oindex[i]][1], this, snd);
    }
    return(false);
  }

  public void goUp(boolean bo)
  /*  boolean bo - true if keypress, false otherwise    */
  {
    if (bo)
    {
      up = true;
      down = false;
    }
    else
    {
      up = false;
      movedir = 0;                      /* reset movement direction */
    }
  }

  public void goDown(boolean bo)
  /*  boolean bo - true if keypress, false otherwise    */
  {
    if (bo)
    {
      down = true;
      up = false;
    }
    else
    {
      down = false;
      movedir = 0;                      /* reset movement direction */
    }
  }

  public void goLeft(boolean bo)
  /*  boolean bo - true if keypress, false otherwise    */
  {
    if (bo)
    {
      left = true;
      right = false;
    }
    else
      left = false;
  }

  public void goRight(boolean bo)
  /*  boolean bo - true if keypress, false otherwise    */
  {
    if (bo)
    {
      right = true;
      left = false;
    }
    else
      right = false;
  }

  public void paint(Graphics g)
  {
    if (alive)
    {
      int cx = (int) getx();
      int cy = (int) gety();
      if (dying)                        /* draw explosion */
      {
        g.setColor(Color.white);
        g.fillOval(cx + WIDTH / 2 - WX * ddelay,
                   cy + HEIGHT / 2 - WY * ddelay,
                   WX * ddelay * 2, WY * ddelay * 2);
    
      }
      else
      {
        shield.paint(g);

        if (god)
          g.setColor(Color.yellow);             /* distinguish GOD mode */
        else
          g.setColor(Color.white);

        if (ConfigJD.DEBUG)
          g.drawRect(cx, cy, WIDTH, HEIGHT);

        if (5 < movedir)
          drawUp(g, cx, cy);
        else if (-5 > movedir)
          drawDown(g, cx, cy);
        else
          drawLevel(g, cx, cy);
      }
    }
    sfireg.paint(g);
    for (int i = 0; i < MAXOPTION; i++)
      option[i].paint(g);
  }

  void drawLevel(Graphics g, int cx, int cy)
  /*  Graphics g - reference to graphics        *\
   *  int cx - current x location               *
  \*  int cy - current y location               */
  {
    g.fillArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT, 270, 90);
    g.setColor(Color.blue);
    g.fillArc(cx, cy, WIDTH, HEIGHT, 0, 90);
    g.setColor(Color.gray);
    g.fillArc(cx, cy, WIDTH, HEIGHT, 90, 90);
  }

  void drawUp(Graphics g, int cx, int cy)
  /*  Graphics g - reference to graphics        *\
   *  int cx - current x location               *
  \*  int cy - current y location               */
  {
    g.fillArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT, 270, 180);
    g.setColor(Color.black);
    g.drawOval(cx, cy + HEIGHT / 4, WIDTH, HEIGHT / 2);
  }

  void drawDown(Graphics g, int cx, int cy)
  /*  Graphics g - reference to graphics        *\
   *  int cx - current x location               *
  \*  int cy - current y location               */
  {
    g.fillArc(cx - WIDTH, cy, WIDTH * 2, HEIGHT, 270, 180);
    g.setColor(Color.blue);
    g.fillArc(cx, cy + HEIGHT / 4, WIDTH, HEIGHT / 2, 270, 180);
    g.setColor(Color.gray);
    g.fillArc(cx, cy + HEIGHT / 4, WIDTH, HEIGHT / 2, 90, 180);
  }

  public void firestat(boolean bo)
  /*  boolean bo - true if pressed, false otherwise     */
  {
    fbon = bo;
    sfireg.firestat(wtype, bo);
    for (int i = 0; i < coption; i++)
      option[i].firestat(wtype, bo);
  }

  public boolean ChkStatus(IFO obj)
  /*  IFO obj - reference to enemy                      *\
   *    Return boolean: true if enemy is killed,        *
  \*                    false otherwise                 */
  {
    return((ChkCrash(obj)) || (ChkFire(obj)) || (ChkShield(obj)));
  }

  public boolean ChkCrash(IFO obj)
  /*  IFO obj - reference to enemy                              *\
   *    Return boolean: true if enemy crashed into ship,        *
  \*                    false otherwise                         */
  {
    if ((alive) && (ready) && (!dying) && (area.intersects(obj.area)))
    {                                   /* crashed into ship */
      if (ConfigJD.TRACE)
        System.out.println("CIS");

      if (!god)
      {
        if (ConfigJD.SFX)
        {
          snd.playSafe(snd.deadship);
          snd.playSafe(snd.ohno);
        }
        dying = true;
        pwrind.surprise = false;	/* stop the surprise indicator */
        for (int i = 0; i < coption; i++)
        {
          pwrcapg.place(option[i].getx(), option[i].gety(), true);
          option[i].delete();
        }
      }
      return(true);
    }
    return(false);
  }

  public boolean ChkShield(IFO obj)
  /*  IFO obj - reference to enemy                              *\
   *    Return: boolean - true if enemy crashed into shield,    *
  \*                      false otherwise                       */
  {
    if ((shield.alive) && (shield.area.intersects(obj.area)))
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.hitshield);
      shield.hit();
      return(true);
    }
    return(false);
  }

  boolean ChkFire(IFO obj)
  /*  IFO obj - reference to enemy                      *\
   *    Return boolean: true if any fire hits enemy,    *
  \*                    false otherwise                 */
  {
    if (sfireg.ChkFire(obj))            /* shot down */
    {
      if (ConfigJD.TRACE)
        System.out.println("SBS");

      return(true);
    }
    for (int i = 0; i < coption; i++)
    {
      if (option[i].sfireg.ChkFire(obj))
      {
        if (ConfigJD.TRACE)
          System.out.println("SBO");

        return(true);
      }
    }
    return(false);
  }

  public boolean ChkPower(PwrCap obj)
  /*  PwrCap obj - reference to capsule                         *\
   *    Return boolean: true if capsule has been picked up,     *
  \*                    false otherwise                         */
  {
    if ((alive) && (area.intersects(obj.area)))
    {                           /* doesn't have to be ready to pick it up */
      if (obj.isOption())
        addoption();            /* if floating option, add option */
      else
        pwrind.pickedPower(snd, obj.type);   /* move power indicator */
      return(true);
    }
    return(false);
  }

  public void powerUp()
  {
    pwrind.powerUp(this, snd);
  }

  public void addoption()
  {
    if (alive)
    {
      if (coption < MAXOPTION)
      {
        if (ConfigJD.SFX)
          snd.playSafe(snd.option);
        option[coption].add(xyarray[oindex[coption]][0],
                            xyarray[oindex[coption]][1]);
        if (fbon)
          option[coption].firestat(wtype, true);
        coption++;
      }
    }
  }

  public void speedUp()
  {
    if (alive)
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.speedup);
      speed += SPEEDINC;
    }
  }

  public void slowDown()
  {
    if (alive)
    {
      if (!isMinSpeed())
      {
        if (ConfigJD.SFX)
          snd.playSafe(snd.slowdown);
        speed -= SPEEDDEC;
        if (0.0 >= speed)
          speed = MINSPEED;
      }
    }
  }

  public void addShield()
  {
    if (alive)
    {
      if (ConfigJD.SFX)
        snd.playSafe(snd.shield);
      shield.add(getx() + WIDTH, gety() - 5);
    }
  }

  public boolean isMaxOption()
  {
    if (coption < MAXOPTION)
      return(false);
    else
      return(true);
  }

  public boolean hasShield()
  {
    return(shield.hasShield());
  }

  public boolean isMinSpeed()
  {
    if (MINSPEED == speed)
      return(true);
    else
      return(false);
  }

  public void doh()
  {
    if (ConfigJD.SFX)
      snd.playSafe(snd.doh);
    speed = INITSPEED;
    wtype = Weapons.WT_BEAM;
    coption = 0;
    for (int i = 0; i < MAXOPTION; i++)
      option[i].delete();
    shield.delete();
  }

  public void godMode()
  {
    god = !god;
  }


  public void changeWeapon()
  {
    if (Weapons.WT_BEAM == wtype)
      wtype = Weapons.WT_LASER;
    else
      wtype = Weapons.WT_BEAM;
  }
}

