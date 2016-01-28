package naoned.sil.lp.naonedchat.components.lastContacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.components.chat.ChatFragment;

/**
 * Created by ACHP on 24/01/2016.
 */
public class ScreenSlidePageFragment extends Fragment {
    //TODO : étendre VCard pour avoir plus d'attributs ? ( notifs ?)
    private VCard contact;
    private ChatFragment chatFragment;

    public void setObject(VCard msg){
        this.contact  = msg;
    }
    public void setChatFragment(ChatFragment fragment){
        this.chatFragment  = fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ((TextView)rootView.findViewById(R.id.lastContactsNomContacts)).setText(this.contact.getFirstName() + " " + this.contact.getLastName());

        byte[] byteArray = this.contact.getAvatar();
        if(byteArray!=null){
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) rootView.findViewById(R.id.lastContactsPhoto);
            image.setImageBitmap(bmp);
        }


        Button button = (Button)rootView.findViewById(R.id.lastContactsTalkTo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mes", "pommes");
                if(chatFragment!=null){
                    Log.d("mes", "de terre");
                   chatFragment.setUser(contact.getFrom());
                }
                Log.d("mes", "sont cuites");
                ViewPager mPager=(ViewPager) getActivity().findViewById(R.id.pager);
               mPager.setCurrentItem(1);

            }
        });


        return rootView;
    }
}