package game3d.effect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class EffectsManager {

	private final Map<Long, List<ActionImpl>> EFFECTS = new ConcurrentHashMap<>();

	public void addEffect(Long objectId, ActionImpl effect) {
		List<ActionImpl> list = EFFECTS.get(objectId);
		if (list == null) {
			list = new LinkedList<>();
		}
		list.add(effect);

		EFFECTS.put(objectId, list);
	}

	public void remove(String objectId, ActionImpl effect) {
		List<ActionImpl> list = EFFECTS.get(objectId);
		if (list == null) {
			list = new LinkedList<>();
		} else {
			list.remove(effect);
		}
	}

	static public class EffectHandler implements Runnable {
		private EffectsManager effectsManager;

		public EffectHandler(EffectsManager effectsManager) {
			this.effectsManager = effectsManager;
		}

		@Override
		public void run() {
			for (Map.Entry<Long, List<ActionImpl>> me : effectsManager.EFFECTS.entrySet()) {
				Long objectId = me.getKey();
				List<ActionImpl> effects = me.getValue();
				for (ActionImpl e : effects) {
					e.run();
				}
				Iterator<ActionImpl> iter = effects.iterator();
				while (iter.hasNext()) {
					if (iter.next().isStopped()) {
						System.out.println("delete effect: " + effects + " from:" + objectId);
						iter.remove();
					}
				}
			}
		}
	}
}
