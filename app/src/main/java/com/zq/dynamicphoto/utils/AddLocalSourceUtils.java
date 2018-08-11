package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.zq.dynamicphoto.R;

import java.util.ArrayList;

/**
 * 获取本地资源
 * Created by Administrator on 2018/8/7.
 */

public class AddLocalSourceUtils {
    private static AddLocalSourceUtils instance;

    /**
     * 单例
     * @return
     */
    public static AddLocalSourceUtils getInstance(){
        if (null == instance) {
            synchronized (AddLocalSourceUtils.class) {
                if (null == instance) {
                    instance = new AddLocalSourceUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 字体名称集合
     * @return
     */
    public ArrayList<String> getTextFontName(){
        ArrayList<String>list = new ArrayList<>();
        list.add("jingdainxiyuanjian.TTF");
        //list.add("jingdiansongti.TTF");
        list.add("jingdianxilishujian.TTF");
        list.add("jingdianxiyuanfan.TTF");
        list.add("jingdianyuantijian.TTF");
        list.add("keshilugangbiheti.TTF");
        list.add("keshilumingti.TTF");
        list.add("keshilumingtibiaodianzhizhong.TTF");
        list.add("keshilumingtiyiti.TTF");
        list.add("langmanyayuan.ttf");
        list.add("liguofushouxieti.ttf");


        list.add("liulixingshu.ttf");
        list.add("lixukemaobixingshu.ttf");
        //list.add("maozedongziti.ttf");
        list.add("minihanzhenguangbiao.ttf");
        list.add("minijianbaiqing.ttf");
        list.add("minijiancaiyun.TTF");
        list.add("minijianchenpinpo.ttf");
        list.add("minijiancuqian.ttf");
        list.add("minijianshaoer.ttf");
        list.add("minijianshengong.TTF");


        list.add("minijianshuihei.TTF");
        list.add("minijianxiqian.ttf");
        list.add("minijianxixingkai.TTF");
        list.add("pangmenzhengdaobiaotiti.TTF");
        list.add("quanxinyingbikaishujian.ttf");
        list.add("quanxinyingbilishujian.ttf");
        list.add("quanxinyingbixingshujian.ttf");
        list.add("shamenghaishufaziti.ttf");
        list.add("shenfenzhengshejiziti.TTF");
        list.add("shishangzhongheijianti.ttf");


        list.add("shizhuangdanxian.ttf");
        list.add("shutifanganjingchengangbixingshu.ttf");
        list.add("shutifangguoxiaoyugangbikaiti.TTF");
        list.add("shutifangwangxueqingangbixingshu.ttf");
        list.add("shutifangyingbixingshu.TTF");
        list.add("shutifangyuyourenbiaozhuncaoshu.ttf");
        list.add("sitonglifangsongertijian.ttf");
        list.add("sitonglifangxiulitijian.ttf");
        list.add("sitonglifangxiyuanti.TTF");
        list.add("sitonglifangzhongyuanjianti.TTF");


        list.add("sunguotingshuputi.ttf");
        list.add("suxinshiluanshiti.ttf");
        list.add("suxinshishubiaoxingshujian.ttf");
        list.add("wanghanzongbiaokaiti_kongxin.TTF");
        list.add("wanghanzongbokati_kongyin.TTF");
        list.add("wanghanzongchaoheitiqiaopidongwuyi.TTF");
        list.add("wanghanzongchaomingtifan.TTF");
        list.add("wanghanzongcugangti_biaozhun.TTF");
        return list;
    }

    public ArrayList<Drawable> getTextFontPic(Activity mContext){
        ArrayList<Drawable> list = new ArrayList<>();
        list.add(mContext.getResources().getDrawable(R.drawable.text_font000));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font001));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font002));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font003));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font004));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font005));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font006));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font007));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font008));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font009));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font010));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font011));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font012));

        list.add(mContext.getResources().getDrawable(R.drawable.text_font013));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font014));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font015));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font016));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font17));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font018));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font019));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font020));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font021));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font022));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font023));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font024));

        list.add(mContext.getResources().getDrawable(R.drawable.text_font025));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font026));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font027));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font028));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font029));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font030));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font031));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font032));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font033));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font034));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font035));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font036));

        list.add(mContext.getResources().getDrawable(R.drawable.text_font037));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font038));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font039));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font040));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font041));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font042));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font043));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font044));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font045));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font046));
        list.add(mContext.getResources().getDrawable(R.drawable.text_font047));
        return list;
    }

    public ArrayList<Drawable> getWaterIconList(Activity mContext){
        ArrayList<Drawable> list = new ArrayList<>();
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon000));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon001));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon002));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon003));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon004));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon005));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon006));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon007));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon008));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon009));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon010));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon011));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon012));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon013));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon014));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon015));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon016));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon017));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon018));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon019));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon020));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon021));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon022));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon023));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon024));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon025));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon026));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon027));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon028));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon029));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon030));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon031));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon032));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon033));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon034));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon035));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon036));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon037));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon038));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon039));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon040));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon041));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon042));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon043));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon044));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon045));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon046));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon047));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon048));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon049));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon050));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon051));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon052));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon053));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon054));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon055));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon056));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon057));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon058));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon059));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon060));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon061));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon062));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon063));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon064));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon065));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon066));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon067));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon068));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon069));
        list.add(mContext.getResources().getDrawable(R.drawable.water_icon070));
        return list;
    }
}
