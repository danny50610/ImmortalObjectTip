package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTipEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class TipInfoEntity extends TipInfoBase {

    public final Entity entity;

    public TipInfoEntity(PacketCreateTipEntity message) {
        super(message);

        Minecraft mc = Minecraft.getMinecraft();
        this.entity = mc.world.getEntityByID(message.entityId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TipInfoEntity that = (TipInfoEntity) o;

        return entity != null ? entity.equals(that.entity) : that.entity == null;
    }

    @Override
    public int hashCode() {
        return entity != null ? entity.hashCode() : 0;
    }
}
