package naoned.sil.lp.naonedchat.components.lastContacts;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.LinkedList;
import java.util.Queue;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.NotificationUtil;
import naoned.sil.lp.naonedchat.components.chat.ChatFragment;
import naoned.sil.lp.naonedchat.components.contacts.ContactListFragment;
import naoned.sil.lp.naonedchat.listeners.chat.MessageListener;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 24/01/2016.
 */
public class ScreenSlideActivity extends FragmentActivity implements MessageListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * Chat activity
     */
    ChatFragment chatFragment;

    /**
     * Contact list
     */
    ContactListFragment contactListFragment;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    Queue<VCard> lastContacts;

    public ScreenSlideActivity() {
        contactListFragment = new ContactListFragment();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_screen_slide);

        lastContacts = new LinkedList<>();

        if (chatFragment != null) {
            lastContacts = chatFragment.getLastContactsQueue();
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), lastContacts);
        mPager.setAdapter(mPagerAdapter);

        Connection.getInstance().listenForChat(this);
    }

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

    public void onNewMessage(Message message) {

        NotificationUtil.makeNotification(this);
        if (chatFragment == null) {
            chatFragment = new ChatFragment();
            chatFragment.setUser(message.getFrom());
        }

        chatFragment.addMessage(message.getFrom(), message);
        lastContacts = chatFragment.getLastContactsQueue();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), lastContacts);

        if (mPager != null) {
            runOnUiThread(new Runnable() {
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

        // lastContact queue to add a list of contact for example
        public ScreenSlidePagerAdapter(FragmentManager fm, Queue<VCard> lastContact) {
            super(fm);
            this.lastContact = lastContact;
            refreshTotalSize();

        }

        private void refreshTotalSize(){
            this.totalSize = lastContact.size() + 1;
            if(chatFragment!=null){
                this.totalSize = totalSize + 1;
            }
        }
        public Fragment getItem(int position) {
            refreshTotalSize();
            if (position == 0) {
                return contactListFragment;
            }

            if (position == 1) {
                return chatFragment;
            }

            VCard[] lastContactsArray = lastContact.toArray( new VCard[lastContact.size()]);
            ScreenSlidePageFragment scpf = new ScreenSlidePageFragment();
            scpf.setChatFragment(chatFragment);
            scpf.setObject(lastContactsArray[position - (totalSize - lastContact.size())]);

            return scpf;
        }

        public int getCount() {
            return totalSize;
        }
    }

    /**
     * Get result inside onActivityResult from fragments.
     *
     * We need this solution to overcome for the fact that a fragment override the onActivityResult
     * method.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}