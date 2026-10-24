package com.example.dualinventory.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class DualInventoryData implements INBTSerializable<CompoundTag> {
    public enum Slot { A, B }
    private final InventorySnapshot slotA = new InventorySnapshot();
    private final InventorySnapshot slotB = new InventorySnapshot();
    private Slot active = Slot.A;

    public Slot getActive() { return active; }
    public void setActive(Slot active) { this.active = active; }
    public InventorySnapshot get(Slot slot) { return slot == Slot.A ? slotA : slotB; }
    public Slot other() { return active == Slot.A ? Slot.B : Slot.A; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Active", active.name());
        tag.put("SlotA", slotA.save());
        tag.put("SlotB", slotB.save());
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag tag) {
        active = tag.contains("Active") && tag.getString("Active").equals("B") ? Slot.B : Slot.A;
        if (tag.contains("SlotA")) slotA.load(tag.getCompound("SlotA"));
        if (tag.contains("SlotB")) slotB.load(tag.getCompound("SlotB"));
    }
    public void copyFrom(DualInventoryData other) { deserializeNBT(other.serializeNBT()); }
}
