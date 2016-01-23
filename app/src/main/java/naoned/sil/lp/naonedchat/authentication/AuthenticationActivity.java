package naoned.sil.lp.naonedchat.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import naoned.sil.lp.naonedchat.HomeActivity;
import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.authentication.Listeners.DisconnectOnClickListener;
import service.XmppService;

/**
 * Created by ACHP on 22/01/2016.
 */
public class AuthenticationActivity extends Activity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button LogInButton;
    Button DisconnectButton;
    public static XmppService xmppService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameEditText = ((EditText)findViewById(R.id.username));
        passwordEditText = ((EditText)findViewById(R.id.password));
        LogInButton = ((Button)findViewById(R.id.LogIn));
        DisconnectButton = ((Button)findViewById(R.id.logout));


        Button LogIn = (Button)findViewById(R.id.LogIn);
        LogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LogInBackground().execute(usernameEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });

        Button LogOut = (Button)findViewById(R.id.logout);
        DisconnectOnClickListener disconnectOnClickListener = new DisconnectOnClickListener(xmppService);
        LogOut.setOnClickListener(disconnectOnClickListener);

    }

    private class LogInBackground extends AsyncTask<String, int[], Boolean> {

        private boolean success = true;


        @Override
        protected Boolean doInBackground(String... params) {

            xmppService = new XmppService();
            xmppService.setAuthenticationCredentials(params[0], params[1]);
            this.success = xmppService.connectToNaonedChat();
            return true;
        }


        /**
         * Called after doInBackground() method
         * This method runs on the UI thread
         */
        @Override
        protected void onPostExecute(Boolean result) {
            if(!this.success){
                Toast.makeText(AuthenticationActivity.this,
                        "Authentication failed", Toast.LENGTH_LONG).show();
                return;
            }
            Intent AuthenticationActivity = new Intent(AuthenticationActivity.this, HomeActivity.class);
            startActivity(AuthenticationActivity);

        }

    }

}
