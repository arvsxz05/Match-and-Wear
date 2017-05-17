package com.example.arvin.matchandwear2;

import java.util.Date;

/**
 * Created by User on 12/5/2016.
 */

public class OutfitDetails {

    int id;
    String outfit_name;
    int type;
    Date date;

    OutfitDetails(int vId, String vOutfitName, int vType, Date vDate) {
        this.id = vId;
        this.outfit_name = vOutfitName;
        this.type = vType;
        this.date = vDate;
    }
}
