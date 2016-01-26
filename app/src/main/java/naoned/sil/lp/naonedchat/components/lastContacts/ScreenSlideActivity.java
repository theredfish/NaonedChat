package naoned.sil.lp.naonedchat.components.lastContacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.LinkedList;
import java.util.Queue;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.components.chat.ChatActivity;
import naoned.sil.lp.naonedchat.listeners.chat.onMessageListener;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 24/01/2016.
 */
public class ScreenSlideActivity extends FragmentActivity implements onMessageListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;



    ChatActivity chatActivity;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    Queue<VCard> lastContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        lastContacts = new LinkedList<>();
        if(chatActivity!=null){
            lastContacts = chatActivity.getLastContactsQueue();
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), lastContacts);
        mPager.setAdapter(mPagerAdapter);

        Connection.getInstance().listenForChat(this);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onNewMessage(Message message) {
        Log.d("ScreenSlideActivity", "notification de nouveau message");
        if(chatActivity==null){
            Log.d("ScreenSlideActivity", "Création d'un nouveau chatActivity");
            chatActivity = new ChatActivity();
            chatActivity.setUser(message.getFrom());

        }

        Log.d("ScreenSlideActivity", "Ajout d'un message dans la hashmap");
        chatActivity.addMessage(message.getFrom(), message);
        lastContacts = chatActivity.getLastContactsQueue();


            mPagerAdapter= new ScreenSlidePagerAdapter(getSupportFragmentManager(),lastContacts );





        if (mPager != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                        mPager.setAdapter(mPagerAdapter);

                        mPagerAdapter.notifyDataSetChanged();


                }
            });

        }




    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Queue<VCard> lastContact;

        private int totalSize;

        public ScreenSlidePagerAdapter(FragmentManager fm, Queue<VCard> lastContact) { //Ajout d'une liste de Contact par exemple.
            super(fm);
            this.lastContact = lastContact;
            this.totalSize = lastContact.size();

        }

        @Override
        public Fragment getItem(int position) {

              if (chatActivity != null && position==0){
                   totalSize++;
                    return chatActivity;
                }

            VCard[] lastContactsArray =lastContact.toArray( new VCard[lastContact.size()]);
            ScreenSlidePageFragment scpf = new ScreenSlidePageFragment();
            scpf.setObject(lastContactsArray[position-1]);
            return scpf;
        }

        @Override
        public int getCount() {
            return totalSize; // La taille de la liste
        }
    }
}