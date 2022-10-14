## 注意
> **用管理员运行程序**

> 修改src/main/resources/locationTemplate.properties为src/main/resources/location.properties
> 并修改其中内容

> 使用前，系统时间需设为星期日，程序会对明天开始一周的表进行登记

> 浏览器chrome；默认半屏

## 文本
covid-img --> 健康码
routine-img --> 行程码
date=1,3,5,9-25
test=true --> 不关闭提交表格、不关闭浏览器

## 坐标
#### 未page down
    1：因工作关系是否必须进入上飞院（是
    2：来访人员是否有48小时（是
    3：访客人员来访当日抗原检测阴性记录是否上传疫测达（是
    4：14天内是否去过或途经中高风险地区（否
#### page down 之后
    5：随行人信息（删除按钮
    6：我承诺所填信息的真实性
    7：来访开始时间（位置
    8：来访开始时间（确认按钮
    9：《立即提交》
    10：《浏览器关闭按钮》
