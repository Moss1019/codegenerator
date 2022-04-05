package com.mossonthetree.codegenerator.view;

public class GuidGenerateRequest {
    private int amount;
    private boolean braced;
    private boolean dashed;

    public GuidGenerateRequest() {

    }

    public GuidGenerateRequest(int amount, boolean braced, boolean dashed) {
        this.amount = amount;
        this.braced = braced;
        this.dashed = dashed;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBraced() {
        return braced;
    }

    public void setBraced(boolean braced) {
        this.braced = braced;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }
}
