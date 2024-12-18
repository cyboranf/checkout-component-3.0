package com.component.checkout.service;

import java.time.LocalDateTime;
import java.util.Date;

public interface TimeProvider {

    LocalDateTime now();
    Date nowDate();
}
