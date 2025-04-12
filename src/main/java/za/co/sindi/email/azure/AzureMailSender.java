package za.co.sindi.email.azure;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;

import za.co.sindi.email.MailSender;
import za.co.sindi.email.exception.MailSendException;

/**
 * @author Buhake Sindi
 * @since 01 March 2025
 */
public class AzureMailSender implements MailSender<AzureEmailMessage, AzureEmailMessage> {
	
	private EmailClient emailClient;

	/**
	 * @param builder
	 */
	public AzureMailSender(EmailClientBuilder builder) {
		super();
		this.emailClient = builder.buildClient();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailSender#createPlaintextMailMessage()
	 */
	@Override
	public AzureEmailMessage createPlaintextMailMessage() {
		// TODO Auto-generated method stub
		return new AzureEmailMessage();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailSender#createMultipartMailMessage()
	 */
	@Override
	public AzureEmailMessage createMultipartMailMessage() {
		// TODO Auto-generated method stub
		return createPlaintextMailMessage();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.email.MailSender#send(za.co.sindi.email.MailMessage[])
	 */
	@Override
	public void send(AzureEmailMessage... mailMessages) throws MailSendException {
		// TODO Auto-generated method stub
		for (AzureEmailMessage mailMessage : mailMessages) {
			SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(mailMessage.getMessage());
			PollResponse<EmailSendResult> response = poller.waitForCompletion();
			LongRunningOperationStatus status = response.getStatus();
			if (status.isComplete() && !LongRunningOperationStatus.SUCCESSFULLY_COMPLETED.equals(status)) {
				throw new MailSendException("Unable to send mail. Status response: " + status);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}
}
