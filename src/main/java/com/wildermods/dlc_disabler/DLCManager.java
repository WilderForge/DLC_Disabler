package com.wildermods.dlc_disabler;

import com.worldwalkergames.legacy.game.model.DLCs;
import com.worldwalkergames.legacy.server.context.ServerDataContext;

public interface DLCManager {

	static class Impl {
		public static volatile boolean disableArmorsAndSkins;
		public static volatile boolean disableOmenroad;
		
		private static void disableImpl(DLCs dlc) {
			System.out.println("[DLCManager] Disabling DLC: " + dlc);
			switch(dlc) {
				case ArmorsAndSkins:
					Impl.disableArmorsAndSkins = true;
					break;
				case Omenroad:
					Impl.disableOmenroad = true;
					break;
				default:
					throw new UnsupportedOperationException("Don't know how to disable DLC " + dlc);
			}
			boolean disabled = isDisabled(dlc);
			assert disabled;
			if (!disabled) {
			    System.err.println("[DLCManager] WARNING: " + dlc + " was not disabled as expected!");
			}
			System.out.println("[DLCManager] " + dlc + " is " + (disabled ? "disabled" : "enabled"));
		}
		
		private static void resetImpl(DLCs dlc) {
			System.out.println("[DLCManager] Resetting DLC state for: " + dlc);
			switch(dlc) {
				case ArmorsAndSkins:
					Impl.disableArmorsAndSkins = false;
					break;
				case Omenroad:
					Impl.disableOmenroad = false;
					break;
				default:
					throw new UnsupportedOperationException("Don't know how to reset DLC " + dlc);
			}
			boolean enabled = isEnabled(dlc);
			System.out.println("[DLCManager] " + dlc + " is " + (enabled ? "enabled" : "disabled"));
		}
	}
	
	/**
	 * Disables the specified DLCs. Overwrites vanilla behavior to disable the DLC even if the 
	 * user owns the DLCs and the game would normally have them enabled.
	 * 
	 * @param dlcs the DLCs to disable
	 */
	public static void disable(DLCs... dlcs) {
		for(DLCs dlc : dlcs) {
			Impl.disableImpl(dlc);
		}
	}
	
	/**
	 * Returns true if the user owns all of the specified DLCs, DLCManager has not disabled
	 * any of the DLCs, and the vanilla game has all of the specified DLCs enabled.
	 * 
	 * @param dlcs the DLCs to check
	 */
	public static boolean isEnabled(DLCs... dlcs) {
		return checkDLCState(true, dlcs);
	}
	
	/**
	 * Resets the DLC state. Removes DLCManager's hold on enabling the specified DLCs.
	 * 
	 * Does not allow the user to access content they do not own.
	 * 
	 * @param dlcs the DLCs to reset
	 */
	public static void reset(DLCs... dlcs) {
		for(DLCs dlc : dlcs) {
			Impl.resetImpl(dlc);
		}
	}
	
	/**
	 * Returns true if ALL of the specified DLCs are disabled for any reason.
	 * 
	 * If any of the specified DLCs are enabled, returns false.
	 * @param dlcs
	 */
	public static boolean isDisabled(DLCs... dlcs) {
		return checkDLCState(false, dlcs);
	}
	
	private static boolean checkDLCState(boolean expectedState, DLCs... dlcs) {
		if(dlcs.length == 0) {
			throw new IllegalArgumentException("Must supply at least one DLC");
		}
		for(DLCs dlc : dlcs) {
			boolean actualState = switch (dlc) {
				case ArmorsAndSkins -> ServerDataContext.isDLCInstalledArmorsAndSkins();
				case Omenroad -> ServerDataContext.isDLCInstalledOmenroad();
				default -> throw new UnsupportedOperationException("Unknown DLC: " + dlc);
			};
			if (actualState != expectedState) {
				return false;
			}
		}
		return true;
	}

}
