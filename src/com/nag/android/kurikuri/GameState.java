package com.nag.android.kurikuri;

import android.view.View;
import android.widget.ImageView;

class GameState implements State{

	private ImageView target;
	private final ResourceManager rm;
	private final Board board;

	public GameState(ResourceManager rm){
		this.rm = rm;
		this.board = rm.getBoard();
		init();
	}
	private void init(){
		rm.getSignBoard().setVisibility(View.GONE);
		rm.getScoreBoard().setVisibility(View.GONE);
		int taps = rm.getProperties().getTaps4Shuffle();
		int length = rm.getProperties().getDragLength4Shuffle();
		board.shuffle(taps, length);
		rm.getScore().start(taps, length);//TODO
	}

	@Override
	public State actionDown(float sx, float sy) {
		down(sx, sy);
		return this;
	}

	@Override
	public void actionMove(float sx, float sy) {
		if(!move(sx, sy)){
			rm.getScore().countUp();
		}
	}

	@Override
	public State actionUp(float sx, float sy) {
		if(up(sx, sy)){
			rm.getScore().finish();
			return new FinishState(rm);
		}else{
			rm.getScore().countUp();
			return this;
		}
	}

	private void down(float sx, float sy){
		if(board.isInArea(sx, sy)){
			target = board.get(sx, sy);
		}
	}

	private boolean move(float sx, float sy){
		if(target!=null){
			if(board.isInArea(sx,sy)){
				if(target!=board.get(sx,sy)){
					board.swapPiece(target, sx, sy);
				}
				target.setX(sx - target.getWidth()/2);
				target.setY(sy - target.getWidth()/2);
				return true;
			}else{
				board.movePiece2HomePostion(target);
				target = null;
				return false;
			}
		}
		return true;
	}

	private boolean up(float sx, float sy){
		if(target!=null){
			board.movePiece2HomePostion(target);
			target = null;
			if(board.isCompleted()){
				board.dance(new TurnDance());
				return true;
			}
		}
		return false;
	}
}
