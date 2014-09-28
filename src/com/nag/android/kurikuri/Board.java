package com.nag.android.kurikuri;

import java.util.Arrays;
import java.util.Random;

import com.nag.android.kurikri.R;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.widget.ImageView;
import android.widget.RelativeLayout;

class Board {
	private static final int ANIMATION_RATE = 100;
	private static final int UNDEFINED_WIDTH = -1;

	private final Random rand = new Random();
	private ImageView[] pieces = null;
	private ImageView[] org = null;
	private int width;
	private int piece_width = UNDEFINED_WIDTH;
	private AppPlayer player;

	private int locx(int i){return i%width;}
	private int locy(int i){return i/width;}
	private int pos(int x, int y){return y*width+x;}

	Board(Context context, AppPlayer player, int width){
		this.player = player;
		this.width = width;
		pieces =new ImageView[width * width];
		for(int i=0; i<pieces.length; ++i){
			pieces[i] = new ImageView(context);
		}
	}

	boolean isInArea(float sx, float sy){
		return sx>=0.0 && sx < (piece_width * width) && sy>=0.0 && sy < (piece_width * width);
	}

	ImageView get(float sx, float sy){
		return pieces[pos((int)sx/piece_width, (int)sy/piece_width)];
	}

	boolean isCompleted(){
		return Arrays.equals(pieces, org);
	}

	void dance(Dance d){
		d.dance(pieces);
	}

	void swapPiece(ImageView target, float sx, float sy){
		int pos1 = pos((int)sx/piece_width, (int)sy/piece_width);
		int pos2 = findPiece(pieces, target);
		movePiece(pieces[pos1], locx(pos2), locy(pos2));
		swap(pieces, pos1, pos2);
		player.play(AppPlayer.CONTENTSID.KURI, locx(pos1) / width, 1.0f - locx(pos1) / width);
	}

	void movePiece2HomePostion(ImageView target){
		int i=findPiece(pieces, target);
		movePiece(target, locx(i), locy(i));
	}

	private void movePiece(ImageView icon, int x, int y){
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(icon
				, PropertyValuesHolder.ofFloat("translationX", icon.getX(), x*icon.getWidth()) 
				, PropertyValuesHolder.ofFloat("translationY", icon.getY(), y*icon.getWidth())); 
		objectAnimator.setDuration( ANIMATION_RATE );
		objectAnimator.start();
	}

	private int findPiece(ImageView[]icons, ImageView target){
		return Arrays.asList(icons).indexOf(target);
	}

	public void init(RelativeLayout layout){
		if(piece_width==UNDEFINED_WIDTH){
			piece_width = layout.getWidth()/width;
			load(layout, BitmapFactory.decodeResource(layout.getContext().getResources(), R.drawable.default_image));
		}
	}

	public void load(RelativeLayout layout, Bitmap source, int angle){
		load(layout, createSquareBitmap(source, angle));
	}

	private void load(RelativeLayout layout, Bitmap source){
		layout.removeAllViews();
		initPanel(source);
		for(ImageView imageview : pieces){
			layout.addView(imageview);
		}
		org = pieces.clone();
	}

	private Bitmap createSquareBitmap(Bitmap source, int angle){
		int src_size = 0;
		int offset_w = 0;
		int offset_h = 0;
		Matrix matrix = new Matrix();
		if(source.getWidth()>source.getHeight()){
			src_size = source.getHeight();
			offset_w=-(source.getWidth()-source.getHeight())/2;
		}else{
			src_size = source.getWidth();
			offset_h=-(source.getHeight()-source.getWidth())/2;
		}
		matrix.postTranslate(offset_w, offset_h);
		matrix.postTranslate(-src_size/2, -src_size/2);
		matrix.postRotate(angle);
		matrix.postTranslate(src_size/2, src_size/2);
		Bitmap ret = Bitmap.createBitmap(src_size, src_size, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(ret);
		c.drawBitmap(source, matrix, null);
		return ret;
	}

	private void locatePiece(ImageView[] pieces) {
		for(int i=0; i< pieces.length; ++i){
			pieces[i].setX(piece_width*locx(i));
			pieces[i].setY(piece_width*locy(i));
		}
	}

	private void initPanel(Bitmap source){
		int source_width = source.getWidth()/width;
		Rect dst = new Rect(0, 0, piece_width, piece_width);
		for(int i=0; i< pieces.length; ++i){
			Bitmap bmp = Bitmap.createBitmap(piece_width, piece_width, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bmp);
			Rect src = new Rect(0, 0, source_width, source_width);
			src.offset(source_width*locx(i), source_width*locy(i));
			c.drawBitmap(source, src, dst, null);
			pieces[i].setImageBitmap(bmp);
		}
		locatePiece(pieces);
	}

	public void shuffle(int taps, int length){
		assert(pieces.length==org.length);
		System.arraycopy(org, 0, pieces, 0, pieces.length);
		for(int i=0;i<taps; ++i){
			drag(pieces, length);
		}
		locatePiece(pieces);
	}

	public void reset(){
		if(org!=null){
			assert(pieces.length==org.length);
			System.arraycopy(org, 0, pieces, 0, pieces.length);
			locatePiece(pieces);
		}
	}

	private void drag(ImageView[]pieces, int length){
		int cur = rand.nextInt(pieces.length);
		int prev = -1;
		for(int i=0;i<length;++i){
			int next = selectNext(pieces.length, cur, prev);
			swap(pieces, cur, next);
			prev = cur;
			cur = next;
		}
	}

	private void swap(ImageView[]icons, int cur, int next){
		ImageView temp = icons[cur];
		icons[cur] = icons[next];
		icons[next] = temp;
	}

	static enum DIRECTION{UP,DOWN,LEFT,RIGHT};

	private int selectNext(int total, int cur, int prev){
		while(true){
			switch(DIRECTION.values()[rand.nextInt(4)]){
			case UP:
				{
					int next = cur-width;
					if(next>0 && next!=prev){
						return next;
					}
				}
				break;
			case DOWN:
				{
					int next = cur+width;
					if(next<total && next!=prev){
						return next;
					}
				}
				break;
			case LEFT:
				{
					int next = cur- 1;
					if(next%width!=0 && next!=prev){
						return next;
					}
				}
				break;
			case RIGHT:
				{
					int next = cur + 1;
					if(next%width!=0 && next!=prev){
						return next;
					}
				}
				break;
			}
		}
	}
}

