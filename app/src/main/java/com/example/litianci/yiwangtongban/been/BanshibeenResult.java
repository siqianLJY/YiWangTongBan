package com.example.litianci.yiwangtongban.been;

import java.util.List;

/**
 * Created by litianci on 2019/5/7.
 */

public class BanshibeenResult {

    /**
     * code : 0
     * msg : ok
     * data : [{"id":18,"name":"生育收养","type":"1","icons":"\\public\\uploads\\20190419\\e415990bc31374474d87844d27bc0859.png"},{"id":21,"name":"社会保障","type":"1","icons":"\\public\\uploads\\20190420\\b16bbae95978867b7845914ae8994291.png"},{"id":22,"name":"医疗卫生","type":"1","icons":"\\public\\uploads\\20190420\\bae44bf5d27d8f22f7b6ebd60fc9b0da.png"},{"id":23,"name":"教育科研","type":"1","icons":"\\public\\uploads\\20190420\\41cbb855d63207de524dd1731640d6a1.png"},{"id":24,"name":"住房保障","type":"1","icons":"\\public\\uploads\\20190420\\b80043e020b53d21a42c91c2b18a451e.png"},{"id":25,"name":"证件办理","type":"1","icons":"\\public\\uploads\\20190420\\3c4a9ef446653369068710fd791b5d03.png"},{"id":26,"name":"社会救助","type":"1","icons":"\\public\\uploads\\20190420\\861d2693236e9eff0ab6809a0681dede.png"},{"id":27,"name":"离职退休","type":"1","icons":"\\public\\uploads\\20190420\\566143c324b13486d54353904011c677.png"},{"id":29,"name":"失业就业","type":"1","icons":"\\public\\uploads\\20190420\\0f8163692d63f793b5a28f251eae7515.png"},{"id":30,"name":"食品药品","type":"1","icons":"\\public\\uploads\\20190420\\0dbdb1a82155553e9e9beef1c49372c6.png"},{"id":31,"name":"独生子女","type":"1","icons":"\\public\\uploads\\20190420\\aa549e7732eaaa8694bd0075facb79e6.png"}]
     * count : 0
     */

    private int code;
    private String msg;
    private int count;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        private int id;
        private String name;
        private String type;
        private String icons;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIcons() {
            return icons;
        }

        public void setIcons(String icons) {
            this.icons = icons;
        }
    }
}
