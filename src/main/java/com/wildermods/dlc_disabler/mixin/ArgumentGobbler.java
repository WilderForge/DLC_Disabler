package com.wildermods.dlc_disabler.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wildermods.dlc_disabler.DLCManager;
import com.worldwalkergames.legacy.LegacyDesktop;
import com.worldwalkergames.legacy.game.model.DLCs;

@Mixin(LegacyDesktop.class)
public class ArgumentGobbler {

	@WrapMethod(method = "main")
	private static void onStartup(String[] args, Operation<Void> original) {
		System.out.println("STARTUP");
		for(String arg : args) {
			if(arg.startsWith("disableDLC")) {
				arg = arg.replace("disableDLC", "");
				DLCs dlc = DLCs.valueOf(arg);
				if(dlc != null) {
					DLCManager.disable(dlc);
				}
			}
		}
		original.call((Object)args);
	}
	
}
