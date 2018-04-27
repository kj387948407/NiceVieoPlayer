package com.uama.vieoplayer.example;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.uama.videoplayer.AutoFullCallBack;
import com.uama.videoplayer.AutoFullVideoPlayerController;
import com.uama.videoplayer.NiceUtil;
import com.uama.videoplayer.NiceVideoPlayer;
import com.uama.videoplayer.NiceVideoPlayerManager;
import com.uama.vieoplayer.R;

/**
 * Created by Jin on 2017/12/13.
 * Description
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ScrollViewAutoFullScreenActivity extends AppCompatActivity {
	
	private NiceVideoPlayer mNiceVideoPlayer;
	private ScrollView mScrollView;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uama_activity);
		
		init();
		trigger();
	}
	
	private void init() {
		mNiceVideoPlayer = (NiceVideoPlayer) findViewById(R.id.nice_video_player);
		mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
		String videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
		mNiceVideoPlayer.setUp(videoUrl, null);
		AutoFullVideoPlayerController controller = new AutoFullVideoPlayerController(this, new AutoFullCallBack() {
			@Override
			public void back() {
				finish();
			}
		});
		controller.setLenght(98000);
		Glide.with(this)
				.load("http://pic.qiantucdn.com/58pic/19/73/22/570f6abca6f01_1024.jpg")
				.placeholder(R.drawable.img_default)
				.crossFade()
				.into(controller.imageView());
		mNiceVideoPlayer.setController(controller);
		mNiceVideoPlayer.start();
		mNiceVideoPlayer.enterFullScreen();
	}
	
	private void trigger() {
		mScrollView = (ScrollView) findViewById(R.id.scroll_view);
		mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
			@Override
			public void onScrollChange(View view, int i, int i1, int i2, int i3) {
				int[] location = new int[2];
				mNiceVideoPlayer.getLocationInWindow(location); //获取在当前窗口内的绝对坐标

				if (location[1] < 0) {
					if (!mNiceVideoPlayer.isIdle()) {
						if (!mNiceVideoPlayer.isTinyWindow()) {
							Log.i("msg", "获取在当前窗口内的绝对坐标" + location[0] + "####----####" + location[1]);
							mNiceVideoPlayer.enterTinyWindow();
						}
					}
				} else {
					if (mNiceVideoPlayer.isTinyWindow()) {
						Log.i("msg", "获取在当前窗口内的绝对坐标" + location[0] + "####----####" + location[1]);
						mNiceVideoPlayer.exitTinyWindow();
					}
				}
			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
	}
	
	@Override
	public void onBackPressed() {
		// 横竖屏状态
		boolean isProtrait = NiceUtil.isScreenOrientationPortrait(this);
		if (isProtrait) {
			finish();
		} else {
			NiceUtil.scanForActivity(this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
}
