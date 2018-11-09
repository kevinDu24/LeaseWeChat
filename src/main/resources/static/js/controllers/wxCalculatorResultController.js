app.controller('wxCalculatorResultController',['$scope', '$rootScope', function($scope, $rootScope) {
    $scope.data = $rootScope.data;
    $scope.pageData = $rootScope.pageData;

    $scope.financeAmount = $scope.data.financeAmount;//融资金额
    $scope.monthPay = $scope.data.monthPay;//月供金额
    $scope.dayPay = $scope.data.dayPay;//日供金额
    $scope.deposit = $scope.data.deposit;//保证金-明白融特有
    $scope.downPayesult = $scope.data.downPay//首付款金额
    $scope.totalInterest = $scope.data.totalInterest;//利息总额
    $scope.yearInterest = $scope.data.yearInterest;//年均利息总额
    $scope.yearEarnings = $scope.data.yearEarnings;//年投资收益
    //----------------------------------------------------------------
    $scope.carObjName = $scope.pageData.carObjName;//车辆类别名称
    $scope.carTypeName = $scope.pageData.carTypeName;//车辆类型名称
    $scope.sellingPrice = $scope.pageData.sellingPrice;//车辆售价
    $scope.gpsPrice = $scope.pageData.gpsPrice;//GPS费用
    $scope.otherPrice = $scope.pageData.otherPrice;//保险或其他费用
    $scope.financeProductId = $scope.pageData.financeProductId;//融资产品
    $scope.financePeriod = $scope.pageData.financePeriod;//融资期限
    $scope.firstPayMoney = $scope.pageData.downPay;//首付金额
    $scope.firstPayPlaceholder = $scope.pageData.downPay;//首付比例
    $scope.payMode = $scope.pageData.payMode;//是否分期-明白融特有
    $scope.depositPlaceholder = $scope.pageData.depositPlaceholder;//保证金比例-明白融特有

    if("-1" != $scope.pageData.financeProductId.indexOf("明白融")){
        $scope.firstPayPlaceholderSign = true;//首付比例--普通融资产品
        $scope.firstPayMoneySign = false;//首付款金额--包牌融特有
        $scope.payModeSign = true;//是否分期-明白融特有
        $scope.depositPlaceholderSign = true;//保证金比例-明白融特有
    }
    else if ("-1" != $scope.pageData.financeProductId.indexOf("包牌融")){
        $scope.firstPayPlaceholderSign = false;//首付比例--普通融资产品
        $scope.firstPayMoneySign = true;//首付款金额---包牌融特有
        $scope.payModeSign = false;//是否分期-明白融特有
        $scope.depositPlaceholderSign = false;//保证金比例-明白融特有
    }else{
        $scope.firstPayPlaceholderSign = true;//首付比例--普通融资产品
        $scope.firstPayMoneySign = false;//首付款金额---包牌融特有
        $scope.payModeSign = false;//是否分期-明白融特有
        $scope.depositPlaceholderSign = false;//保证金比例-明白融特有
    }
}])
;
