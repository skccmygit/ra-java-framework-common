package kr.co.skcc.base.com.common.util;

import jakarta.mail.internet.MimeMessage;
import kr.co.skcc.base.com.common.api.dto.emailDto.EmailContent;
import kr.co.skcc.base.com.common.api.dto.emailDto.EmailSender;
import kr.co.skcc.base.com.common.api.dto.emailDto.EmailUser;
import kr.co.skcc.base.com.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailUtil {

    private static String sendMailAddress;

    private static EmailSender emailSender;

    private static JavaMailSender mailSender;

    @Value("${app.email.sendMailAddress:#{null}}")
    public void setSendMailAddress(String value){
        this.sendMailAddress = value;
    }

    @Autowired
    private EmailUtil(EmailSender mailProperties, JavaMailSender mailSender) {
        this.emailSender = mailProperties;
        this.mailSender = mailSender;
    }

    public static String sendMail( String recivedEmail, String reciveCemail, String subj, String strCon) throws Exception {
        String result = "";

        try {
            EmailUser user = new EmailUser();
            EmailContent content = new EmailContent();

            String tempStr;
            String[] emailAddress = {};
            String[] cemailAddress = {};
            String subject;
            String strContent;

            if (recivedEmail == null || "".equals(recivedEmail)) {
                throw new ServiceException("COM.I0020");
            }
            tempStr = recivedEmail;
            emailAddress = tempStr.split(";");

            if (reciveCemail == null || "".equals(reciveCemail)) {

            } else {
                tempStr = reciveCemail;
                cemailAddress = tempStr.split(";");
            }
            subject = subj;
            strContent = strCon;

//            user.setFromEmailAddress(fromEmail); // sendJavaMail 에서 강제 주입
            // 메일주소 리스트
            user.setEmailAddress(emailAddress);
            // 메일 참조 리스트
            user.setCemailAddress(cemailAddress);
            // 메일 제목
            content.setSubject(subject);
            // 메일 내용
            content.setContent(strContent);

            //content.setAttFilePath(attFilePath);
            result = sendJavaMail(content, user);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return result;
        }
        return result;
    }

    public static String sendJavaMail(EmailContent content, EmailUser user) {
        String result = "";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            // 보내는이
            if (user.getFromEmailAddress() != "")       helper.setFrom(sendMailAddress);
            // 이메일
            if (user.getEmailAddress().length != 0)      helper.setTo(user.getEmailAddress());
            // 제목
            helper.setSubject(content.getSubject());
            // 참조
            if (user.getCemailAddress().length > 0 && user.getCemailAddress()[0] != "")      helper.setBcc(user.getCemailAddress());
            // 내용
            if (content.getContent() != "")              helper.setText(content.getContent());

            // 첨부
            //let's attach the infamous windows Sample file (this time copied to c:/)
            //FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
            //helper.addAttachment("CoolImage.jpg", file);
            mailSender.send(message);
            log.info("### SEND email");
            result = "Sucess";
        } catch (Exception e) {
            // simply log it and go on...
            log.info("### FAIL email" + e.getMessage());
            result = "Fail";
        }
        return result;
    }

}
