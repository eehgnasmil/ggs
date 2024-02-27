package ggs.ggs.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "jesuskisd5@gmail.com";
    private static int number;
    private static String ResetPw;

    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public static void createPw(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        ResetPw = generatedString;
    }


    //이메일 인증번호 발송
    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    //이메일 아이디 발송
    public MimeMessage CreateIdMail(String mail, String id){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Hyeok's 아이디 찾기");
            String body = "";
            body += "<h3>" + "요청하신 메일의 아이디 입니다." + "</h3>";
            body += "<h1>" + id + "</h1>";
            body += "<h3>" + "사이트로 돌아가 로그인 하세요." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }
    //이메일 초기화 비밀번호 발송
    public MimeMessage CreatePwMail(String mail){
        createPw();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Hyeok's 비밀번호 찾기");
            String body = "";
            body += "<h3>" + "패스워드" + "</h3>";
            body += "<h1>" + ResetPw + "</h1>";
            body += "<h3>" + "사이트로 돌아가 로그인 하세요." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail){
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }

    public void sendIdMail(String mail, String id){
        MimeMessage message = CreateIdMail(mail, id);
        javaMailSender.send(message);
    }

    public String sendPwMail(String mail) {
        MimeMessage message = CreatePwMail(mail);
        javaMailSender.send(message);
        return ResetPw;
    }
}
