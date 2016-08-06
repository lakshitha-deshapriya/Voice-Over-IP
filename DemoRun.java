/***
** Here demonstrate functionality of VOIP 
** just need to run with pear IP address
***/

import java.net.* ;

public class DemoRun{
	
	public static void main(String[] args){
	 	//check whether user has input enough argument in terminal  
		if( args.length != 1 ){
			System.out.println( "usage:peer ip" ) ;
			return ;
		} 
		
		
		//create object of CaptureAudio(client) & PlayAudio (server) and start work
		CaptureAudio cap = new CaptureAudio(args[0]);
		PlayAudio play = new PlayAudio(args[0]);
		
		cap.configeCapture();
		play.configePlay();
		
		cap.start();
		play.start();		
	}
}