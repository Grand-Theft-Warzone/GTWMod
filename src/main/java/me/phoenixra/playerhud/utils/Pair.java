package me.phoenixra.playerhud.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Pair<A, B> {
    /**
     * The first item in the tuple.
     */
    @NotNull @Getter @Setter
    private A first;

    /**
     * The second item in the tuple.
     */
    @NotNull @Getter @Setter
    private B second;
}
