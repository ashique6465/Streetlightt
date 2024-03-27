package com.example.streetlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private TextView text1,text2,text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference= FirebaseDatabase.getInstance().getReference("Automated Street Light");


        text1 =findViewById(R.id.textView2);
        text2 =findViewById(R.id.textView4);
        text3 =findViewById(R.id.textView6);

    }

    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value1 = snapshot.child("Fault Status").getValue(String.class);
                String value2 = snapshot.child("Motion Status").getValue(String.class);
                String value3 = snapshot.child("Street ID").getValue(String.class);

                text1.setText(value1);
                text2.setText(value2);
                text3.setText(value3);

                notification(value1);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    public  void notification(String message){
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Street Light Monitor")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (notificationManager != null) {

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }
}