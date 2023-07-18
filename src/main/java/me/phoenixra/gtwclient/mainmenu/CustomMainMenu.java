package me.phoenixra.gtwclient.mainmenu;

import com.google.common.collect.Lists;
import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.api.font.CustomFontRenderer;
import me.phoenixra.gtwclient.api.font.Fonts;
import me.phoenixra.gtwclient.api.font.GlyphPage;
import me.phoenixra.gtwclient.api.gui.GtwGuiMenu;
import me.phoenixra.gtwclient.api.gui.GuiElementColor;
import me.phoenixra.gtwclient.api.gui.GuiElementLayer;
import me.phoenixra.gtwclient.api.gui.impl.GuiElementButton;
import me.phoenixra.gtwclient.api.gui.impl.GuiElementImage;
import me.phoenixra.gtwclient.api.gui.impl.GuiElementText;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import me.phoenixra.gtwclient.utils.Pair;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class CustomMainMenu extends GtwGuiMenu {

    private static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/background.png");
    private static final ResourceLocation QUIT_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/quit_button.png");
    private static final ResourceLocation SETTINGS_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/settings_button.png");
    private static final ResourceLocation DISCORD_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/discord_button.png");
    private static final ResourceLocation WEBSITE_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/website_button.png");
    private static final ResourceLocation PLAY_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/play_button.png");
    private static final ResourceLocation SINGLEPLAYER_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/singleplayer_button.png");
    private static final ResourceLocation MULTIPLAYER_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/multiplayer_button.png");

    private final int TEXT_COLOR = 10636599;

    public CustomMainMenu() {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();
        // Remove existing buttons (optional)
        this.buttonList.clear();
        this.getElements().clear();
        // ---------buttons---------
        // QUIT BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(QUIT_BUTTON_IMAGE))
                .setActionOnClick(() -> {mc.shutdown();})
                .setLayer("main_menu.buttons.quit.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.quit.x")
                .setY("main_menu.buttons.quit.y")
                .setWidth("main_menu.buttons.quit.width")
                .setHeight("main_menu.buttons.quit.height")
                .build().register();
        // SETTINGS BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(SETTINGS_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                })
                .setLayer("main_menu.buttons.settings.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.settings.x")
                .setY("main_menu.buttons.settings.y")
                .setWidth("main_menu.buttons.settings.width")
                .setHeight("main_menu.buttons.settings.height")
                .build().register();
        // DISCORD BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(DISCORD_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    try {
                        Desktop.getDesktop().browse(new URI(GTWClient.settings.getDiscordLink()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .setLayer("main_menu.buttons.discord.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.discord.x")
                .setY("main_menu.buttons.discord.y")
                .setWidth("main_menu.buttons.discord.width")
                .setHeight("main_menu.buttons.discord.height")
                .build().register();
        // WEBSITE BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(WEBSITE_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    try {
                        Desktop.getDesktop().browse(new URI(GTWClient.settings.getWebsiteLink()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .setLayer(
                        "main_menu.buttons.website.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.website.x")
                .setY("main_menu.buttons.website.y")
                .setWidth("main_menu.buttons.website.width")
                .setHeight("main_menu.buttons.website.height")
                .build().register();
        // PLAY BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(PLAY_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    FMLClientHandler.instance().connectToServerAtStartup(
                            GTWClient.settings.getServerHost(),
                            GTWClient.settings.getServerPort()
                    );
                })
                .setLayer("main_menu.buttons.play.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.play.x")
                .setY("main_menu.buttons.play.y")
                .setWidth("main_menu.buttons.play.width")
                .setHeight("main_menu.buttons.play.height")
                .build().register();
        // SINGLEPLAYER BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(SINGLEPLAYER_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    this.mc.displayGuiScreen(new GuiWorldSelection(this));
                })
                .setLayer("main_menu.buttons.singleplayer.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.singleplayer.x")
                .setY("main_menu.buttons.singleplayer.y")
                .setWidth("main_menu.buttons.singleplayer.width")
                .setHeight("main_menu.buttons.singleplayer.height")
                .build().register();
        // MULTIPLAYER BUTTON
        GuiElementButton.builder(this)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(MULTIPLAYER_BUTTON_IMAGE))
                .setActionOnClick(() -> {
                    this.mc.displayGuiScreen(new GuiMultiplayer(this));
                })
                .setLayer("main_menu.buttons.multiplayer.layer",
                        GuiElementLayer.FOREGROUND
                )
                .setX("main_menu.buttons.multiplayer.x")
                .setY("main_menu.buttons.multiplayer.y")
                .setWidth("main_menu.buttons.multiplayer.width")
                .setHeight("main_menu.buttons.multiplayer.height")
                .build().register();

        //------texts------
        GlyphPage glyphPage = Fonts.registerFont(
                "main_menu",
                GTWClient.class.getResourceAsStream("/assets/gtwclient/fonts/main_menu.ttf")
        );
        CustomFontRenderer fontRenderer = new CustomFontRenderer(
                glyphPage,
                glyphPage,
                glyphPage,
                glyphPage
        );

        //player name
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> mc.getSession().getUsername())
                .setColor("main_menu.texts.player_name.color")
                .setLayer("main_menu.texts.player_name.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.player_name.x")
                .setY("main_menu.texts.player_name.y")
                .setWidth("main_menu.texts.player_name.width")
                .setHeight("main_menu.texts.player_name.height")
                .build().register();
        //rank
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> ClientProxy.playerData.getRank())
                .setColor("main_menu.texts.rank.color")
                .setLayer("main_menu.texts.rank.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.rank.x")
                .setY("main_menu.texts.rank.y")
                .setWidth("main_menu.texts.rank.width")
                .setHeight("main_menu.texts.rank.height")
                .build().register();
        //gang name
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> ClientProxy.playerData.getGang())
                .setColor("main_menu.texts.gang_name.color")
                .setLayer("main_menu.texts.gang_name.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.gang_name.x")
                .setY("main_menu.texts.gang_name.y")
                .setWidth("main_menu.texts.gang_name.width")
                .setHeight("main_menu.texts.gang_name.height")
                .build().register();
        //level
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> String.valueOf(ClientProxy.playerData.getLevel()))
                .setColor("main_menu.texts.level.color")
                .setLayer("main_menu.texts.level.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.level.x")
                .setY("main_menu.texts.level.y")
                .setWidth("main_menu.texts.level.width")
                .setHeight("main_menu.texts.level.height")
                .build().register();
        //money
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> "$ " + ClientProxy.playerData.getMoney())
                .setColor("main_menu.texts.money.color")
                .setLayer("main_menu.texts.money.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.money.x")
                .setY("main_menu.texts.money.y")
                .setWidth("main_menu.texts.money.width")
                .setHeight("main_menu.texts.money.height")
                .build().register();
        //kills
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> ClientProxy.playerData.getOtherOrDefault("kills", "0"))
                .setColor("main_menu.texts.kills.color")
                .setLayer("main_menu.texts.kills.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.texts.kills.x")
                .setY("main_menu.texts.kills.y")
                .setWidth("main_menu.texts.kills.width")
                .setHeight("main_menu.texts.kills.height")
                .build().register();
        //deaths
        GuiElementText.builder(this)
                .setFontRenderer(fontRenderer)
                .setText(() -> ClientProxy.playerData.getOtherOrDefault("deaths", "0"))
                .setColor("main_menu.texts.deaths.color")
                .setLayer("main_menu.texts.deaths.layer",
                        GuiElementLayer.MIDDLE
                )

                .setX("main_menu.texts.deaths.x")
                .setY("main_menu.texts.deaths.y")
                .setWidth("main_menu.texts.deaths.width")
                .setHeight("main_menu.texts.deaths.height")
                .build().register();


        //----images----

        //background
        GuiElementImage.builder(this)
                .setLayer(GuiElementLayer.BACKGROUND)
                .setImageBinder(() -> mc.getTextureManager().bindTexture(BACKGROUND_IMAGE))
                .setX((scaleFactor) -> 0)
                .setY((scaleFactor) -> 0)
                .setWidth((scaleFactor) -> width)
                .setHeight((scaleFactor) -> height)
                .setTextureWidth(-1)
                .setTextureHeight(-1)
                .build().register();

        //player head
        GuiElementImage.builder(this)
                .setImageBinder(ClientProxy::bindPlayerSkinTexture)
                .setTextureX(32)
                .setTextureY(32)
                .setTextureWidth(32)
                .setTextureHeight(32)
                .setLayer("main_menu.player_head.layer",
                        GuiElementLayer.MIDDLE
                )
                .setX("main_menu.player_head.x")
                .setY("main_menu.player_head.y")
                .setWidth("main_menu.player_head.width")
                .setHeight("main_menu.player_head.height")
                .build().register();


    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }

        if (button.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion"), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 12));
            }
        }
    }
}
