package com.lifengdi.generatefile;

import com.lifengdi.GenerateFileApplication;
import com.lifengdi.commons.utils.Either;
import com.lifengdi.exception.ApiException;
import com.lifengdi.file.builder.HtmlToPDFBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 15:09 2019/9/5
 */
@SpringBootTest(classes = GenerateFileApplication.class)
public class GenerateTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private HtmlToPDFBuilder htmlToPDFBuilder;

    @Test
    public void buildPdf() {
        Map<String, Object> map = new HashMap<>();
        map.put("carVin", "123456789098765432");
        map.put("logoImg", "/Users/goddess/image005.png");
        map.put("carPic", "https://www.russia-online.cn/Upload/20151211/20151211100135_8627.jpg");
        map.put("carName", "carName");
        map.put("carDate", "carDate");
        map.put("mileage", "mileage");
        map.put("accidentImg", "accidentImg");
        map.put("accident", "accident");
        map.put("reportDetails", Arrays.asList());

//        JSONObject jsonObject = JSON.parseObject("{\"id\":5,\"creator\":\"用户\",\"creatorId\":\"c340b6cdf5c44a33ab29a1a80c0963c4\",\"groupId\":\"5970bca3130000225ce66442\",\"groupName\":\"测试林芝\",\"createTime\":\"2019-05-16T19:47:05.000Z\",\"updater\":\"董长磊\",\"updateTime\":\"2019-05-20T09:40:59.000Z\",\"businessLineKey\":\"receipt_line\",\"businessLineValue\":\"收单\",\"businessSourceKey\":\"financial_source\",\"businessSourceValue\":\"金融\",\"objectNum\":\"1\",\"vin\":\"DALDFD1234567DCLS\",\"status\":3,\"statusDescription\":\"更新中\",\"deleted\":0,\"carName\":\"qqq\",\"evaluationPrice\":\"34700\",\"guidePrice\":\"qqq\",\"brand\":\"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq\",\"engine\":\"f\",\"fuel\":\"乙醇\",\"vehicleType\":\"订单\",\"transmission\":\"啊啊\",\"ratedPassenger\":\"5\",\"natureUse\":\"自用\",\"carColor\":\"幻彩\",\"tranferCount\":\"0\",\"appraiser\":\"哈哈哈哈哈哈\",\"numberPlate\":\"888888\",\"registration\":\"bj\",\"displacement\":\"4\",\"mileage\":\"18w\",\"dateManufacture\":\"2019年05月01日\",\"registrationDate\":\"2019年05月02日\",\"cycleTransferCount\":\"0\",\"mortgage\":\"否\",\"deadLine\":\"2019年06月19日\",\"reviewAppraiser\":\"嘿嘿嘿哈哈哈\",\"exterior\":\"啊\",\"interior\":\"的\",\"fireInspection\":\"火烧检查\",\"frontWindshield\":\"火烧检查\",\"dashBoard\":\"仪表盘\",\"otherInstructions\":\"仪表盘\",\"paintSurface\":\"车架\",\"frame\":\"车架\",\"soakingWater\":\"泡水检查\",\"rearWindshield\":\"后风挡玻璃\",\"dateInspection\":\"2019年05月20日\",\"frNum\":\"1\",\"ycCxId\":\"1\",\"jzgCxId\":\"1\",\"pdfUrl\":\"未生成\"}");

        Either<ApiException, String> either = htmlToPDFBuilder.builder("PDFTestStamper125", "test", map);
        if (either.isRight()) {
            System.out.println(either.getRight());
        } else {
            System.out.println(either.getLeft());
        }
    }
}
