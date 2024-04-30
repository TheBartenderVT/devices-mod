package com.ultreon.devices.api.event;

import com.ultreon.devices.api.TrayItemAdder;
import com.ultreon.devices.mineos.client.MineOS;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface LaptopEvent {
    Event<SetupTrayItems> SETUP_TRAY_ITEMS = EventFactory.createLoop();

    interface SetupTrayItems extends LaptopEvent {
        void setupTrayItems(MineOS laptop, TrayItemAdder trayItems);
    }
}
