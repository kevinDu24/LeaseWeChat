## 接口更新:
### 1. 更新"14. 计算器"接口:修改日供字段为dayPay
### 2. 更新"15. 获取计算历史记录"接口:修改日供字段为dayPay
### 3. 更新"16. 获取app最新版本信息"接口:url

# 一、术语
## 1. 请求方式
- GET
- POST
- PUT
- DELETE

## 2. 入参位置
- Request Body
- Request Parameter
- Path Variable
每一个接口不限于使用一种入参位置，具体参考每个接口的说明。

## 3. domain
- `ip`:222.73.56.22
- `port`:80

## 4. 全局返回值
- `status`: 请求状态（成功为SUCCESS,失败为ERROR）
- `error`: 请求失败时，服务器的错误信息
- `data`: 返回的数据

示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
  }
}
```


# 二、接口

## 1. 登录
- url: `http://domain/user`
- 请求方式： `GET`
- 入参位置： `Request Header`
- 入参参数：
	- `Authorization`: 认证信息
- 入参解释
```
    对“账号:密码”字符串进行base64加密，得到密钥与字符串“Basic ”拼接，以键值对的形式存放到headers中，键值为“Authorization”，app端调用所有授权接口都需要传。
```
- 出参参数:
  - `isHplUser`: 是否是先锋太盟总部人员
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "isHplUser": true
  }
}
```

## 2. 查询合同申请状态列表
- url：`http://domain/contracts/state`
- 请求方式： `GET`
- 入参位置:  `Request Parameter`
- 入参参数：
	- `applyNum`: 申请编号或姓名(按姓名查询时输入)
	- `fpName`: FP名称
	- `startDate`: 起时间(格式:20150601)
	- `endDate`: 止时间(格式:20150601)
	- `state`: 申请状态(审批阶段、放款阶段、待材料归档、取消、拒绝)
	- `page`: 当前页数(起始页:1)
- 入参示例:
	http://domain/contracts/state?applyNum=&fpName=&startDate=&endDate=&state=&page=1
- 出参参数：
	- `page`: 分页信息
		- `BAZYEL`: 总页数
		- `BADQSL`: 每页数量
		- `BAPAGE`: 当前页
		- `BAZTSL`: 总数
	- `contractstatelist`: 合同集合
		- `BASQXM`: 申请姓名
		- `BASQZT`: 申请状态
		- `BAKHJC`: FP简称
		- `BASQBH`: 申请编号
		- `BASQRQ`: 申请日期
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "page": {
      "BAZYEL": "5000",
      "BADQSL": "20",
      "BAPAGE": "1",
      "BAZTSL": "100000"
    },
    "contractstatelist": [
      {
        "BASQXM": "李加华",
        "BASQZT": "创建申请",
        "BAKHJC": "贵州津港",
        "BASQBH": "36155890",
        "BASQRQ": "20160901"
      }
    ]
  }
}
```


## 3. 查询合同申请审批日志
- url: `http://domain/contracts/{condition}/log`
- 请求方式：`GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `condition`: 查询条件(申请编号、姓名、身份证)
- 入参示例：
	http://domain/contracts/124555362/log
- 出参参数：
	- `contractinfo`: 合同信息
		- `BASQXM`: 申请姓名
		- `BASQBH`: 申请编号
		- `contractstatelist`:
			- `XTCZRY`: 审批人
			- `BASQZT`: 申请状态
			- `BATHYY`: 挂起,取消,拒绝备注(申请状态为条件通过时，附加该备注)
			- `BASHRQ`: 状态时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractinfo": {
      "BASQXM": "唐相福",
      "BASQBH": "36157040",
      "contractstatelist": [
        {
          "XTCZRY": "SH100",
          "BASQZT": "GPS保单审批",
          "BATHYY": "",
          "BASHRQ": "20160901113417"
        }
      ]
    }
  }
}
```

