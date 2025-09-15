# 📋 Kotlin Todo List

> A modern Android task management application built with Kotlin, featuring Firebase integration, Material Design UI, and intelligent notification system.

![Android](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue?style=flat-square&logo=kotlin)
![Firebase](https://img.shields.io/badge/Backend-Firebase-orange?style=flat-square&logo=firebase)
![Material Design](https://img.shields.io/badge/UI-Material%20Design-purple?style=flat-square&logo=materialdesign)

## ✨ Features

### 🚀 Core Features
- ✅ **Task Management** - Create, edit, delete, and organize tasks with priority levels
- ✅ **Firebase Authentication** - Secure login/logout with email authentication
- ✅ **Real-Time Database** - Live synchronization of tasks across devices using Firebase Realtime Database
- ✅ **Smart Notifications** - Firebase Cloud Messaging (FCM) for task reminders and updates
- ✅ **Navigation Drawer** - Intuitive side navigation for seamless app navigation
- ✅ **Permission Management** - Runtime notification permissions with user-friendly handling
- ✅ **Edge-to-Edge Design** - Modern immersive UI experience

### 📱 User Experience
- **Clean Material Design** - Modern, intuitive interface following Google's design guidelines
- **Responsive Navigation** - Smooth transitions and navigation patterns
- **Priority-Based Organization** - Categorize tasks by importance and urgency
- **Real-Time Updates** - Instant synchronization across all user devices
- **Offline Support** - Local data persistence when network is unavailable

## 🛠 Tech Stack

### **Frontend**
- **Kotlin** - Primary programming language
- **Android Jetpack** - Modern Android development components
- **Navigation Component** - Single-Activity architecture with fragment navigation
- **Material Design Components** - Google's design system implementation
- **ViewBinding** - Type-safe view references

### **Backend & Services**
- **Firebase Authentication** - User management and secure authentication
- **Firebase Realtime Database** - NoSQL cloud database for real-time data sync
- **Firebase Cloud Messaging (FCM)** - Push notifications and messaging
- **Firebase Analytics** - User behavior tracking and app insights

### **Architecture & Libraries**
- **MVVM Architecture** - Model-View-ViewModel pattern for separation of concerns
- **AndroidX Libraries** - Backward-compatible Android support libraries
- **Notification API** - Android's native notification system
- **Gradle Kotlin DSL** - Modern build configuration

## 📥 Installation

### Prerequisites
- **Android Studio** Arctic Fox (2020.3.1) or newer
- **Android SDK** with minimum API level 21 (Android 5.0)
- **Kotlin** 1.8.0 or newer
- **Google Services** account for Firebase setup

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/nilabjamitra/KotlinTodo.git
   cd KotlinTodo
   ```

2. **Firebase Configuration**
   - Create a new project in [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download the `google-services.json` file
   - Place it in the `app/` directory

3. **Enable Firebase Services**
   - **Authentication**: Enable Email/Password authentication
   - **Realtime Database**: Create a database in test mode
   - **Cloud Messaging**: Enable FCM for push notifications

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open the project in Android Studio and run it directly.

## 🔧 Configuration

### Firebase Rules (Database)
```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "auth != null && auth.uid == $uid",
        ".write": "auth != null && auth.uid == $uid"
      }
    }
  }
}
```

### Notification Permissions
The app handles Android 13+ notification permission requests automatically. Users will be prompted to allow notifications when first launching the app.

## 📱 Usage

1. **Authentication**
   - Sign up with email and password
   - Login to access your personalized task list

2. **Task Management**
   - Tap the "+" button to create new tasks
   - Set priority levels (High, Medium, Low)
   - Edit tasks by tapping on them
   - Mark tasks as completed with checkboxes

3. **Navigation**
   - Use the drawer menu to navigate between sections
   - Access settings and user profile options

4. **Notifications**
   - Receive reminders for upcoming tasks
   - Get notified when tasks are shared or updated

## 🏗 Project Structure

```
app/
├── src/main/
│   ├── java/com/nilabjamitra/kotlintodo/
│   │   ├── ui/           # UI components and fragments
│   │   ├── data/         # Data models and repositories
│   │   ├── utils/        # Utility classes and helpers
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/       # XML layout files
│   │   ├── values/       # Colors, strings, styles
│   │   └── drawable/     # Icons and graphics
│   └── AndroidManifest.xml
├── build.gradle.kts      # App-level build configuration
└── google-services.json  # Firebase configuration
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Nilabja Mitra**
- GitHub: [@nilabjamitra](https://github.com/nilabjamitra)
- Email: nilabja22@gmail.com

## 🙏 Acknowledgments

- Google Firebase for backend services
- Material Design team for design guidelines
- Android Jetpack for modern development tools
- Open source community for inspiration and support

---

⭐ **Star this repository if you find it helpful!**
