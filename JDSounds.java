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

import java.applet.*;
import java.net.*;

public class JDSounds
{
  public AudioClip intro;		/* title screen voice */
  public AudioClip letsgo;		/* ship start voice */
  public AudioClip ohno;		/* ship dead voice */

  public AudioClip speedup;		/* power up voices */
  public AudioClip slowdown;
  public AudioClip beam;
  public AudioClip laser;
  public AudioClip option;
  public AudioClip doh;
  public AudioClip shield;

  public AudioClip surprise;		/* picked up surprise power voice */

  public AudioClip deadship;		/* ship dead sound effect */
  public AudioClip beamfire;		/* beam fire sound effect */
  public AudioClip laserfire;		/* laser fire sound effect */
  public AudioClip getpower;		/* picked up power sound effect */
  public AudioClip annoy;		/* surprise power indicator SF */
  public AudioClip hitshield;		/* hit shield sound effect */

  public AudioClip deadenemy;		/* dead enemy sound effect */
  public AudioClip deadboss;		/* dead boss sound effect */
  public AudioClip emissfire;		/* missile fire sound effect */
  public AudioClip hitboss;		/* hit boss sound effect */

  public AudioClip stage1;		/* stage1 music */
  public AudioClip stage1b;		/* stage1 boss music */

  public JDSounds(Applet a)
  {
    URL url;
    try
    {				/* only applet can play audio */
      url = a.getDocumentBase();
      JavaDegas.appletflag = true;	/* set global flag */
    }
    catch (NullPointerException e)
    {
      JavaDegas.appletflag = false;
    }

    if (JavaDegas.appletflag)
    {
      if (ConfigJD.SFX)
      {
        intro = a.getAudioClip(a.getDocumentBase(), "audio/intro.au");
        letsgo = a.getAudioClip(a.getDocumentBase(), "audio/letsgo.au");
        ohno = a.getAudioClip(a.getDocumentBase(), "audio/ohno.au");

        speedup = a.getAudioClip(a.getDocumentBase(), "audio/speedup.au");
        slowdown = a.getAudioClip(a.getDocumentBase(), "audio/slowdown.au");
        beam = a.getAudioClip(a.getDocumentBase(), "audio/beam.au");
        laser = a.getAudioClip(a.getDocumentBase(), "audio/laser.au");
        option = a.getAudioClip(a.getDocumentBase(), "audio/option.au");
        doh = a.getAudioClip(a.getDocumentBase(), "audio/doh.au");
        shield = a.getAudioClip(a.getDocumentBase(), "audio/shield.au");

        surprise = a.getAudioClip(a.getDocumentBase(), "audio/surprise.au");

        deadship = a.getAudioClip(a.getDocumentBase(), "audio/deadship.au");
        beamfire = a.getAudioClip(a.getDocumentBase(), "audio/beamfire.au");
        laserfire = a.getAudioClip(a.getDocumentBase(), "audio/laserfire.au");
        getpower = a.getAudioClip(a.getDocumentBase(), "audio/getpower.au");
        annoy = a.getAudioClip(a.getDocumentBase(), "audio/annoy.au");
        hitshield = a.getAudioClip(a.getDocumentBase(), "audio/hitshield.au");

        deadenemy = a.getAudioClip(a.getDocumentBase(), "audio/deadenemy.au");
        deadboss = a.getAudioClip(a.getDocumentBase(), "audio/deadboss.au");
        emissfire = a.getAudioClip(a.getDocumentBase(), "audio/emissfire.au");
        hitboss = a.getAudioClip(a.getDocumentBase(), "audio/hitboss.au");
      }

      if (ConfigJD.MUSIC)
      {
        stage1 = a.getAudioClip(a.getDocumentBase(), "music/stage1.au");
        stage1b = a.getAudioClip(a.getDocumentBase(), "music/stage1b.au");
      }
    }
  }

  public void stopall()
  {
    if (JavaDegas.appletflag)
    {
      if (ConfigJD.SFX)
      {
        stopSafe(intro);
        stopSafe(letsgo);
        stopSafe(ohno);

        stopSafe(speedup);
        stopSafe(slowdown);
        stopSafe(beam);
        stopSafe(laser);
        stopSafe(option);
        stopSafe(doh);
        stopSafe(shield);

        stopSafe(surprise);

        stopSafe(deadship);
        stopSafe(beamfire);
        stopSafe(laserfire);
        stopSafe(getpower);
        stopSafe(annoy);
        stopSafe(hitshield);

        stopSafe(deadenemy);
        stopSafe(deadboss);
        stopSafe(emissfire);
        stopSafe(hitboss);
      }

      if (ConfigJD.MUSIC)
      {
        stopSafe(stage1);
        stopSafe(stage1b);
      }
    }
  }

  public void playSafe(AudioClip au)
  {
    if (JavaDegas.appletflag)
    {
      try
      {
        au.play();
      }
      catch (NullPointerException e)
      {
        System.out.println("Missing audio file: " + e.toString());
      }
    }
  }

  public void loopSafe(AudioClip au)
  {
    if (JavaDegas.appletflag)
    {
      try
      {
        au.loop();
      }
      catch (NullPointerException e)
      {
        System.out.println("Missing audio file: " + e.toString());
      }
    }
  }

  public void stopSafe(AudioClip au)
  {
    if (JavaDegas.appletflag)
    {
      try
      {
        au.stop();
      }
      catch (NullPointerException e)
      {
        System.out.println("Missing audio file: " + e.toString());
      }
    }
  }

}