## 4. 查询产品详情
- url: `http://domain/contracts/{applyNum}/details`
- 请求方式：`GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `applyNum`: 申请编号
- 入参示例：
	http://domain/contracts/36157040/details
- 出参参数：
	- `contractinfo`:
		- `BABXJE`: 申请姓名
		- `BASXFS`: 申请编号
		- `BASFJE`: 首付金额（如有）
		- `BAXSJG`: 销售价格
		- `BAWFJE`: 尾付金额（如有）
		- `BACLCX`: 车型
		- `BACLPP`: 品牌
		- `BAYZYG`: 月供
		- `BARZQX`: 融资期限
		- `BASQBH`: 申请编号
		- `BABZJE`: 保证金金额（如有）
		- `BACLLX`: 车辆类型
		- `BASXFJ`: 手续费金额（如有）
		- `BATZZE`: 投资总额（含gps）
		- `BAGPSY`: GPS硬件
		- `BAYBJE`: 延保金额（如有）
		- `BARYBJ`: 融延保
		- `BARAXB`: 融安心宝
		- `BARBXJ`: 融保险
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractinfo": {
      "BABXJE": "4677",
      "BASXFS": "",
      "BASFJE": "8888",
      "BAXSJG": "35900",
      "BAWFJE": "0",
      "BACLCX": "2014款1.3L幸福型",
      "BACLPP": "欧诺",
      "BAYZYG": "1745.95",
      "BARZQX": "24",
      "BASQBH": "36157040",
      "BARAXB": "0",
      "BABZJE": "0",
      "BACLLX": "新车",
      "BASXFJ": "0",
      "BATZZE": "44311",
      "BAGPSY": "0",
      "BAYBJE": "0"
    }
  }
}
```

## 5. 查询合同还款计划列表
- url: `http://domain/contracts/repayment`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `applyNum`: 申请编号或姓名(按姓名查询时输入)
	- `fpName`: FP名称
	- `startDate`: 起时间(格式:20150601)
	- `endDate`: 止时间(格式:20150601)
	- `state`: 扣款状态(正常扣款、逾期30天内、逾期超过30天)
	- `page`: 当前页数(起始页:1)
- 入参示例：
	http://domain/contracts/repayment?applyNum=&fpName=&startDate&endDate&repayState=&page=1
- 出参参数：
	- `page`: 分页信息
		- `BAZYEL`: 总页数
		- `BADQSL`: 每页数量
		- `BAPAGE`: 当前页
		- `BAZTSL`: 总数
	- `contractrepayplanlist`:
		- `BASQXM`: 申请姓名
		- `BASQZT`: 申请状态
		- `BAKHJC`: FP简称
		- `BASQBH`: 申请编号
		- `BASQRQ`: 申请日期
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "page": {
      "BAZYEL": "3687",
      "BADQSL": "20",
      "BAPAGE": "1",
      "BAZTSL": "73734"
    },
    "contractrepayplanlist": [
      {
        "BAHKZT": "正常",
        "BASQXM": "叶伟英",
        "BAKHJC": "深圳乾丰联合",
        "BASQBH": "3689771",
        "BASQRQ": "20160129"
      }
    ]
  }
}
```


## 6. 查询合同还款计划明细表
- url: `http://domain/contracts/{condition}/repayDetail`
- 请求方式： `GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `condition`: 查询条件(申请编号、姓名、身份证)
- 入参示例：
	http://domain/contract/1234426/repayDetail
- 出参参数：
	- `contractrepayplaninfo`:
		- `BAYQTS`: 逾期天数
		- `BAYHQS`: 已还期数
		- `BASQXM`: 申请姓名
		- `BAYQLX`: 逾期利息
		- `BASQZQ`: 租期
		- `BAYQQS`: 逾期期数
		- `BASQBH`: 申请编号
		- `repayplan`:
			- `BAHKRQ`: 还款日期
			- `BAKKZT`: 待扣款
			- `BAYQLX`: 预期利息
			- `BAHKYG`: 月供
			- `BASQQS`: 期数

- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractrepayplaninfo": {
      "BAYQTS": "",
      "BAYHQS": "0",
      "BASQXM": "唐相福",
      "BAYQLX": "",
      "BASQZQ": "24",
      "BAYQQS": "0",
      "BASQBH": "36157040",
      "repayplan": [
        {
          "BAHKRQ": "20171001",
          "BAKKZT": "待扣款",
          "BAYQLX": "0",
          "BAHKYG": "1745.95",
          "BASQQS": ""
        }
      ]
    }
  }
}
```

