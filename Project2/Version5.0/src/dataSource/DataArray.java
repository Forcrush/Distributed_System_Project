package dataSource;


import java.util.ArrayList;
import java.util.Iterator;

public class DataArray {

	/**
	 * @param args
	 */
	public ArrayList<DataSource> array=null;
	public DataArray(){
		array=new ArrayList<DataSource>();
	}
	public synchronized void addData(DataSource dataSource){
		array.add(dataSource);
	}
	public Iterator<DataSource> iterator(){
		return array.iterator();
	}
}
