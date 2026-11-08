// Copyright 2013 The Flutter Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import androidx.annotation.Nullable;
import androidx.camera.core.ImageAnalysis;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SkillDevs fork: lets the app consume image stream frames natively instead of in Dart.
 *
 * <p>When an analyzer is set here, starting the image stream binds it (on a background thread) in place of the Dart
 * analyzer, so frames never cross the platform channel and the Dart image stream receives nothing. The analyzer must
 * close every {@link androidx.camera.core.ImageProxy} it gets. Set it before the stream starts, e.g. in the
 * activity's onCreate.
 */
public final class ExternalImageAnalyzer {
  private static volatile ImageAnalysis.Analyzer analyzer;
  private static ExecutorService executor;

  private ExternalImageAnalyzer() {}

  public static void set(@Nullable ImageAnalysis.Analyzer externalAnalyzer) {
    analyzer = externalAnalyzer;
  }

  @Nullable
  public static ImageAnalysis.Analyzer get() {
    return analyzer;
  }

  static synchronized ExecutorService executor() {
    if (executor == null) {
      executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "ExternalImageAnalyzer"));
    }
    return executor;
  }
}
