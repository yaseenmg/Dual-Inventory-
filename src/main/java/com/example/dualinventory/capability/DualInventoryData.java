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
        tag.m_128359_("Active", active.name()); // putString - proven SRG name from real CompoundTag.class
        tag.m_128365_("SlotA", slotA.save());   // put
        tag.m_128365_("SlotB", slotB.save());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        active = tag.m_128441_("Active") && tag.m_128461_("Active").equals("B") ? Slot.B : Slot.A; // contains, getString
        if (tag.m_128441_("SlotA")) slotA.load(tag.m_128469_("SlotA")); // getCompound
        if (tag.m_128441_("SlotB")) slotB.load(tag.m_128469_("SlotB"));
    }

    public void copyFrom(DualInventoryData other) {
        deserializeNBT(other.serializeNBT());
    }
}
