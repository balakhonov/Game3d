package game3d;

import game3d.effect.EffectsManager;
import game3d.mapping.Tank;
import game3d.mapping.User;
import game3d.timeout.ConnectionManager;
import game3d.websocketserver.handler.ObjectHandler;
import game3d.websocketserver.handler.TankHandler;
import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class RoomImpl implements Room {
	private static final Logger LOG = Logger.getLogger(RoomImpl.class);

	/**
	 * 
	 */
	private final Set<Channel> CHANNELS = new ConcurrentSet<>();

	/**
	 * 
	 */
	private final ConnectionManager CONNACTION_MANAGER = new ConnectionManager();

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
	private final Map<String, Tank> TANKS = new ConcurrentHashMap<>();

	/**
	 * 
	 */
	private final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);

	/**
	 * 
	 */
	private long startDate = new Date().getTime();

	/**
	 * 
	 */
	public RoomImpl() {
		final Room that = this;
		
		EXECUTOR.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				boolean positionChange = false;
				long currentDateTime = new Date().getTime();

				synchronized (TANKS) {
					for (Tank tank : TANKS.values()) {

						if (tank.isConnected()) {
							// check connection timeout
							// tank.getTimeoutHandler().checkTimeout();
							//
							// if (tank.getTimeoutHandler().isConnected()) {
							if (tank.isMoveForwardFlag())
								tank.moveForward();
							else if (tank.isMoveBackFlag())
								tank.moveBack();

							if (tank.isTurnLeftFlag())
								tank.turnLeft();
							else if (tank.isTurnRightFlag())
								tank.turnRight();

							if (tank.isMoveForwardFlag() || tank.isMoveBackFlag()
									|| tank.isTurnLeftFlag() || tank.isTurnRightFlag()) {
								long diff = currentDateTime - startDate;

								if (diff > 60) {
									positionChange = true;
									ObjectHandler.updatePosition(that, tank);
								}
							}
						}
						// }
					}
				}

				if (positionChange) {
					startDate = currentDateTime;
				}
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}

	@Override
	public void addUser(User user, Channel channel) {
		// add user to room
		USERS.put(user.getSessionId(), user);

		// add user channel to room
		CHANNELS.add(channel);

		// send tanks to client
		TankHandler.initAll(channel, TANKS.values());
		LOG.debug("Total tanks: " + TANKS.values());

		// initialize new user tank
		initialiseTank(channel, user);
	}

	private void initialiseTank(Channel channel, User user) {
		String sessionId = user.getSessionId();

		Tank tank = TANKS.get(sessionId);
		if (tank == null) {
			// it is first connection
			tank = user.getCurrentTank();

			TANKS.put(sessionId, tank);
		} else {
			tank.setConnected(true);
		}

		TankHandler.init(this, tank);
	}

	@Override
	public void removeUser(String sessionId,Channel channel) {
		USERS.remove(sessionId);
		CHANNELS.remove(channel);
	}

	@Override
	public Map<String, User> getUsers() {
		return USERS;
	}

	@Override
	public Map<String, Tank> getTanks() {
		return TANKS;
	}

	public Set<Channel> getChannels() {
		return CHANNELS;
	}
}
