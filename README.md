# AI Face Studio (personal-use WebView client)

A small Android WebView wrapper that opens `https://aiswapface.org/`.

## Included
- Java Android Activity; no Android Studio required on your PC.
- WebView with JavaScript, DOM storage and cookies enabled.
- Website file picker for uploads (photo/video and multi-select where supported by Android picker).
- Web camera/microphone permission bridge when the site requests WebRTC access.
- DownloadManager integration for result downloads.
- Back button navigates WebView history.
- GitHub Actions workflow builds a debug APK on push or manual dispatch.

## Important
- This is an independent, personal-use WebView client, not an official app and not affiliated with or endorsed by aiswapface.org.
- The website and its services remain controlled by their operator. This app does not copy its AI model, backend, API, or website source.
- Use only photos/videos you own or have permission to use, and obtain consent from people depicted. Do not create deceptive or non-consensual intimate imagery.
- The website may change, block WebView traffic, require browser-specific features, or disallow embedding. If that happens, use the website in Chrome; a WebView wrapper cannot guarantee every website feature will work.
- File selection and downloads are delegated to Android's system picker and DownloadManager. Camera capture from the upload picker depends on the Android picker/device; in-page camera access is separately permission-gated.
- This starter does not include private signing credentials. GitHub Actions builds a debug APK suitable for personal testing. Do not publish it as a release build.

## Build APK with GitHub Actions
1. Create a new GitHub repository named `AI-Face-Studio`.
2. Upload/push all files from this project to the repository's `main` branch.
3. Open the repository's **Actions** tab.
4. Select **Build AI Face Studio APK** and click **Run workflow**, or push a commit.
5. Wait for the workflow to finish successfully.
6. Open the completed workflow run and download the `AI-Face-Studio-debug-apk` artifact.
7. Extract the downloaded ZIP; install `app-debug.apk` on your Android phone.

## Build locally (optional)
Requires JDK 17 and Gradle 8.9:
```bash
gradle --no-daemon assembleDebug
```
APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## App identity
- App name: AI Face Studio
- Application ID: `com.shanpalia.aifacestudio`
- Website: `https://aiswapface.org/`
