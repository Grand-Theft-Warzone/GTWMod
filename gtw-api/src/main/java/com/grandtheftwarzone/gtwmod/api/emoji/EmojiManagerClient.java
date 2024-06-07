package com.grandtheftwarzone.gtwmod.api.emoji;

public interface EmojiManagerClient {

    void addRLEmoji(RLEmoji emoji);
    void removeRLEmoji(RLEmoji emoji);

    void setRenderEmoji(boolean renderEmoji);

    boolean isRenderEmoji();
}
