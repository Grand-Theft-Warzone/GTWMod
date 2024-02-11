package com.grandtheftwarzone.gtwmod.api.gui.phone.annotations;

import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an app in the
 * {@link PhoneManager}
 * implementation
 * <p></p>
 * The app class must implement
 * {@link PhoneApp}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterPhoneApp {
}
