package com.example.shang.meihuo.config;

import java.util.ArrayList;

/**
 * .bkt.clouddn.com为新创建存储空间后系统默认为用户生成的测试域名，此类测试域名，限总流量，限单 IP 访问频率，限速，仅供测试使用。
 * 单IP每秒限制请求次数10次，大于10次403禁止5秒。
 * 单url限速8Mbps，下载到10MB之后，限速1Mbps。
 *
 */

public class ConstantsImageUrl {


    private static ArrayList<String> myOnepic;

    public static ArrayList<String> getMyOnepic(){
        if (myOnepic==null){
            synchronized (ArrayList.class){
                if (myOnepic==null){
                    myOnepic = new ArrayList<>();
                    for (int i=1;i<10;i++){
                        //http://oqcummb0e.bkt.clouddn.com/bizhi_1.jpg
                        myOnepic.add("oqcummb0e.bkt.clouddn.com/bizhi"+i+".jpg");
                    }
                    return myOnepic;
                }
            }
        }
        return myOnepic;
    }

}
