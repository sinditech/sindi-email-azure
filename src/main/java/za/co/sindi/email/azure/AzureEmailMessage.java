package za.co.sindi.email.azure;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailAttachment;
import com.azure.communication.email.models.EmailMessage;
import com.azure.core.util.BinaryData;

import za.co.sindi.email.AbstractMailMessage;
import za.co.sindi.email.MultipartMailMessage;
import za.co.sindi.email.exception.MailAddressException;
import za.co.sindi.email.exception.MailException;

/**
 * @author Buhake Sindi
 * @since 01 March 2025
 */

public class AzureEmailMessage extends AbstractMailMessage implements MultipartMailMessage {

	private EmailMessage message = new EmailMessage()
						.setHeaders(new LinkedHashMap<>())
						.setToRecipients(new ArrayList<>())
						.setCcRecipients(new ArrayList<>())
						.setBccRecipients(new ArrayList<>())
						.setReplyTo(new ArrayList<>())
						.setAttachments(new ArrayList<>());
	
	/* (non-Javadoc)
	 * @see za.co.sindi.email.MessageHeader#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value)/* throws MailException */ {
		// TODO Auto-generated method stub
		message.getHeaders().put(name, value);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MessageHeader#containsHeader(java.lang.String)
	 */
	@Override
	public boolean containsHeader(String name)/* throws MailException */ {
		// TODO Auto-generated method stub
		return message.getHeaders().containsKey(name);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MessageHeader#getHeader(java.lang.String)
	 */
	@Override
	public String[] getHeader(String name)/* throws MailException */ {
		// TODO Auto-generated method stub
		return new String[] { message.getHeaders().get(name) };
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MessageHeader#removeHeader(java.lang.String)
	 */
	@Override
	public void removeHeader(String name)/* throws MailException */ {
		// TODO Auto-generated method stub
		message.getHeaders().remove(name);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MessageHeader#getHeaderNames()
	 */
	@Override
	public String[] getHeaderNames()/* throws MailException */ {
		// TODO Auto-generated method stub
		Set<String> names = message.getHeaders().keySet();
		return names.toArray(new String[names.size()]);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MultipartMailMessage#attach(java.io.File, java.lang.String)
	 */
	@Override
	public void attach(File file, String description)/* throws MailException */ {
		// TODO Auto-generated method stub
		try {
			BinaryData attachmentContent = BinaryData.fromFile(file.toPath());
			EmailAttachment attachment = new EmailAttachment(file.getName(), determineMimeType(file), attachmentContent);
			message.getAttachments().add(attachment);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MailException(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MultipartMailMessage#attach(java.net.URL, java.lang.String, java.lang.String)
	 */
	@Override
	public void attach(URL url, String name, String description)/* throws MailException */ {
		// TODO Auto-generated method stub
		try {
			BinaryData attachmentContent = BinaryData.fromStream(url.openStream());
			EmailAttachment attachment = new EmailAttachment(name, url.openConnection().getContentType(), attachmentContent);
			message.getAttachments().add(attachment);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MailException(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MultipartMailMessage#embed(java.lang.String, java.io.File, java.lang.String)
	 */

	@Override
	public void embed(String contentID, File file, String description)/* throws MailException */ {
		// TODO Auto-generated method stub
		try {
			BinaryData attachmentContent = BinaryData.fromFile(file.toPath());
			EmailAttachment attachment = new EmailAttachment(file.getName(), determineMimeType(file), attachmentContent).setContentId(contentID);
			message.getAttachments().add(attachment);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MailException(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MultipartMailMessage#embed(java.lang.String, java.net.URL, java.lang.String, java.lang.String)
	 */
	@Override
	public void embed(String contentID, URL url, String name, String description)/* throws MailException */ {
		// TODO Auto-generated method stub
		try {
			BinaryData attachmentContent = BinaryData.fromStream(url.openStream());
			EmailAttachment attachment = new EmailAttachment(name, url.openConnection().getContentType(), attachmentContent).setContentId(contentID);
			message.getAttachments().add(attachment);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MailException(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MultipartMailMessage#setContent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setContent(String content, String subType, String charsetName) {
		// TODO Auto-generated method stub
		if ("plain".equals(subType)) {
			message.setBodyPlainText(content);
		} else if ("html".equals(subType)) {
			message.setBodyHtml(content);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#setFrom(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setFrom(String emailAddress, String displayName, String charsetName) throws MailAddressException {
		// TODO Auto-generated method stub
		message.setSenderAddress(emailAddress);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#addTo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addTo(String emailAddress, String displayName, String charsetName) throws MailAddressException {
		// TODO Auto-generated method stub
		EmailAddress address = new EmailAddress(emailAddress).setDisplayName(displayName);
		message.getToRecipients().add(address);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#addCC(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addCC(String emailAddress, String displayName, String charsetName) throws MailAddressException {
		// TODO Auto-generated method stub
		EmailAddress address = new EmailAddress(emailAddress).setDisplayName(displayName);
		message.getCcRecipients().add(address);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#addBCC(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addBCC(String emailAddress, String displayName, String charsetName) throws MailAddressException {
		// TODO Auto-generated method stub
		EmailAddress address = new EmailAddress(emailAddress).setDisplayName(displayName);
		message.getBccRecipients().add(address);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#addReplyTo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addReplyTo(String emailAddress, String displayName, String charsetName) throws MailAddressException {
		// TODO Auto-generated method stub
		EmailAddress address = new EmailAddress(emailAddress).setDisplayName(displayName);
		message.getReplyTo().add(address);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#getSubject()
	 */
	@Override
	public String getSubject()/* throws MailException */ {
		// TODO Auto-generated method stub
		return message.getSubject();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#setSubject(java.lang.String, java.lang.String)
	 */
	@Override
	public void setSubject(String subject, String charsetName)/* throws MailException */ {
		// TODO Auto-generated method stub
		message.setSubject(subject);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#setText(java.lang.String, java.lang.String)
	 */
	@Override
	public void setText(String text, String charsetName)/* throws MailException */ {
		// TODO Auto-generated method stub
		message.setBodyPlainText(text);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#getSentDate()
	 */
	@Override
	public Date getSentDate()/* throws MailException */ {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#setSentDate(java.util.Date)
	 */
	@Override
	public void setSentDate(Date sentDate)/* throws MailException */ {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailMessage#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(OutputStream out) throws IOException/*, MailException */ {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the message
	 */
	public EmailMessage getMessage() {
		return message;
	}

	private String determineMimeType(final File file) throws IOException {
		String mimeType = Files.probeContentType(file.toPath());
		if (mimeType == null) mimeType = file.toURI().toURL().openConnection().getContentType();
		if (mimeType == null) mimeType = URLConnection.guessContentTypeFromName(file.getName());
		return mimeType;
	}
}
