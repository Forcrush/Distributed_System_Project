package dataSource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Stroke;

public class DataSource {
	private int id;//user id
	private int paintType;//the type of drawing
	private Color pencilcolor;
	private Color erasercolor;
	private Stroke pencilStroke;
	private Stroke eraserStroke;
	private String text;
	private String fonttype;
	private int bolder;
	private int fontsize;
	private Point startPoint;
	private Point endPoint;
	DataSource(){
		paintType=0;
		pencilcolor=Color.black;
		erasercolor=Color.white;
		pencilStroke = new BasicStroke(1.0f);
	    eraserStroke = new BasicStroke(5.0f);
	    fonttype = "Arial";
	    bolder = Font.PLAIN;
	    fontsize=12;
		startPoint=new Point(0,0);
		endPoint=new Point(0,0);
	
	}
	public DataSource(int id,int paintType,Point startPoint,Point endPoint,Color pencilcolor,Color earsercolor,Stroke pencilStroke,
			Stroke eraserStroke,String text,String fonttype,int bolder,int fontsize){
		this.id=id;
		this.paintType=paintType;
		this.pencilcolor=pencilcolor;
		this.erasercolor=earsercolor;
		this.pencilStroke=pencilStroke;
		this.eraserStroke=eraserStroke;
		this.text=text;
		this.fonttype=fonttype;
		this.bolder=bolder;
		this.fontsize=fontsize;
		this.startPoint=startPoint;
		this.endPoint=endPoint;
	
	}
	
	public int getId() {
		return id;
	}
	public int getPaintType() {
		return paintType;
	}
	public Color getPencilcolor() {
		return pencilcolor;
	}
	public Color getEarsercolor() {
		return erasercolor;
	}
	public Stroke getPencilStroke() {
		return pencilStroke;
	}
	public Stroke getEraserStroke() {
		return eraserStroke;
	}
	public String getText() {
		return text;
	}
	public String getFonttype() {
		return fonttype;
	}
	public int getBolder() {
		return bolder;
	}
	public int getFontsize() {
		return fontsize;
	}
	public Point getStartPoint() {
		return startPoint;
	}
	public Point getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

}
