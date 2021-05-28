package me.dcal.thermoconnectapp.Modeles;

public class TerraListData {

    BodyTerrarium bt;
    BodyTerrariumData bd;

    public TerraListData(BodyTerrarium bt, BodyTerrariumData bd) {
        this.bt = bt;
        this.bd = bd;
    }

    public BodyTerrarium getBt() {
        return bt;
    }

    public void setBt(BodyTerrarium bt) {
        this.bt = bt;
    }

    public BodyTerrariumData getBd() {
        return bd;
    }

    public void setBd(BodyTerrariumData bd) {
        this.bd = bd;
    }
}
