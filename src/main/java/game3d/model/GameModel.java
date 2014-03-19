package game3d.model;

import game3d.effect.EffectsManager;
import game3d.effect.HealthBurnEffect;
import game3d.effect.HealthHealEffect;
import game3d.mapping.Tank;
import game3d.socketserver.model.DeviceRequest;
import game3d.socketserver.model.DeviceSocketChannel;
import game3d.task.MoveForwardObjectTask;
import game3d.websocketserver.WebSocketServerHandler;
import game3d.websocketserver.handler.ObjectHandler;
import io.netty.handler.mapping.ResponsePackageData;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;


public class GameModel {
	private static final Logger LOG = Logger.getLogger(GameModel.class);
	private Tank tank;

	static final EffectsManager EFFECTS_MANAGER = new EffectsManager();
	static final Map<String, Tank> TANKS = new ConcurrentHashMap<>();

	static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

	static ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(1, 1, 1000,
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10000));

	static long startDate = new Date().getTime();

	static {
		scheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				boolean positionChange = false;
				long currentDateTime = new Date().getTime();

				synchronized (WebSocketServerHandler.ACTIVE_TANKS) {
					for (Tank tank : WebSocketServerHandler.ACTIVE_TANKS.values()) {
						if (tank.isMoveForwardFlag())
							tank.moveForward();
						else if (tank.isMoveBackFlag())
							tank.moveBack();

						if (tank.isTurnLeftFlag())
							tank.turnLeft();
						else if (tank.isTurnRightFlag())
							tank.turnRight();

						if (tank.isMoveForwardFlag() || tank.isMoveBackFlag() || tank.isTurnLeftFlag()
								|| tank.isTurnRightFlag()) {
							long diff = currentDateTime - startDate;

							if (diff > 60) {
								positionChange = true;
								ObjectHandler.updatePosition(1, tank);
							}
						}
					}
				}

				if (positionChange) {
					startDate = currentDateTime;
				}
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}

	public static void main1(String[] args) {
		TANKS.put("11", new Tank("11", 1000));
		TANKS.put("12", new Tank("12", 1000));

		final Tank tank = TANKS.get("11");
		final Tank tank2 = TANKS.get("12");

		System.out.println("tank: " + tank.getId());
		System.out.println("tank2: " + tank2.getId());

		EFFECTS_MANAGER.addEffect("11", new HealthBurnEffect(tank, 50, 10 * 1000, 2));
		EFFECTS_MANAGER.addEffect("11", new HealthHealEffect(tank, 20, 10 * 1000, 3));

		scheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				taskExecutor.execute(new MoveForwardObjectTask(tank));
				taskExecutor.execute(new EffectsManager.EffectHandler(EFFECTS_MANAGER));
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}


	public GameModel(DeviceSocketChannel ac) {
		if (ac == null) {
			throw new IllegalArgumentException("ChannelActivity should not be null");
		}
		String userId = ac.getDeviceInfo().getSessionId();

		this.tank = WebSocketServerHandler.ACTIVE_TANKS.get(userId);
	}


	@DeviceRequest(command = "forwardDown")
	public ResponsePackageData forwardDown() {
		LOG.warn("forwardDown" + tank.getId());
		tank.setMoveForwardFlag(true);
		return null;
	}


	@DeviceRequest(command = "forwardUp")
	public ResponsePackageData forwardUp() {
		LOG.warn("forwardUp" + tank);
		tank.setMoveForwardFlag(false);
		ObjectHandler.updatePosition(1, tank);
		return null;
	}


	@DeviceRequest(command = "backDown")
	public ResponsePackageData backDown() {
		LOG.warn("backDown" + tank);
		tank.setMoveBackFlag(true);
		return null;
	}


	@DeviceRequest(command = "backUp")
	public ResponsePackageData backUp() {
		LOG.warn("backUp" + tank);
		tank.setMoveBackFlag(false);
		ObjectHandler.updatePosition(1, tank);
		return null;
	}


	@DeviceRequest(command = "rotateLeftDown")
	public ResponsePackageData rotateLeftDown() {
		LOG.warn("rotateLeftDown" + tank.getId());
		tank.setTurnLeftFlag(true);
		return null;
	}


	@DeviceRequest(command = "rotateLeftUp")
	public ResponsePackageData rotateLeftUp() {
		LOG.warn("rotateLeftUp" + tank.getId());
		tank.setTurnLeftFlag(false);
		ObjectHandler.updatePosition(1, tank);
		return null;
	}


	@DeviceRequest(command = "rotateRightDown")
	public ResponsePackageData rotateRightDown() {
		LOG.warn("rotateRightDown" + tank.getId());
		tank.setTurnRightFlag(true);
		return null;
	}


	@DeviceRequest(command = "rotateRightUp")
	public ResponsePackageData rotateRightUp() {
		LOG.warn("rotateRightUp" + tank.getId());
		tank.setTurnRightFlag(false);
		ObjectHandler.updatePosition(1, tank);
		return null;
	}
}
