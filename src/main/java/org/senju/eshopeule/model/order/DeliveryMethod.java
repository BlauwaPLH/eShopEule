package org.senju.eshopeule.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryMethod {
    GRAB_EXPRESS("Grab Express"),
    SHOPPE_EXPRESS("Shoppe Express"),
    YAS_EXPRESS("Yas Express");

    private final String name;
}
