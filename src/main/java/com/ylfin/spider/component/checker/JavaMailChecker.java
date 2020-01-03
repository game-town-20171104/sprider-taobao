package com.ylfin.spider.component.checker;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.SortTerm;
import com.ylfin.spider.mail.vo.MailContent;
import com.ylfin.spider.mail.service.MailReader;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import java.util.Properties;
import java.util.function.Predicate;

@Slf4j
public class JavaMailChecker implements Checker {
    public static final String HOST = "imap.";//服务器地址
    public static final String PROTOCOL = "imap";//协议
    public static final String PORT = "143";//pop默认端口为110

    private static final String NINTENDO_CODE = "Nintendo Account";
    private static final String SONY_CODE = "账户登记成功确认";

    @Override
    public String getCheckCode(MailBean mailBean, RegisterType registerType) {
        MailContent content = readMail(mailBean);
        String code = null;
        switch (registerType) {
            case nintendo:
                String str = content.getSubject().split("]|】")[0];
                code = str.substring(1);
                break;
            case nintendoPwdModify:
                String url = content.getContent();
                if(StringUtils.isBlank(url)){
                    throw new RuntimeException("邮件内容读取失败");
                }
                code = "https"+StringUtils.substringBefore(StringUtils.substringAfter(url,"https"),"\n");
                break;
            case sony:
                throw new RuntimeException("暂未实现");
            default:
                break;
        }
        return code;
    }





    private MailContent readMail(MailBean mailBean) {

        String hostAddress = HOST+ subDomain(mailBean.getEmail());

        MailContent content = new MailContent();
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", PROTOCOL);
        props.setProperty("mail.imap.host", hostAddress);
        props.setProperty("mail.imap.port", PORT);

        Session session = Session.getDefaultInstance(props);
        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore();
            store.connect(HOST, mailBean.getEmail(), mailBean.getPassword());

			/*
             * POP3 supports only a single folder named "INBOX".
			 * POP协议的话，这里只能是INBOX
			 */
            folder = store.getFolder("INBOX");
            //以只读方式打开收件箱
            if(folder!=null){
                IMAPFolder imapFolder = (IMAPFolder) folder;
                imapFolder.open(Folder.READ_WRITE);
                System.out.println("邮件总数：" + imapFolder.getMessageCount());
                int size =imapFolder.getMessageCount();
                int start =(size-10)<=0?1:(size-10);
                //由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount()得到的是收件箱的邮件总数
                System.out.println("未读邮件数：" + imapFolder.getUnreadMessageCount());
                //由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
                System.out.println("删除邮件数：" + imapFolder.getDeletedMessageCount());
                System.out.println("新邮件数：" + imapFolder.getNewMessageCount());
                SortTerm[] term = new SortTerm[1];
                term[0] = SortTerm.DATE;
//                this.parseMessages(imapFolder.getMessages(start,size), content);
                MailReader.parseMessages(imapFolder.getMessages(start,size), content,new Condition());
            }


        } catch (Exception e) {
            log.error("邮件读取失败：{}",mailBean);
            e.printStackTrace();
        } finally {
            try {
                if(folder!=null){
                    folder.close(false);
                }
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
        return content;
    }

    class Condition implements Predicate<MailContent> {
        @Override
        public boolean test(MailContent content) {
            String subject = content.getSubject();
            if (subject.contains(NINTENDO_CODE)) {
                return true;
            }
            if (subject.contains(SONY_CODE)) {
                return true;
            }
            return false;
        }
    }

    private static String subDomain(String mail){
        if(mail.indexOf("@")<1){
            throw new RuntimeException("邮件格式不正确");
        }
        return mail.split("@")[1];
    }



    public static void main(String[] args) {
        MailBean mailBean = new MailBean();
        mailBean.setEmail("sd00456@gamesheaven.tech");
        mailBean.setPassword("A2y730d1");


        Checker mailChecker = new JavaMailChecker();
        System.out.println(mailChecker.getCheckCode(mailBean, RegisterType.nintendoPwdModify));
    }
}
