package chickie8x.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        username =(EditText)findViewById(R.id.getUsername);
        password =(EditText)findViewById(R.id.getPassword);
        btnLogin =(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        mAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chờ xí ...");

    }

    @Override
    public void onClick(View v) {
        LoginAccount();
    }
    public void LoginAccount(){
        final String userName =username.getText().toString();
        if (userName.isEmpty() ) {
            Toast.makeText(this, "Email không hợp lệ...", Toast.LENGTH_SHORT).show();
            return;
        }
        String passWord = password.getText().toString();
        if (passWord.isEmpty()) {
            Toast.makeText(this, "Password không hợp lệ...", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(userName,passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                            Toast.makeText(Login.this, "Đăng nhập thành công\n "+"UserID : "+user.getUid(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this,ViewContent.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Đăng nhập méo được\n"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
