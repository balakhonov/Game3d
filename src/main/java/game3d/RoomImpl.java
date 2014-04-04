package game3d;

import game3d.effect.EffectsManager;
import game3d.mapping.AbstractTank;
import game3d.mapping.User;
import game3d.motion.MotionController;
import game3d.timeout.ConectionTimeoutAction;
import game3d.timeout.ConnectionManager;
import game3d.timeout.TankConnectionTimeout;
import game3d.websocketserver.handler.ObjectHandler;
import game3d.websocketserver.handler.TankHandler;
import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RoomImpl implements Room {
	private static final Logger LOG = Logger.getLogger(RoomImpl.class);

	/**
	 *
	 */
	private final Set<Channel> CHANNELS = new ConcurrentSet<>();

	/**
	 *
	 */
	private final ConnectionManager CONNECTION_MANAGER = new ConnectionManager();

	/**
	 *
	 */
	private final EffectsManager EFFECTS_MANAGER = new EffectsManager();

	/**
	 *
	 */
	private final Map<String, User> USERS = new ConcurrentHashMap<>();

	/**
	 *
	 */
	private final Map<String, AbstractTank> TANKS = new ConcurrentHashMap<>();

	/**
	 *
	 */
	private final Map<String, MotionController> TANK_MOTION_CONTROLLERS = new ConcurrentHashMap<>();

	/**
	 *
	 */
	private final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);

	/**
	 *
	 */
	public RoomImpl() {
		EXECUTOR.scheduleAtFixedRate(new RoomTaskProcessor(this), 0, 10, TimeUnit.MILLISECONDS);
	}

	@Override
	public void addUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User should not be null");
		}

		LOG.debug(String.format("Add %s to room %s", user, this));

		// add user to room
		USERS.put(user.getSessionId(), user);

		// initialize new user tank
		initialiseTank(user);
	}

	@Override
	public void addChannel(Channel channel) {
		if (channel == null) {
			throw new IllegalArgumentException("Channel should not be null");
		}

		// add user channel to room
		CHANNELS.add(channel);

		// send tanks to client
		TankHandler.initAll(channel, TANKS.values());
	}

	private void initialiseTank(User user) {
		String sessionId = user.getSessionId();
		AbstractTank tank = user.getCurrentTank();

		if (tank == null) {
			throw new IllegalStateException("Tank should not be null before adding User to Room");
		}

		// add user tank to room
		TANKS.put(sessionId, tank);

		// add motion controller
		TANK_MOTION_CONTROLLERS.put(sessionId, new MotionController(tank));

		// create and add tank connection timeout listener
		ConectionTimeoutAction ta = new TankConnectionTimeout(tank, this, 5000);
		CONNECTION_MANAGER.add(sessionId, ta);

		// send new tank info to users in room
		TankHandler.init(this, tank);
	}

	@Override
	public void removeUser(String sessionId) {
		if (sessionId == null || sessionId.isEmpty()) {
			throw new IllegalArgumentException("Session ID should not be null or empty");
		}

		// remove user
		USERS.remove(sessionId);

		// remove user tank
		AbstractTank tank = TANKS.remove(sessionId);
		if (tank != null) {
			TankHandler.remove(this, tank);
		}

		// remove tank motion controller
		TANK_MOTION_CONTROLLERS.remove(sessionId);
	}

	@Override
	public void removeChannel(Channel channel) {
		if (channel == null) {
			throw new IllegalArgumentException("Channel should not be null");
		}

		CHANNELS.remove(channel);
	}

	@Override
	public Map<String, User> getUsers() {
		return USERS;
	}

	@Override
	public Map<String, AbstractTank> getTanks() {
		return TANKS;
	}

	public Set<Channel> getChannels() {
		return CHANNELS;
	}

	public MotionController getMotionController(String sessionId) {
		return TANK_MOTION_CONTROLLERS.get(sessionId);
	}

	private class RoomTaskProcessor implements Runnable {
		private Room room;

		/**
		 *
		 */
		private long startDate = new Date().getTime();

		public RoomTaskProcessor(Room room) {
			this.room = room;
		}

		@Override
		public void run() {
			boolean positionChange = false;
			long currentDateTime = new Date().getTime();

			// check connections
			CONNECTION_MANAGER.run();

			// process object effects
			new EffectsManager.EffectHandler(EFFECTS_MANAGER).run();

			// process object relocations
			synchronized (TANK_MOTION_CONTROLLERS) {
				for (MotionController mc : TANK_MOTION_CONTROLLERS.values()) {
					// if (tank.isConnected()) {
					mc.move();

					long diff = currentDateTime - startDate;
					if (diff > 60) {
						if (mc.isMoving() || mc.isChanged()) {
							mc.setChanged(false);
							positionChange = true;
							// update tank position
							ObjectHandler.updatePosition(room, (AbstractTank) mc.getMovable());
						}

						if (mc.isTowerMoving() || mc.isTowerChanged()) {
							mc.setTowerChanged(false);
							positionChange = true;
							// update tank tower rotation
							TankHandler.updateTowerRotation(room, (AbstractTank) mc.getMovable());
						}
					}
					// }
				}
			}

			if (positionChange) {
				startDate = currentDateTime;
			}
		}
	}
}
