package com.projectdummy.dummy.project.Scanner;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public abstract interface IScanner {
    public abstract void initialize(Logger log);

    public abstract byte[] captureBytes();

    public abstract BufferedImage captureImage();

    public abstract boolean stopCapture();

    public abstract boolean close();


}
