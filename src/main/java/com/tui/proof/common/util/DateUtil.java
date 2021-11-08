package com.tui.proof.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
