/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipc_app.mlkit.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.camera.core.CameraSelector;
import com.google.android.gms.common.images.Size;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import pt.ipc_app.R;
import pt.ipc_app.mlkit.CameraSource;

/** Utility class to retrieve shared preferences. */
public class PreferenceUtils {

  private static final int POSE_DETECTOR_PERFORMANCE_MODE_FAST = 1;

  static void saveString(Context context, @StringRes int prefKeyId, @Nullable String value) {
    PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putString(context.getString(prefKeyId), value)
        .apply();
  }

  @Nullable
  public static CameraSource.SizePair getCameraPreviewSizePair(Context context, int cameraId) {
    Preconditions.checkArgument(
        cameraId == CameraSource.CAMERA_FACING_BACK
            || cameraId == CameraSource.CAMERA_FACING_FRONT);
    String previewSizePrefKey;
    String pictureSizePrefKey;
    if (cameraId == CameraSource.CAMERA_FACING_BACK) {
      previewSizePrefKey = context.getString(R.string.pref_key_rear_camera_preview_size);
      pictureSizePrefKey = context.getString(R.string.pref_key_rear_camera_picture_size);
    } else {
      previewSizePrefKey = context.getString(R.string.pref_key_front_camera_preview_size);
      pictureSizePrefKey = context.getString(R.string.pref_key_front_camera_picture_size);
    }

    try {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
      return new CameraSource.SizePair(
          Size.parseSize(sharedPreferences.getString(previewSizePrefKey, null)),
          Size.parseSize(sharedPreferences.getString(pictureSizePrefKey, null)));
    } catch (Exception e) {
      return null;
    }
  }

  @RequiresApi(VERSION_CODES.LOLLIPOP)
  @Nullable
  public static android.util.Size getCameraXTargetResolution(Context context, int lensfacing) {
    Preconditions.checkArgument(
        lensfacing == CameraSelector.LENS_FACING_BACK
            || lensfacing == CameraSelector.LENS_FACING_FRONT);
    String prefKey =
        lensfacing == CameraSelector.LENS_FACING_BACK
            ? context.getString(R.string.pref_key_camerax_rear_camera_target_resolution)
            : context.getString(R.string.pref_key_camerax_front_camera_target_resolution);
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    try {
      return android.util.Size.parseSize(sharedPreferences.getString(prefKey, null));
    } catch (Exception e) {
      return null;
    }
  }

  public static PoseDetectorOptionsBase getPoseDetectorOptionsForLivePreview(Context context) {
    int performanceMode =
        getModeTypePreferenceValue(
            context,
            R.string.pref_key_live_preview_pose_detection_performance_mode,
            POSE_DETECTOR_PERFORMANCE_MODE_FAST);
    boolean preferGPU = preferGPUForPoseDetection(context);
    if (performanceMode == POSE_DETECTOR_PERFORMANCE_MODE_FAST) {
      PoseDetectorOptions.Builder builder =
          new PoseDetectorOptions.Builder().setDetectorMode(PoseDetectorOptions.STREAM_MODE);
      if (preferGPU) {
        builder.setPreferredHardwareConfigs(PoseDetectorOptions.CPU_GPU);
      }
      return builder.build();
    } else {
      AccuratePoseDetectorOptions.Builder builder =
          new AccuratePoseDetectorOptions.Builder()
              .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE);
      if (preferGPU) {
        builder.setPreferredHardwareConfigs(AccuratePoseDetectorOptions.CPU_GPU);
      }
      return builder.build();
    }
  }


  public static boolean preferGPUForPoseDetection(Context context) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    String prefKey = context.getString(R.string.pref_key_pose_detector_prefer_gpu);
    return sharedPreferences.getBoolean(prefKey, true);
  }


  /**
   * Mode type preference is backed by {@link android.preference.ListPreference} which only support
   * storing its entry value as string type, so we need to retrieve as string and then convert to
   * integer.
   */
  private static int getModeTypePreferenceValue(
      Context context, @StringRes int prefKeyResId, int defaultValue) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    String prefKey = context.getString(prefKeyResId);
    return Integer.parseInt(sharedPreferences.getString(prefKey, String.valueOf(defaultValue)));
  }

  public static boolean isCameraLiveViewportEnabled(Context context) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    String prefKey = context.getString(R.string.pref_key_camera_live_viewport);
    return sharedPreferences.getBoolean(prefKey, false);
  }


  private PreferenceUtils() {}
}
