import processing.core.PApplet;

class DecayTriangle {
  float x, y; // X-coordinate, y-coordinate
  float diameter; // Diameter of the ring
  boolean on = false; // Turns the display on and off
  
  //// color changing values
  int fillColor;
  float rnd;
  float rnd2;
  float rnd3;
  /// scaling values
  float baseScale = 1;
  //
  int theOpacity;

  
//applet
 PApplet pApp;
 AppProfile theAppProfile;

  void start() {
	/// init the applet
	theAppProfile = theAppProfile.getInstance();
	pApp = theAppProfile.pApp;
		    
   //  x = xpos;
   //  y = ypos;
    on = true;
    // diameter = 1;
    // translate(width/2, height/2);
  }
  void update(float rot, int objOpacity) {
    
	pApp.rotate(rot);
	pApp.translate(0-60, 0-60);
    theOpacity = objOpacity;
    
  }
  void display() {
    if (on == true) {
      // noFill();
      rnd = pApp.random(1);
      rnd2 = pApp.random(1);
      rnd3 = pApp.random(1);
      fillColor = pApp.color((int) (rnd*255), (rnd2*255),255, theOpacity);
      // fillColor = color((int) (rnd*255), (rnd2*255), (rnd3*255));
      // stroke(155, 153);
      pApp.triangle(60, 40, 80, 80, 40, 80);
      
      //////// pt1x, pt1y, pt2x, pt2y,pt3x,pt3y
      // triangle(0, 10, 10, 30, -10, 30); 
      pApp.stroke(0, 24,255);
      pApp.strokeWeight(1);
     //  noFill();
      pApp.fill(fillColor);
      
    }
  }
}