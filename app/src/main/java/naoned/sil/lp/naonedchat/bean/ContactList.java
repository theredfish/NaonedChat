package naoned.sil.lp.naonedchat.bean;

import android.util.Log;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.LinkedList;

import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.listeners.chat.MessageListener;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 03/02/2016.
 */
public class ContactList  implements naoned.sil.lp.naonedchat.listeners.chat.MessageListener{

    private ArrayList<User> contacts;
    private LinkedList<User> lastContactQueue;
    private static ContactList contactList;

    private ArrayList<MessageListener> messageListeners;

    public void addOnMessageListener(MessageListener messageListener){
        this.messageListeners.add(messageListener);
    }
    public static ContactList getInstance(){
        if(contactList==null){
            contactList=new ContactList();
        }
        return contactList;
    }
    private ContactList(){
        this.messageListeners = new ArrayList<>();
        this.contacts = new ArrayList<>();
        lastContactQueue = new LinkedList<>();
        Connection.getInstance().listenForChat(this);
    }

    public ArrayList<User> getList(){
        return contacts;
    }

    public void sendMessage(String username, Message message){
        username = UserUtil.cleanUserJid(username);
        refreshLastContactQueue(username);
        getUser(username).newMessage(message);

    }


     private void initConversation(User user){
       //TODO :

    }

    public User getUser(String username){
        username = UserUtil.cleanUserJid(username);
        for(User user: contacts){
            if(username.equals(UserUtil.cleanUserJid(user.getVCard().getFrom()))){
                return user;
            }
        }
        return null;
    }
    private void refreshLastContactQueue(String username){
        username = UserUtil.cleanUserJid(username);
        for(User user: lastContactQueue){
            if(username.equals(UserUtil.cleanUserJid(user.getVCard().getFrom()))){
                lastContactQueue.remove(user);
                break;
            }
        }
        if (lastContactQueue.size()>=5) {
            lastContactQueue.poll();
        }
        lastContactQueue.offer(Connection.getInstance().getUser(username));
        for(User u:this.getLastContactQueue()){
            Log.d("CONTACTQUEUE", u.getVCard().getFrom());
        }
    }

    public LinkedList<User> getLastContactQueue() {
        return lastContactQueue;
    }



    /**
     * Fonction permettant d'agir à l'arriver d'un nouveau message.
     * On va chercher l'utilisateur qui a envoyé le message. Dans les contacts récent, ou dans
     * la liste de tous les contats.
     * On va lui ajouter ce nouveau message.
     * Ensuite, si le chat n'est pas initialisé, on va l'initialiser avec le user
     * Si le chat est initialiser avec un user, et que c'est le meme qui a envyé le message, on ajoute ce nouveau message au chat.
     * @param message
     */
    @Override
    public void onNewMessage(Message message) {
        //On recher l'émetteur du message parmi les utilisateurs.
        String username = UserUtil.cleanUserJid(message.getFrom());
        User user = getUser(username);
        if(user==null){
            user = Connection.getInstance().getUser(username);
            this.contacts.add(user);
        }
        //Ajout du nouveau message pour l'utilisateur.
        user.newMessage(message);

        //refresh last contact TODO : ça ne devrait pas etre ala reception d'un message
        refreshLastContactQueue(username);

        //Notification à la vue
        for(MessageListener m :  this.messageListeners){
            m.onNewMessage(message);
        }
    }
}
