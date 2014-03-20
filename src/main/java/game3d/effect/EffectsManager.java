package game3d.effect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EffectsManager {

	private final Map<String, List<Action>> EFFECTS = new ConcurrentHashMap<>();

	public void addEffect(String objectId, Action effect) {
		List<Action> list = EFFECTS.get(objectId);
		if (list == null) {
			list = new LinkedList<>();
		}
		list.add(effect);

		EFFECTS.put(objectId, list);
	}

	public void remove(String objectId, Action effect) {
		List<Action> list = EFFECTS.get(objectId);
		if (list == null) {
			list = new LinkedList<>();
		} else {
			list.remove(effect);
		}
	}

	static public class EffectHandler implements Runnable {
		private EffectsManager effectsManager;
		private long sDate = new Date().getTime();
		static int counter = 0;

		public EffectHandler(EffectsManager effectsManager) {
			this.effectsManager = effectsManager;
		}

		@Override
		public void run() {
			for (Map.Entry<String, List<Action>> me : effectsManager.EFFECTS.entrySet()) {
				String objectId = me.getKey();
				List<Action> effects = me.getValue();
				for (Action e : effects) {
					e.run();
				}
				Iterator<Action> iter = effects.iterator();
				while (iter.hasNext()) {
					if (iter.next().isStopped()) {
						System.out.println("delete effect: " + effects + " from:" + objectId);
						iter.remove();
					}
				}
			}
			System.out.println((new Date().getTime() - sDate) + " ms " + ++counter);
		}
	}
}
