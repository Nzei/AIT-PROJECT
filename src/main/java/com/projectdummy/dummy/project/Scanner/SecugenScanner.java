package com.projectdummy.dummy.project.Scanner;

import SecuGen.FDxSDKPro.jni.*;
import com.projectdummy.dummy.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SecugenScanner implements IScanner {

    @Autowired
    private CustomerService customerService;

    private static SecugenScanner scanner;
    JSGFPLib sgfplib = new JSGFPLib();
    SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
    long err = 0L;
    int[] score = new int[1];
    int[] maxSize = new int[1];

    @Override
    public void initialize(Logger log) {
        start();
    }

    public void initialize() {
        initialize(Logger.getGlobal());
    }

    @Override
    public byte[] captureBytes() {
        // getImage() - 1st Capture
        System.out.println("Call SetLedOn(true)");
        err = sgfplib.SetLedOn(true);
        System.out.println("SetLedOn returned : [" + err + "]");
        //System.out.print("Capture 1. Please place [" + finger + "] on sensor with LEDs on and press <ENTER> ");
        byte[] originalImage = new byte[deviceInfo.imageHeight * deviceInfo.imageWidth];

        System.out.println("Call GetImage()");
        err = sgfplib.GetImage(originalImage); //image is gotten here
        System.out.println("GetImage returned : [" + err + "]");

        if (err != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("ERROR: Fingerprint image capture failed.");
            return null; //Cannot continue test if image not captured
        }

        //image not null
        int[] quality = new int[1];
        err = sgfplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, originalImage, quality);
        System.out.println("GetImageQuality returned : [" + err + "]");
        System.out.println("Image Quality is : [" + quality[0] + "]");

        return originalImage;

    }

    public static BufferedImage toImage(byte[] data, int w, int h) {
        DataBuffer buffer = new DataBufferByte(data, data.length);
        WritableRaster raster = Raster.createInterleavedRaster(buffer,
                w, h, w, 1, new int[]{0}, null);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorModel cm = new ComponentColorModel(cs, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, raster, false, null);
    }

    public BufferedImage toImage(byte[] data) {
        return toImage(data, deviceInfo.imageWidth, deviceInfo.imageHeight);
    }


    @Override
    public BufferedImage captureImage() {
        BufferedImage image = null;
        try {
            image = toImage(captureBytes(), deviceInfo.imageWidth, deviceInfo.imageHeight);
            ImageIO.write(image, "jpg", new File(System.currentTimeMillis() + ".jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public boolean stopCapture() {
        return false;
    }

    @Override
    public boolean close() {
        stop();
        return true;
    }

    public static SecugenScanner getInstance() {
        if (scanner == null) {
            scanner = new SecugenScanner();
        }
        return scanner;
    }

    private JSGFPLib getDriverInstance() {
        if (sgfplib == null) {
            sgfplib = new JSGFPLib();
        }

        return sgfplib;
    }

    /**
     * Starts the scanner library and DlL by Instantiating JSGFPLib object
     */
    public void start() {
        System.out.println("Instantiate JSGFPLib Object");
        getDriverInstance();
        if ((sgfplib != null) && (sgfplib.jniLoadStatus != SGFDxErrorCode.SGFDX_ERROR_JNI_DLLLOAD_FAILED)) {
            System.out.println(sgfplib);
        } else {
            System.out.println("An error occurred while loading JSGFPLIB.DLL JNI Wrapper");
            return;
        }

        init();
        openDevice();
    }

    /**
     * Initialises the scanner to get it ready
     */
    private void init() {
        System.out.println("Call Init(SGFDxDeviceName.SG_DEV_AUTO)");
        err = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        System.out.println("Init returned : [" + err + "]");
    }

    /**
     * Open the scanner device
     */
    private void openDevice() {
        System.out.println("Call OpenDevice(SGPPPortAddr.AUTO_DETECT)");
        err = sgfplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
        System.out.println("OpenDevice returned : [" + err + "]");

        displayDeviceInfo();
        playWithLights();
    }

    /**
     * Displays the information of the device
     */
    private void displayDeviceInfo() {
        ///////////////////////////////////////////////
        // GetDeviceInfo()
        System.out.println("Call GetDeviceInfo()");
        deviceInfo = new SGDeviceInfoParam();
        err = sgfplib.GetDeviceInfo(deviceInfo);
        System.out.println("GetDeviceInfo returned : [" + err + "]");
        System.out.println("\tdeviceInfo.DeviceSN:    [" + new String(deviceInfo.deviceSN()) + "]");
        System.out.println("\tdeviceInfo.Brightness:  [" + deviceInfo.brightness + "]");
        System.out.println("\tdeviceInfo.ComPort:     [" + deviceInfo.comPort + "]");
        System.out.println("\tdeviceInfo.ComSpeed:    [" + deviceInfo.comSpeed + "]");
        System.out.println("\tdeviceInfo.Contrast:    [" + deviceInfo.contrast + "]");
        System.out.println("\tdeviceInfo.DeviceID:    [" + deviceInfo.deviceID + "]");
        System.out.println("\tdeviceInfo.FWVersion:   [" + deviceInfo.FWVersion + "]");
        System.out.println("\tdeviceInfo.Gain:        [" + deviceInfo.gain + "]");
        System.out.println("\tdeviceInfo.ImageDPI:    [" + deviceInfo.imageDPI + "]");
        System.out.println("\tdeviceInfo.ImageHeight: [" + deviceInfo.imageHeight + "]");
        System.out.println("\tdeviceInfo.ImageWidth:  [" + deviceInfo.imageWidth + "]");
    }

    /**
     * Turns on the light and turns it back off
     */
    private void playWithLights() {
        turnOnLight();
        sleep(1);
        turnOffLight();
    }

    public void sleep(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Turn on the light
     */
    private void turnOnLight() {
        System.out.println("Call SetLedOn(true)");
        err = sgfplib.SetLedOn(true);
        System.out.println("SetLedOn returned : [" + err + "]");
    }

    /**
     * Turn off the light
     */
    private void turnOffLight() {
        System.out.println("Call SetLedOn(false)");
        err = sgfplib.SetLedOn(false);
        System.out.println("SetLedOn returned : [" + err + "]");
    }

    /**
     * Closes the device and then closes the device Libraries afterwards
     */
    public void stop() {
        closeDevice();
        closeDeviceLibrary();
    }

    /**
     * closes the device
     */
    private void closeDevice() {
        System.out.println("Call CloseDevice()");
        err = sgfplib.CloseDevice();
        System.out.println("CloseDevice returned : [" + err + "]");
    }

    /**
     * Closes the device libraries (DLL)
     */
    private void closeDeviceLibrary() {
        // Close JSGFPLib native library
        System.out.println("Call Close()");
        sgfplib.Close();
        System.out.println("Close returned : [" + err + "]");

        sgfplib = null;
        err = 0L;

    }

    public boolean match(byte[] image1, byte[] image2) {
        ///////////////////////////////////////////////
        // Set Template format SG400
        System.out.println("Call SetTemplateFormat(SG400)");
        err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
        System.out.println("SetTemplateFormat returned : [" + err + "]");

        ///////////////////////////////////////////////
        // Get Max Template Size for SG400
        System.out.println("Call GetMaxTemplateSize()");
        err = sgfplib.GetMaxTemplateSize(maxSize);
        System.out.println("GetMaxTemplateSize returned : [" + err + "]");
        System.out.println("Max SG400 Template Size is : [" + maxSize[0] + "]");

        byte[] templatedImage1 = new byte[maxSize[0]];
        byte[] templatedImage2 = new byte[maxSize[0]];

        ///////////////////////////////////////////////
        // Create SG400 Template for Finger 1
        System.out.println("Call CreateTemplate()");
        err = sgfplib.CreateTemplate(new SGFingerInfo(), image1, templatedImage1);
        err = sgfplib.CreateTemplate(new SGFingerInfo(), image2, templatedImage2);
        System.out.println("CreateTemplate returned : [" + err + "]");


        score[0] = 0;
        err = sgfplib.GetMatchingScore(templatedImage1, templatedImage2, score);

        System.out.println("Matching Error: " + err);
        System.out.println("Matching score: " + score[0]);

        if (score[0] > 120) {
            return true;
        }

        return false;
    }

}
