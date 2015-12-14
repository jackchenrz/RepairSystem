package com.czvv.repairsystemmobile.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PullFlushListView extends ListView {

	private LinearLayout refreshRoot;

	public PullFlushListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PullFlushListView(Context context) {
		super(context);
		initView();
	}

	@ViewInject(R.id.refresh_header_view)
	/**
	 *  刷新的view
	 */
	private LinearLayout refreshView;

	@ViewInject(R.id.refresh_header_progressbar)
	/**
	 * 刷新时的进度条
	 */
	private ProgressBar refresh_header_progressbar;

	@ViewInject(R.id.refresh_header_text)
	private TextView refresh_header_text;

	@ViewInject(R.id.refresh_header_imageview)
	private ImageView refresh_header_imageview;
	@ViewInject(R.id.refresh_header_time)
	private TextView refresh_header_time;

	private RotateAnimation down2Up;

	private RotateAnimation up2Down;

	// refreshView布局的高度
	private int refreshViewHeight;

	/**
	 * 第一个可见的条目
	 */
	protected int firstItem;

	private void initView() {
		refreshRoot = (LinearLayout) View.inflate(getContext(),
				R.layout.refresh_header, null);
		ViewUtils.inject(this, refreshRoot);

		addHeaderView(refreshRoot);

		initAnimation();
		// 参数是二个0，意思是，不对该view,大小做出限制，该view，想要多大，要多大
		refreshView.measure(0, 0);
		refreshViewHeight = refreshView.getMeasuredHeight();
		// 设置padding的值来隐藏刷新的布局
		refreshView.setPadding(0, -refreshViewHeight, 0, 0);

		// 设置滑动的监听
		this.setOnScrollListener(scrollListener);
	}

	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// OnScrollListener.SCROLL_STATE_IDLE; // 空闲状态
			// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL; // 触摸滑动
			// OnScrollListener.SCROLL_STATE_FLING; // 没有触摸，还在滑动

			int lastPosition = view.getLastVisiblePosition();

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastPosition == getCount() - 1 && !isAddMore) {
				// 当前是空闲状态，同时，可见的是最后一个条目,加载更多
				isAddMore = false;
				// handler.sendEmptyMessageDelayed(22, 2000);
				if (onFlushListener != null) {
					onFlushListener.onLoadingMore();
				}
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			firstItem = firstVisibleItem;

		}
	};

	/**
	 * 两种动画的初始化
	 */
	private void initAnimation() {
		down2Up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		down2Up.setDuration(300);
		down2Up.setFillAfter(true);

		up2Down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		up2Down.setDuration(300);
		up2Down.setFillAfter(true);
	}

	/**
	 * 下拉刷新的状态值
	 */
	private final int state_down_flush = 100;
	/**
	 * 释放刷新的状态值
	 */
	private final int state_relase_flush = 101;
	/**
	 * 正在刷新的状态值
	 */
	private final int state_ing_flush = 102;

	/**
	 * 当前的状态值， 默认为 下拉刷新
	 */
	private int currState = state_down_flush;
	/**
	 * listView的高度
	 */
	private int listViewHeight = 0;

	/**
	 * 第一个moveY事件
	 */
	private int firstMoveY;
	private int downY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//
			// int[] winLoc = new int[2];
			// //得到rollView在屏幕的位置
			// rollView.getLocationInWindow(winLoc);
			// rollViewHeight = winLoc[1];
			// // 只有当轮播在屏幕的高度大于等于listView在屏幕的高度值时，，此时，刷新头的高度才会受到影响
			//
			// if(rollViewHeight >= listViewHeight){

			if (firstMoveY == 0) {// 开始下拉时，第一个move事件
				firstMoveY = (int) ev.getY();
				break;
			}
			int moveY = (int) ev.getY();
			int disY = moveY - firstMoveY;
			if (disY > 0) {

				int padTop = -refreshViewHeight + disY;

				// 可见的是，第一个条目，同时，手指向下移动，此时，才改变padTop
				if (firstItem == 0 && disY > 0) {
					refreshView.setPadding(0, padTop, 0, 0);
					// 如果当前是下拉刷新，且，padingTop值 >0 那么，应改为释放刷新
					if (currState == state_down_flush && padTop > 0) {
						currState = state_relase_flush;
						flushState();
					}
					// 如果当前是释放刷新，且，paddingTop值 < 0 ，那么，应改为 下拉刷新
					if (currState == state_relase_flush && padTop < 0) {
						currState = state_down_flush;
						flushState();
					}
					// 如果padTop值发生了改变，就消费事件，不再将事件传递给listview。
					if(padTop != 0){
						return false;
					}
				}
				// }
			}
			break;
		case MotionEvent.ACTION_UP:
			// 如果是下拉刷新，说明拉出来的不多，松手后，复原
			if (currState == state_down_flush) {
				refreshView.setPadding(0, -refreshViewHeight, 0, 0);
			}
			// 如果是释放刷新，松手后，开始刷新
			if (currState == state_relase_flush) {
				// 显示刷新头
				refreshView.setPadding(0, 0, 0, 0);
				// 改为正在刷新
				currState = state_ing_flush;
				flushState();
			}
			firstMoveY = 0;
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新状态
	 */
	private void flushState() {
		refresh_header_time.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		switch (currState) {
		case state_relase_flush:// 从下拉刷新，变成释放刷新
			refresh_header_text.setText("释放刷新");
			refresh_header_imageview.startAnimation(down2Up);
			break;
		case state_down_flush:// 从释放刷新，变成下拉刷新
			refresh_header_text.setText("下拉刷新");
			refresh_header_imageview.startAnimation(up2Down);
			break;
		case state_ing_flush:// 从释放刷新，变成正在刷新
			refresh_header_text.setText("正在刷新");

			refresh_header_imageview.clearAnimation(); // 清除动画
			refresh_header_imageview.setVisibility(View.GONE);

			// 显示进度条
			refresh_header_progressbar.setVisibility(View.VISIBLE);
			// 执行刷新的动作

			// handler.sendEmptyMessageDelayed(33, 1000);
			if (onFlushListener != null) {
				onFlushListener.onReFlush();
			}

			break;

		}
	}

	private View footView;

	private int footViewHeight;

	private boolean isAddMore;

	/**
	 * 刷新完成
	 */
	public void flushFinish() {

		if (isAddMore) {
			// 开始加载
			isAddMore = false;
			footView.setPadding(0, -footViewHeight, 0, 0);
		} else {
			// 恢复初始值
			refresh_header_progressbar.setVisibility(INVISIBLE);
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_text.setText("下拉刷新");

			// 状态值
			currState = state_down_flush;

			refreshView.setPadding(0, -refreshViewHeight, 0, 0);
		}
	}

	private OnFlushListener onFlushListener;

	public void setOnFlushListener(OnFlushListener onFlushListener) {
		this.onFlushListener = onFlushListener;
	}

	public interface OnFlushListener {
		/**
		 * 下拉刷新时，的回调方法 ，刷新完成以后，调用flushFinish();
		 */
		void onReFlush();

		/**
		 * 上拉，需要加载更多时的回调方法 刷新完成以后，调用flushFinish();
		 */
		void onLoadingMore();
	}

}
