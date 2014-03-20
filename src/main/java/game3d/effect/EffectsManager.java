package game3d.effect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EffectsManager {

	private final Map<String, List<ActionImpl>> EFFECTS = new ConcurrentHashMap<>();

	public void addEffect(String objectId, ActionImpl effect) {
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
		private long sDate = new Date().getTime();
		static int counter = 0;

		public EffectHandler(EffectsManager effectsManager) {
			this.effectsManager = effectsManager;
		}

		@Override
		public void run() {
			for (Map.Entry<String, List<ActionImpl>> me : effectsManager.EFFECTS.entrySet()) {
				String objectId = me.getKey();
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
			System.out.println((new Date().getTime() - sDate) + " ms " + ++counter);
		}
	}
}
