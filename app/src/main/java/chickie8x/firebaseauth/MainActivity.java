package chickie8x.firebaseauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRegister;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String USERID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        btnRegister =(Button)findViewById(R.id.btnRegister);
        btnLogin    =(Button)findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        FirebaseUser check = FirebaseAuth.getInstance().getCurrentUser();
        if (check != null) {
            Intent intent = new Intent(MainActivity.this,ViewContent.class);
            startActivity(intent);
            finish();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    Toast.makeText(MainActivity.this, "Đã đăng nhập \n "+user.getUid(), Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View v) {
        if (v==btnRegister){
            Intent regIntent = new Intent(MainActivity.this,Register.class);
            startActivity(regIntent);
            finish();
        }
        if (v==btnLogin){
            Intent loginIntent = new Intent(MainActivity.this,Login.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
