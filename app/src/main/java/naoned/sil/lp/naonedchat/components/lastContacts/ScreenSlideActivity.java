package naoned.sil.lp.naonedchat.components.lastContacts;

import android.content.Context;
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
import java.util.LinkedList;
import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.bean.Chat;
import naoned.sil.lp.naonedchat.bean.ContactList;
import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.components.chat.ChatFragment;
import naoned.sil.lp.naonedchat.components.contacts.ContactListFragment;
import naoned.sil.lp.naonedchat.listeners.chat.MessageListener;

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


    private static ScreenSlideActivity ssa;
    public ScreenSlideActivity() {
        contactListFragment = new ContactListFragment();
        chatFragment = new ChatFragment();
    }

    public static ScreenSlideActivity getInstance(){
        return ssa;
    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        ContactList.getInstance().addOnMessageListener(this);
        ssa = this;
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

    public void refreshAdapter() {
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        if (mPager != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Chat.getInstance().isInitialized()) {
                        mPager.setAdapter(mPagerAdapter);

                        // mPager.getAdapter().notifyDataSetChanged();
                    } else {
                        mPager.setAdapter(mPagerAdapter);
                    }
                }
            });
        }
    }



    public void onNewMessage(Message message) {
        refreshAdapter();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private LinkedList<User> lastContact;
        private int totalSize;
        private FragmentManager fragmentManager;

        // lastContact queue to add a list of contact for example
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
            lastContact = ContactList.getInstance().getLastContactQueue();
            refreshTotalSize();
        }

        private void refreshTotalSize() {
            this.totalSize = lastContact.size() +1 ;//enlever le +1
            if (Chat.getInstance().isInitialized()) {
                this.totalSize++;
            }
        }

        /**
         * Stack overflow solution to notify pageFragmentAdapter that DataSet changed
         */
        public int getItemPosition(Object object) {
            if(fragmentManager.getFragments().contains(object)){
                return POSITION_NONE;
            } else{
                return POSITION_UNCHANGED;
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

            User[] lastContactsArray = lastContact.toArray(new User[lastContact.size()]);
            ScreenSlidePageFragment scpf = new ScreenSlidePageFragment();
            scpf.setChatFragment(chatFragment);
            scpf.setUser(lastContactsArray[position - (totalSize - lastContact.size())]);
           // scpf.setUser(lastContact.get())

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