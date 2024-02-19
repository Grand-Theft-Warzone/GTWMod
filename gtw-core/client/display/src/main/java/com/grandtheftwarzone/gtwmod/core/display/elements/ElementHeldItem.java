package com.grandtheftwarzone.gtwmod.core.display.elements;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RegisterDisplayElement(templateId = "gtw_image_held_item")
public class ElementHeldItem extends BaseElement {
    private ResourceLocation defaultImage;


    private AtumColor color = AtumColor.WHITE;


    private AtumColor colorDefault = AtumColor.WHITE;
    private int textureXDefault;
    private int textureYDefault;
    private int textureWidthDefault;
    private int textureHeightDefault;


    public ElementHeldItem(@NotNull AtumMod atumMod,
                        @NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);

    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {

        if(Minecraft.getMinecraft().player == null ||
                Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(
                    defaultImage
            );
            colorDefault.useColor();
            if(textureHeightDefault==0||textureWidthDefault==0){
                RenderUtils.drawCompleteImage(
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight()
                );
            }else {
                RenderUtils.drawPartialImage(
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        textureXDefault,
                        textureYDefault,
                        textureWidthDefault,
                        textureHeightDefault
                );
            }

            super.hasOutline = false;
            return;
        }
        color.useColor();
        RenderUtils.renderItemIntoGUI(
                Minecraft.getMinecraft().player.getHeldItemMainhand(),
                getX()+getWidth()/2,
                getY()+getHeight()/2,
                getWidth(),
                getHeight()
        );
        super.hasOutline = true;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        defaultImage = new ResourceLocation(
                config.getStringOrDefault("defaultImage.location",
                        "atumodcore:textures/button.png")
        );
        this.colorDefault = AtumColor.fromHex(
                config.getStringOrDefault("defaultImage.color",
                        "#FFFFFFFF")
        );
        this.textureXDefault = config.getIntOrDefault("defaultImage.textureX",0);
        this.textureYDefault = config.getIntOrDefault("defaultImage.textureY",0);
        this.textureWidthDefault = config.getIntOrDefault("defaultImage.textureWidth",0);
        this.textureHeightDefault = config.getIntOrDefault("defaultImage.textureHeight",0);

        String color = config.getStringOrNull("color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }

    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementHeldItem cloneImage = (ElementHeldItem) clone;
        if(cloneImage.color!=null) {
            cloneImage.color = new AtumColor(cloneImage.color.getRed(), cloneImage.color.getGreen(), cloneImage.color.getBlue(), cloneImage.color.getAlpha());
        }
        return cloneImage;
    }

}