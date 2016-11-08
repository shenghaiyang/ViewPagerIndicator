# Indicator

Android ViewPager Indicator

![](screenshots/cci.png)

## Usage

gradle：

```
compile 'shenghaiyang:indicator:1.0.0'
```

xml中：

```xml
<shenghaiyang.indicator.CircleChangeIndicator
    android:id="@+id/indicator"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal|bottom"
    android:layout_marginBottom="48dp"
    app:normalSolidColor="#00000000"
    app:normalSolidRadius="4dp"
    app:normalStrokeColor="#ffffff"
    app:normalStrokeWidth="1dp"
    app:orientation="horizontal"
    app:selectedSolidColor="#ffffff"
    app:selectedSolidRadius="4dp"
    app:selectedStrokeColor="#ffffff"
    app:selectedStrokeWidth="1dp"
    app:spacing="5dp" />
```

## License

```
Copyright 2016 shenghaiyang.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```