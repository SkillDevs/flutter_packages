// Copyright 2013 The Flutter Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import CoreVideo
import Foundation

/// SkillDevs fork: lets the app consume image stream frames natively instead of in Dart.
///
/// While a consumer is set, a started image stream hands its frames to it, on the capture queue, and the Dart image
/// stream receives nothing. A frame is resized (the stream's custom resolution) and handed over only when `wantsFrame`
/// returns true; the others are dropped untouched. The pixel buffer is 32BGRA and valid during the `consume` call. Set
/// the consumer before the stream starts, e.g. in the app delegate.
public enum ExternalImageStream {
  public struct Consumer {
    /// Called for every frame, so it must be cheap
    public let wantsFrame: () -> Bool
    /// The frame and its presentation time in nanoseconds
    public let consume: (CVPixelBuffer, Int64) -> Void

    public init(wantsFrame: @escaping () -> Bool, consume: @escaping (CVPixelBuffer, Int64) -> Void) {
      self.wantsFrame = wantsFrame
      self.consume = consume
    }
  }

  private static let lock = NSLock()
  private static var _consumer: Consumer?

  public static var consumer: Consumer? {
    get {
      lock.lock()
      defer { lock.unlock() }
      return _consumer
    }
    set {
      lock.lock()
      defer { lock.unlock() }
      _consumer = newValue
    }
  }
}
