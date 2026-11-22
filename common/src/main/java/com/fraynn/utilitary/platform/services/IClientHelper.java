package com.fraynn.utilitary.platform.services;

import com.fraynn.utilitary.platform.ServiceUtil;
import com.fraynn.utilitary.util.Phantom;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface IClientHelper {
    IClientHelper INSTANCE = ServiceUtil.load(IClientHelper.class);

    Phantom<KeyMapping> registerKeyMapping(String modId, String name, int value, KeyMapping.Category category);
    Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category);
    Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category, int order);
}
