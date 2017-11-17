import com.ylfin.spider.SpiderClient;
import com.ylfin.spider.service.KeyWordsService;
import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.KeyWords;
import com.ylfin.spider.vo.bean.TaoBaoResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpiderClient.class})
public class TaoBaoResultServiceImplTest {

    @Autowired
    KeyWordsService keyWordsService ;

    @Autowired
    TaoBaoResultService taoBaoResultService;

    @Test
    public void keyWordsTest(){
      List<KeyWords> list = keyWordsService.query();
        System.out.println(list);
    }


    @Test
    public void findActive(){
        List<KeyWords> list = keyWordsService.findActive();
        System.out.println(list);
    }


    @Test
    public void batchInserTest(){
        List<TaobaoVO> list = new ArrayList<>();
        TaobaoVO t = new TaobaoVO();
        t.setKeywordId(1);
        t.setShop("xxx");
        list.add(t);
        taoBaoResultService.batchSave(list);
    }
}
