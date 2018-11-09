app.controller('wxCalculatorController',['$scope', '$state', '$http','toaster','$rootScope', function($scope,$state,$http,toaster,$rootScope) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    /*
     * 设计思路：
     * 五个对象：
     * 	车类别：carObj（新车、二手车等）-数组
     * 	车辆类型：carType（乘用车、轻客等）-数组
     * 	融资产品：finaProduct（明白融、包牌融等）-数组
     *	融资产品子类：finaSubclass（明白融常规产品等）-数组
     *	融资对象：finaObj（融资期限、手续费分期等）-数组
     */

    $scope.factorageList = [
        {
            id :"1",
            name : "是"
        },
        {
            id :"0",
            name : "否"
        }
    ];
    $scope.factorage = $scope.factorageList[0];

    //数据重新包装后的类：
    var obj = {};
    obj.carObj = [];//车类别：carObj（新车、二手车等）-数组
    obj.carType = [];//车辆类型：carType（乘用车、轻客等）-数组
    obj.finaProduct = [];//融资产品：finaProduct（明白融、包牌融）-数组
    obj.finaSubclass = [];//融资产品子类：finaSubclass（明白融常规产品等）-数组
    obj.finaObj = [];//融资对象：finaObj（融资期限、手续费分期等）-数组

    $scope.sign = {
        carObj : true,// 车辆类别
        carType : true,// 车辆类型
        carPrice : true,// 车辆售价
        gpsPrice : true,// GPS费用
        otherPrice : true,// 保险或其他费用
        product : true,// 融资产品
        childProduct : true,// 子类
        deadline : true,// 融资期限
        firstPay : true,// 首付比例
        deposit : true,// 保证金比例
        firstPayMoney : true,// 首付款金额
        factorage : true// 手续费是否分期
    };

    $scope.carObjList = [];//车辆类别
    $scope.carTypeList = [];//车辆类型
    $scope.productList = [];//融资产品
    $scope.childProductList = [];//子类
    $scope.deadlineList = [];//融资期限

    //页面初始化加载数据
    $scope.getData = function(){
        $http.get('http://wx.xftm.com/financeProducts').success(function(data){
            if("SUCCESS" == data.status){
                    initData(data.data);
                    initHTML();
                }else{
                    $scope.pop('error', '', data.statusText);
                }
        }).error(function (data) {
            $scope.pop('error', '', '页面加载失败，请联系网站管理人员！');
        });
    }
    $scope.getData();

    function initData(data){
        for (var i = 0; i < data.length; i++) {
            //车类别：carObj（新车、二手车等）-数组
            var carObj = {};
            if("0" == data[i].used){
                carObj.id = data[i].used;
                carObj.name = "新车";
            }else if("1" == data[i].used){
                carObj.id = data[i].used;
                carObj.name = "二手车";
            }else{
                carObj.id = data[i].used;
                carObj.name = "其他";
            }
            if(hasCarObj(carObj)){
                obj.carObj.push(carObj);
            }

            //车辆类型：carType（乘用车、轻客等）-数组
            var carType = {};
            carType.carObjId = data[i].used;
            carType.id = data[i].id;
            carType.name = data[i].name;
            if(hasCarType(carType)){
                obj.carType.push(carType);
            }

            //融资产品：finaProduct（明白融、包牌融）-数组----obj.finaProduct = [];
            //融资产品子类：finaSubclass（明白融常规产品等）-数组----finaSubclass = [];
            //融资对象：finaObj（融资期限、手续费分期等）-数组----obj.finaObj = [];
            for ( var j = 0; j < data[i].financeProducts.length; j++){
                if("-1" != data[i].financeProducts[j].name.indexOf("明白融")){
                    //融资产品：finaProduct（明白融、包牌融）-数组----obj.finaProduct = [];
                    var finaProduct = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        child : "1",//0代表没有子节点 1代表有子节点
                        id : "明白融",
                        name : "明白融",//融资产品名称
                        finaSubclassId : data[i].financeProducts[j].id,//融资产品id
                    };
                    if(hasFinaProduct(finaProduct)){
                        obj.finaProduct.push(finaProduct);
                    }

                    //融资产品子类：finaSubclass（明白融常规产品等）-数组----finaSubclass = [];
                    var finaSubclass = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        finaProductId : "明白融",
                        id : data[i].financeProducts[j].id,//融资产品id
                        name : data[i].financeProducts[j].name,//融资产品名称
                    }
                    if(hasFinaSubclass(finaSubclass)){
                        obj.finaSubclass.push(finaSubclass);
                    }

                    //融资对象：finaObj（融资期限、手续费分期等）-数组----obj.finaObj = [];
                    var finaObj = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        id : data[i].financeProducts[j].id,//融资产品id

                        downPay : data[i].financeProducts[j].downPay,//最低首付
                        minPeriod : data[i].financeProducts[j].minPeriod,//最小融资周期(月)
                        maxPeriod : data[i].financeProducts[j].maxPeriod,//最大融资周期(月)
                        depositRate : data[i].financeProducts[j].depositRate,//明白融特有的保证金比例
                        rate : data[i].financeProducts[j].rate,
                        maxFinance : data[i].financeProducts[j].maxFinance,
                        carType : data[i].financeProducts[j].carType,
                    }
                    if(hasFinaObj(finaObj)){
                        obj.finaObj.push(finaObj);
                    }
                }else if("-1" != data[i].financeProducts[j].name.indexOf("包牌融")) {
                    //融资产品：finaProduct（明白融、包牌融）-数组----obj.finaProduct = [];
                    var finaProduct = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        child : "1",//0代表没有子节点 1代表有子节点
                        id : "包牌融",//融资产品id
                        name : "包牌融",//融资产品名称
                        finaSubclassId : data[i].financeProducts[j].id,//融资产品id
                    };
                    if(hasFinaProduct(finaProduct)){
                        obj.finaProduct.push(finaProduct);
                    }

                    //融资产品子类：finaSubclass（明白融常规产品等）-数组----finaSubclass = [];
                    var finaSubclass = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        finaProductId : "包牌融",
                        id : data[i].financeProducts[j].id,//融资产品id
                        name : data[i].financeProducts[j].name,//融资产品名称
                    }
                    if(hasFinaSubclass(finaSubclass)){
                        obj.finaSubclass.push(finaSubclass);
                    }

                    //融资对象：finaObj（融资期限、手续费分期等）-数组----obj.finaObj = [];
                    var finaObj = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        id : data[i].financeProducts[j].id,//融资产品id

                        downPay : data[i].financeProducts[j].downPay,//最低首付
                        minPeriod : data[i].financeProducts[j].minPeriod,//最小融资周期(月)
                        maxPeriod : data[i].financeProducts[j].maxPeriod,//最大融资周期(月)
                        depositRate : data[i].financeProducts[j].depositRate,//明白融特有的保证金比例
                        rate : data[i].financeProducts[j].rate,
                        maxFinance : data[i].financeProducts[j].maxFinance,
                        carType : data[i].financeProducts[j].carType,
                    }
                    if(hasFinaObj(finaObj)){
                        obj.finaObj.push(finaObj);
                    }
                }else{
                    //融资产品：finaProduct（明白融、包牌融）-数组----obj.finaProduct = [];
                    var finaProduct = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        child : "0",//0代表没有子节点 1代表有子节点
                        id : data[i].financeProducts[j].id,//融资产品id
                        name : data[i].financeProducts[j].name,//融资产品名称
                        finaSubclassId : data[i].financeProducts[j].id,//融资产品id---简化代码使用
                    };
                    if(hasFinaProduct(finaProduct)){
                        obj.finaProduct.push(finaProduct);
                    }

                    //融资对象：finaObj（融资期限、手续费分期等）-数组----obj.finaObj = [];
                    var finaObj = {
                        carObjId : data[i].used, //车类别
                        carTypeId : data[i].id, //车辆类型
                        id : data[i].financeProducts[j].id,//融资产品id

                        downPay : data[i].financeProducts[j].downPay,//最低首付
                        minPeriod : data[i].financeProducts[j].minPeriod,//最小融资周期(月)
                        maxPeriod : data[i].financeProducts[j].maxPeriod,//最大融资周期(月)
                        depositRate : data[i].financeProducts[j].depositRate,//明白融特有的保证金比例
                        rate : data[i].financeProducts[j].rate,
                        maxFinance : data[i].financeProducts[j].maxFinance,
                        carType : data[i].financeProducts[j].carType,
                    }
                    if(hasFinaObj(finaObj)){
                        obj.finaObj.push(finaObj);
                    }
                }
            }
        }
    }

    function initHTML(){
        initSetCarObj();//初始化：车类别
        initSetCarType();//初始化：车辆类型
        initSetProduct();//初始化：融资产品
        initSetFinaSubclass();//初始化：融资产品子类
        initSetFinaObj();//初始化：融资对象
    }

    //初始化：车类别
    function initSetCarObj(){
        $scope.carObjList =  obj.carObj;
        $scope.carObj = $scope.carObjList[0];
    }

    //初始化：车辆类型
    function initSetCarType(){
        $scope.carTypeList = [];
        for (var i = 0;i < obj.carType.length; i++ ) {
            if($scope.carObj.id == obj.carType[i].carObjId){
                $scope.carTypeList.push(obj.carType[i]);
            }
        }
        $scope.carType = $scope.carTypeList[0];
    }

    //初始化：融资产品
    function initSetProduct(){
        $scope.productList = [];
        for (var i = 0;i < obj.finaProduct.length; i++ ) {
            if($scope.carObj.id == obj.finaProduct[i].carObjId && $scope.carType.id == obj.finaProduct[i].carTypeId) {
                $scope.productList.push(obj.finaProduct[i]);
            }
        }
        $scope.product = $scope.productList[0];
    }

    //初始化：融资产品子类
    function initSetFinaSubclass(){
        $scope.childProductList = [];
        if("包牌融" == $scope.product.name){
            for (var i = 0;i < obj.finaSubclass.length; i++ ) {
                if($scope.carObj.id == obj.finaSubclass[i].carObjId && $scope.carType.id == obj.finaSubclass[i].carTypeId && $scope.product.id == obj.finaSubclass[i].finaProductId){
                    $scope.childProductList.push(obj.finaSubclass[i]);
                }
            }
            $scope.childProduct = $scope.childProductList[0];
            BPRShow();//包牌融显示
        }else if ("明白融" == $scope.product.name) {
            for (var i = 0;i < obj.finaSubclass.length; i++ ) {
                if($scope.carObj.id == obj.finaSubclass[i].carObjId && $scope.carType.id == obj.finaSubclass[i].carTypeId && $scope.product.id == obj.finaSubclass[i].finaProductId){
                    $scope.childProductList.push(obj.finaSubclass[i]);
                }
            }
            $scope.childProduct = $scope.childProductList[0];
            MBRShow();//明白融显示
        }else {
            YBShow();//除了包牌融和明白融显示
        }
    }

    //初始化：融资对象
    function initSetFinaObj(){
        $scope.deadlineList = [];
        if("包牌融" == $scope.product.name){
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayMoneyPlaceholder = obj.finaObj[i].downPay + ".0 元起";
                }
            }
        }else if ("明白融" == $scope.product.name){
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayPlaceholder = obj.finaObj[i].downPay*100 + "起";
                    $scope.deposit = obj.finaObj[i].depositRate*100;//保证金比例
                }
            }
        }else {
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayPlaceholder = obj.finaObj[i].downPay*100 + "起";
                }
            }
        }
    }

    //Change：车类别
    $scope.carObjChange = function(){
        clearTopInput();
        initSetCarType();//车辆类型
        initSetProduct();//融资产品
        initSetFinaSubclass();//融资产品子类
        initSetFinaObj();//融资对象
    }

    //Change：车辆类型
    $scope.carTypeChange = function(){
        clearTopInput();
        initSetProduct();//融资产品
        initSetFinaSubclass();//融资产品子类
        initSetFinaObj();//融资对象
    }

    //Change：融资产品
    $scope.productChange = function(){
        clearBottomInput();
        productChangeSubclass();//Change：融资产品改变时子类改变
        productChangeFinaObj();//Change：融资产品改变时融资对象改变
    }

    //Change：融资产品改变时子类改变
    function productChangeSubclass(){
        $scope.childProductList = [];
        if("明白融" == $scope.product.name){
            for (var i = 0;i < obj.finaSubclass.length; i++ ) {
                if($scope.carObj.id == obj.finaSubclass[i].carObjId && $scope.carType.id == obj.finaSubclass[i].carTypeId && $scope.product.id == obj.finaSubclass[i].finaProductId){
                    $scope.childProductList.push(obj.finaSubclass[i]);
                }
            }
            $scope.childProduct = $scope.childProductList[0];
            MBRShow();//明白融显示
        }else if("包牌融" == $scope.product.name){
            for (var i = 0;i < obj.finaSubclass.length; i++ ) {
                if($scope.carObj.id == obj.finaSubclass[i].carObjId && $scope.carType.id == obj.finaSubclass[i].carTypeId && $scope.product.id == obj.finaSubclass[i].finaProductId){
                    $scope.childProductList.push(obj.finaSubclass[i]);
                }
            }
            $scope.childProduct = $scope.childProductList[0];
            BPRShow();//包牌融显示
        }else {
            YBShow();//除了包牌融和明白融显示
        }
    }

    //Change：融资产品改变时融资对象改变
    function productChangeFinaObj(){
        $scope.deadlineList = [];
        clearBottomInput();
        if("包牌融" == $scope.product.name){
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.finaSubclassId == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayMoneyPlaceholder = obj.finaObj[i].downPay + ".0 元起";
                }
            }
        }else if ("明白融" == $scope.product.name){
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.finaSubclassId == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayPlaceholder = obj.finaObj[i].downPay*100 + "起";
                    $scope.deposit = obj.finaObj[i].depositRate*100;//保证金比例
                }
            }
        }else {
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.product.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayPlaceholder = obj.finaObj[i].downPay*100 + "起";
                }
            }
        }
    }

    $scope.childProductChange = function(){
        clearBottomInput();
        if("包牌融" == $scope.product.name){
            $scope.deadlineList = [];
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.childProduct.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayMoneyPlaceholder = obj.finaObj[i].downPay + ".0 元起";
                }
            }
        }else if ("明白融" == $scope.product.name){
            $scope.deadlineList = [];
            for (var i = 0;i < obj.finaObj.length; i++ ) {
                if($scope.carObj.id == obj.finaObj[i].carObjId && $scope.carType.id == obj.finaObj[i].carTypeId && $scope.childProduct.id == obj.finaObj[i].id){
                    var period  = getPeriod(obj.finaObj[i].minPeriod, obj.finaObj[i].maxPeriod);//周期数组
                    for(var j = 0; j < period.length; j ++){
                        $scope.deadlineList.push(period[j]);
                    }
                    $scope.deadline =  $scope.deadlineList[0];
                    $scope.firstPayPlaceholder = obj.finaObj[i].downPay*100 + "起";
                    $scope.deposit = obj.finaObj[i].depositRate*100;//保证金比例
                }
            }
        }
    }

    $scope.submit = function(){
        debugger;
        var data = {};
        if("明白融" == $scope.product.name){
            data.carTypeId = $scope.carType.id;//车辆类别
            data.financeProductId = $scope.childProduct.id;//融资产品子类id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            data.payMode = $scope.factorage.id;//支付方式,0为一次性收取手续费,1为分期支付(明白融产品必传参数，其他可不传)
            if ($scope.otherPrice)    data.otherPrice = $scope.otherPrice;//其他费用
            else    data.otherPrice = "0";//其他费用
            if (required()){
                if(""===$scope.firstPay || null===$scope.firstPay || undefined===$scope.firstPay){
                    $scope.pop('error', '', '请输出入"首付比例"!');
                    return false;
                }else
                    data.downPay = $scope.firstPay/100;//首付比例--包牌融首付比例为首付金额
            }else {
                return false;
            }
        }else if("包牌融" == $scope.product.name){
            data.carTypeId = $scope.carType.id;//车型id
            data.financeProductId = $scope.childProduct.id;//融资产品子类id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            if ($scope.otherPrice)    data.otherPrice = $scope.otherPrice;//其他费用
            else    data.otherPrice = "0";//其他费用
            if (required()){
                if(""==$scope.firstPayMoney || null==$scope.firstPayMoney || undefined==$scope.firstPayMoney){
                    $scope.pop('error', '', '请输出入"首付款金额"!');
                    return false;
                }else
                    data.downPay = $scope.firstPayMoney;//首付比例--包牌融首付比例为首付款金额
            }else {
                return false;
            }
        }else{
            data.carTypeId = $scope.carType.id;//车型id
            data.financeProductId = $scope.product.id;//融资产品id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            data.downPay = $scope.firstPay/100;//首付比例--包牌融首付比例为首付金额
            if ($scope.otherPrice)   data.otherPrice = $scope.otherPrice;//其他费用
            else   data.otherPrice = "0";//其他费用
            if (required()){
                if(""==$scope.firstPay || null==$scope.firstPay || undefined==$scope.firstPay){
                    $scope.pop('error', '', '请输出入"首付比例"!');
                    return false;
                }else
                    data.downPay = $scope.firstPay/100;//首付比例--包牌融首付比例为首付金额
            }else {
                return false;
            }
        }
        calculation(data);
    }

    function calculation(data){
        $http({
            url:'http://wx.xftm.com/calculators/web',
            method:'post',
            data:JSON.stringify(data),
            headers:{'Content-Type' : 'application/json;charset=utf-8'}
        }).success(function (data) {
            if("SUCCESS" == data.status){
                $rootScope.data = data.data;
                $rootScope.pageData = setPageData();
                $state.go('wx.calculatorResult');
            }else{
                $scope.pop('error', '', data.error);
            }
        }).error(function (data) {
            $scope.pop('error', '', '计算失败，请联系网站管理人员！');
        });
    }

    function setPageData() {
        var data = {};
        if("明白融" == $scope.product.name){
            data.carObjName = $scope.carObj.name;//车辆类别名称
            data.carTypeName = $scope.carType.name;//车辆类型名称
            data.financeProductId = $scope.childProduct.name;//融资产品子类id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            data.downPay = $scope.firstPay;//首付比例--包牌融首付比例为首付款金额
            data.payMode = $scope.factorage.name;//支付方式,0为一次性收取手续费,1为分期支付(明白融产品必传参数，其他可不传)
            data.depositPlaceholder = $scope.deposit;//保证金比例-明白融特有
            if ($scope.otherPrice)    data.otherPrice = $scope.otherPrice;//其他费用
            else    data.otherPrice = "0";//其他费用
        }else if("包牌融" == $scope.product.name){
            data.carObjName = $scope.carObj.name;//车辆类别名称
            data.carTypeName = $scope.carType.name;//车辆类型名称
            data.financeProductId = $scope.childProduct.name;//融资产品子类id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            data.downPay = $scope.firstPayMoney;//首付比例--包牌融首付比例为首付款金额
            if ($scope.otherPrice)    data.otherPrice = $scope.otherPrice;//其他费用
            else    data.otherPrice = "0";//其他费用
        }else{
            data.carObjName = $scope.carObj.name;//车辆类别名称
            data.carTypeName = $scope.carType.name;//车辆类型名称
            data.financeProductId = $scope.product.name;//融资产品id
            data.sellingPrice = $scope.carPrice;//车价
            data.gpsPrice = $scope.gpsPrice;//GPS费用
            data.financePeriod = $scope.deadline.id;//融资期限
            data.downPay = $scope.firstPay;//首付比例--包牌融首付比例为首付款金额
            if ($scope.otherPrice)   data.otherPrice = $scope.otherPrice;//其他费用
            else   data.otherPrice = "0";//其他费用
        }
        return data;
    }

    function getPeriod(minPeriod,maxPeriod){
        var period = [];
        for (var i = 0; i < maxPeriod/minPeriod; i++) {
            var periodObj = {
                id : minPeriod + 12*i,
                name : minPeriod + 12*i
            }
            period.push(periodObj)
        }
        return period;
    }

    //清除输入:类别（新车、旧车）、车辆类型
    function clearTopInput(){
        $scope.carPrice = "";//车辆售价
        $scope.gpsPrice = "";//GPS费用
        $scope.otherPrice = "";//保险或其他费用
        $scope.firstPay = "";//首付比例
        $scope.deposit = "";//保证金比例
        $scope.firstPayMoney = "";//首付款金额
    }
    //清除输入:融资产品
    function clearBottomInput(){
        $scope.firstPay = "";//首付比例
        $scope.deposit = "";//保证金比例
        $scope.firstPayMoney = "";//首付款金额
    }
    //除了包牌融和明白融显示
    function YBShow(){
        $scope.sign = {
            carObj : true,// 车辆类别
            carType : true,// 车辆类型
            carPrice : true,// 车辆售价
            gpsPrice : true,// GPS费用
            otherPrice : true,// 保险或其他费用
            product : true,// 融资产品
            childProduct : false,// 子类
            deadline : true,// 融资期限
            firstPay : true,// 首付比例
            deposit : false,// 保证金比例
            firstPayMoney : false,// 首付款金额
            factorage : false// 手续费是否分期
        };
    }
    //明白融显示
    function MBRShow(){
        $scope.sign = {
            carObj : true,// 车辆类别
            carType : true,// 车辆类型
            carPrice : true,// 车辆售价
            gpsPrice : true,// GPS费用
            otherPrice : true,// 保险或其他费用
            product : true,// 融资产品
            childProduct : true,// 子类
            deadline : true,// 融资期限
            firstPay : true,// 首付比例
            deposit : true,// 保证金比例
            firstPayMoney : false,// 首付款金额
            factorage : true// 手续费是否分期
        };
    }
    //包牌融显示
    function BPRShow(){
        $scope.sign = {
            carObj : true,// 车辆类别
            carType : true,// 车辆类型
            carPrice : true,// 车辆售价
            gpsPrice : true,// GPS费用
            otherPrice : true,// 保险或其他费用
            product : true,// 融资产品
            childProduct : true,// 子类
            deadline : true,// 融资期限
            firstPay : false,// 首付比例
            deposit : false,// 保证金比例
            firstPayMoney : true,// 首付款金额
            factorage : false// 手续费是否分期
        };
    }
    //车类别：carObj（新车、二手车等）-数组
    function hasCarObj(carObj){
        var status = "0";
        for (var i = 0; i < obj.carObj.length; i++) {
            var object = obj.carObj[i];
            if(carObj.id == object.id)
                status = "1";
        }
        if("0" == status)   return true;
        else    return false;
    }
    //车辆类型：carType（乘用车、轻客等）-数组
    function hasCarType(carType){
        var status = "0";
        for (var i = 0; i < obj.carType.length; i++) {
            var object = obj.carType[i];
            if(carType.carObjId == object.carObjId && carType.id == object.id )
                status = "1";
        }
        if("0" == status)   return true;
        else    return false;
    }
    //融资产品：finaProduct（明白融、包牌融）-数组
    function hasFinaProduct(finaProduct){
        var status = "0";
        for (var i = 0; i < obj.finaProduct.length; i++) {
            var object = obj.finaProduct[i];
            if(finaProduct.carObjId == object.carObjId && finaProduct.carTypeId == object.carTypeId && finaProduct.id == object.id)
                status = "1";
        }
        if("0" == status)   return true;
        else   return false;
    }
    //融资产品子类：finaSubclass（明白融常规产品等）-数组
    function hasFinaSubclass(finaSubclass){
        var status = "0";
        for (var i = 0; i < obj.finaSubclass.length; i++) {
            var object = obj.finaSubclass[i];
            if(finaSubclass.carObjId == object.carObjId && finaSubclass.carTypeId == object.carTypeId && finaSubclass.id == object.id)
                status = "1";
        }
        if("0" == status)   return true;
        else   return false;
    }
    //融资对象：finaObj（融资期限、手续费分期等）-数组
    function hasFinaObj(finaObj){
        var status = "0";
        for (var i = 0; i < obj.finaObj.length; i++) {
            var object = obj.finaObj[i];
            if(finaObj.carObjId == object.carObjId && finaObj.carTypeId == object.carTypeId && finaObj.id == object.id)
                status = "1";
        }
        if("0" == status) return true;
        else return false;
    }

    function required(){
        if(""==$scope.carPrice || null==$scope.carPrice || undefined==$scope.carPrice){
            $scope.pop('error', '', '请输出入"车辆售价"!');
            return false;
        }else if (""==$scope.gpsPrice || null==$scope.gpsPrice || undefined==$scope.gpsPrice){
            $scope.pop('error', '', '请输出入"GPS费用"!');
            return false;
        }else {
            return true;
        }
    }
}]);