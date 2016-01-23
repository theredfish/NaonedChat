package service;

import android.os.AsyncTask;
import android.util.Log;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smack.util.stringencoder.Base64;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by ACHP on 22/01/2016.
 */
public class XmppService {

    private XMPPTCPConnection con;
    private String login;
    private String password;

    public void sendMessage(String friendUsername, String message){
        Log.d("MESSAGE", "Message a envoyer "+ message+" friendUsername:" + friendUsername);
           // Start a new conversation with John Doe and send him a message.
        Chat chat = ChatManager.getInstanceFor(con).createChat("test2@naonedchat", new ChatMessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                Log.d("MESSAGE", "Message received"+message);
            }
        });
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        // Disconnect from the server
    }

    public void setAuthenticationCredentials(String login, String password){
            this.login = login;
            this.password = password;
    }
    private void connectToGoogle(){
        final String googleLogin ="";
        final String token ="";
            // insert cool stuff with authPreferences.getToken()
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setHost("talk.google.com");
            configBuilder.setPort(5222);
            configBuilder.setServiceName("gmail.com");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);



            final AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build())
            {
                @Override
                public void send(PlainStreamElement auth) throws SmackException.NotConnectedException
                {
                    if(auth instanceof SaslStreamElements.AuthMechanism)
                    {
                        final XmlStringBuilder xml = new XmlStringBuilder();
                        xml.halfOpenElement(SaslStreamElements.AuthMechanism.ELEMENT)
                                .xmlnsAttribute(SaslStreamElements.NAMESPACE)
                                .attribute("mechanism", "X-OAUTH2")
                                .attribute("auth:service", "oauth2")
                                .attribute("xmlns:auth", "http://www.google.com/talk/protocol/auth")
                                .rightAngleBracket()
                                .optAppend(Base64.encodeToString(StringUtils.toBytes("\0" + googleLogin + "\0" + token)))
                                .closeElement(SaslStreamElements.AuthMechanism.ELEMENT);
                        super.send(new PlainStreamElement()
                        {
                            @Override
                            public String toXML()
                            {
                                return xml.toString();
                            }
                        });
                    }
                    else super.send(auth);
                }
            };


            try {
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }

            try {
                connection.login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    public boolean connectToNaonedChat(){
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setHost("5.135.145.225");
        configBuilder.setServiceName("5.135.145.225");
        configBuilder.setPort(5222);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        this.con = new XMPPTCPConnection(configBuilder.build());

        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        // Connect to the server
        try {
            con.connect();
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
        Log.d("OK", "Connexion ok");

        try {
            con.login(this.login, this.password);
        } catch (SmackException | IOException | XMPPException e) {
        e.printStackTrace();
        return false;
        }
        Log.d("OK", "login ok with :"+ this.login+", "+  this.login);
        return true;


    }

    public void disconnect(){
        this.con.disconnect();
    }


}
