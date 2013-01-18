// import processing.core.PImage;
import processing.core.*;

import processing.opengl.PGraphicsOpenGL;

import java.io.IOException;
import java.io.PrintWriter;
import java.awt.geom.*;

import org.openkinect.processing.*;
import org.openkinect.*;

import rwmidi.*;


////
/// add interface and clickable UI
/// add dynamically loading data/client info
@SuppressWarnings("serial")

public class bit08 extends PApplet{
//////FULL SCREEN HANDLER
	/*
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "bit08" });
	  }
	  */

	
	//
	AppProfile theAppProfile;
	PApplet pApp;
	
	// Kinect Library object
	Kinect kinect;

	/// depth threshhold params and display
	int threshold0 = 0;
	// foreground
	int threshold1 = 50;
	/// middleground
	int threshold2 = 645;
	/// background
	int threshold3 = 945; 

	int threshold4 = 845;
	int threshold5 = 1500;

	boolean displayThreshold = true;
	PFont SanSerif; /// font for display

	/// opacity for objects
	int objOpacity = 100;

	/// rotate values
	float a = 0;
	float flexRotate = 0.05f;
	float rotateRange = 2;
	boolean isSwinging = false;

	/// objects for animation display
	// Ring circle = new Ring();
	//DecayTriangle theTri  = new DecayTriangle();
	// PolygonBuilder theHex  = new PolygonBuilder();

	///// KINECT CONTROL VARS
	// Size of kinect image
	int w = 640;
	int h = 480;

	int skip = 17; /// number of pixels we'll actually render
	int kScale = 900; /// scaling of the kinect image

	/// kinect tilt vars
	float deg = 0; // Start at 0 degrees

	//// MIDI input
	MidiInput input;
	MidiOutput output;


	// We'll use a lookup table so that we don't have to repeat the math over and over
	float[] depthLookUp = new float[2048];


	////// SETUP //////////
	public void setup() {
	  
	  size(800,600,P3D);
	  
	  theAppProfile = theAppProfile.getInstance();
	  theAppProfile.SetPApp(this);
	  
	  // init font
	  SanSerif = createFont("SansSerif-14.vlw", 14); /// font for display
	  
	  kinect = new Kinect(this);
	  kinect.start();
	  kinect.enableDepth(true);
	  // We don't need the grayscale image in this example
	  // so this makes it more efficient
	  kinect.processDepthImage(false);
	  
	  /// midi input
	  input = RWMidi.getInputDevices()[0].createInput(this);
	  output = RWMidi.getOutputDevices()[0].createOutput();

	  // Lookup table for all possible depth values (0 - 2047)
	  for (int i = 0; i < depthLookUp.length; i++) {
	    depthLookUp[i] = rawDepthToMeters(i);
	  }
	  
	  // smooth();
	}
	/////////////////////////////////
	//// init MIDI tracking //////////
	////////////////////////////////////
	void controllerChangeReceived(rwmidi.Controller cntrl){
	 println("cc recieved");
	 println(": " + cntrl.getCC()) ;

	 
	 if(cntrl.getCC() == 2){
	    modThresh1(cntrl.getValue()); /// adjust threshold1
	  }
	  
	   if(cntrl.getCC() == 3){
	    modThresh2(cntrl.getValue()); /// adjust threshold1
	  }
	  
	   if(cntrl.getCC() ==4){
	    modThresh3(cntrl.getValue()); /// adjust threshold1
	  }
	  
	  if(cntrl.getCC() == 5){ //// adjust skip
	    modSkip(cntrl.getValue());
	  }
	  
	  if(cntrl.getCC() == 6){ /// adjus scale
	    modScale(cntrl.getValue());
	  }
	}

	/// notes
	void noteOnReceived(Note note) {
	  
	  println("note on " + note.getPitch());
	  if(note.getPitch() == 76){
	    // modThresh1(note.getPitch());
	  }
	  if(note.getPitch() == 77){
	    // modThresh2(note.getPitch());
	  }
	  
	  if(note.getPitch() == 78){
	    // modThresh3(note.getPitch());
	  }
	}
	void sysexReceived(rwmidi.SysexMessage msg) {
	  println("sysex " + msg);
	}

	/// end MIDI tracking //////////////
	////////////////////////////////////

	public void mousePressed() {
	  int ret = output.sendNoteOn(0, 3, 3);
	  ret = output.sendSysex(new byte[] {(byte)0xF0, 1, 2, 3, 4, (byte)0xF7});
	}

