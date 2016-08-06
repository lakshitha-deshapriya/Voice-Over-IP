import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;

//define new own packet format to send packet no
public class PacketFormat implements Serializable {
    
    public byte tempBuffer[] = new byte[500];
	//sequence number for Packet
	private static long packetSeqNo = 0;
    public long seqNo; 
    public byte[] sendBuf; //for buffer data
    public ArrayList<Long> list; 

     //add sequence number to the packet 
    public PacketFormat(long seqNo1){  
        this.seqNo = seqNo1;
    }
   
    //return the sequence number     
    public long getSeqNo(){
        return this.seqNo;
    }
    
	//given sequence number set to sequence number in this class
    public static void setSeqNo(long SeqNo2){
        packetSeqNo = SeqNo2;
    }
     
	//provided TempBuffer set to  tempBuffer in this class     
    public void setTempBuffer(byte[] tempbuffer1){
        this.tempBuffer = tempbuffer1;
    }
	
	//return the tempBuffer when needed
    public byte[] getTempBuffer(){
		return this.tempBuffer;
    }
    
    //serialize the byte stream to the object 
    public static byte[] serialize(Object obt) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos))){
                oos.writeObject(obt);
            }
            return baos.toByteArray();
        }
    }
    
    //de-serialize the object to the byte stream
    public static Object deserialize(byte[] bytes) throws Exception {
        try(ByteArrayInputStream baos = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream oos = new ObjectInputStream(new BufferedInputStream(baos))){
				return oos.readObject();
            }
        }
    } 	
}

 
