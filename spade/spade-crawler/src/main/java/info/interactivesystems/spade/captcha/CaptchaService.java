package info.interactivesystems.spade.captcha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Service
public class CaptchaService {

    @Resource
    private TiffConverter tiffConverter;

    @Resource
    private Tesseract tesseract;

    public String getValueFromCaptcha(HtmlPage page) {
        try {
            HtmlImage image = page.getFirstByXPath("//img");
            File imageFile = new File(FileNamer.getOutputName("jpeg"));
            image.saveAs(imageFile);

            String result = tesseract.recognizeCaptcha(imageFile);

            return result;

        } catch (NullPointerException e) {
            log.error("TIFF does not exist");
        } catch (IOException e) {
            log.error("Could not save/find the image file from file system", e);
        }
        return null;
    }

    public String getValueForCaptcha(URL captcha) {
        try {
            InputStream captchaStream = captcha.openStream();

            // TODO Maybe build an OS Switch, see TiffConverter for more informations.
            // BufferedImage imageCaptcha = ImageIO.read(captchaStream);
            // File captchaImage = tiffConverter.saveTiff(getOutputName("tiff"), imageCaptcha);

            File captchaImage = tiffConverter.saveAsJPEG(FileNamer.getOutputName("jpeg"), captchaStream);
            String result = tesseract.recognizeCaptcha(captchaImage);

            return result;

        } catch (MalformedURLException e) {
            log.error("URL to Captcha is malformed", e);
        } catch (IOException e) {
            log.error("Could not find or download the Captcha", e);
        } catch (NullPointerException e) {
            log.error("TIFF does not exist");
        }
        return null;
    }

    public String getValueForCaptcha(String captcha) {
        try {
            URL url = new URL(captcha);
            return getValueForCaptcha(url);
        } catch (MalformedURLException e) {
            log.error("Could not convert '{}' into URL Object", captcha, e);
        }
        return null;
    }
}