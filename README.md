# 📱 Nariksha – Women Safety Android App  

Nariksha is an Android app designed to enhance women’s safety with essential emergency features like SOS alerts, emergency contacts, and AI chatbot assistance.  

---

## 📂 Project Structure  

```
app
├── manifests
│ └── AndroidManifest.xml
│
├── java
│ └── com.example.nariksha
│ ├── ui
│ │ ├── ChatbotActivity
│ │ ├── Contact
│ │ ├── ContactsDBHelper
│ │ ├── MainActivity
│ │ └── ManageContactsActivity
│ │
│ ├── com.example.nariksha (androidTest)
│ └── com.example.nariksha (test)
│
├── res
│ ├── drawable
│ │ ├── bg_pic_2.jpg
│ │ ├── circular_button.xml
│ │ ├── header_image.jpg
│ │ ├── ic_dashboard_black_24dp.xml
│ │ ├── ic_home_black_24dp.xml
│ │ ├── ic_launcher_background.xml
│ │ ├── ic_launcher_foreground.xml
│ │ ├── ic_notifications_black_24dp.xml
│ │ ├── sos_button_bg.xml
│ │ └── text_box.xml
│ │
│ ├── layout
│ │ ├── activity_chatbot.xml
│ │ ├── activity_main.xml
│ │ ├── activity_manage_contacts.xml
│ │ ├── fragment_dashboard.xml
│ │ ├── fragment_home.xml
│ │ └── fragment_notifications.xml
│ │
│ ├── menu
│ ├── mipmap
│ │ ├── ic_launcher
│ │ ├── ic_launcher_foreground
│ │ └── ic_launcher_round
│ │
│ ├── navigation
│ ├── values
│ │ ├── colors.xml
│ │ ├── dimens.xml
│ │ ├── strings.xml
│ │ ├── styles.xml
│ │ └── themes
│ │ ├── themes.xml
│ │ └── themes.xml (night)
│ │
│ └── xml
│
├── res (generated)
│
└── Gradle Scripts
├── build.gradle.kts (Project: Nariksha)
├── build.gradle.kts (Module: app)
├── proguard-rules.pro
├── gradle.properties
├── gradle-wrapper.properties
├── libs.versions.toml
├── local.properties
└── settings.gradle.kts
```

## 📸 Screenshots  

### 🏠 Home Page  
<img src="screenshots/home_page.jpg" width="300">  

### 🤖 AI Chatbot  
<img src="screenshots/ai_chatbot.jpg" width="300">  

### 📞 Emergency Contacts  
<img src="screenshots/emergency_contacts.jpg" width="300">  

### 🚨 SOS Message  
<img src="screenshots/sos_msg.jpg" width="300">  

### ❌ Delete Contact  
<img src="screenshots/delete_contact.jpg" width="300">  

### 📳 112 Dial Triggered by Shaking  
<img src="screenshots/112_dial_triggered_by_shaking.jpg" width="300">  

---

## ⚙️ Features  

- 🆘 **SOS Button**: Instantly sends emergency messages.  
- 📞 **Emergency Contacts**: Store and manage trusted contacts.  
- 🤖 **AI Chatbot**: Provides quick assistance.  
- 📳 **Shake Trigger**: Automatically dials `112` when phone is shaken.  

---

## 🚀 Installation  

1. Clone this repo:  
   ```bash
   git clone https://github.com/2004M-chitranshu791113/Nariksha.git
Open in Android Studio.

Build & Run on emulator or physical device.

👨‍💻 Author
Developed by Chitranshu ✨
