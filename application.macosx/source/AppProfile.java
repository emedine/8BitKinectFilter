import processing.core.PApplet;

public class AppProfile {

	 private static AppProfile instance = null;
	 public static PApplet pApp;
	 int maxFollowers = 10;
	 int maxFriends = 10;
	 int maxFavorites = 10;

	 protected AppProfile() {
	      // Exists only to defeat instantiation.
	   }
   public static AppProfile getInstance() {
      if(instance == null) {
         instance = new AppProfile();
      }
      return instance;
   }

   public void SetPApp(PApplet p){
	   pApp = p;

   }

}
