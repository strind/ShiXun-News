package com.strind.wemedia;

import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmNewsPageDto;
import com.strind.model.wemedia.pojos.WmUser;
import com.strind.thread.WmmediaThreadLocalUtil;
import com.strind.wemedia.service.WmChannelService;
import com.strind.wemedia.service.WmNewsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

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
}
