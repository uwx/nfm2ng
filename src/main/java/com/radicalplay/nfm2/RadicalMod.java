package com.radicalplay.nfm2;

import fallk.logmaster.HLogger;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RadicalMod implements RadicalMusic {

    private SuperClip sClip;
    private byte[] modf;
    private boolean playing;
    public int loaded;

    @Override
    public void setPaused(boolean paused) {
        if (paused) {
            if (!playing && loaded == 2) {
                sClip.resume();
                if (sClip.stoped == 0) {
                    playing = true;
                }
            }
        } else {
            if (playing && loaded == 2) {
                sClip.stop();
                playing = false;
            }
        }
    }

    public RadicalMod(String s) {
        playing = false;
        loaded = 0;
        loaded = 1;
        try {
            ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(s));
            ZipEntry zipentry = zipinputstream.getNextEntry();
            int size = (int) zipentry.getSize();
            modf = new byte[size];
            int offset = 0;
            for (int bytesRead; size > 0; size -= bytesRead) {
                bytesRead = zipinputstream.read(modf, offset, size);
                offset += bytesRead;
            }

        } catch (Exception exception) {
            HLogger.error("Error loading com.radicalplay.nfm2.Mod from zip file: " + exception);
            loaded = 0;
        }
    }

    void unloadAll() {
        if (playing && loaded == 2) {
            sClip.stop();
        }
        try {
            sClip.close();
            sClip = null;
        } catch (Exception _ex) {
        }
        try {
            modf = null;
        } catch (Exception _ex) {
        }
        System.gc();
    }

    @Override
    public void play() {
        if (!playing && loaded == 2) {
            sClip.play();
            if (sClip.stoped == 0) {
                playing = true;
            }
        }
    }

    @Override
    public void unload() {
        if (loaded == 2) {
            if (playing) {
                sClip.stop();
                playing = false;
            }
            try {
                sClip.close();
                sClip = null;
            } catch (Exception _ex) {
            }
            System.gc();
            loaded = 1;
        }
    }

    public void loadMod(int i, int j, int k) {
        if (loaded == 1) {
            loaded = 2;
            int l = 22000;
            j = (int) ((j / 8000F) * 2.0F * l);
            Mod mod = new Mod(new ByteArrayInputStream(modf));
            ModSlayer modslayer = new ModSlayer(mod, j, i, k);
            try {
                byte[] abyte0 = modslayer.turnbytesNorm();
                sClip = new SuperClip(abyte0, modslayer.oln, l);
            } catch (Exception exception) {
                HLogger.error("Error making a com.radicalplay.nfm2.Mod: " + exception);
                loaded = 0;
            }
        }
    }
}
