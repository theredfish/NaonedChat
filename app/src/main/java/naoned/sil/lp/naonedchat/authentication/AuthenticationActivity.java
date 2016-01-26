package naoned.sil.lp.naonedchat.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import naoned.sil.lp.naonedchat.components.lastContacts.ScreenSlideActivity;
import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.listeners.authentification.DisconnectOnClickListener;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 22/01/2016.
 */
public class AuthenticationActivity extends Activity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button disconnectButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameEditText = ((EditText)findViewById(R.id.username));
        passwordEditText = ((EditText)findViewById(R.id.password));
        loginButton = ((Button)findViewById(R.id.LogIn));
        disconnectButton = ((Button)findViewById(R.id.logout));

        Button LogIn = (Button)findViewById(R.id.LogIn);

        LogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new LogInBackground().execute(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new DisconnectOnClickListener());

    }

    private class LogInBackground extends AsyncTask<String, int[], Boolean> {
        private boolean success = true;
        private boolean isConnecting=false;

        protected Boolean doInBackground(String... params) {
            Connection connection = Connection.getInstance();
            if(isConnecting ||connection.getConnection().isConnected()){return false;}

            isConnecting = true;
            Log.d("CONNEXION", "Tentative de connexion !");
            if (!connection.connect()) {
                isConnecting=false;
                this.success=false;
                return false;
            }

            return connection.login(params[0], params[1]);
        }


        /**
         * Called after doInBackground() method
         * This method runs on the UI thread
         */
        protected void onPostExecute(Boolean result) {
            if (!this.success) {
                Toast.makeText(AuthenticationActivity.this,
                        "Authentication failed", Toast.LENGTH_LONG).show();

                return;
            }

            //TODO : remove chat activity from here
           // Intent AuthenticationActivity = new Intent(AuthenticationActivity.this, HomeActivity.class);
            //startActivity(AuthenticationActivity);

            Intent screenSlideActivity = new Intent(AuthenticationActivity.this, ScreenSlideActivity.class);
            startActivity(screenSlideActivity);
        }

    }

}
