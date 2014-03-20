package game3d;

import game3d.mapping.Tank;
import game3d.mapping.User;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;


public interface Room {
	public void addUser(User user, Channel channel);

	public void removeUser(String sessionId, Channel channel);

	public Map<String, User> getUsers();

	public Map<String, Tank> getTanks();
	
	public Set<Channel> getChannels();
}