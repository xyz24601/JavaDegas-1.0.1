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
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class JavaDegas extends Applet implements MouseListener, KeyListener,
                                                    Runnable

{
  static public final int G_WIDTH_TEST = 100;
  static public final int G_HEIGHT_TEST = 100;
  static public final int P_HEIGHT_TEST = 10;
  static public final int S_HEIGHT_TEST = 10;

  static public final int G_WIDTH_REL = 500;
  static public final int G_HEIGHT_REL = 350;
  static public final int P_HEIGHT_REL = 20;
  static public final int S_HEIGHT_REL = 20;

  static public int G_WIDTH;			/* width of play area */
  static public int G_HEIGHT;			/* height of play area */
  static public int P_HEIGHT;			/* height of power area */
  static public int S_HEIGHT;			/* height of score area */

  static public int E_INITX;		/* starting point for most enemy */

//  static private final int MAIN_DELAY = 50;   /* delay between each cycle */
//  static private final int MAIN_DELAY = 10;   /* delay between each cycle */
  static private final int MAIN_DELAY = 20;   /* delay between each cycle */
  static public final double SCROLL_SPEED = 1.0;
                                                /* speed of scrolling screen */
  static public boolean sstopped;               /* flag for scrolling */

  static private Colors colors = new Colors();  /* needs to call constructor */
  static private boolean kup, kdown, kleft, kright, kspace;
                                /* need these to avoid auto key repeat */

  private volatile Thread runner = null;

  private boolean suspended = false;
  private Image offscreenI;             /* Image for double buffering */
  private Graphics offscreenG;          /* Graphics for double buffering */
  private Dimension dmn;
  private boolean titleshowing;         /* flag for title */

  private boolean scon, saon;

  private StarG starg;
  private Ship ship;
  private PwrCapG pwrcapg;
  private PwrInd pwrind;
  private Scores scores;
  private Title title;

  private EFireG efireg;
  private AllEnemy allenemy;

  private JDSounds sounds;

  private Stage1 stage1;
  private Stage2 stage2;

  private int cstage;	/* current stage */

  static public boolean appletflag;		/* true if run as applet */

  public void init()
  {
    if (ConfigJD.TEST)
    {
      G_WIDTH = G_WIDTH_TEST;
      G_HEIGHT = G_HEIGHT_TEST;
      P_HEIGHT = P_HEIGHT_TEST;
      S_HEIGHT = S_HEIGHT_TEST;
    }
    else
    {
      G_WIDTH = G_WIDTH_REL;
      G_HEIGHT = G_HEIGHT_REL;
      P_HEIGHT = P_HEIGHT_REL;
      S_HEIGHT = S_HEIGHT_REL;
    }
    E_INITX = G_WIDTH + 5;

    copyleft();
    addMouseListener(this);
    addKeyListener(this);
    requestFocus();
    dmn = new Dimension(G_WIDTH, G_HEIGHT + P_HEIGHT + S_HEIGHT);
    offscreenI = createImage(dmn.width, dmn.height);
    offscreenG = offscreenI.getGraphics();

    kup = kdown = kleft = kright = kspace = false;
    scon = saon = false;
    sstopped = false;

    sounds = new JDSounds(this);

    starg = new StarG();
    pwrcapg = new PwrCapG();
    pwrind = new PwrInd(this);
    scores = new Scores(this);
    title = new Title(this);

    ship = new Ship(pwrcapg, pwrind, sounds);

    efireg = new EFireG();

    allenemy = new AllEnemy();

    stage1 = new Stage1(allenemy);
    stage2 = new Stage2(allenemy);

    titleshowing = true;
    if (ConfigJD.SFX)
      sounds.playSafe(sounds.intro);

    starg.goDown();
  }

  void resetall()
  {
    titleshowing = false;
    starg.goLeft();
    pwrcapg.deleteall();
    allenemy.killthemall();
    ship.init();
    pwrind.init();
    scores.reset();
    efireg.reset();
    stage1.reset(sounds);
    stage2.reset(sounds);
    cstage = 1;
  }

  public void update(Graphics g)
  {
    offscreenG.setColor(Color.black);
    offscreenG.fillRect(0, 0, dmn.width, dmn.height);

    starg.paint(offscreenG);          /* paint stars first */

    if (titleshowing)
    {
      title.paint(offscreenG);
    }
    else
    {
      pwrcapg.paint(offscreenG);
      efireg.paint(offscreenG);
      switch (cstage)
      {
        case 1: stage1.paint(offscreenG);
                break;
        case 2: stage2.paint(offscreenG);
                break;
      }
      ship.paint(offscreenG);
    }

    offscreenG.setColor(Color.black);
    offscreenG.fillRect(0, G_HEIGHT, G_WIDTH, P_HEIGHT + S_HEIGHT);

    pwrind.paint(offscreenG, ship, titleshowing);
    scores.paint(offscreenG);

    paint(g);
  }

  public void paint(Graphics g)
  {
    g.drawImage(offscreenI, 0, 0, this);        /* slap image to screen */
  }

  public void run()
  {
    Thread thisThread = Thread.currentThread();
    while (runner == thisThread)
    {
      try { Thread.sleep(MAIN_DELAY); }         /* add delay between cycle */
      catch (InterruptedException e) {}

      starg.move();

      if (!titleshowing)
      {
        pwrcapg.move(ship, scores);
        pwrind.move(sounds);
        efireg.move(ship);
        switch (cstage)
        {
          case 1: if (!stage1.move(ship, efireg, pwrcapg, scores, sounds))
                  {
                    stage1.nextStage();
                    cstage++;
                  }
                  break;
          case 2: if (!stage2.move(ship, efireg, pwrcapg, scores, sounds))
                  {
                    stage2.nextStage();
                    efireg.nextStage();	/* speed up enemy fire */
                    cstage = 1;		/* back to stage 1 */
                  }
        }
        if (ship.move())
        {
          titleshowing = true;  /* all done */
          if (ConfigJD.SFX)
          {
            sounds.stopSafe(sounds.stage1);
            sounds.stopSafe(sounds.stage1b);
            sounds.playSafe(sounds.intro);
          }
          sstopped = false;
          scores.sethigh();
          starg.goDown();
        }
      }

      repaint();
    }
  }

  public void start()
  {
    if (runner == null)
    {
      runner = new Thread(this);
      runner.start();
    }
  }

  public void stop()
  {
    runner = null;
  }

  public void mousePressed(MouseEvent e) {}

  public void mouseClicked(MouseEvent e)
  {
    e.consume();
    if (null == runner)
      start();
    else
      stop();
  }

  public void mouseReleased(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void keyPressed(KeyEvent e)
  {
    int kc = e.getKeyCode();
    e.consume();
    if (titleshowing)
    {
      if (KeyEvent.VK_SPACE == kc)
        resetall();
    }
    else
    {
      if (((KeyEvent.VK_UP == kc) || (KeyEvent.VK_K == kc)) && (!kup))
      {
        kup = true;
        ship.goUp(true);
      }
      else if (((KeyEvent.VK_DOWN == kc) || (KeyEvent.VK_J == kc)) && (!kdown))
      {
        kdown = true;
        ship.goDown(true);
      }
      else if (((KeyEvent.VK_LEFT == kc) || (KeyEvent.VK_H == kc)) && (!kleft))
      {
        kleft = true;
        ship.goLeft(true);
      }
      else if (((KeyEvent.VK_RIGHT == kc) || (KeyEvent.VK_L == kc)) &&
               (!kright))
      {
        kright = true;
        ship.goRight(true);
      }
      else if ((KeyEvent.VK_SPACE == kc) && (!kspace))
      {
        kspace = true;
        ship.firestat(true);
      }
      else if ((KeyEvent.VK_N == kc) || (KeyEvent.VK_M == kc))
        ship.powerUp();
      else if (KeyEvent.VK_CONTROL == kc)
        scon = true;
      else if (KeyEvent.VK_ALT == kc)
        saon = true;

/* secret commands */
      if ((scon) && (saon))
      {
        if (KeyEvent.VK_O == kc)
          ship.addoption();
        else if (KeyEvent.VK_P == kc)
          ship.changeWeapon();
        else if (KeyEvent.VK_S == kc)
          ship.speedUp();
        else if (KeyEvent.VK_A == kc)
          ship.addShield();
        else if (KeyEvent.VK_Q == kc)
          ship.godMode();
        else if (KeyEvent.VK_I == kc)
          pwrind.pickedPower(sounds, 1);
        else if (KeyEvent.VK_E == kc)
          ship.ChkCrash(ship);              /* self destruct */
      }
    }
  }

  public void keyReleased(KeyEvent e)
  {
    int kc = e.getKeyCode();
    e.consume();
    if (!titleshowing)
    {
      if (((KeyEvent.VK_UP == kc) || (KeyEvent.VK_K == kc)) && (kup))
      {
        kup = false;
        ship.goUp(false);
      }
      else if (((KeyEvent.VK_DOWN == kc) || (KeyEvent.VK_J == kc)) && (kdown))
      {
        kdown = false;
        ship.goDown(false);
      }
      else if (((KeyEvent.VK_LEFT == kc) || (KeyEvent.VK_H == kc)) && (kleft))
      {
        kleft = false;
        ship.goLeft(false);
      }
      else if (((KeyEvent.VK_RIGHT == kc) || (KeyEvent.VK_L == kc)) && (kright))
      {
        kright = false;
        ship.goRight(false);
      }
      else if ((KeyEvent.VK_SPACE == kc) && (kspace))
      {
        kspace = false;
        ship.firestat(false);
      }
      else if (KeyEvent.VK_CONTROL == kc)
        scon = false;
      else if (KeyEvent.VK_ALT == kc)
        saon = false;
    }
  }

  public void keyTyped(KeyEvent e) {}

  public void destroy()
  {
    sounds.stopall();
  }

  void copyleft()
  {
    System.out.println("JavaDegas v1.0.1 -- Space Shooting Game Classic");
    System.out.println("Copyright (C) 2000  Shinji Umeki (shinji@umeki.org)");
    System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
    System.out.println("See the file COPYING for details.");
  }
}

