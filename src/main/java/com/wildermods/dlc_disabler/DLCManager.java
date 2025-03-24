package com.wildermods.dlc_disabler;

import com.worldwalkergames.legacy.game.model.DLCs;
import com.worldwalkergames.legacy.server.context.ServerDataContext;

public interface DLCManager {

	static class Impl {
		public static boolean disableArmorsAndSkins;
		public static boolean disableOmenroad;
		
		private static void disableImpl(DLCs dlc) {
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
		}
		
		private static void resetImpl(DLCs dlc) {
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
		}
	}
	
	public static void disable(DLCs... dlcs) {
		for(DLCs dlc : dlcs) {
			Impl.disableImpl(dlc);
		}
	}
	
	public static boolean isEnabled(DLCs... dlcs) {
		if(dlcs.length == 0) {
			throw new IllegalArgumentException("Must supply at least one DLC");
		}
		for(DLCs dlc : dlcs) {
			switch(dlc) {
				case ArmorsAndSkins:
					if(!ServerDataContext.isDLCInstalledArmorsAndSkins()) {
						return false;
					}
					break;
				case Omenroad:
					if(!ServerDataContext.isDLCInstalledOmenroad()) {
						return false;
					}
					break;
				default:
					throw new UnsupportedOperationException("Don't know how to check if DLC " + dlc + " is enabled!");
			}
		}
		return true;
	}
	
	public static void reset(DLCs... dlcs) {
		if(dlcs.length == 0) {
			throw new IllegalArgumentException("Must supply at least one DLC");
		}
		for(DLCs dlc : dlcs) {
			Impl.resetImpl(dlc);
		}
	}
	
	public static boolean isDisabled(DLCs... dlcs) {
		if(dlcs.length == 0) {
			throw new IllegalArgumentException("Must supply at least one DLC");
		}
		for(DLCs dlc : dlcs) {
			switch(dlc) {
				case ArmorsAndSkins:
					if(ServerDataContext.isDLCInstalledArmorsAndSkins()) {
						return false;
					}
					break;
				case Omenroad:
					if(ServerDataContext.isDLCInstalledOmenroad()) {
						return false;
					}
					break;
				default:
					throw new UnsupportedOperationException("Don't know how to check if DLC " + dlc + " is enabled!");
			}
		}
		return true;
	}

}
