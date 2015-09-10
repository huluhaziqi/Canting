package cn.caimatou.canting.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLResourcesUtil;


public class IndexBar extends View {
	private char[] mIndexes;

	private int mIndexSize;
	private SectionIndexer mSectionIndexter = null;
	private ListView mListView;
	private TextView mDialogText;

	private Paint mIndexPaint;

	public IndexBar(Context context) {
		super(context);
		init();
	}

	public IndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mIndexes = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '*' };
		mIndexSize = mIndexes.length;
		mIndexPaint = new Paint();
		mIndexPaint.setTextSize(20);
		mIndexPaint.setTextAlign(Paint.Align.CENTER);
		setBackgroundResource(R.color.gray3);
	}
	
	private boolean isFriendIndexes = false;
	public void friendIndexes() {
		mIndexes = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '*' };
		mIndexSize = mIndexes.length;
		isFriendIndexes = true;
	}

	public IndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setListView(ListView listView) {
		mListView = listView;
		mSectionIndexter = (SectionIndexer) listView.getAdapter();
	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setBackgroundResource(R.color.gray4);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			setBackgroundResource(R.color.gray3);
		}
		int i = (int) event.getY();
		int idx = i / (getMeasuredHeight() / mIndexSize);
		if (idx >= mIndexes.length) {
			idx = mIndexes.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			mDialogText.setVisibility(View.VISIBLE);
			String curChar = "" + mIndexes[idx];
			mDialogText.setText(curChar);
			if (isFriendIndexes) {
//				if (NeteaseMusicApplication.getInstance().getString(R.string.recentContactPeopleSymbol).equals((curChar))) {
//					mDialogText.setTextSize(40);
//					mDialogText.setPadding(mDialogText.getPaddingLeft(), mDialogText.getPaddingTop(), mDialogText.getPaddingRight(), (mDialogText.getPaddingTop() * 2));
//					mListView.setSelection(0);
//					return true;
//				} else {
					mDialogText.setPadding(mDialogText.getPaddingLeft(), mDialogText.getPaddingTop(), mDialogText.getPaddingRight(), mDialogText.getPaddingTop());
					mDialogText.setTextSize(50);
//				}
			}
			if (mSectionIndexter == null) {
				mSectionIndexter = (SectionIndexer) mListView.getAdapter();
			}
			int position = mSectionIndexter.getPositionForSection(mIndexes[idx]);
			if (position >= 0) {
				mListView.setSelection(position);
			}
		} else {
			mDialogText.setVisibility(View.INVISIBLE);
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		float widthCenter = getMeasuredWidth() / 2;
		float height = getMeasuredHeight() * 1.0f / mIndexSize;
		for (int i = 0; i < mIndexes.length; i++) {
			float textStartY = i * height;
			canvas.save();
			canvas.translate(0, textStartY);
			canvas.drawText(String.valueOf(mIndexes[i]), widthCenter, (height - mIndexPaint.ascent()) / 2, mIndexPaint);
			canvas.restore();
		}
		super.onDraw(canvas);
	}
}
