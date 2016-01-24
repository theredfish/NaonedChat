package naoned.sil.lp.naonedchat.FavoriteContacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import naoned.sil.lp.naonedchat.R;

/**
 * Created by ACHP on 24/01/2016.
 */
public class ScreenSlidePageFragment extends Fragment {
    private String msg;
    public void setObject(String msg){
        this.msg  = msg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ((TextView)rootView.findViewById(R.id.monTextModifiable)).setText(this.msg);
        return rootView;
    }
}