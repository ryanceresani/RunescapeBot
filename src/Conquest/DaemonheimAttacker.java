package Conquest;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

import java.awt.*;
import java.util.concurrent.TimeUnit;
 
public final class DaemonheimAttacker extends LoopingScript implements PaintListener {
    private String aSetting;
    Player me = Players.getLocal();
    StopWatch sw = new StopWatch();
    boolean attacking = false;
    int notFoundCount = 0;
	
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
    }

	@Override
    public void onLoop() {
		Player idler = Players.getLoaded("BDK_Jim").first();
		if(idler == null){
			System.out.println("error - could not find player.");
		}
		
		//if currently attacking, randomly turn camera and check the timer
		if(idler != null && attacking){
			System.out.println("attacking...");
			notFoundCount = 0;
			Execution.delay(1000, 1500);
			if(seriouslyIdle()){
				attacking = idler.interact("Attack");
			}
			if(Random.nextInt(10) == 1){
				Camera.turnTo(Random.nextInt(358));
			}
			System.out.println(sw.getRuntime(TimeUnit.SECONDS));
			if(sw.getRuntime(TimeUnit.SECONDS) > 32){
				Execution.delay(1000, 5000);
				sw.reset();
				attacking = false;
			}
		//if not attacking, attack
		}else{
			System.out.println("not attacking...");
			if(idler != null && idler.isVisible()){
				Execution.delay(500);
				attacking = idler.interact("Attack");
				if(attacking){
					sw.start();
				}
			}else if(idler != null && !idler.isVisible()){
				Camera.turnTo(idler);
				Camera.turnTo(Random.nextInt(358));
			}else{
				//need to make idler visible
				System.out.println("error - idler not visible!");
				Camera.turnTo(Random.nextInt(358));
				if(notFoundCount++ == 400){
					System.out.println("couldn't find, logging out");
					RuneScape.logout();
					System.exit(0);
				}
			}
		}
	}
	
    private boolean seriouslyIdle() {
		if(me.getAnimationId() == -1){
			Execution.delay(300);
			if(me.getAnimationId() == -1){
				Execution.delay(300);
				if(me.getAnimationId() == -1){
					Execution.delay(300);
					if(me.getAnimationId() == -1){
						Execution.delay(300);
						if(me.getAnimationId() == -1){
							Execution.delay(300);
							if(me.getAnimationId() == -1){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
    public void onPaint(Graphics2D g2d) {
    	Traversal.getDefaultWeb().render(g2d);
    }
}