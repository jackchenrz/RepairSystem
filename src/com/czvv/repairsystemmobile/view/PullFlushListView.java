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
	 *  ˢ�µ�view
	 */
	private LinearLayout refreshView;

	@ViewInject(R.id.refresh_header_progressbar)
	/**
	 * ˢ��ʱ�Ľ�����
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

	// refreshView���ֵĸ߶�
	private int refreshViewHeight;

	/**
	 * ��һ���ɼ�����Ŀ
	 */
	protected int firstItem;

	private void initView() {
		refreshRoot = (LinearLayout) View.inflate(getContext(),
				R.layout.refresh_header, null);
		ViewUtils.inject(this, refreshRoot);

		addHeaderView(refreshRoot);

		initAnimation();
		// �����Ƕ���0����˼�ǣ����Ը�view,��С�������ƣ���view����Ҫ���Ҫ���
		refreshView.measure(0, 0);
		refreshViewHeight = refreshView.getMeasuredHeight();
		// ����padding��ֵ������ˢ�µĲ���
		refreshView.setPadding(0, -refreshViewHeight, 0, 0);

		// ���û����ļ���
		this.setOnScrollListener(scrollListener);
	}

	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// OnScrollListener.SCROLL_STATE_IDLE; // ����״̬
			// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL; // ��������
			// OnScrollListener.SCROLL_STATE_FLING; // û�д��������ڻ���

			int lastPosition = view.getLastVisiblePosition();

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastPosition == getCount() - 1 && !isAddMore) {
				// ��ǰ�ǿ���״̬��ͬʱ���ɼ��������һ����Ŀ,���ظ���
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
	 * ���ֶ����ĳ�ʼ��
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
	 * ����ˢ�µ�״ֵ̬
	 */
	private final int state_down_flush = 100;
	/**
	 * �ͷ�ˢ�µ�״ֵ̬
	 */
	private final int state_relase_flush = 101;
	/**
	 * ����ˢ�µ�״ֵ̬
	 */
	private final int state_ing_flush = 102;

	/**
	 * ��ǰ��״ֵ̬�� Ĭ��Ϊ ����ˢ��
	 */
	private int currState = state_down_flush;
	/**
	 * listView�ĸ߶�
	 */
	private int listViewHeight = 0;

	/**
	 * ��һ��moveY�¼�
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
			// //�õ�rollView����Ļ��λ��
			// rollView.getLocationInWindow(winLoc);
			// rollViewHeight = winLoc[1];
			// // ֻ�е��ֲ�����Ļ�ĸ߶ȴ��ڵ���listView����Ļ�ĸ߶�ֵʱ������ʱ��ˢ��ͷ�ĸ߶ȲŻ��ܵ�Ӱ��
			//
			// if(rollViewHeight >= listViewHeight){

			if (firstMoveY == 0) {// ��ʼ����ʱ����һ��move�¼�
				firstMoveY = (int) ev.getY();
				break;
			}
			int moveY = (int) ev.getY();
			int disY = moveY - firstMoveY;
			if (disY > 0) {

				int padTop = -refreshViewHeight + disY;

				// �ɼ����ǣ���һ����Ŀ��ͬʱ����ָ�����ƶ�����ʱ���Ÿı�padTop
				if (firstItem == 0 && disY > 0) {
					refreshView.setPadding(0, padTop, 0, 0);
					// �����ǰ������ˢ�£��ң�padingTopֵ >0 ��ô��Ӧ��Ϊ�ͷ�ˢ��
					if (currState == state_down_flush && padTop > 0) {
						currState = state_relase_flush;
						flushState();
					}
					// �����ǰ���ͷ�ˢ�£��ң�paddingTopֵ < 0 ����ô��Ӧ��Ϊ ����ˢ��
					if (currState == state_relase_flush && padTop < 0) {
						currState = state_down_flush;
						flushState();
					}
					// ���padTopֵ�����˸ı䣬�������¼������ٽ��¼����ݸ�listview��
					if(padTop != 0){
						return false;
					}
				}
				// }
			}
			break;
		case MotionEvent.ACTION_UP:
			// ���������ˢ�£�˵���������Ĳ��࣬���ֺ󣬸�ԭ
			if (currState == state_down_flush) {
				refreshView.setPadding(0, -refreshViewHeight, 0, 0);
			}
			// ������ͷ�ˢ�£����ֺ󣬿�ʼˢ��
			if (currState == state_relase_flush) {
				// ��ʾˢ��ͷ
				refreshView.setPadding(0, 0, 0, 0);
				// ��Ϊ����ˢ��
				currState = state_ing_flush;
				flushState();
			}
			firstMoveY = 0;
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ˢ��״̬
	 */
	private void flushState() {
		refresh_header_time.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		switch (currState) {
		case state_relase_flush:// ������ˢ�£�����ͷ�ˢ��
			refresh_header_text.setText("�ͷ�ˢ��");
			refresh_header_imageview.startAnimation(down2Up);
			break;
		case state_down_flush:// ���ͷ�ˢ�£��������ˢ��
			refresh_header_text.setText("����ˢ��");
			refresh_header_imageview.startAnimation(up2Down);
			break;
		case state_ing_flush:// ���ͷ�ˢ�£��������ˢ��
			refresh_header_text.setText("����ˢ��");

			refresh_header_imageview.clearAnimation(); // �������
			refresh_header_imageview.setVisibility(View.GONE);

			// ��ʾ������
			refresh_header_progressbar.setVisibility(View.VISIBLE);
			// ִ��ˢ�µĶ���

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
	 * ˢ�����
	 */
	public void flushFinish() {

		if (isAddMore) {
			// ��ʼ����
			isAddMore = false;
			footView.setPadding(0, -footViewHeight, 0, 0);
		} else {
			// �ָ���ʼֵ
			refresh_header_progressbar.setVisibility(INVISIBLE);
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_text.setText("����ˢ��");

			// ״ֵ̬
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
		 * ����ˢ��ʱ���Ļص����� ��ˢ������Ժ󣬵���flushFinish();
		 */
		void onReFlush();

		/**
		 * ��������Ҫ���ظ���ʱ�Ļص����� ˢ������Ժ󣬵���flushFinish();
		 */
		void onLoadingMore();
	}

}
