package com.component.checkout.service;

import java.util.Date;

/**
 * Provides the current time in a Polish date format.
 */
public interface TimeProvider {

    /**
     * Returns the current date and time formatted in Polish standard style.
     * For example: "dd.MM.yyyy HH:mm:ss"
     *
     * @return A String representing the current date/time in Polish format.
     */
    String nowDate();

    /**
     * @return date
     */
    Date now();
}
