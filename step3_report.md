# API Documentation

This document provides an overview of the two APIs used in the project — the Random Word API and the Google Maps SDK for Android — including their purpose, usage, and error-handling strategies.

---

## Word Repository API

### Which API Was Chosen and Why

- **API Chosen:** [Random Word API](https://random-word-api.herokuapp.com)  
- **Reason for Selection:**  
  The Random Word API was chosen because it is simple, free, and easy to use. It allows the retrieval of random English words with optional parameters for word length and number of words returned.  
  This makes it an ideal choice for a Wordle-style game, where the application needs random words of a fixed length to generate puzzles dynamically.

---

### Example API Endpoint

The code dynamically constructs the URL based on the desired word length.  
A specific example for retrieving a 5-letter word is:  
[https://random-word-api.herokuapp.com/word?length=5](https://random-word-api.herokuapp.com/word?length=5)

The endpoint returns a JSON array of words. Example response:  
`["apple"]`

---

### Error Handling Strategy

The error handling in the `WordRepository` is designed to ensure stability and reliability during network operations. It uses a two-layered approach to gracefully handle both server-side and client-side issues.

#### 1. HTTP Response Validation

The implementation checks the HTTP response code returned by the API.  
Only when the response code indicates a successful request (200 OK) does the function proceed to process the data.  
If the response code indicates an error (such as 404 or 500), the function immediately returns `null`, avoiding attempts to handle invalid data.

#### 2. Exception Handling

All network and parsing operations are enclosed within a general exception-handling block.  
If any exception occurs — for example, due to no internet connection, a timeout, or unexpected data format — the error is logged for debugging purposes, and the function returns `null`.  
This prevents the application from crashing while allowing the program to handle failures gracefully.

---

### Summary Table

| Aspect            | Details                                                      |
| ----------------- | ------------------------------------------------------------ |
| **API**           | Random Word API (`random-word-api.herokuapp.com`)            |
| **Example Endpoint** | `https://random-word-api.herokuapp.com/word?length=5`     |
| **Concurrency Model** | Kotlin coroutines (`Dispatchers.IO`)                     |
| **Timeouts**      | 5000 ms connect, 5000 ms read                                |
| **Response Format** | JSON array, e.g., `["apple"]`                              |
| **Returned Value** | Uppercased first word (e.g., `"APPLE"`), or `null` on error |
| **Error Handling** | HTTP response check + general exception handling            |

---

## Google Maps SDK for Android

### Which API Was Chosen and Why

- **API Chosen:** [Google Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk)  
- **Reason for Selection:**  
  The Google Maps SDK for Android is the standard and most robust solution for embedding Google Maps within an Android application.  
  It was chosen to display the user’s location and the locations of Wordle games, providing an interactive map interface, which is a core feature of the app.

---

### Example API Usage

The Google Maps SDK is not accessed through direct web endpoints like the Random Word API.  
Instead, it is used through Android components provided by the SDK.  
The main entry point in the code is the `GoogleMap` composable (used in `MapScreen.kt`).  

The API key is configured in the project’s:
- `AndroidManifest.xml`
- `build.gradle.kts`  

These configurations authorize the app to use Google’s map services through the Maps SDK.

---

### Error Handling Strategy

The app’s map-related error handling focuses on three main areas: API key validation, location permissions, and location availability.

#### 1. API Key Validation

The most critical error is a missing or invalid API key.  
The build configuration ensures that if the `MAPS_API_KEY` is not found in the `local.properties` file, the build fails.  
This prevents the app from running with a non-functional map or without proper authorization.

#### 2. Location Permissions

The app checks for location access permissions (`ACCESS_FINE_LOCATION`) before attempting to display the user’s location.  
If permission is not granted, the map still functions but defaults to a hardcoded fallback location (`LatLng(58.3780, 26.7290)` in Tartu).  
This ensures the app remains usable even without location permissions.

#### 3. Location Availability

If permissions are granted but the device fails to retrieve the current location (for instance, if GPS is turned off), the `userLocation` remains `null`.  
In this case, the map again defaults to the same Tartu location, ensuring consistent behavior and preventing app crashes.

---

### Summary Table

| Aspect                | Details |
|------------------------|---------|
| **API**                | Google Maps SDK for Android |
| **Integration Method** | Android SDK components (not direct HTTP calls) |
| **Configuration Files** | `AndroidManifest.xml`, `build.gradle.kts`, `local.properties` |
| **Primary Usage**      | Displaying the user’s and game locations on an interactive map |
| **Error Handling**     | API key validation, permission checks, and fallback location handling |
| **Fallback Location**  | Tartu, Estonia (`LatLng(58.3780, 26.7290)`) |

---

# Overall Summary

| API | Purpose | Integration Type | Error Handling Highlights |
|-----|----------|------------------|----------------------------|
| **Random Word API** | Generates random words for Wordle gameplay | HTTP GET requests | HTTP status validation and exception handling |
| **Google Maps SDK for Android** | Displays user and game locations interactively | Android SDK / Composable component | API key validation, permission checks, and location fallback |