	////////////////////////////
	//// do the threshold display
	////////////////////////////////
	void doThreshDisplay(){
	    /// init colors
	    int greenFill = color(125,125,0, 65);
	    int redFill = color(255,0,0,65);
	    int blueFill = color(0,0,255, 85);
	    //
	    float spacing = 15;
	    // 
	    textFont(SanSerif);
	    /// hint(ENABLE_NATIVE_FONTS) ;
	    
	    /// position
	    float dPosY3 = 0-height/2;
	    float dPosY2 = 0-height/2 + spacing;
	    float dPosY1 = 0-height/2 + spacing + spacing;
	    
	    /// make the text flush with the display bar
	    float dPosX3 = 0-width/2 + (threshold3 * .5f) + 10f;
	    float dPosX2 = 0-width/2 + (threshold2 * .5f)  + 10f;
	    float dPosX1 = 0-width/2 + (threshold1 * .5f)  + 10f;
	    
	    // thresh 3 -- green squares
	    strokeWeight(1);
	    
	    fill(greenFill);
	    stroke(64,208,0);
	    rect(0-width/2, dPosY3, threshold3 * .5f, 10f);
	    fill(255,255,0);
	    text("BACKGROUND: " + threshold3 * .5, dPosX3, dPosY3 + 10);
	    
	    
	    /// thresh 2 -- red circles
	    fill(redFill);
	    stroke(204,0,0);
	    /// rotate(-15);
	    rect(0-width/2,dPosY2, threshold2 * .5f, 10f);
	    fill(204,0,0);
	    text("MIDDLEGROUND: " + threshold2 * .5f, dPosX2, dPosY2 + 10f);
	    
	        //// thresh 1 -- blue triangles
	    fill(blueFill);
	    stroke(0,24,255);
	    /// rotate(-15);
	    rect(0-width/2, dPosY1, threshold1 * .5f, 10f);
	    fill(0,24,255);
	    text("FOREGROUND: " + threshold1 * .5, dPosX1, dPosY1 + 10);
	    
	    


	}


	public void draw() {
	  
	  colorMode(RGB);

	  background(0,0,0);

	  // textMode(SCREEN);
	  //// text("Kinect FR: " + (int)kinect.getDepthFPS() + "\nProcessing FR: " + (int)frameRate,10,16);
	  // fill(255);

	  // Get the raw depth as array of integers
	  int[] depth = kinect.getRawDepth();

	  // Translate and rotate
	  translate(width/2,height/2,-50);
	  rotateY(a);
	  
	  //// run our matrix, integrate our skip function
	  for(int x=0; x<w; x+=skip) {
	    for(int y=0; y<h; y+=skip) {
	      
	      // old offset
	      int offset = x+y*w;
	      /// let's mirror it instead
	     //  int offset = w-x-1+y*w;

	      // Convert kinect data to world xyz coordinate
	      int rawDepth = depth[offset];

	      PVector v = depthToWorld(x,y,rawDepth);
	      /// println("THIS IS THE FUNKY HIT: " + rawDepth);

	      pushMatrix();
	      // Scale the entire thing
	      float factor = kScale;
	      translate(v.x*factor,v.y*factor,factor-v.z*factor);
	      
	      /// find depth threshhold and mess with it
	      ///// FOREGROUND
	       if (rawDepth < threshold1) {
	         /// rotation, opacity

	       //// MIDDLEGROUND  
	       } else if (rawDepth > threshold1 && rawDepth < threshold2){
	          //// x, y, diameter, opacity
	    	   fill(255,0,0);
	    	   rect(0,0,10,10);

	          
	       //// BACKGROUND
	       } else if (rawDepth > threshold2 && rawDepth < threshold3){
	           
	          // build our hex from the polygon class
	          //// number vertices, xpos, ypos, length, rotation, opacity
	          // theHex.doPolygon(4, 0, 0, 45, flexRotate*-1, objOpacity);
	          // theHex.update(flexRotate*-1);
	       }
	      popMatrix();
	    }
	  }
	 
	  /// figure out some way to link this to raw depth-- value between 400 and 2000;
	  flexRotate -= .075; /// increment rotation value, we use this for rotating the objects
	    
	  if (isSwinging == true){
	     float newRot = sin(flexRotate); /// get our rotation value
	     a = newRot/rotateRange; //// compress the rotation depending on the most populated threshold
	    
	  }
	  
	  /// set up display boxes
	  if(displayThreshold == true){
	    doThreshDisplay();
	  
	  }

	}


	// These functions come from: http://graphics.stanford.edu/~mdfisher/Kinect.html
	float rawDepthToMeters(int depthValue) {
	  if (depthValue < 2047) {
	    return (float)(1.0 / ((double)(depthValue) * -0.0030711016 + 3.3309495161));
	  }
	  return 0.0f;
	}

