import processing.core.PApplet;
import java.awt.geom.*;

class PolygonBuilder{ 
  
  int theOpacity;
  
//applet
 PApplet pApp;
 AppProfile theAppProfile;
 
 public PolygonBuilder() {
	    /// init the applet
	    theAppProfile = theAppProfile.getInstance();
	    pApp = theAppProfile.pApp;
	  }
  
  void update(float rot) {
	  pApp.println("aaaaa");
    // rotate(rot);
	  pApp.translate(0-60, 0-60);
    
  }
  
  //// number vertices, xpos, ypos, length, rotation
  void doPolygon(int n, float cx, float cy, float r, float rot, int objOpacity){
    theOpacity = objOpacity;
    int fillColor;
    float rnd = pApp.random(1);
    float rnd1 = pApp.random(1);
    // fillColor = color((int) (rnd*255), rnd1*255, (0), theOpacity);
    fillColor = pApp.color((int) (rnd*255), (rnd*255), (0), theOpacity);
    
    float angle = 360.0f / n;
    
    pApp.fill(fillColor);
    pApp.stroke(0, rnd*225, 12);
    pApp.strokeWeight(1);
  
    pApp.beginShape();
    for (int i = 0; i < n; i++){
    	pApp.vertex(cx + r * pApp.cos(pApp.radians(angle * i)),
        cy + r * pApp.sin(pApp.radians(angle * i)));
    }

    // 
    pApp.rotate(rot);
    pApp.endShape(); // previously this: pApp.endShape(CLOSE);
    

    
    
  }
  
  /// end class
}