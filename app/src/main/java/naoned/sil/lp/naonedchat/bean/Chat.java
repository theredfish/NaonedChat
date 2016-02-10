package naoned.sil.lp.naonedchat.bean;

import android.support.v4.app.Fragment;
import android.view.View;

import org.jivesoftware.smack.packet.Message;

import java.sql.Timestamp;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.components.chat.ChatAdapter;
import naoned.sil.lp.naonedchat.components.chat.ChatFragment;
import naoned.sil.lp.naonedchat.service.Connection;


/**
 * Created by ACHP on 06/02/2016.
 */
public class Chat {
    private User currentUser;
    private ChatFragment chatFragment;
    private ChatAdapter chatAdapter;
    private View view;
    private boolean isInitialized;

    private static Chat chat;

    /**
     * Permet de récupérer le singleton Chat.
     *
     * @return l'instance de Chat.
     */
    public static Chat getInstance() {
        if (chat == null) {
            chat = new Chat();
        }
        return chat;
    }

    /**
     * Permet d'initialiser le singleton Chat, en créant les élèments nécessaire à la vue.
     *
     * @param currentUser correspond au user avec lequel on veut initialiser le chat.
     */
    public void init(User currentUser) {
        this.currentUser = currentUser;
        this.isInitialized = true;
    }

    /**
     * Cette méthode permet de savoir si un utilisateur est l'utilisateur en cour dans le chat.
     *
     * @param user
     * @return
     */
    public boolean isCurrentUser(User user) {
        if (this.currentUser.getJID().equals(user.getJID())) {
            return true;
        }
        return false;
    }

    /**
     * Cette méthode permet d'envoyer un message à l'utilisateur
     *
     * @param message
     */
    public void sendMessage(Message message) {
        Connection.getInstance().sendMessage(this.currentUser.getJID(), message);
    }

    /**
     * Permet de savoir si le singleton a été initialisé ou non.
     *
     * @return
     */
    public boolean isInitialized() {
        return this.isInitialized;
    }

    /**
     * Constructeur de la classe Chat
     */
    private Chat() {
        this.isInitialized = false;
    }

    /**
     * Permet de changer d'interlocuteur dans le chat.
     *
     * @param currentUser
     */
    public void changeUser(User currentUser) {
        this.currentUser = currentUser;
        this.chatAdapter = new ChatAdapter(view.getContext(),
                R.layout.row_chat_left,
                currentUser.getConversation()
        );
        this.chatFragment.listViewMessage.setAdapter(chatAdapter);
    }

    /**
     * Fonction permettant d'ajouter un message dans le chat actif
     */
    public void addMessage(Message message) {

        //this.chatAdapter.add(message);
    }

    /**
     * Fonction permettant de récupérer le fragment  ( La vue) du chat.
     *
     * @return
     */
    public Fragment getFragment() {
        return this.chatFragment;
    }


    public User getUser() {
        return this.currentUser;
    }
}
