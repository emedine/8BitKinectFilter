import processing.core.PApplet;

class Ring {
  float x, y; // X-coordinate, y-coordinate
  // float diameter; // Diameter of the ring
  boolean on = false; // Turns the display on and off
  // color fillColor;
  float rnd;
  float rnd2;
  float rnd3;
  
  int theOpacity;
  float diameter;
  
//applet
 PApplet pApp;
 AppProfile theAppProfile;
  
  void start(float xpos, float ypos, float theD, int objOpacity) {
	  
	/// init the applet
	theAppProfile = theAppProfile.getInstance();
	pApp = theAppProfile.pApp;
	    
    theOpacity = objOpacity;
    diameter = theD;
    x = xpos;
    y = ypos;
    on = true;
    // diameter = 1;
  }
  void grow() {
    if (on == true) {
      diameter += .5;
      if (diameter > 85) {
        /// 
        diameter = 45;
      }
    }
  }
  void display() {
    if (on == true) {
      // noFill();
      rnd = pApp.random(1);
      rnd2 = pApp.random(1);
      rnd3 = pApp.random(1);
      // fillColor = color((int) (rnd*255), (rnd2*255), (rnd3*255), 65);

      // stroke(155, 153);
      pApp.ellipse(x, y, diameter, diameter);
      pApp.stroke(255, 0,0);
      pApp.strokeWeight(1);
      pApp.fill(rnd*255, rnd2*255, rnd3*255, theOpacity);
      // smooth();
    }
  }
  
  
  //// end class
}