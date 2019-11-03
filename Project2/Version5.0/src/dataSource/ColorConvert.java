package dataSource;


import java.awt.Color;

public class ColorConvert {

public static Color String2Color(String str) {

int i =   Integer.parseInt(str.substring(1),16);

return new Color(i);

}

public static String Color2String(Color color) {

String R = Integer.toHexString(color.getRed());

R = R.length()<2?('0'+R):R;

String B = Integer.toHexString(color.getBlue());

B = B.length()<2?('0'+B):B;

String G = Integer.toHexString(color.getGreen());

G = G.length()<2?('0'+G):G;

return '#'+R+B+G;

}

}
