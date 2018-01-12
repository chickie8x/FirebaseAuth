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

public class Register extends AppCompatActivity implements View.OnClickListener{
    private Button btnRegister;
    private EditText username;
    private EditText password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        mAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ký tài khoản \n Chờ xí :)");
    }

    @Override
    public void onClick(View v) {
        Register();
    }

    public void Register(){
        String userName =username.getText().toString();
        if (userName.isEmpty()){
            Toast.makeText(this, "Tên đăng nhập không hợp lệ...", Toast.LENGTH_SHORT).show();
            return;
        }
        String passWord =password.getText().toString();
        if (passWord.isEmpty()){
            Toast.makeText(this, "Mật khẩu không hợp lệ...", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(userName,passWord).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Đăng ký thành công ahihi...  ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this,ViewContent.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Failed to register :"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
