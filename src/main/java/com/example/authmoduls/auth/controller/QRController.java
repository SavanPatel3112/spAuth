package com.example.authmoduls.auth.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.awt.image.BufferedImage;
@RestController
@RequestMapping("/users")
public class QRController{
    // get mapping and produce result as PNG image
    @GetMapping(path = "/api/v1/getQrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage generateQRCodeImage(@RequestParam String url) throws Exception {
        //QRCode generator logic
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
