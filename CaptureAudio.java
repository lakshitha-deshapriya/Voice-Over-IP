import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine;
import java.net.* ;

public class CaptureAudio extends AudioOut {
	
	int port = 20000;
	TargetDataLine targetDataLine;
	byte tempBuffer[] = new byte[432];
	boolean stopCapture;
	public static DatagramSocket socket;
	DatagramPacket packet;
	InetAddress host;
	private static long sequenceNo=0;
	
	//get pear address 
	public CaptureAudio(String ip){
		try{
			this.stopCapture=false;
			host = InetAddress.getByName(ip);		
		}catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	//setup capturing audio	
	public void configeCapture(){
		try{
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		}catch(LineUnavailableException e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	//packet sending method with serialization using new packet format and sequence number
	public void sendAudio(byte[] data){
		
		byte[] sendBuf=new byte[432];
		
		try{			
			PacketFormat myPacket=new PacketFormat(++sequenceNo);
			myPacket.setTempBuffer(data);			
            sendBuf = PacketFormat.serialize(myPacket);				
			socket = new DatagramSocket() ;
			packet = new DatagramPacket(sendBuf, sendBuf.length, host, port);		
			socket.send( packet ); 			
		}catch(IOException e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	//threading 
	@Override
	public void run (){
		try {
            int readCount;
			
			//capture sound into buffer and send to client
            while (!this.stopCapture) {
				readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  
				sendAudio(tempBuffer);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
	}
	
	//to start and stop capturing
	public void startCapture(){
		this.stopCapture=false;
	}	
	
	public void stopCapture(){
		this.stopCapture=true;
	}
	
}