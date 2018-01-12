package chickie8x.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;

import static chickie8x.firebaseauth.MainActivity.USERID;

public class SetTime extends AppCompatActivity implements View.OnClickListener {
    private EditText inputTime;
    private Button btnAdd;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        inputTime=(EditText)findViewById(R.id.inputTime);
        btnAdd =(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        USERID =user.getUid();
        userEmail=user.getEmail();


    }

    @Override
    public void onClick(View v) {
        if (v==btnAdd){
            TimeSet();
        }
    }
    public void TimeSet(){
        String time = inputTime.getText().toString();
        if (time.isEmpty()){
            Toast.makeText(this, "Không tăng ca thì bấm vào đây làm méo gì @@", Toast.LENGTH_SHORT).show();
        } else{
            MyTask();
        }
    }
    public void MyTask(){
        Intent intent = getIntent();
        String keyDate = intent.getStringExtra(ViewContent.DATE);
        String[] extract = keyDate.split("-");
        String getDay = extract[0];
        String getMonth = extract[1];
        String getYear = extract[2];
        String timeValue=inputTime.getText().toString();
        DatabaseReference databaseRefrence = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childRefUID = databaseRefrence.child(USERID);
        DatabaseReference childInfo =childRefUID.child("User Detail");
        DatabaseReference childEmail =childInfo.child("Email");
        DatabaseReference childOT =childRefUID.child("OverTime");
        DatabaseReference childYear =childOT.child(getYear);
        DatabaseReference childMonth =childYear.child(getMonth);
        DatabaseReference childDay =childMonth.child(getDay);


        try {
            childEmail.setValue(userEmail);
            childDay.setValue(timeValue);
            Toast.makeText(this, "Đã lưu :) ", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi !Lưu không thành công : "+e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
