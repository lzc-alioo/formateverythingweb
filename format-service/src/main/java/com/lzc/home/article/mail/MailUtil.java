package com.lzc.home.article.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 项目名称：springmvc_hibernate
 * 类名称：MailUtil
 * 类描述：
 * 创建人：Cherish
 * 联系方式：785427346@qq.com
 * 创建时间：2016年4月22日 下午3:51:48
 *
 * @version 1.0
 */
public class MailUtil {

    private static final String HOST = "smtp.163.com";
    private static final String SMTP = "smtp";
    private static final String USERNAME = "lzc_alioo@163.com";
    private static final String PASSWORD = "alioo1028";
    private static final int PORT = 25;//587/465//25
    private static final String DEFAULTENCODING = "UTF-8";

    private static JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

    private static Properties prop = new Properties();

    static {
        // 设定mail server
        senderImpl.setHost(HOST);
        senderImpl.setProtocol(SMTP);
        senderImpl.setUsername(USERNAME);
        senderImpl.setPassword(PASSWORD);
        senderImpl.setPort(PORT);
        senderImpl.setDefaultEncoding(DEFAULTENCODING);

        // 设定properties
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        //设置调试模式可以在控制台查看发送过程
        prop.put("mail.debug", "true");

        senderImpl.setJavaMailProperties(prop);
    }

    public static void main(String args[]) {
        // 设置收件人，寄件人 用数组发送多个邮件
//      String[] array = new String[] {"88888@qq.com","666666@qq.com","999999999@qq.com",USERNAME};
        String[] array = new String[]{USERNAME};
        String subject = "Cherish内嵌图片、音乐的邮件";

//      StringBuffer sb = new StringBuffer();
//      try {
//          URL url = new URL("http://www.imooc.com/");//http://android-studio.org/
//
//          URLConnection conn = url.openConnection();
//          InputStream is = conn.getInputStream();
//
//          BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//          String string = null;
//          while ((string = reader.readLine()) != null) {
//              sb.append(string);
//          }
//
//          //System.out.println(sb.toString());
//
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//
//      boolean result = htmlMail(array, subject, sb.toString());

        String filePath = "/Users/didi/Downloads/IMG_1223.JPG";
        String html = "<html><head>" +
                "</head><body>" +
                "<audio src='http://m10.music.126.net/20160422225433/25b43b999bcdaf3425b9194514340596/ymusic/8c94/b9af/69e3/7ebe35b8e00154120822550b21b0c9c5.mp3' autoplay='autoplay' controls='controls' loop='-1'>爱你</audio>" +
                "<h1>Hello,Nice to meet you!</h1>" +
                "<span style='color:red;font-size:36px;'>并摸了一把你的小奶</span>" +
                "<img src='cid:javaxmail.png'>" +
                "</body></html>";
        boolean result = inlineFileMail(array, subject, html, filePath);

        if (result) {
            System.out.println("发送邮件成功。。。。");
        }


    }

    /**
     * 发送简单邮件
     *
     * @param to      收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public static boolean singleMail(String to, String subject, String content) {
        String[] array = new String[]{to};
        return singleMail(array, subject, content);
    }


    /**
     * 发送简单文本邮件
     *
     * @param to      收件人邮箱数组
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public static boolean singleMail(String[] to, String subject, String content) {
        boolean result = true;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        mailMessage.setTo(to);
        mailMessage.setFrom(USERNAME);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        // 发送邮件
        try {
            senderImpl.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    /**
     * 发送html邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param html    html代码
     * @return
     */
    public static boolean htmlMail(String[] to, String subject, String html) {
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

        try {
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件
            messageHelper.setText(html, true);

            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送内嵌图片的邮件   （cid:资源名）
     *
     * @param to      收件人邮箱
     * @param subject 主题
     * @param html    html代码
     * @return
     */
    public static boolean inlineFileMail(String[] to, String subject, String html, String filePath) {
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();
        try {
            //设置true开启嵌入图片的功能
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件
            messageHelper.setText(html, true);


            if (filePath != null) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                messageHelper.addInline(file.getFilename(), file);
            }


            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param html
     * @param filePath
     * @return
     */
    public static boolean attachedFileMail(String[] to, String subject, String html, String filePath) {
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();

        try {
            // multipart模式 为true时发送附件 可以设置html格式
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件
            messageHelper.setText(html, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            // 这里的方法调用和插入图片是不同的。
            messageHelper.addAttachment(file.getFilename(), file);

            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
