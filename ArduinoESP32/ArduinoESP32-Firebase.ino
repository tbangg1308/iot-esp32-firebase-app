#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "DHT.h"

// Thổng tin wifi và firebase - information wifi and fisebase
const char* ssid = "Your wifi name";
const char* password = " your wifi password";

#define FIREBASE_HOST "Secrets code"
#define FIREBASE_AUTH "url firebase" 
#define API_KEY ""  // Nếu chỉ dùng RTDB thì có thể để trống -If using RTDB only, it can be left blank.

//CẤU HÌNH CẢM BIẾN DHT22 -DHT22 Sensor Configuration
##define DHTPIN 4
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

//CẤU HÌNH LED-LED configuration
#define LED_PIN 5

//CẤU HÌNH FIREBASE-FIREBASEconfiguration
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

void setup() {
  Serial.begin(115200);
  dht.begin();

  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  // Kết nối WiFi
  WiFi.begin(ssid, password);
  Serial.print("Đang kết nối WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\n✅ Đã kết nối WiFi");

  // Cấu hình Firebase
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  // ==== Đọc nhiệt độ và độ ẩm từ DHT22 ====
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  if (!isnan(temperature) && !isnan(humidity)) {
    Serial.printf("🌡️ Temp: %.2f°C | 💧 Humidity: %.2f%%\n", temperature, humidity);

    // Gửi dữ liệu cảm biến lên Firebase
    Firebase.RTDB.setFloat(&fbdo, "/devices/esp32/sensors/temperature", temperature);
    Firebase.RTDB.setFloat(&fbdo, "/devices/esp32/sensors/humidity", humidity);

    // Gửi cảnh báo nếu nhiệt độ cao
    if (temperature > 35) {
      Firebase.RTDB.setString(&fbdo, "/devices/esp32/alert", "Cảnh báo! Nhiệt độ cao");
    } else {
      Firebase.RTDB.setString(&fbdo, "/devices/esp32/alert", "");
    }

    // ==== Đọc trạng thái LED từ Firebase ====
    if (Firebase.RTDB.getBool(&fbdo, "/devices/esp32/control/led")) {
      bool ledState = fbdo.boolData();
      digitalWrite(LED_PIN, ledState ? HIGH : LOW);
      Serial.printf("💡 LED = %s\n", ledState ? "BẬT" : "TẮT");
    } else {
      Serial.printf("❌ Không đọc được trạng thái LED: %s\n", fbdo.errorReason().c_str());
    }
  } else {
    Serial.println("❌ Lỗi đọc dữ liệu từ cảm biến DHT22");
  }

  delay(2000);
}