package me.phoenixra.gtwclient.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Pair<A,B> {
    @Getter @Setter
    private A first;
    @Getter @Setter
    private B second;
}
