# **Music App**
* Oragnization: **NHL Stenden University of Applied Sciences**
* Subject: **Secure Programming**
* Date: **2022, March 6th**
* Period: **2**
* Made by **MusicApp group**

## Description
An Android application that uses the Spotify API and the YouTube Data API. The application also uses Firebase for authentication and data storage. Besides Firebase and the two data sources, there is also a custom API and database to store encrypted ClientID and song lists created by the user. The application is mainly written in the programming language Kotlin with parts being written in Java. The API is written in NodeJS using the Express.js framework.

### List of sources the app uses:
1. Firebase Authentication & Data storage.
2. Sportify API
3. YouTube API
4. Node.js RESTful API

### Testing tools:
1. MobSF
2. Jenkins
3. DebugTool
4. Unittest

## Getting Started
To run Android application, follow this quideline: **https://developer.android.com/studio/run/emulator**.

## Linking you Spotify Account

To this application to access your own Spotify playlists and songs, do the following:

1. Go to https://developer.spotify.com/dashboard/login and create a project, this provides you with
   a unique ClientID, which will be used for entering the **"Enter Client ID" on the application
   when linking the Spotify**.
2. Click on the "Edit Setting" on the project that has been created
3. Under the Redirect URIs, add this: https://com.example.musicapplication_sp//callback
4. Under the Android Package Name add: "com.example.musicapplication_sp" and this as the SHA1
   Fingerprint "EF:80:6B:1A:7E:10:85:4F:85:AF:B7:AD:E9:DE:AD:B9:D2:7E:4B:02"

Note: **Click on setting on the application to link your account**
This are the steps needed to link your Spotify account.
