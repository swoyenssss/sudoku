package game;

public class Coordinate {
	private int x;
	private int y;
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Coordinate) {
			Coordinate other2 = (Coordinate)other;
			if(this.x==other2.x && this.y==other2.y) return true;
		}
		return false;
	}
}
