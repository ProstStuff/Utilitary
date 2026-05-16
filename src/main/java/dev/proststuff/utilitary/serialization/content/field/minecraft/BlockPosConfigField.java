package dev.proststuff.utilitary.serialization.content.field.minecraft;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import net.minecraft.core.BlockPos;

public class BlockPosConfigField extends ConfigField<BlockPos> {
    public BlockPosConfigField(String name, BlockPos value) {
        super(name, value, ConfigCodecs.BLOCK_POS);
    }
}