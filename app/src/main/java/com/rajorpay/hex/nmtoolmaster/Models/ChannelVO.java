package com.rajorpay.hex.nmtoolmaster.Models;

public class ChannelVO {
    private String channel;
    private int amount;

    public ChannelVO() {
    }

    public ChannelVO(String channel, int amount) {
        this.channel = channel;
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
