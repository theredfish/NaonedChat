package naoned.sil.lp.naonedchat.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.bean.ContactList;
import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.listeners.chat.NaonedChatManagerListener;
import naoned.sil.lp.naonedchat.listeners.chat.MessageListener;
import naoned.sil.lp.naonedchat.listeners.contact.SubscriptionListener;

/**
 * Created by ACHP on 22/01/2016.
 */
public class Connection {

    private XMPPTCPConnection con;
    private static Connection connection;
    private Handler mHandler;
    private ChatManager chatManager;

    private Connection(){
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setHost("5.135.145.225");
        configBuilder.setServiceName("5.135.145.225");
        configBuilder.setPort(5222);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        con = new XMPPTCPConnection(configBuilder.build());
    }

    public static Connection getInstance() {
        if (connection == null) {
            connection = new Connection();
        }

        return connection;
    }

    public XMPPTCPConnection getConnection() {
        return this.con;
    }

    public boolean connect() {

        try {
            con.connect();
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public User getUser(String username) {
        // Here we want the BareJid without /Smack or /Spark client
        if (username.contains("/")) {
            username = username.split("/")[0];
        }

        User user;

        try {
            user = new User(VCardManager.getInstanceFor(con).loadVCard(username));
            return  user;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean login(String username, String password){
        try {
            con.login(username, password);
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();

            return false;
        }

        listenForFile();
        return true;
    }

    public void listenForFile(){
        // Create the file transfer manager
        final FileTransferManager manager = FileTransferManager.getInstanceFor(this.con);
        // Create the listener
        manager.addFileTransferListener(new FileTransferListener() {
            public void fileTransferRequest(FileTransferRequest request) {
                // Check to see if the request should be accepted


                Log.d("FILE", " NEW FILE");
                // Accept it
                IncomingFileTransfer transfer = request.accept();
                try {
                    InputStream inputStream = transfer.recieveFile();
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                    ContactList.getInstance().newBitmap(bmp, UserUtil.cleanUserJid(request.getRequestor()));

                    Log.d("bitmap to string", bmp.toString());
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void listenForChat(MessageListener myMessageListener) {
        this.chatManager = ChatManager.getInstanceFor(con);
        this.chatManager.addChatListener(new NaonedChatManagerListener(myMessageListener));
    }

    public void sendMessage(String friendUsername, Message message) {
        try {
            Chat chat = ChatManager.getInstanceFor(con).createChat(friendUsername);
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        ContactList.getInstance().sendMessage(message.getTo(), message);
    }



    private void connectToGoogle() {
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
            public void send(PlainStreamElement auth) throws SmackException.NotConnectedException
            {
                if (auth instanceof SaslStreamElements.AuthMechanism) {
                    final XmlStringBuilder xml = new XmlStringBuilder();

                    xml.halfOpenElement(SaslStreamElements.AuthMechanism.ELEMENT)
                            .xmlnsAttribute(SaslStreamElements.NAMESPACE)
                            .attribute("mechanism", "X-OAUTH2")
                            .attribute("auth:naoned.sil.lp.naonedchat.service", "oauth2")
                            .attribute("xmlns:auth", "http://www.google.com/talk/protocol/auth")
                            .rightAngleBracket()
                            .optAppend(Base64.encodeToString(StringUtils.toBytes("\0" + googleLogin + "\0" + token)))
                            .closeElement(SaslStreamElements.AuthMechanism.ELEMENT);

                    super.send(new PlainStreamElement() {
                        public String toXML()
                        {
                            return xml.toString();
                        }
                    });
                } else {
                    super.send(auth);
                }
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

    public void disconnect(){
        con.disconnect();
    }



}

//Créer une classe connection qui est un singleton

//Class utils pour se connecter
//getConnection()
//Connect()
//Login

