# White Noise

A small native Android white-noise app built with Kotlin and Jetpack Compose.

The project started as a simple way to generate continuous white noise for my daughter rather than playing a prerecorded audio file.

It also became an experiment in native Android development, real-time audio generation, and eventually audio/DSP concepts.

## Current state

The app is a working MVP.

### Working

- White noise is generated in real time using `AudioTrack`
- 48 kHz, 16-bit PCM, mono
- Noise starts automatically when the app is opened
- Large Play / Stop control
- Playback continues when:
  - the app is in the background
  - the phone is locked
  - the phone is folded
- Playback stops when:
  - Stop is pressed
  - the app is removed from Recents
  - a Bluetooth audio device is disconnected
- Foreground service keeps playback alive
- Persistent playback notification
- Tapping the notification opens the app
- Notification has a Stop action
- Dark-mode UI

## Architecture

The basic playback flow is:

```text
MainActivity
    │
    │ PLAY / STOP Intent
    ▼
NoiseService
    │
    ▼
NoiseEngine
    │
    ▼
AudioTrack
    │
    ▼
Android audio output

