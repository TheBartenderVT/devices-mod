package com.ultreon.devices;

import org.jetbrains.annotations.Contract;

public enum ModDeviceTypes implements IDeviceType {
    COMPUTER, PRINTER, FLASH_DRIVE, ROUTER, SEAT;

    @Override
    @Contract(pure = true, value = "-> this")
    public ModDeviceTypes getDeviceType() {
        return this;
    }
}
