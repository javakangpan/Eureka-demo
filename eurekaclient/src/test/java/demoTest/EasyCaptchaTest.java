package demoTest;

import com.wf.captcha.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * https://gitee.com/whvse/EasyCaptcha
 * ==>类型	描述
 * TYPE_DEFAULT	数字和字母混合
 * TYPE_ONLY_NUMBER	纯数字
 * TYPE_ONLY_CHAR	纯字母
 * TYPE_ONLY_UPPER	纯大写字母
 * TYPE_ONLY_LOWER	纯小写字母
 * TYPE_NUM_AND_UPPER	数字和大写字母
 *
 * 使用方法：
 * SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
 * captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
 * 只有SpecCaptcha和GifCaptcha设置才有效果。
 *
 * ==>字体使用方法：Captcha.FONT_1 -> Captcha.FONT_10
 * SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
 * // 设置内置字体
 * captcha.setFont(Captcha.FONT_1);
 * // 设置系统字体
 * captcha.setFont(new Font("楷体", Font.PLAIN, 28));
 *
 * ==>输出base64编码
 * SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
 * 输出base64编码
 * specCaptcha.toBase64();
 * // 如果不想要base64的头部data:image/png;base64,
 * specCaptcha.toBase64("");  // 加一个空的参数即可
 */
public class EasyCaptchaTest {
    @Test
    public void test() throws Exception{
        FileOutputStream outputStream = new FileOutputStream(new File("../captcha.png"));
        // png类型
        SpecCaptcha captcha = new SpecCaptcha(130, 48);
        captcha.text();  // 获取验证码的字符
        captcha.textChar();  // 获取验证码的字符数组

        // gif类型
        //GifCaptcha captcha = new GifCaptcha(130, 48);

        // 中文类型
        //ChineseCaptcha captcha = new ChineseCaptcha(130, 48);

        // 中文gif类型
        //ChineseGifCaptcha captcha = new ChineseGifCaptcha(130, 48);

        // 算术类型
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        //captcha.setLen(3);  // 几位数运算，默认是两位
        //captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        //captcha.text();  // 获取运算的结果：5

        captcha.out(outputStream);  // 输出验证码
    }
}
