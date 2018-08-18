package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/7/27.
 */

public class ColorUtils {
    public static int getColor(int positon){
        int color = 0;
        Resources resources = MyApplication.getAppContext().getResources();
        if (positon == 1){
            //color = resources.getColor(R.color.btn_login_bg_color);
        }else if (positon == 2){
            color = resources.getColor(R.color.white);
        }else if (positon == 3){
            color = resources.getColor(R.color.water_bg_002);
        }else if (positon == 4){
            color = resources.getColor(R.color.water_bg_003);
        }else if (positon == 5){
            color = resources.getColor(R.color.water_bg_004);
        }else if (positon == 6){
            color = resources.getColor(R.color.water_bg_005);
        }else if (positon == 7){
            color = resources.getColor(R.color.water_bg_006);
        }else if (positon == 8){
            color = resources.getColor(R.color.water_bg_007);
        }else if (positon == 9){
            color = resources.getColor(R.color.water_bg_008);
        }else if (positon == 10){
            color = resources.getColor(R.color.water_bg_009);
        }else if (positon == 11){
            color = resources.getColor(R.color.water_bg_010);
        }else if (positon == 12){
            color = resources.getColor(R.color.water_bg_011);
        }else if (positon == 13){
            color = resources.getColor(R.color.water_bg_012);
        }else if (positon == 14){
            color = resources.getColor(R.color.water_bg_013);
        }else if (positon == 15){
            color = resources.getColor(R.color.water_bg_014);
        }else if (positon == 16){
            color = resources.getColor(R.color.water_bg_015);
        }else if (positon == 17){
            color = resources.getColor(R.color.water_bg_016);
        }else if (positon == 18){
            color = resources.getColor(R.color.water_bg_017);
        }else if (positon == 19){
            color = resources.getColor(R.color.water_bg_018);
        }else if (positon == 20){
            color = resources.getColor(R.color.water_bg_019);
        }else if (positon == 21){
            color = resources.getColor(R.color.water_bg_020);
        }else if (positon == 22){
            color = resources.getColor(R.color.water_bg_021);
        }else if (positon == 23){
            color = resources.getColor(R.color.water_bg_022);
        }else if (positon == 24){
            color = resources.getColor(R.color.water_bg_023);
        }else if (positon == 25){
            color = resources.getColor(R.color.water_bg_024);
        }else if (positon == 26){
            color = resources.getColor(R.color.water_bg_025);
        }else if (positon == 27){
            color = resources.getColor(R.color.text_watermarkid_5007_color);
        }else if (positon == 28){
            color = resources.getColor(R.color.text_watermarkid_5009_color);
        }else if (positon == 29){
            color = resources.getColor(R.color.text_watermarkid_7005_color);
        }
        return color;
    }
}
