package lab1;

public class Main {

  public static void main(String[] args) {
    String s = "";
    for (int i = 0; i < 1000; ++i) {
      s += " " + String.valueOf(i);
    }
    System.out.println(s.length());
  }
}