	PVector depthToWorld(int x, int y, int depthValue) {

	  final double fx_d = 1.0 / 5.9421434211923247e+02;
	  final double fy_d = 1.0 / 5.9104053696870778e+02;
	  final double cx_d = 3.3930780975300314e+02;
	  final double cy_d = 2.4273913761751615e+02;

	  PVector result = new PVector();
	  double depth =  depthLookUp[depthValue];//rawDepthToMeters(depthValue);
	  result.x = (float)((x - cx_d) * depth * fx_d);
	  result.y = (float)((y - cy_d) * depth * fy_d);
	  result.z = (float)(depth);
	  return result;
	}

	////////////////////////////////////
	///////// KEYBOARD COMMANDS ////////
	////////////////////////////////////


	public void keyPressed() {
	  if (key == CODED) {
	    ///// geometry per pixel
	    if (keyCode == UP) {
	      skip+=1;
	    } 
	    if (keyCode == DOWN) {
	      if (skip >= 5){
	      skip-=1;
	      }
	    }
	    ///// scale ratio (fits the kinect image to the canvas)
	    if (keyCode == LEFT) {
	      kScale-=100;
	    } 
	    if (keyCode == RIGHT) {
	      kScale+=100;
	    } 
	    
	  }
	  
	    ///// adjust Kinect tilt
	   if(keyPressed) {
	      if (key == 'a' || key == 'A') {
	        deg++;
	      } else if (key == 'z' || key == 'Z') {
	        
	        deg--;
	      }
	      deg = constrain(deg,0,30);
	      kinect.tilt(deg);
	      
	      ///// adjust depth threshold
	    } if (key == 's' || key == 'S') { /// thresh 1
	        if(threshold1 < threshold2){
	        threshold1 +=10;
	        println("threshold1: " + threshold1);
	        }
	      } if (key == 'x' || key == 'X') { /// thresh 1
	          if(threshold1 > 0 ){
	          threshold1 -=10;
	          println("threshold1: " + threshold1);
	          }
	      } if (key == 'd' || key == 'D') {  /// thresh 2
	          if(threshold2 < threshold3){
	            threshold2 +=10;
	            println("threshold2: " + threshold2);
	          }
	      } if (key == 'c' || key == 'C') {  /// thresh 2
	          if(threshold2 > threshold1){
	            threshold2 -=10;
	            println("threshold2: " + threshold2); 
	          }
	      }if (key == 'f' || key == 'F') { /// thresh 3
	          if(threshold3 < threshold4){
	          threshold3 +=10;
	          println("threshold2: " + threshold2);
	          }
	      }if (key == 'v' || key == 'v') { /// thresh 3
	          if(threshold3 > threshold2){
	          println("threshold3: " + threshold3);
	          threshold3 -=10;
	          }
	      //// this does opacity for the objects
	      }if (key == 'g' || key == 'V') { /// thresh 3
	          if(threshold3 > threshold2){
	          ///// println("threshold3: " + threshold3);
	          objOpacity +=10;
	          }
	      }if (key == 'b' || key == 'B') { /// thresh 3
	          if(threshold3 > threshold2){
	         ////  println("threshold3: " + threshold3);
	            objOpacity -=10;
	          }
	      }if (key == ' ') { /// toddgle threshold display
	          if(displayThreshold == true){
	            displayThreshold = false;
	          } else if (displayThreshold == false){
	            displayThreshold = true;
	          }
	      }
	      /// end key pressed
	    
	    //// end
	}

	/////////////////////////////////
	////MIDI threshold modifiers
	/////////////////////////////////
	void modThresh1(int theValue){
	  threshold1=10*theValue;
	  // threshold2 = threshold1 + 1;
	  println("mod t1: " + threshold1) ;
	}
	void modThresh2(int theValue){
	  threshold2=10*theValue;
	  // threshold3 = threshold2 + 1;
	  println("mod t2: " + threshold2) ;
	}
	void modThresh3(int theValue){
	  threshold3=10*theValue;
	  threshold4 = threshold3 + 1;
	  println("mod t3: " + threshold3) ;
	}
	void modSkip(int theValue){
	  skip = floor(theValue * .5f);
	  println("value" + theValue + " skip: " + skip);
	  
	}
	void modScale(int theValue){
	   
	  kScale = theValue * 8;
	  println("scale" + kScale); ////  should upper limit at 900
	  
	}

	///////// end threshold

	public void stop() {
	  kinect.quit();
	  super.stop();
	}

}
