package naoned.sil.lp.naonedchat.FavoriteContacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.chat.ChatActivity;
import service.Connection;

/**
 * Created by ACHP on 24/01/2016.
 */
public class ScreenSlideActivity extends FragmentActivity implements service.onMessageListener {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        List<String> list = new ArrayList<>();
        list.add("fenetre 1");
        list.add("fenetre 2");


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), list);
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
        if (mPager != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPager.setAdapter(mPagerAdapter);
                }
            });

        }




    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<String> list;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<String> list) { //Ajout d'une liste de Contact par exemple.
            super(fm);
            this.list = list;

        }

        @Override
        public Fragment getItem(int position) {


               if (chatActivity != null && position==0){
                    return chatActivity;
                }

            ScreenSlidePageFragment scpf = new ScreenSlidePageFragment();
            scpf.setObject(list.get(position));
            return scpf;
        }

        @Override
        public int getCount() {
            return list.size(); // La taille de la liste
        }
    }
}