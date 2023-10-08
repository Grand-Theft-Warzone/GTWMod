package com.grandtheftwarzone.gtwclient.api.gui.phone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an app in the
 * {@link com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneGui}
 * implementation
 * <p></p>
 * The app class must implement
 * {@link com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneApp}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterPhoneApp {
}
