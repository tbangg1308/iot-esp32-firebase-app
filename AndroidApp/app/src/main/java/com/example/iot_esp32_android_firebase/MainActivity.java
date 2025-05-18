package com.example.iot_esp32_android_firebase;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView temperatureText, humidityText;
    private SwitchMaterial ledSwitch;

    private DatabaseReference tempRef, humidRef, ledRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo file XML của bạn là activity_main.xml

        // Ánh xạ view
        temperatureText = findViewById(R.id.temperatureText);
        humidityText = findViewById(R.id.humidityText);
        ledSwitch = findViewById(R.id.ledSwitch);

        // Tham chiếu Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tempRef = database.getReference("devices/esp32/sensors/temperature");
        humidRef = database.getReference("devices/esp32/sensors/humidity");
        ledRef = database.getReference("devices/esp32/control/led");

        // Lắng nghe nhiệt độ
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Double temp = snapshot.getValue(Double.class);
                if (temp != null) {
                    temperatureText.setText(temp + "°C");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        // Lắng nghe độ ẩm
        humidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Double humid = snapshot.getValue(Double.class);
                if (humid != null) {
                    humidityText.setText(humid + "%");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        // Lắng nghe trạng thái LED
        ledRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean ledState = snapshot.getValue(Boolean.class);
                if (ledState != null) {
                    ledSwitch.setChecked(ledState);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        // Khi người dùng bật/tắt công tắc LED
        ledSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ledRef.setValue(isChecked);
        });
    }
}