## 7. 查询销售统计量
- url: `http://domain/TM/sales/queryStatisticsByDate`
- 请求方式: `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `startDate`: 开始日期(格式：20160708)
	- `endDate`: 结束日期(格式：20170909)
- 出参参数：
	- `tableGrid`:列表数据，比率为不带百分号的数字
		- `id`:编号
		- `levelId`:层级代码
		- `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串
		- `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
		- `area`:地区
		- `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
		- `largArea`:大区
		- `refuseCount`:拒绝数量
		- `passCount`:通过数量
		- `cancelCount`:取消数量
		- `tbGrowthRate`:同比增长率
		- `hbGrowthRate`:环比增长率
- 入参示例：
	http://domain/sales/queryStatisticsByDate?type=1&userLevel=81&beginTime=20160801&endTime=20160831
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "tableGrid": [
      {
        "realCount": "26",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "",
        "hbGrowthRate": "104",
        "arrivalRate": "433",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "HPL",
        "refuseCount": "",
        "tbGrowthRate": "130"
      }
    ]
  }
}
```


## 8. 查询当前用户层级下的所有权限地区
- url: `http://domain/sales/queryAreaLevel`
- 请求方式: `GET`
- 出参参数：
	- `level`:层级
	- `levelId`:层级ID
	- `parentId`:上层级的ID
	- `areaCode`:地区编码
	- `areaName`:地区名称
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "parentId": "0",
      "level": "ZGS",
      "areaName": "HPL",
      "areaCode": "",
      "levelId": "81"
    }
  ]
}
```

## 9. 查询用户信息
- url: `http://domain/sysUsers/userInfo`
- 请求方式: `GET`
- 出参参数:
	- `name`:姓名
	- `companyName`:公司名称
	- `role`: 职位
	- `username`:账号
	- `phoneNum`:手机
	- `email`:邮箱
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "name": "超级管理员",
    "companyName": "先锋太盟融资租赁有限公司",
    "role": "超级管理员",
    "username": "admin",
    "phoneNum": "111",
    "email": "11"
  }
}
```

## 10. 按年查询销售统计-人员统计报表
- url: `http://domain/sales/queryStatisticsByYear`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `year`: 年份(格式：2016)
- 出参参数：
  - `tableGrid`:列表数据
    - `id`:编号
    - `levelId`:层级代码
    - `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `area`:地区
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
    - `largArea`:大区
    - `refuseCount`:拒绝数量
    - `passCount`:通过数量（不使用）
    - `cancelCount`:取消数量
    - `tbGrowthRate`:同比增长率
    - `hbGrowthRate`:环比增长率
  - `lineGrid`:按年统计数据
    - `id`:编号
    - `month`:月份
    - `realCount`:实际量（type = 1，展业账号；type = 2，空串；type = 3，空串；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `tbGrowthRate`:同比增长率
    - `hbGrowthRate`:环比增长率
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，空串；type = 5，空串）
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "lineGrid": [
      {
        "id": "6",
        "realCount": "178",
        "month": "201601",
        "hbGrowthRate": "0",
        "arrivalRate": "17800",
        "planCount": "1",
        "tbGrowthRate": "0"
      },
      ...
    ],
    "tableGrid": [
      {
        "realCount": "31",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "0",
        "hbGrowthRate": "",
        "arrivalRate": "517",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "0",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "先锋太盟融资租赁有限公司",
        "refuseCount": "0",
        "tbGrowthRate": ""
      },
      ...
    ]
  }
}
```

