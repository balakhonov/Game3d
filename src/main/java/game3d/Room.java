package game3d;

import game3d.mapping.AbstractTank;
import game3d.mapping.User;
import game3d.motion.MotionController;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;


public interface Room {
	public void addUser(User user);

	public void removeUser(String sessionId);
	
	public void removeChannel(Channel channel);

	public Map<String, User> getUsers();

	public Map<String, AbstractTank> getTanks();

	public Set<Channel> getChannels();

	public void addChannel(Channel channel);
	
	public MotionController getMotionController(String sessionId);
}
