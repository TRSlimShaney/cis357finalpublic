# CIS 357 FinalProject - Water Reminder
## Topic -  Notifications / Firebase Cloud Messaging

## Table of Contents
- [Overview](#overview)
- [Getting Started](#getting-started)
- [Step-by-Step Coding Instructions](#step-by-step-coding-instructions)
  * [Designing the Fragments](#designing-the-fragments)
    * [The Home Fragment](#the-home-fragment)
    * [The Date Picker Fragment](#the-date-picker-fragment)
    * [Schedule Fragment](#schedule-fragment)
  * [Adding Notifications](#adding-notifications)
  * [Incorporating Firebase Cloud Messaging](#incorporating-firebase-cloud-messaging)
 - [Conclusion](#conclusions)

  
## Overview
Our platform focus area was Android Notifications and Firebase Cloud Messaging.
The demo application is a reminder application, specifically for drinking water. The application allows you to either view currently existing reminders to drink water, or create a new reminder to drink water.

## Getting Started
The software development environment requires the following:
* Internet Access
* A computer capable of running Android Studio and the Android Emulator
  * Preferably the latest version of Android Studio and the latest plugin versions
  * Windows, macOS, and Linux versions are available
  * Hardware Virtualization Acceleration is recommended but not required. If you do not have it enabled Android Studio will prompt you with directions to enable this.
* A Google Account to create a [Firebase](https://console.firebase.google.com/) Project. 
* Install  [Android Studio]( https://developer.android.com/studio) and the Android SDK.

## Step-by-Step Coding Instructions

Start a new project in Android studio with the basic activity template.

### Designing the Fragments

We will need three fragments for this project, one that holds a home page, one that contains a date picker, and one that has a recycler view that shows all of our scheduled reminders. 

#### The Home Fragment
Lets begin with the First Fragment. This will be the "Home Page" where you can navigate to your current reminders and create new reminders.
It will look like this:

<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial18.png" width="200" height="400" />

The exact design specifications for this fragment can found at [app/src/main/res/layout/fragment_first.xml](https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/res/layout/fragment_first.xml).

The logic within it can be found at [app/src/main/java/edu/gvsu/cis/notifications/FirstFragment.kt](https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/FirstFragment.kt).


#### The Date Picker Fragment
Our second fragment will allow us to choose a time and create a new reminder when transitioned to from the home fragment and will be able to edit existing reminders when called from the schedule fragment. It will look like this:

<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial19.png" width="200" height="400" />

The exact design specifications for this fragment can found at [app/src/main/res/layout/fragment_second.xml](https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/res/layout/fragment_second.xml).

The logic within it can be found at [app/src/main/java/edu/gvsu/cis/notifications/SecondFragment.kt]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/SecondFragment.kt).


#### Schedule Fragment
For this portion we are creating a recycler view that holds all of our reminders. Each reminder will be able to be edited when selected as well as deleted when its respective delete button is pressed. This fragment will look like this"

<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial20.png" width="200" height="400" />

The exact design specifications for this fragment can found at [app/src/main/res/layout/fragment_schedules.xml](https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/res/layout/fragment_schedules.xml)  and  [app/src/main/res/layout/fragment_schedule_list.xml]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/res/layout/fragment_schedule_list.xml) 

The logic within it can be found at [app/src/main/java/edu/gvsu/cis/notifications/ScheduleRecyclerViewAdapter.kt]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/ScheduleRecyclerViewAdapter.kt)  and [app/src/main/java/edu/gvsu/cis/notifications/SchedulerDataViewModel.kt]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/SchedulerDataViewModel.kt).

You also must create a time item to represent the scheduled times which can be found at: [app/src/main/java/edu/gvsu/cis/notifications/Items/TimeContent.kt]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/Items/TimeContent.kt) 

### Adding Notifications

The first step in creating a notification and being able to deliver it, is to create a notification channel. You must create this channel before you are able to create any notifications so it is best to do this as early as possible. To create it as early as possible we will create a function called createNotificationChannel in the MainActivity and call it in the onCreate function. The createNotificationChannel function should look something like this: 
``` kotlin
private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "drinkchannel"
            val descriptionText = "remindstodrink"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("drinkchannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
```
Now that we have a notification channel, we are now able to create a notification. We create notifications using a NotificationCompat.Builder object and it contains a small icon, a title, the body text, and the notification priority. The code to do this will look like this: 
``` kotlin
val builder = NotificationCompat.Builder(this@NotificationService, "drinkchannel")
                                        .setSmallIcon(R.drawable.clock)
                                        .setContentTitle("Take a drink!")
                                        .setContentText("Your drink alarm for ${item.toString()} just went off.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
```
This code also calls setAutoCancel(true), which makes it so it automatically removes the notification when the user taps it.

Now that we have our notification created, in order to show it we need to call NotificationManagerCompat.notify() passing it a unique ID for the notification and the result of NotificationCompat.Builder.build(). The code to complete this is below:
``` kotlin
with(NotificationManagerCompat.from(this@NotificationService)) {
                                    notify(24, builder.build())
                                }
```

Now that we know how to create and deliver notifications, now we just need to know when to send those notifications. In this application we want deliver a notification when it is the time of our one of our reminders. To accomplish this we created a NotificationService class that extends Service and start this service in the onCreate function of the MainActivity. This NotificationService continuously in the background checks what the current time is and checks if any of the times in our list of reminders matches the current time. If so it creates a notification and delivers it.

The full code for the NotificationService can be found at
[app/src/main/java/edu/gvsu/cis/notifications/Items/NotificationService.kt]( https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/app/src/main/java/edu/gvsu/cis/notifications/Items/NotificationService.kt)

### Incorporating Firebase Cloud Messaging

To integrate Firebase Cloud Messaging there are a series of steps we must follow:

1. Go to https://console.firebase.google.com/u/0/ to create a new project
2. Select the add project option
<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial08.png" width="300" height="200" />
3.Enter the project name and use the defaults for steps 2 and 3
<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial09.png" width="300" height="200" /> <img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial10.png" width="300" height="200" /> <img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial11.png" width="300" height="200" />
4. Go to the sidebar and select Grow->Cloud Messaging
<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial12.png" width="200" height="500" />
5. Click on Android setup and enter the Android package name
<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial13.png" width="300" height="200" /> <img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial14.png" width="300" height="300" />
6. Download the google-services.json and place it in the project's base directory
<img src="https://github.com/TRSlimShaney/CIS357FinalProject/blob/main/img/tutorial15.png" width="300" height="300" />
7. Modify your gradle files to include the google-services

``` gradle
/* Project build.gradle */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    ext.kotlin_version = "1.4.10"
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.1.1"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"
        classpath "com.google.gms:google-services:4.3.4"
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

``` gradle
/* App gradle.build file*/
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.gms.google-services'
}

//The android portion is specific to our app and not relevant to the Firebase dependencies
android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "edu.gvsu.cis.notifications"
        minSdkVersion 21
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {

    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.3.1'
    implementation 'androidx.navigation:navigation-ui-ktx:2.3.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation platform('com.google.firebase:firebase-bom:26.1.0')
    implementation 'com.google.firebase:firebase-messaging'
    implementation 'com.google.firebase:firebase-analytics'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    apply plugin: 'kotlin-android-extensions'
}
```
8. Modify the AndroidManifest.xml to add a service and some meta-data that is used for the notification

``` xml
        <service
            android:name=".MessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
        
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_icon"
            android:resource="@drawable/clock" />
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_color"
            android:resource="@color/teal_200" />
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_channel_id"
            android:value="@string/default_notification_channel_id" />
        
```

9. Lastly we need to implement the MessagingService class that we included as a service in our AndroidManifest.xml. Our MessagingService will extend the FirebaseMessagingService class and override the onMessageRecieved function so that we are able to handle recieving messages. Our implementation of the function logs the message's body and then creates and pushes a notification saying it has recieved a message from Firebase. The code for our MessagingService class is below:

``` kotlin
class MessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MessagingService", "Message Notification Body: ${it.body}")
        }

        val builder = NotificationCompat.Builder(this, "drinkchannel")
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Take a drink!")
                .setContentText("Firebase says take a drink.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(23, builder.build())
        }        
    }
}
```

Congrats we've now created an Android application capable of creating and displaying notifications as well as recieving messages from Google's Firebase Cloud Messaging.

## Conclusions
In this tutorial we have discussed how to create an application that reminds you to drink water at specificied times with the use of Android's built in notification library along with the application being able to recieve and handle messages utilizing Firebase Cloud Messaging. While there are not really any other approaches to push notifications other than using Android's built in notification service, if you wanted to use something other than Firebase Cloud Messaging to get a similar effect you could use Google Cloud messaging or perhaps OneSignal. Both are capable of high volume notification pushing for mobile applications. A link to the repo where all of this code can be found is here: https://github.com/TRSlimShaney/CIS357FinalProject
