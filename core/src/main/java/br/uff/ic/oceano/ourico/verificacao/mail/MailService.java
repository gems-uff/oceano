/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.verificacao.mail;

/**
 *
 * @author marapao
 */
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


//clase que retorna uma autenticacao para ser enviada e verificada pelo servidor smtp
public class MailService {

	private String mailSMTPServer;
	private String mailSMTPServerPort;

        /*
	 * quando instanciar um Objeto ja sera atribuido o servidor SMTP do GMAIL
	 * e a porta usada por ele
	 */
	public MailService() { //Para o GMAIL
		mailSMTPServer = "smtp.gmail.com";
		mailSMTPServerPort = "465";
	}
	/*
	 * caso queira mudar o servidor e a porta, so enviar para o contrutor
	 * os valor como string
	 */
	public MailService(String mailSMTPServer, String mailSMTPServerPort) { //Para outro Servidor
		this.mailSMTPServer = mailSMTPServer;
		this.mailSMTPServerPort = mailSMTPServerPort;
	}

	public void sendMail(String from, String to, String subject, String message, String login, String senha) {

		Properties props = new Properties();

                // quem estiver utilizando um SERVIDOR PROXY descomente essa parte e atribua as propriedades do SERVIDOR PROXY utilizado
                /*
                props.setProperty("proxySet","true");
                props.setProperty("socksProxyHost","192.168.155.1"); // IP do Servidor Proxy
                props.setProperty("socksProxyPort","1080");  // Porta do servidor Proxy
                */

		props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", mailSMTPServer); //server SMTP do GMAIL
		props.put("mail.smtp.auth", "true"); //ativa autenticacao
		props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", mailSMTPServerPort); //porta
		props.put("mail.smtp.socketFactory.port", mailSMTPServerPort); //mesma porta para o socket
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		//Cria um autenticador que sera usado a seguir
		SimpleAuth auth = null;

                //colocar email e senha quando for testar.
		auth = new SimpleAuth (login, senha);

		//Session - objeto que ira realizar a conexão com o servidor
		/*Como há necessidade de autenticação é criada uma autenticacao que
		 * é responsavel por solicitar e retornar o usuário e senha para
		 * autenticação */
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false); //Habilita o LOG das ações executadas durante o envio do email

		//Objeto que contém a mensagem
		Message msg = new MimeMessage(session);

		try {
			//Setando o destinatário
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//Setando a origem do email
			msg.setFrom(new InternetAddress(from));
			//Setando o assunto
			msg.setSubject(subject);
			//Setando o conteúdo/corpo do email
			msg.setContent(message,"text/plain");

		} catch (Exception e) {
			System.out.println(">> Erro: Completar Mensagem");
			e.printStackTrace();
		}

		//Objeto encarregado de enviar os dados para o email
		Transport tr;
		try {
			tr = session.getTransport("smtp"); //define smtp para transporte
			/*
			 *  1 - define o servidor smtp
			 *  2 - seu nome de usuario do gmail
			 *  3 - sua senha do gmail
			 */
			tr.connect(mailSMTPServer, login, senha);
			msg.saveChanges(); // don't forget this
			//envio da mensagem
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(">> Erro: Envio Mensagem");
			e.printStackTrace();
		}

	}
}


class SimpleAuth extends Authenticator {
	public String username = null;
	public String password = null;


	public SimpleAuth(String user, String pwd) {
		username = user;
		password = pwd;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication (username,password);
	}
}
