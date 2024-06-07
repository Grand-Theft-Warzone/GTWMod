package com.grandtheftwarzone.gtwmod.api.emoji;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public class RLEmoji extends Emoji {

    ResourceLocation resourceLocation;

    public RLEmoji(ResourceLocation resourceLocation, List<String> strings) {
        this.strings = strings;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public ResourceLocation getResourceLocationForBinding() {
        return resourceLocation;
    }

}
