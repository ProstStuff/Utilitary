package dev.proststuff.utilitary.api.field.value.minecraft;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import net.minecraft.core.BlockPos;

public class BlockPosConfigField extends ConfigField<BlockPos> {
    public BlockPosConfigField(String name, BlockPos value) {
        super(name, value, ConfigCodecs.BLOCK_POS);
    }
}