import java.util.ArrayList;
import java.util.Collections;

//Store packet sequence numbers in array list and find packet loss
public class Record {    
	public ArrayList<Long> list; 

	//create array list 
	public Record(){
		this.list = new ArrayList<Long>();
	}

	//Add number to list that create sequentially 
	public void addPktNo(Long seq){
		this.list.add(seq);
	}

	//process store data in array to find data after some considerable time gap
	public Long getLsCount(){
		Collections.sort(list);
		return list.get(list.size()-1)- list.get(0) - list.size() + 1;
	}	
}
