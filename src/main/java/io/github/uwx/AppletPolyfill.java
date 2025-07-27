package io.github.uwx;

import javax.swing.*;
import java.awt.*;

public abstract class AppletPolyfill extends JPanel {
    public void stop() {}

    public abstract void start();
}
