package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneState;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.utils.MathUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.ElementImage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;

public class CanvasPhoneImpl extends CanvasPhone {


    private List<PhoneApp> appDrawingOrder;
    private List<IconPosition> appIconPositions;


    protected ElementImage background;
    protected ElementImage phoneDisplay;

    private HashMap<DisplayResolution, Integer> iconPadding = new HashMap<>();
    private HashMap<DisplayResolution, Integer> iconsPerRow = new HashMap<>();
    private int iconPaddingDefault = 10;
    private int iconsPerRowDefault = 3;

    private float horizontalShapeScale = 1.5f;


    private PhoneShapeDrawer phoneShapeDrawer;

    private boolean init;

    public CanvasPhoneImpl(AtumMod atumMod) {
        super(atumMod);
        appDrawingOrder = GtwAPI.getInstance()
                .getPhoneManager().getAppDrawingOrder();


    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor,
                          int mouseX, int mouseY) {
        super.onDraw(resolution, scaleFactor, mouseX, mouseY);

        phoneShapeDrawer.draw(resolution, scaleFactor, mouseX, mouseY);

        switch (state) {
            case OPENED_DISPLAY:
                if (!init) {
                    appIconPositions = IconPosition.calculateIconLayout(
                            phoneDisplay.getX(),
                            phoneDisplay.getY(),
                            phoneDisplay.getWidth(),
                            phoneDisplay.getHeight(),
                            appDrawingOrder.size(),
                            iconsPerRow.getOrDefault(resolution, iconsPerRowDefault),
                            iconPadding.getOrDefault(resolution, iconPaddingDefault)
                    );
                    init = true;
                }

                for (int i = 0; i < appDrawingOrder.size(); i++) {
                    appDrawingOrder.get(i).drawIcon(this, resolution,
                            appIconPositions.get(i).x,
                            appIconPositions.get(i).y,
                            appIconPositions.get(i).size,
                            isAppHovered(
                                    appIconPositions.get(i).x,
                                    appIconPositions.get(i).y,
                                    appIconPositions.get(i).size,
                                    mouseX,
                                    mouseY
                            )
                    );
                }
                break;
            case OPENED_APP:
                int displayX = phoneDisplay.getX();
                int displayY = phoneDisplay.getY();
                int displayWidth = phoneDisplay.getWidth();
                int displayHeight = phoneDisplay.getHeight();

                //calculated phone display location depending on a shape
                if (shape == PhoneShape.VERTICAL) {
                    openedApp.draw(this, resolution,
                            displayX,
                            displayY,
                            displayWidth,
                            displayHeight,
                            mouseX, mouseY);

                } else if (shape == PhoneShape.HORIZONTAL) {
                    int centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    int centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);

                    displayX = centerX - (centerY - phoneDisplay.getY());
                    displayY = centerY - (phoneDisplay.getX() +
                            phoneDisplay.getWidth() - centerX);
                    displayWidth = phoneDisplay.getHeight();
                    displayHeight = phoneDisplay.getWidth();
                    int widthScaled = (int) (displayWidth * horizontalShapeScale);
                    int heightScaled = (int) (displayHeight * horizontalShapeScale);
                    openedApp.draw(this, resolution,
                            displayX - (widthScaled - displayWidth) / 2,
                            displayY - (heightScaled - displayHeight) / 2,
                            widthScaled,
                            heightScaled,
                            mouseX, mouseY);

                } else {
                    openedApp.draw(this, resolution,
                            0,
                            0,
                            Display.getWidth(),
                            Display.getHeight(),
                            mouseX, mouseY);
                }
                break;
        }
    }

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config, configKey);
        appDrawingOrder = GtwAPI.getInstance()
                .getPhoneManager().getAppDrawingOrder();

        HashMap<PhoneState, Integer> animationDuration = new HashMap<>();
        for (PhoneState phoneState : PhoneState.values()) {
            animationDuration.put(
                    phoneState,
                    config.getIntOrDefault(
                            "animationDuration." + phoneState.name(),
                            25
                    )
            );
        }
        phoneShapeDrawer = new PhoneShapeDrawer(
                this,
                animationDuration
        );

        iconPaddingDefault = config.getIntOrDefault("iconPadding", 32);
        iconsPerRowDefault = config.getIntOrDefault("iconsPerRow", 4);

        horizontalShapeScale = (float) config.getDoubleOrDefault("horizontalShapeScale", 1.5f);

        if (config.hasPath("background")) {
            Config config1 = config.getSubsection("background");
            background = new ElementImage(
                    getAtumMod(),
                    this
            );

            background.updateVariables(config1, "background");
            background.setOriginX(getOriginX());
            background.setOriginY(getOriginY());
            background.setOriginWidth(getOriginWidth());
            background.setOriginHeight(getOriginHeight());
        }
        if (config.hasPath("phoneDisplay")) {
            Config config1 = config.getSubsection("phoneDisplay");
            phoneDisplay = new ElementImage(
                    getAtumMod(),
                    this
            );
            phoneDisplay.updateVariables(config1, "phoneDisplay");
            phoneDisplay.setOriginX(getOriginX() + phoneDisplay.getOriginX());
            phoneDisplay.setOriginY(getOriginY() + phoneDisplay.getOriginY());
            phoneDisplay.setOriginWidth(getOriginWidth() + phoneDisplay.getOriginWidth());
            phoneDisplay.setOriginHeight(getOriginHeight() + phoneDisplay.getOriginHeight());
        }
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        iconPadding.put(resolution, config.getIntOrDefault("iconPadding", 10));
        iconsPerRow.put(resolution, config.getIntOrDefault("iconsPerRow", 3));
    }

    private void updateState() {
        switch (state) {
            case OPENING:
                background.setAdditionY(0);
                phoneDisplay.setAdditionY(0);
                state = PhoneState.OPENED_DISPLAY;
                break;
            case CLOSING:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case HORIZONTAL_TO_FULL_SCREEN:
            case HORIZONTAL_TO_VERTICAL:
            case VERTICAL_TO_HORIZONTAL:
            case VERTICAL_TO_FULL_SCREEN:
            case FULL_SCREEN_TO_HORIZONTAL:
            case FULL_SCREEN_TO_VERTICAL:
                state = openedApp == null ?
                        PhoneState.OPENED_DISPLAY : PhoneState.OPENED_APP;
                break;
            default:
                break;
        }
    }

    @Override
    protected void setState(@NotNull PhoneState state) {
        if (this.state == PhoneState.CLOSING) return;
        this.state = state;
        phoneShapeDrawer.animationTimer = 0;
    }

    @Override
    public void changeShape(@NotNull PhoneShape shape) {
        setState(PhoneState.fromShape(this.shape, shape));
        this.shape = shape;
    }


    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {
        int pressed = Mouse.getEventButton();
        PhoneState phoneState = getState();
        if (phoneState != PhoneState.OPENED_DISPLAY) {
            return;
        }
        if (Mouse.getEventButtonState() && pressed == 0) {
            for (int i = 0; i < appDrawingOrder.size(); i++) {
                PhoneApp app = appDrawingOrder.get(i);
                if (isAppHovered(
                        appIconPositions.get(i).x,
                        appIconPositions.get(i).y,
                        appIconPositions.get(i).size,
                        getLastMouseX(),
                        getLastMouseY()
                )) {
                    System.out.println("Opening app: " + app.getAppName());
                    openedApp = app;
                    app.onOpen(this);
                    if (app.getShapeRequired() != PhoneShape.VERTICAL) {
                        changeShape(app.getShapeRequired());
                        return;
                    }
                    setState(PhoneState.OPENED_APP);
                    return;
                }
            }
        }
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }

    private boolean isAppHovered(int x, int y, int size,
                                 int mouseX, int mouseY) {
        return mouseX >= x &&
                mouseX <= x + size
                && mouseY >= y
                && mouseY <= y + size;
    }


    @Override
    protected BaseElement onClone(BaseElement baseCanvas) {

        CanvasPhoneImpl canvasPhone = (CanvasPhoneImpl) super.onClone(baseCanvas);
        canvasPhone.init = false;
        canvasPhone.phoneShapeDrawer = new PhoneShapeDrawer(
                canvasPhone,
                phoneShapeDrawer == null ? new HashMap<>() : phoneShapeDrawer.animationDuration
        );
        if (background != null) {
            canvasPhone.background = (ElementImage) background.cloneWithRandomId();
        }
        if (phoneDisplay != null) {
            canvasPhone.phoneDisplay = (ElementImage) phoneDisplay.cloneWithRandomId();
        }
        canvasPhone.iconPadding = new HashMap<>(iconPadding);
        canvasPhone.iconsPerRow = new HashMap<>(iconsPerRow);
        canvasPhone.openedApp = null;
        return canvasPhone;
    }


    private static class PhoneShapeDrawer {
        private CanvasPhoneImpl phone;
        @Getter
        private HashMap<PhoneState, Integer> animationDuration;
        private int animationTimer = 0;

        private PhoneShapeDrawer(CanvasPhoneImpl phone, HashMap<PhoneState, Integer> animationDuration) {
            this.phone = phone;
            this.animationDuration = animationDuration;
        }

        public void draw(DisplayResolution resolution, float scaleFactor,
                         int mouseX, int mouseY) {
            PhoneState phoneState = phone.getState();
            ElementImage background = phone.background;
            ElementImage phoneDisplay = phone.phoneDisplay;

            //-------WITHOUT ANIMATION-------
            if (phoneState == PhoneState.OPENED_APP
                    || phoneState == PhoneState.OPENED_DISPLAY) {


                if (phone.shape == PhoneShape.VERTICAL) {
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    return;
                } else if (phone.shape == PhoneShape.HORIZONTAL) {
                    int rotationAngle = -90;
                    int centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    int centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);

                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAngle,
                            phone.horizontalShapeScale,
                            0
                    );
                } else {
                    RenderUtils.fill(
                            0,
                            0,
                            Display.getWidth(),
                            Display.getHeight(),
                            0x000000,
                            1.0f
                    );
                }

                return;
            }

            //-------WITH ANIMATION-------
            animationTimer++;
            int animationDuration = this.animationDuration.getOrDefault(
                    phoneState,
                    25
            );

            int animate;
            int rotationAngle;
            float scaleShape;
            int centerX;
            int centerY;
            float rotationAnimation;
            float scaleAnimation;
            double b;
            switch (phoneState) {
                case OPENING:
                    animate = (int) (MathUtils.fastCos(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    ) * (1920 - background.getOriginY())
                    );
                    background.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    break;
                case CLOSING:
                    animate = (int) (MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    ) * (1920 - background.getOriginY())
                    );
                    background.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    break;
                case VERTICAL_TO_HORIZONTAL:
                    scaleShape = phone.horizontalShapeScale;
                    rotationAngle = -90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAnimation,
                            1,
                            scaleAnimation
                    );
                    break;
                case HORIZONTAL_TO_VERTICAL:
                    scaleShape = -phone.horizontalShapeScale;
                    rotationAngle = 90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            rotationAnimation,
                            phone.horizontalShapeScale,
                            scaleAnimation
                    );
                    break;

                case VERTICAL_TO_FULL_SCREEN:
                    scaleShape = 4;
                    rotationAngle = -90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAnimation,
                            1,
                            scaleAnimation
                    );
                    break;
                case FULL_SCREEN_TO_VERTICAL:
                    scaleShape = -4;
                    rotationAngle = 90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            rotationAnimation,
                            4,
                            scaleAnimation
                    );
                    break;
                case HORIZONTAL_TO_FULL_SCREEN:
                    scaleShape = 4;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            0,
                            phone.horizontalShapeScale,
                            scaleAnimation
                    );
                    break;
                case FULL_SCREEN_TO_HORIZONTAL:
                    scaleShape = -(4-phone.horizontalShapeScale);
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            0,
                            4,
                            scaleAnimation
                    );
                    break;
                default:
                    break;

            }
            if (animationTimer >= animationDuration) {
                phone.updateState();
                animationTimer = 0;
            }
        }

        private void drawRotated(ElementImage background,
                                 ElementImage phoneDisplay,
                                 DisplayResolution resolution,
                                 float scaleFactor,
                                 int mouseX, int mouseY,
                                 int centerX, int centerY,
                                 float rotationAngleDefault,
                                 float rotationAngle,
                                 float scaleDefault,
                                 float scaleNew) {
            GL11.glPushMatrix();
            GL11.glTranslatef(centerX, centerY, 0); // Translate to the center
            GL11.glRotatef(rotationAngleDefault + rotationAngle, 0, 0, 1); // Rotate around the center
            GL11.glScalef(scaleDefault + scaleNew, scaleDefault + scaleNew, 1); // Scale to the new size
            GL11.glTranslatef(-centerX, -centerY, 0); // Translate back

            background.draw(resolution, scaleFactor, mouseX, mouseY);
            phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);

            // Restore the matrix state
            GL11.glPopMatrix();
        }
    }
}
