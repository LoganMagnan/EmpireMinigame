package org.altopia.empiregame.plugin.managers.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Ability {

    private String name;
    private String description;
    private AbilityType abilityType;
}
