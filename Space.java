
public class Space {
	
	private boolean isHidden;
	private boolean hasBomb;
	private boolean isFlagged;
	private int number;
	
	public Space() {
		isHidden = true;
		hasBomb = false;
		isFlagged = false;
		number = 0;
	}
	public void setFlag() {
		if(isHidden) {
			isFlagged = !isFlagged;
		}
	}
	public void setBomb() {
		if(!hasBomb) {
			hasBomb = true;
		}
	}
	public void setNumber(int n) {
		number = n;
	}
	public String doAction() {
		if(isHidden && !isFlagged) {
			isHidden = false;
		}
		return (hasBomb && !isFlagged) ? "game over" : "";
	}
	public boolean isHidden() {
		return isHidden;
	}
	public boolean hasBomb() {
		return hasBomb;
	}
	public int getNumber() {
		return number;
	}
	public boolean isFlagged() {
		return isFlagged;
	}
	
}
