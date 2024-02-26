package ggs.ggs.member;

public interface MailService {
    int sendMail(String mail);

    void sendIdMail(String mail, String id);

    CharSequence sendPwMail(String mail);
}
