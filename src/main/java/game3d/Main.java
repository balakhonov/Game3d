package game3d;

import game3d.effect.EffectsManager;
import game3d.mapping.AbstractTank;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Main {

	static final EffectsManager EFFECTS_MANAGER = new EffectsManager();
	static final Map<String, AbstractTank> TANKS = new ConcurrentHashMap<>();

	static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

	static ThreadPoolExecutor taskExecuter = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(10000));

	static int totalExecuted = 0;


	/**
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		// executor.prestartAllCoreThreads();
//
//		TANKS.put("11", new Tank("11", 1000));
//		TANKS.put("12", new Tank("12", 1000));
//
//		final Tank tank = TANKS.get("11");
//		EFFECTS_MANAGER.addEffect("11", new HealthBurnEffect(tank, 50, 10 * 1000, 2));
//		EFFECTS_MANAGER.addEffect("11", new HealthHealEffect(tank, 20, 10 * 1000, 3));
//
//		scheduledExecutor.scheduleAtFixedRate(new Runnable() {
//
//			@Override
//			public void run() {
//				taskExecuter.execute(new MoveForwardObjectTask(tank));
//				taskExecuter.execute(new EffectsManager.EffectHandler(EFFECTS_MANAGER));
//			}
//		}, 0, 10, TimeUnit.MILLISECONDS);
//
//		// for (int threadCounter = 0; true; threadCounter++) {
//		// System.out.println("Adding MoveObjectTask : " + threadCounter);
//		//
//		// executor.execute(new MoveForwardObjectTask(tank));
//		// executor.execute(new HealthBurnEffect(tank, 10 * 1000, 2));
//		//
//		// if (threadCounter == 1000)
//		// break;
//		// }
//		// executor.shutdown();
//	}
}
