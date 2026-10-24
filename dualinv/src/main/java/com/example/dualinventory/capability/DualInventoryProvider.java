package com.example.dualinventory.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DualInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    private final DualInventoryData data = new DualInventoryData();
    private final LazyOptional<DualInventoryData> optional = LazyOptional.of(() -> data);
    public DualInventoryData getData() { return data; }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityHandler.DUAL_INVENTORY ? optional.cast() : LazyOptional.empty();
    }
    @Override public CompoundTag serializeNBT() { return data.serializeNBT(); }
    @Override public void deserializeNBT(CompoundTag tag) { data.deserializeNBT(tag); }
}
