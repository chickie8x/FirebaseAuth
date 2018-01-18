package chickie8x.firebaseauth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class ViewContent extends AppCompatActivity implements View.OnClickListener{
    public static String DATE;
    private String cDate;
    private String sDate;
    private String rDate;
    private Button btnAdd;
    private TextView identifierView;
    private String USERID;
    private TextView todayOT;
    private TextView monthOT;
    private String extractDay;
    private String extractMonth;
    private String extractYear;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);
        final MaterialCalendarView materialCalendarView =(MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1900,0,1))
                .setMaximumDate(CalendarDay.from(2100,11,30))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorator(new CurrentDateDecorator(this));
        //get the selected date if selected
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String selectedDate =DateReform(materialCalendarView.getSelectedDate().toString());
                String[] extract =selectedDate.split("-");
                String month = extract[1];
                if (month.length()==1){
                    extract[1] ="0"+month.toString();
                }
                else {
                    extract[1] =month.toString();
                }
                sDate =extract[0]+"-"+extract[1]+"-"+extract[2];
                String[] OTDate = MonthChanged(sDate);
                DatabaseListening(USERID,OTDate[0],OTDate[1],OTDate[2]);

            }
        });

        //get the current date
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal =Calendar.getInstance();
        cDate =date.format(cal.getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String identifier =user.getEmail().toString();
        USERID =user.getUid();
        String getSelectedDate = GetDate();
        final String[] extract =getSelectedDate.split("-");
        extractDay=extract[0];
        extractMonth=extract[1];
        extractYear =extract[2];
        todayOT=(TextView)findViewById(R.id.todayOT);
        monthOT=(TextView)findViewById(R.id.monthOT);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        identifierView=(TextView)findViewById(R.id.identifier);
        identifierView.setText("ID : "+identifier);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHr = calendar.get(Calendar.HOUR_OF_DAY);
        if (curHr>=13){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.MINUTE,20);
        calendar.set(Calendar.SECOND,0);
        Intent intent = new Intent("chickie8x.firebaseauth.action.DISPLAY_NOTIFICATION");
        PendingIntent pendingIntent= PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager  alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        DatabaseListening(USERID,extractDay,extractMonth,extractYear);

        //listening on month change
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String[] monthYear = MonthChanged(DateReform(date.toString()));
                DatabaseListening(USERID,monthYear[0],monthYear[1],monthYear[2]);
            }
        });

    }
    //database listening
    public void DatabaseListening (String UID , final String day,final String month , final String year){
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference(UID);
        DatabaseReference refOT =ref.child("OverTime");
        DatabaseReference refYear =refOT.child(year);
        DatabaseReference refMonth =refYear.child(month);
        refMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String ,String> map =(Map)dataSnapshot.getValue();
                if (map !=null){
                    String value =map.get(day);
                    todayOT.setText("Ngày "+day+"-"+month+"-"+year+" : " +value+" giờ");
                    float sumValues=0;
                    Set<String> set = map.keySet();
                    for (String key:set){
                        sumValues+=Float.parseFloat(map.get(key));
                    }
                    monthOT.setText("Tổng tháng "+month +"-"+year +" :"+String.valueOf(sumValues)+" giờ");

                }else {
                    Toast.makeText(ViewContent.this, "Lỗi kết nối cơ sở dữ liệu.....", Toast.LENGTH_SHORT).show();
                    todayOT.setText("Ngày : Không có dữ liệu");
                    monthOT.setText("Tổng tháng : Không có dữ liệu");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //reform date
    public String DateReform(String date){
        String dateInput =date.substring(date.indexOf('{')+1,date.indexOf('}'));
        String [] extract =dateInput.split("-");
        extractYear = extract[0];
        Integer correctMonth = Integer.parseInt(extract[1])+1;
        String  showMonth = correctMonth.toString();
        if (showMonth.length()==1){
            extractMonth ="0"+showMonth;
        }
        else {
            extractMonth =showMonth;
        }
        if (extract[2].length()==1){
            extractDay ="0"+extract[2];
        }
        else {
            extractDay =extract[2];
        }
        String extractDate = extractDay+"-"+extractMonth+"-"+extractYear;
        return extractDate;
    }
    //get date for set time parameter
    public String GetDate(){
        if (sDate==null){
            rDate = cDate;
        }
        else {
            rDate =sDate;
        }

        return rDate;
    }

    @Override
    public void onClick(View v) {
        if(v==btnAdd){
            String putDate=GetDate();
            Intent intent =new Intent(ViewContent.this,SetTime.class);
            intent.putExtra(DATE,putDate);
            startActivity(intent);
        }
    }

    public void SignOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Đã thoát tài khoản ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewContent.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public String[] MonthChanged(String date){
        String[] month = date.split("-");
        return month;
    }
}





