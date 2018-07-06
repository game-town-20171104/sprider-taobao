package com.ylfin.spider.component.checker;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.SortTerm;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;

@Slf4j
public class JavaMailChecker implements Checker {
    public static final String HOST = "imap.gamesheaven.tech";//服务器地址
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
            case sony:
                throw new RuntimeException("暂未实现");
            default:
                break;
        }
        return code;
    }





    private MailContent readMail(MailBean mailBean) {

        MailContent content = new MailContent();
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", PROTOCOL);
        props.setProperty("mail.imap.host", HOST);
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
                this.parseMessages(imapFolder.getMessages(start,size), content);
            }


        } catch (Exception e) {
            log.error("邮件读取失败：{}",mailBean);
            e.printStackTrace();
        } finally {
            try {
                folder.close(false);
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
        return content;
    }

    private boolean condition(String subject) {
        if (subject.contains(NINTENDO_CODE)) {
            return true;
        }
        if (subject.contains(SONY_CODE)) {
            return true;
        }
        return false;
    }

    /**
     * 解析邮件
     *
     * @param messages
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    public MailContent parseMessages(Message[] messages, MailContent content) throws MessagingException, IOException {
        if (messages.length < 1 || messages == null) {
            System.out.println("没有邮件");
        } else {
            for (int i = messages.length-1; i >=0; i--) {
                MimeMessage each = (MimeMessage) messages[i];
                System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件-------------------- ");
                String subject = getSubject(each);
                content.setSubject(subject);
                content.setSender(getFrom(each));
                StringBuffer sb = new StringBuffer(30);
                getMailTextContent(each, sb);
                content.setContent(sb.toString());
                if (condition(subject)) {
                    System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件【"+subject+"】完成-------------------- ");
                    return content;
                }
                System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件【"+subject+"】完成,未命中-------------------- ");

            }
        }
        return null;
    }


    /**
     * 解析邮件内容
     *
     * @param part
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    private void getMailTextContent(Part part, StringBuffer content)
            throws MessagingException, IOException {
        // 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }


    /**
     * 获取发件人
     *
     * @param msg 邮件
     * @return 姓名 <邮箱地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = msg.getFrom();
        StringBuilder from = new StringBuilder("");
        if (froms.length < 1) {
            from.append("没有收件人");
        } else {
            InternetAddress address = (InternetAddress) froms[0];
            from.append((address.getPersonal() == null) ? "" : MimeUtility.decodeText(address.getPersonal()));
            from.append(" <").append(address.getAddress()).append(">");
        }

        return from.toString();
    }

    /**
     * 获得邮件主题
     *
     * @param msg 邮件
     * @return 解码后的邮件主题
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    @Data
    class MailContent {
        private String subject;
        private String sender;
        private String content;
        private Date sendDate;

    }

    public static void main(String[] args) {
        MailBean mailBean = new MailBean();
        mailBean.setEmail("songfl5@gamesheaven.tech");
        mailBean.setPassword("asd123");


        Checker mailChecker = new JavaMailChecker();
        System.out.println(mailChecker.getCheckCode(mailBean, RegisterType.nintendo));
    }
}
