package com.strind.wemedia;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.strind.common.ProtostuffUtil;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmNewsPageDto;
import com.strind.model.wemedia.pojos.WmUser;
import com.strind.thread.WmmediaThreadLocalUtil;
import com.strind.wemedia.service.WmChannelService;
import com.strind.wemedia.service.WmNewsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
class NewsWemediaServiceApplicationTests {

    @Autowired
    private WmChannelService wmChannelService;

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * description: 测试频道的获取
     */
    @Test
    public void testGetChannels() {
        System.out.println(wmChannelService.getChannels().getData());
    }

    /**
     * description: 测试自媒体端内容的分页获取
     * problem: 想要在threadlocal中设置用户
     */
    @Test
    public void testLoadNews() {
        WmNewsPageDto dto = new WmNewsPageDto();
        dto.setPage(1);
        dto.setSize(10);

        RespResult list = wmNewsService.findList(dto);
        System.out.println(list.getData());
    }

    /**
     * description:
     */
    @Test
    public void testJson() {
        List<Map> maps = JSONArray.parseArray("[\n"
            + "  {\n"
            + "    \"type\": \"text\",\n"
            + "    \"value\": \"愿你成为坚持看到天亮的人\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"type\": \"text\",\n"
            + "    \"value\": \"小时候我们都听过阿拉丁神灯的故事，擦下神灯便可以向灯神许三个愿望，那么现在我想问，如果真的给你实现三个愿望，你会要什么。富可敌国的财富？一手遮天的权利？倾国倾城的美女？健康的身体抑或是长命百岁的寿命？可能三个愿望可以勉强达到你内心的欲望需求，你既可以顾及这个又可以得到另一个，那么如果只能许一个愿望呢。花花世界，你想要的东西太多了，乱花渐欲迷人眼，你这个也想要，那个也觉得好，总都不是你想放弃的，患得患失到最后迷茫了，纠结忐忑到最后抑郁了。好了，有了刚才那些问题后，那么现在想想什么最重要，你最想要的又是什么。这个问题很多人回答不上来，年轻人尤甚。我曾经也一度迷失自己，不明白人活一世到底要追求什么，想要什么样的人生，我向往过大都市的繁华与纸醉金迷，向往过呼风唤雨的权利游戏，之心美丽的灵魂伴侣。但是随着时间的推移，慢慢的脑海中那些灯红酒绿退去了，渐渐浮现于脑海中的却是小桥流水人家，闲云野鹤古刹。最后明白，原来那些梦幻泡影退去后，要的不过就是内心中片刻的宁静。或许有的人，活一世直至死亡时才能幡然醒悟，或许有些人至死还在被欲望支使着执着那些虚幻的“美梦”。\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"type\": \"image\",\n"
            + "    \"value\": \"http://123.57.165.115:9000/shixun/2024/03/24/90569db22b61466abb91dae7d61a2eaa.jpeg\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"type\": \"text\",\n"
            + "    \"value\": \"请在这里输入正文\"\n"
            + "  }\n"
            + "]", Map.class);
        for (Map map : maps) {
            System.out.println(map);
        }
    }

    /**
     * description:
     */
    @Test
    public void testLen() {
        Map<String,String> map = new HashMap<>();
        map.put("articleId",String.valueOf(154687554L));
        map.put("type",String.valueOf(15));
        byte[] serialize = ProtostuffUtil.serialize(map);
        System.out.println(serialize.length);

        Map ans = ProtostuffUtil.deserialize(serialize, Map.class);
        ans.forEach((k,v)->{
            System.out.println(k + "  ==  " + v);
        });
    }
}
