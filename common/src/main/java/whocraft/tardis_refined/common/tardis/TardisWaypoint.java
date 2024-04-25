package whocraft.tardis_refined.common.tardis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class TardisWaypoint {

    private UUID id;
    private TardisNavLocation location;

    public TardisWaypoint(TardisNavLocation location) {
        this.id = UUID.randomUUID();
        this.location = location;
    }

    public TardisWaypoint(UUID id, TardisNavLocation location) {
        this.id = id;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public TardisNavLocation getLocation() {
        return location;
    }

    public void setLocation(TardisNavLocation location) {
        this.location = location;
    }

    public static TardisWaypoint deserialise(CompoundTag tag) {
        TardisNavLocation loc = TardisNavLocation.deserialise(tag.getCompound("location"));
        UUID id = tag.getUUID("id");

        TardisWaypoint waypoint = new TardisWaypoint(id, loc);

        return waypoint;
    }

    public CompoundTag serialise() {
        CompoundTag tag = new CompoundTag();
        tag.put("location", this.location.serialise());
        tag.putUUID("id", this.id);

        return tag;
    }
}
