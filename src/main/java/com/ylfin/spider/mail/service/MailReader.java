package com.ylfin.spider.mail.service;


import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.SortTerm;
import com.ylfin.spider.mail.vo.MailContent;
import com.ylfin.spider.register.vo.bean.MailBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * @author: godslhand
 * @date: 2019/2/17
 * @description: 1小时能读取 1200封邮件,解析200ms一封
 */
@Slf4j
public class MailReader {


    public static final String HOST = "imap.gamesheaven.tech";
    public static final String PROTOCOL = "imap";
    public static final String PORT = "143";

    public static List<MailContent> searchMail(MailBean mailBean, List<String> keyWords){
        SearchTerm searchTerm = null;
        List<SearchTerm> orList= new ArrayList<>();
        for (String keyWord :keyWords){
            SearchTerm[] items = { new SubjectTerm(keyWord),new BodyTerm(keyWord),new FromStringTerm(keyWord)};
            orList.add(new OrTerm(items));
        }
        if(!CollectionUtils.isEmpty(orList)){
            SearchTerm[] items = new SearchTerm[orList.size()];
            searchTerm = new AndTerm(orList.toArray(items));
        }
        return readMail(mailBean,searchTerm);
    }
    public static List<MailContent> searchMail(MailBean mailBean, MailContent queryContent){
        SearchTerm searchTerm = null;
        List<SearchTerm> andList= new ArrayList<>();
        if(StringUtils.isNotBlank(queryContent.getSubject())){
            andList.add(new SubjectTerm(queryContent.getSubject()));
        }
        if(StringUtils.isNotBlank(queryContent.getContent())){
            andList.add(new BodyTerm(queryContent.getContent()));
        }
        if(StringUtils.isNotBlank(queryContent.getSender())){
            andList.add(new FromStringTerm(queryContent.getSender()));
        }
        if(queryContent.getSendDate()!=null){
            andList.add(new SentDateTerm(ComparisonTerm.GE ,queryContent.getSendDate()));
        }
        if(andList.size()>0){
            SearchTerm[] items = new SearchTerm[andList.size()];
            searchTerm = new AndTerm(andList.toArray(items));
        }
        return readMail(mailBean,searchTerm);
    }

    public static List<MailContent> readMail(MailBean mailBean,SearchTerm searchTerm) {

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", PROTOCOL);
        props.setProperty("mail.imap.host", HOST);
        props.setProperty("mail.imap.port", PORT);
        Session session = Session.getDefaultInstance(props);
        Store store = null;
        Folder folder = null;
        List<MailContent> list = null;
        try {
            store = session.getStore();
            store.connect(HOST, mailBean.getEmail(), mailBean.getPassword());
            folder = store.getFolder("INBOX");
            if(folder!=null){
                IMAPFolder imapFolder = (IMAPFolder) folder;
                imapFolder.open(Folder.READ_WRITE);
                System.out.println("邮件总数：" + imapFolder.getMessageCount());
                int size =imapFolder.getMessageCount();
//                int start =(size-10)<=0?1:(size-10);
                //由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount()得到的是收件箱的邮件总数
//                System.out.println("未读邮件数：" + imapFolder.getUnreadMessageCount());
                //由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
                System.out.println("删除邮件数：" + imapFolder.getDeletedMessageCount());
                System.out.println("新邮件数：" + imapFolder.getNewMessageCount());
                SortTerm[] term = new SortTerm[1];
                term[0] = SortTerm.DATE;
                Long start =System.currentTimeMillis();
                Message[] messages =imapFolder.getSortedMessages(term,searchTerm);
                Long end = System.currentTimeMillis();
                System.out.println("查询消耗时间："+(end -start)+" 毫秒");
                System.out.println("查询结果条数："+messages.length);
                list= MailReader.getMessages( messages, null);
                System.out.println("解析消耗时间："+(System.currentTimeMillis()-end)+" 毫秒");
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
        return list;
    }

    public static List<MailContent> getMessages(Message[] messages,  Predicate<MailContent> predicate) throws MessagingException, IOException {
        if (messages.length < 1 || messages == null) {
            System.out.println("没有邮件");
        } else {
            List<MailContent> all = new ArrayList<>();
            for (int i = messages.length-1; i >=0; i--) {
                MailContent content = new MailContent();
                MimeMessage each = (MimeMessage) messages[i];
                System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件,id:"+each.getSubject()+"-------------------- ");
                String subject = getSubject(each);
                content.setSubject(subject);
                content.setSender(getFrom(each));
                content.setSendDate(each.getSentDate());
                StringBuffer sb = new StringBuffer(30);
                getMailTextContent(each, sb);
                content.setContent(sb.toString());
                if(predicate==null){
                    all.add(content);
                    continue;
                }
                if(predicate.test(content)) {
                    System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件【"+subject+"】完成-------------------- ");
                    all.add(content);
                    continue;
                }
                System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件【"+subject+"】完成,未命中-------------------- ");
            }
            return  all;

        }
        return null;
    }


    /**
     * 解析邮件
     *
     * @param messages
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    public static MailContent parseMessages(Message[] messages, MailContent content, Predicate<MailContent> predicate) throws MessagingException, IOException {
        if (messages.length < 1 || messages == null) {
            System.out.println("没有邮件");
        } else {
            for (int i = messages.length-1; i >=0; i--) {
                MimeMessage each = (MimeMessage) messages[i];
                System.out.println("------------------解析第" + each.getMessageNumber() + "封邮件,id:"+each.getMessageID()+"-------------------- ");
                String subject = getSubject(each);
                content.setSubject(subject);
                content.setSender(getFrom(each));
                StringBuffer sb = new StringBuffer(30);
                getMailTextContent(each, sb);
                content.setContent(sb.toString());
                if(predicate!=null&&predicate.test(content)) {
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
    private static void getMailTextContent(Part part, StringBuffer content)
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
    private static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = msg.getFrom();
        StringBuilder from = new StringBuilder();
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
    private static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }


    public static void main(String[] args) {

    }

}
