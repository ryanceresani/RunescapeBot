package Conquest;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.LoopingScript;
 



import java.awt.*;
 
public final class DaemonheimIdler extends LoopingScript implements PaintListener {
    private String aSetting;
    Player me = Players.getLocal();
    Area idleArea;
	
    @Override
    public void onStart(String... args) {
    	//Submit your PaintListener
    	getEventDispatcher().addListener(this);
    	//Sets the length of time in milliseconds to wait before calling onLoop again
    	setLoopDelay(800, 1500);
    	//Load script configuration
    	aSetting = getSettings().getProperty("setting");
    	System.out.println("lets go");
    	System.out.println("Starting coordinates: " + me.getPosition());
    	
    	idleArea = new Area.Polygonal(
    		    new Coordinate[]{
    		    new Coordinate(2298,3191,0),
    		    new Coordinate(2297,3185,0),
    		    new Coordinate(2293,3187,0),
    		    new Coordinate(2294,3192,0)});

    }

	@Override
    public void onLoop() {
		SpriteItem eq_staff = Equipment.getItems().first();
		if(eq_staff == null){
			//click on first
			SpriteItem inv_staff = Inventory.getItems().first();
			if(inv_staff == null){
				System.out.println("error - staff not equipped, and couldn't find staff in inventory!");
			}else{
				inv_staff.click();
			}
		}
		
		//turn camera randomly
		if(Random.nextInt(20) == 1){
			Camera.turnTo(Random.nextInt(358));
		}
		
		//walk around randomly
		if(Random.nextInt(50) == 1){
			Coordinate c = idleArea.getRandomCoordinate();
			PredefinedPath path = PredefinedPath.create(c);
			path.step();
		}
	}
	
    @Override
    public void onPaint(Graphics2D g2d) {
    	Traversal.getDefaultWeb().render(g2d);
    }
}