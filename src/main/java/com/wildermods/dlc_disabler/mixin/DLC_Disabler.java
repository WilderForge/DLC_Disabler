package com.wildermods.dlc_disabler.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wildermods.dlc_disabler.DLCManager;
import com.worldwalkergames.legacy.server.context.ServerDataContext;

@Mixin(ServerDataContext.class)
public class DLC_Disabler implements DLCManager {
	
	private static @Shadow Boolean isDLCInstalledArmorsAndSkins;
	private static @Shadow Boolean isDLCInstalledOmenroad;
	
	@WrapMethod(
		method = "isDLCInstalledArmorsAndSkins"
	)
	private static final boolean setArmorsAndSkinsState(Operation<Boolean> original) {
		boolean enabled = original.call(); //first we check if the DLC would be enabled by the vanilla game
		if(enabled && (Impl.disableArmorsAndSkins)) { //if the DLC would be enabled by the vanilla game, but we want to disable it
			return isDLCInstalledArmorsAndSkins = false; //then we disable it and return that it is disabled.
		}
		return enabled; //otherwise, we return what the vanilla game would normally expect
	}
	
	@WrapMethod(
		method = "isDLCInstalledOmenroad"
	)
	private static final boolean setOmenroadState(Operation<Boolean> original) {
		boolean enabled = original.call();
		if(enabled && (Impl.disableOmenroad)) {
			return isDLCInstalledOmenroad = false;
		}
		return enabled;
	}
	
}