## 11. 按月查询销售统计-人员统计报表
- url: `http://domain/sales/queryStatisticsByMonth`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `year`: 年份(格式：2016)
  - `month`:月份（格式：1）
- 出参参数
- 出参参数：
  - `tableGrid`:列表数据
    - `id`:编号
    - `levelId`:层级代码
    - `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `area`:地区
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
    - `largArea`:大区
    - `refuseCount`:拒绝数量
    - `passCount`:通过数量（不使用）
    - `cancelCount`:取消数量
    - `tbGrowthRate`:同比增长率
    - `hbGrowthRate`:环比增长率
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "lineGrid": [
      {
        "id": "6",
        "realCount": "178",
        "month": "201601",
        "hbGrowthRate": "0",
        "arrivalRate": "17800",
        "planCount": "1",
        "tbGrowthRate": "0"
      },
      ...
    ],
    "tableGrid": [
      {
        "realCount": "31",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "0",
        "hbGrowthRate": "",
        "arrivalRate": "517",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "0",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "先锋太盟融资租赁有限公司",
        "refuseCount": "0",
        "tbGrowthRate": ""
      },
      ...
    ]
  }
}
```

## 12. 销售计划查询
- url: `http://domain/sales/plan`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `parentId`: 要查询的父级levelId
	- `startDate`: 开始日期，格式20160909
	- `endDate`: 结束日期，格式20160909
	- `planType`: 类型，销售计划（合同量）为7，销售计划（申请量）为6
- 入参示例：
	http://domain/sales/plan?parentId=81&startDate=20160208&endDate=20160908&planType=6
- 出参参数：
	- `levelId`: 层级id
	- `area`: 地区名称
	- `planCount`: 计划量
	- `realCount`: 实际量
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "levelId": 2374,
      "area": "华南区",
      "planCount": 8529,
      "realCount": 2147
    },
    ...
	]
}
```

## 13. 获取融资方案(车型,融资产品)
- url: `http://domain/financeProducts`
- 请求方式： `GET`
- 出参参数：
	- `id`: 车型id
	- `name`: 车型名称
	- `used`: 类别（0为新车，1为二手车）
	- `financeProducts`: 适用产品
		- `id`: 融资产品id
		- `name`: 产品名称
		- `downPay`: 最低首付
		- `minPeriod`: 最小融资周期(月)
		- `maxPeriod`: 最大融资周期(月)
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "id": 1,
      "name": "乘用车",
      "used": 0,
      "financeProducts": [
        {
          "id": 101,
          "name": "轻松融",
          "downPay": 0.2,
          "minPeriod": 12,
          "maxPeriod": 60,
        }
        ...
      ]
    },
    {
      "id": 2,
      "name": "轻客",
      "used": 0,
      "financeProducts": [
        {
          "id": 107,
          "name": "轻客轻松融",
          "downPay": 0.2
          "minPeriod": 12,
          "maxPeriod": 36
        }
        ...
      ]
    },
    {
      "id": 11,
      "name": "皮卡",
      "used": 1,
      "financeProducts": [
        {
          "id": 149,
          "name": "微面倾心融",
          "downPay": 0.2
          "minPeriod": 12,
          "maxPeriod": 36
        }
        ...
      ]
    }
    ...
  ]
}
```

## 14. 计算器
- url: `http://domain/calculators`
- 请求方式： `POST`
- 入参位置：Request Body
- 入参参数：
	- `carTypeId`: 车型id
	- `financeProductId`: 融资产品id
	- `sellingPrice`: 车价
	- `gpsPrice`: gps费用
	- `otherPrice`: 其他费用(保险/人身意外保障/其他等)
	- `financePeriod`: 融资期限(自贸融明白、明白融融资期限必须为12的整数倍)
	- `downPay`: 首付比例
	- `payMode`: 支付方式,0为一次性收取手续费,1为分期支付(明白融产品必传参数，其他可不传)
