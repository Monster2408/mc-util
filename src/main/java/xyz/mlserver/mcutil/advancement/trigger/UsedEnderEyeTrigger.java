package xyz.mlserver.mcutil.advancement.trigger;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import xyz.mlserver.mcutil.advancement.shared.RangeObject;
import xyz.mlserver.mcutil.advancement.util.JsonBuilder;

/**
 * Fires whenever the player uses an Eye of Ender.
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class UsedEnderEyeTrigger extends Trigger {
	private @Nullable RangeObject distance = null;
	
	public UsedEnderEyeTrigger() {
		super(Type.USED_ENDER_EYE);
	}
	
	
	
	/**
	 * @return information about the distance of the player and the nearest stronghold's center or null, if none was specified. The vertical distance is not included
	 */
	public @Nullable RangeObject getDistance() {
		return distance;
	}
	
	/**
	 * @param distance information about the distance of the player and the nearest stronghold's center or null, if it should be cleared. The vertical distance is not included
	 * @return the current trigger for chaining
	 */
	public UsedEnderEyeTrigger setDistance(@Nullable RangeObject distance) {
		this.distance = distance;
		return this;
	}
	
	
	
	@Override
	protected JsonObject getConditions() {
		return distance == null ? null : new JsonBuilder()
				.add("distance", distance)
				.build();
	}
}
