package me.lucky.core.api.signaling;

public enum SignalType {
    CHECK_VERSION("checkVersion", "checkVersionResponse");

    private final String channel;
    private final String answerChannel;

    private SignalType(String channel) {
        this.channel = channel;
        this.answerChannel = channel;
    }

    private SignalType(String channel, String answerChannel) {
        this.channel = channel;
        this.answerChannel = answerChannel;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getAnswerChannel() {
        return answerChannel;
    }
}
