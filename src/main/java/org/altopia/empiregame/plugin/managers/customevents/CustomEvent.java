package org.altopia.empiregame.plugin.managers.customevents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomEvent {

    private String name;
    private String description;
    private CustomEventType customEventType;

}