- 入参示例：
```javascript
{
    "carTypeId":1,
    "financeProductId":169,
    "sellingPrice":10000,
    "gpsPrice": 1000,
    "otherPrice":1000,
    "financePeriod":12,
    "downPay":0.9,
    "payMode":0
}
```
- 出参参数：
	- `financeAmount`: 融资金额
	- `monthPay`: 月供
	- `dayPay`: 日供
	- `downPay`: 首付
	- `deposit`: 保证金（明白融有保证金这一项，其他没有）
	- `totalInterest`: 利息总额
	- `yearInterest`: 年均利息
	- `yearEarnings`: 年投资收益（按8%计算）
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "financeAmount": 2000,
    "monthPay": 166.66666666666666,
    "dayPay": 5.555555555555555,
    "downPay": 9000,
    "deposit": 400,
    "totalInterest": 0,
    "yearInterest": 0,
    "yearEarnings": 160
  }
}
```

## 15. 获取计算历史记录
- url: `http://domain/calculators/records`
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `startDate`: 起始日期（可选，格式：20160909）
	- `endDate`: 结束日期（可选，格式：20160909）
	- `page`: 当前页（从1开始）
	- `size`: 每页条数
- 入参示例：
	http://domain/calculators/records?startDate=20160701&endDate=20160916&page=1&size=10
- 出参参数：
	- `number`: 当前页（从0开始）
	- `size`: 每页条数
	- `numberOfElements`: 每页条数
	- `hasNext`: 是否有下一页
	- `hasPrevious`: 是否有上一页
	- `totalPages`: 总页数
	- `totalElements`: 总记录数
	- `last`: 是否是最后一页
	- `first`: 是否是第一页
	- `content`:
		- `id`: 历史记录id
		- `condition`: 用户输入的参数
			- `carTypeId`: 车型id
			- `financeProductId`: 融资产品id
			- `sellingPrice`: 车价
			- `gpsPrice`: gps费用
			- `otherPrice`: 其他费用
			- `financePeriod`: 融资期限
			- `downPay`: 首付比例
			- `carTypeName`: 车型名称
			- `financeProductName`: 融资产品名称
		- `record`: 
			- `financeAmount`: 融资金额
			- `monthPay`: 月供
			- `dayPay`: 日供
			- `downPay`: 首付
			- `deposit`: 保证金（明白融有保证金这一项，其他没有）
			- `totalInterest`: 利息总额
			- `yearInterest`: 年均利息
			- `yearEarnings`: 年投资收益（按8%计算）
		- `time`: 计算时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "number": 0,
    "size": 5,
    "numberOfElements": 5,
    "content": [
      {
        "id": 20212,
        "condition": {
          "carTypeId": 1,
          "financeProductId": 169,
          "sellingPrice": 10000,
          "gpsPrice": 1000,
          "otherPrice": 1000,
          "financePeriod": 12,
          "downPay": 0.9,
          "carTypeName": "乘用车",
          "financeProductName": "明白融2证1卡产品"
        },
        "record": {
          "financeAmount": 2000,
          "monthPay": 166.66666666666666,
          "dayPay": 5.555555555555555,
          "downPay": 9000,
          "deposit": 400,
          "totalInterest": 0,
          "yearInterest": 0,
          "yearEarnings": 160
        },
        "time": 1473817928251
      },
      ...
    ],
    "hasNext": true,
    "hasPrevious": false,
    "totalPages": 4,
    "totalElements": 20,
    "last": false,
    "first": true
  }
}
```

## 16. 获取app最新版本信息
- url: `http://222.73.56.22:8089/appVersions/latest`
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `type`: app类型，安卓为0，ios为1
- 入参示例：
	http://222.73.56.22:8089/appVersions/latest?type=0
- 出参参数：
	- `version`: 版本号
	- `downloadUrl`: 下载地址(文件名+后缀名,前缀为固定的:http://222.73.56.22:89/android/)
	- `description`: 更新描述
	- `updateTime`: 更新时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "version": "v1.0.1",
    "downloadUrl": "log",
    "description": "222",
    "updateTime": 1474354031000
  }
}
```