<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>测试PDF</title>
    <style type="text/css">
        @page {
            size: A4;
            margin: 0;
        }

        * {
            font-family: "SourceHanSansCN";
            font-weight: 450;
        }
        html {
            height: 100%;
        }

        body, div, ul, ol, li, h1, h2, h3, h4, h5, h6, p {
            margin: 0;
            padding: 0;
            color: #333333;
            font-size: 14px;
            line-height: 1.5;
        }
        .page {
            width: 19cm;
            min-height: 27.699cm;
            margin: 0 auto;
            background: #FAF8F3 no-repeat center center;
            background-size: 100% 100%;
            padding: 1cm;
        }

        h1 {
            text-align: center;
            font-size: 36px;
            color: #9A6B15;
        }

        h2 {
            padding-top: 60px;
            font-size: 22px;
            color: #444444;
            font-weight: 500;
            padding-bottom: 10px;
        }
        h3 {
            padding-top: 40px;
            font-size: 20px;
            color: #444444;
            font-weight: 500;
        }
        h4 {
            text-align: center;
            font-size: 18px;
            color: #9A6B15;
        }

        .color_f00 {
            color: #FF0000;
        }

        .f1 {
            font-size: 20px;
        }

        p {
            line-height: 2
        }
        div > p {
            line-height: 3.2;
            border-bottom: 1px #E8E2D7 solid;
        }
        div > p > span {
            width: 33%;
            display: inline-block;
        }
        .center {
            text-align: center;
        }

        div.footer > p {
            border-bottom: none;
            width: 68%;
            margin: 0 auto;
        }
        .footerWarp {
            padding-top: 50px;
        }

        div.footer > p > span:first-child {
            margin-right: -30px;
        }
        div p.border_none {
            border: none;
        }

    </style>
</head>
<body>
<div class="page">
    <header>
        <h1>测试PDF</h1>
        <h4>NO.1234567890</h4>
    </header>
    <section>
        <h2>日产 轩逸 2009款 1.6L 自动 XL豪华天窗版</h2>
        <p>VIN：${carVin}</p>
        <p>评估价格 <span class="color_f00 f1">20.01</span>万 （厂商指导价<span>19.37</span>万）</p>
    </section>
    <div>
        <h3>鉴定结果</h3>
        <p>
            <span>外观：少量明显损伤</span>
            <span>漆面：少量喷漆修复痕迹</span>
            <span>内饰：少量脏污</span>
        </p>
        <p>
            <span>车架：无拆卸</span>
            <span>火烧检查：未见火烧迹象</span>
            <span>排水检查：未见排水迹象</span>
        </p>
        <p>
            <span>前风挡玻璃：无更换</span>
            <span>后风挡玻璃：无更换</span>
            <span>仪表盘：有故障提示</span>
        </p>
        <p>
            <span style="width: 100%">其他说明：市场保有量较高，市场认可度较高，市场保值率较高，此车登记证车身颜色为黄色。</span>
        </p>
    </div>
    <div>
        <h3>手续信息</h3>
        <p>
            <span>品牌型号：轩逸牌DFL7162ACC</span>
        </p>
        <p>
            <span>发动机号：616603W</span>
            <span>车牌号：浙AB777</span>
            <span>燃料：汽油</span>
        </p>
        <p>
            <span>注册地：浙江杭州</span>
            <span>车辆类型：两厢车</span>
            <span>厂商指导价：19.37万元</span>
        </p>
        <p>
            <span>变速器：自动</span>
            <span>排量：1.6L</span>
            <span>额定载客：5人</span>
        </p>
        <p>
            <span>表显里程：9.23万公里</span>
            <span>使用性质：非营运</span>
            <span>出厂日期：2011年11月10日</span>
        </p>
        <p>
            <span>车身颜色：黑色</span>
            <span>登记日期：2011年11月10日</span>
            <span>过户次数：1</span>
        </p>
        <p>
            <span>循环过户时间间隔：无</span>
            <span>是否抵押：无</span>
            <span></span>
        </p>
    </div>
    <div class="footerWarp">
        <p class="color_f00 center border_none">报告说明：本报告有效期30天，自报告完成日起至2019年6月4日止。</p>
        <div class="footer">
            <p>
                <span>评估师：哈哈哈</span>
                <span>复审评估师：哈哈哈</span>
                <span>检测日期：2019年5月5日</span>
            </p>
        </div>
    </div>
</div>
</body>
</html>
