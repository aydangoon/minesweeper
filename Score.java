public class Score {

  private final String left;
  private final int right;

  public Score(String left, int right) {
    this.left = left;
    this.right = right;
  }

  public String getLeft() { 
	  return left; 
  }
  public int getRight() { 
	  return right; 
  }     
  public String toString() {
	  return left + ": " + right;
  }
}