package hust.phone.web.controller;

import hust.phone.constant.WebConst;
import hust.phone.utils.pojo.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.phone.service.interFace.UavService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UavController {

    @Autowired
    UavService planeServiceImpl;

    //获取无人机实时位置
/*    @RequestMapping(value = "/getlocation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getlocation(Plane plane) {

        Plane plane2 = planeServiceImpl.getPlaneByPlane(plane);
        PlaneVo planevo = new PlaneVo(plane2);

        return JsonUtils.objectToJson(planevo);
    }*/

    @RequestMapping(value = "getRealTimePic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getRealtimePic(@RequestParam(value = "taskid", required = false) String taskId) {
        List<String> testList = new ArrayList<>();
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531915145246&di=2d9496c9c1c0fdd4b58f009ee2c3ecc3&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F3ac79f3df8dcd100f40ca602788b4710b9122f04.jpg");
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531915145417&di=51170bd836e7b94b3c6dd8edbf244894&imgtype=0&src=http%3A%2F%2Fupbbsimg.cehome.com%2Fforum%2F201606%2F10%2F163716ql7ig69jqd6ykq19.jpg");
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531915145415&di=47cd1cfe383e09cc56249a25462d6859&imgtype=0&src=http%3A%2F%2Fphoto.go007.com%2F2014%2F01%2F20140114%2F635253046484697262.JPG");
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532400442694&di=ad1d0cebdccc653c57b09e69e8cdc066&imgtype=0&src=http%3A%2F%2Fwww.hinews.cn%2Fpic%2F0%2F10%2F74%2F58%2F10745858_871615.jpg");
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532400442692&di=b95dcac597261474d41ca661e72face6&imgtype=0&src=http%3A%2F%2Fwww.gov.cn%2Fjrzg%2Fimages%2Fimages%2F001e3741a4060f36e6c602.jpg");
        testList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532400442692&di=02c1f4e8b6f9b5bdcb2b153363cc69d3&imgtype=0&src=http%3A%2F%2Fnews.xinhuanet.com%2Fpolitics%2F2016-11%2F09%2F129356800_14786507875911n.gif");
        return JsonView.render(0, WebConst.SUCCESS_RESULT,testList);
    }


}
