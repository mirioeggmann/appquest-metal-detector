# AppQuest Metal Detector

![HSR](http://appquest.hsr.ch/images/fho.png)

## About

AppQuest Metal Detector is the 1. application for the [App Quest 2016](http://appquest.hsr.ch/) Treasure Hunt. The application must be able to find a magnetical object by showing the magnetic value in a progress bar. While the Treasure Hunt the goal will be to find the treasure with the "gold" inside. Finally it must be possible to upload the QR-code string to the AppQuest Logbuch.

### General
|   |  |
|---|---|
| Application Requirements | http://appquest.hsr.ch/2016/metall-detektor-android |
| Minimum API Level | [API level 23 (Marshmallow)](https://developer.android.com/about/versions/marshmallow/android-6.0.html) |
| Development Environment | [Android Studio](https://developer.android.com/studio/index.html) |

### Example
![Metal Detector](http://appquest.hsr.ch/2013/wp-content/uploads/metalldetektor-1024x345.png)

### Given code snippets

Work with the magnet sensor data
```java
float[] mag = event.values;
double betrag = FloatMath.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] * mag[2]);
```

---

AppQuest Logbuch format
```
{
  "task": "Metalldetektor",
  "solution": "$Lösungswort"
}
```

## License
[MIT License](https://github.com/mirioeggmann/appquest-metal-detector/blob/master/LICENSE)
