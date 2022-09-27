package org.altopia.empiregame.plugin.managers.customevents;

import lombok.Getter;
import lombok.Setter;
import org.altopia.empiregame.Main;
import org.altopia.empiregame.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomEventManager {

    private Main main = Main.getInstance();

    private List<CustomEvent> customEvents = new ArrayList<CustomEvent>();

    private CustomEventType currentCustomEvent = CustomEventType.NONE;

    private long duration;

    public CustomEventManager(){
        for (CustomEventType customEventType : CustomEventType.values()){
            String name = customEventType.name().toLowerCase().replace("_", "-");

            CustomEvent customEvent = new CustomEvent(this.main.getCustomEventsConfig().getConfig().getString("custom-events." + name + ".name"), this.main.getAbilitiesConfig().getConfig().getString("custom-events." + name + ".description"), customEventType);

            this.customEvents.add(customEvent);
        }
    }

    public void startCustomEvent(CommandSender sender, String[] args){
        if (this.main.getCustomEventManager().getCurrentCustomEvent() != CustomEventType.NONE){
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.start.custom-event-already-started")));

            return;
        }

        String customEventName = args[1].toLowerCase();

        for (CustomEventType customEventType : CustomEventType.values()) {
            String name = customEventType.name().toLowerCase().replace("_", "");

            if (customEventName.equals(name)) {
                this.currentCustomEvent = customEventType;

                break;
            }
        }

        String duration = args[2].toLowerCase();

        this.duration = Utils.parseTime(duration);

        for (CustomEvent customEvent : this.customEvents) {
            if (this.currentCustomEvent != customEvent.getCustomEventType()) {
                continue;
            }

            List<String> message = new ArrayList<String>();

            for (String string : this.main.getMessagesConfig().getConfig().getStringList("event-command.start.custom-event-started")) {
                message.add(string.replace("(customeventname)", customEvent.getName()).replace("(customeventdescription)", customEvent.getDescription()));
            }

            for (String string : message) {
                Bukkit.broadcastMessage(Utils.translate(string));
            }
        }
    }

    public void stopCustomEvent(CommandSender sender) {
        if (this.main.getCustomEventManager().getCurrentCustomEvent() == CustomEventType.NONE) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString( "event-command.stop.custom-event-not-started")));

            return;
        }

        this.currentCustomEvent = CustomEventType.NONE;
        this.duration = 0L;

        Bukkit.broadcastMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("event-command.stop.custom-event-stopped")));
    }

    public void showAllCustomEvents(CommandSender sender) {
        sender.sendMessage(Utils.translate("&cEvents:"));

        for (CustomEvent customEvent : this.customEvents) {
            sender.sendMessage(Utils.translate("  &c" + customEvent.getName()));
        }
    }
}
