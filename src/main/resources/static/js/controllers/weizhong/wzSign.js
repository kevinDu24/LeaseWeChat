app.controller('wzSignController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location', '$cookies',  function(toaster,$scope,$state,$http,$modal,$window,$location, $cookies) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    $scope.isOpen = false;
    $scope.totalInvestment = false;
    $scope.getTotalInvestment = function(){
        if($scope.totalInvestment){
            $scope.totalInvestment = false;
        }else{
            $scope.totalInvestment = true;
        }
    }
    $scope.creditFinanceAmount = false;
    $scope.getCreditFinanceAmount = function(){
        if($scope.creditFinanceAmount){
            $scope.creditFinanceAmount = false;
        }else{
             $scope.creditFinanceAmount = true;
        }
    }

    $scope.applyNum = $location.search().applyNum;
    var openId  = $cookies.get("wzAuthorization");
    if(openId == null){
         $scope.code = $location.search().code;
            if($scope.code == null){
                $scope.code1 = ($location.$$absUrl.split('?code=')[1]);
                if($scope.code1 == null){
                    var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx743de4a4fa762293&redirect_uri=http%3A%2F%2Ffwh.xftm.com%2F%23%2Fwx%2FwzSign%3FapplyNum=' + $scope.applyNum + '&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect';
                    $window.location.href = url;
                    return;
                }
                 $scope.code = ($location.$$absUrl.split('?code=')[1]).split('&state')[0];
                 getOpenId();
            }
    }else {
            try {
                 signSearch();
            } catch(err) {
                alert("查询签约信息异常" + err);
                return;
            }
    }


    function is_weixn(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }

    $scope.showMsg = '获取验证码';
    $scope.user = {};
    $scope.applyInfo = {};
    var timestamp = '';
    $scope.device = {};
    $scope.networkType= '';
    var phoneNum = '';

    function getOpenId(){
        if(!is_weixn()){
            alert('请在微信浏览器中打开');
            return;
        }
        $http.get('/apply/getUserInfo?code=' + $scope.code).success(function(data){
            if(data.status == 'SUCCESS'){
                openId = data.data.openId;
                var expireDate = new Date();
                expireDate.setDate(expireDate.getDate() + 30);
                $cookies.put("wzAuthorization", data.data.openId, {'expires': expireDate});
                try {
                     signSearch();
                } catch(err) {
                    alert("查询签约信息异常" + err);
                    return;
                }
            }else{
                if(data.status == 'ERROR' && data.error == '1'){
                    alert('您的微信号暂未绑定【HPL太盟融资租赁】公众号，请先进入公众号完成绑定再录单！');
                    $window.wx.closeWindow();
                    return;
                }else {
                alert(data.error);
                $window.wx.closeWindow();
                return;
                }

            }
        }).error(function(){
            alert( '系统错误，请稍后再试');
             $window.wx.closeWindow();
            return;
        });
    }

    function signSearch(){
        if(!is_weixn()){
            alert('请在微信浏览器中打开');
            return;
        }
        var url = '';
        if($scope.applyNum != null && $scope.applyNum != 'undefined'){
            url = '/apply/signSearch?openId=' + openId + '&applyNum=' + $scope.applyNum;
        }else{
            url = '/apply/signSearch?openId=' + openId;
        }
        $http.get(url).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.applyInfo = data.data;
                phoneNum = $scope.applyInfo.phoneNum;
                $scope.phoneNum = phoneNum.substring(7);
                $scope.user.openId = $scope.applyInfo.openId;
                var compulsoryInsurance = $scope.applyInfo.compulsoryInsurance;
                var commercialInsurance = $scope.applyInfo.commercialInsurance;
                var vehicleTax = $scope.applyInfo.vehicleTax;
                var compulsoryInsurances = []; //定义一数组
                compulsoryInsurances = compulsoryInsurance.split(","); //字符分割
                var commercialInsurances = commercialInsurance.split(",");
                var vehicleTaxes = vehicleTax.split(",");

                //三项保险都是0不显示
                for (i=0;i<compulsoryInsurances.length ;i++ ) {
                   if(compulsoryInsurances[i] != null && compulsoryInsurances[i] != '' && compulsoryInsurances[i] != '0' && compulsoryInsurances[i] != '0.0'){
                        $scope.showCompulsoryInsurance = true;
                        break;
                   }
                }
                for (i=0;i<commercialInsurances.length ;i++ ) {
                   if(commercialInsurances[i] != null && commercialInsurances[i] != '' && commercialInsurances[i] != '0' && commercialInsurances[i] != '0.0'){
                        $scope.showCommercialInsurances = true;
                        break;
                   }
                }
                for (i=0;i<vehicleTaxes.length ;i++ ) {
                   if(vehicleTaxes[i] != null && vehicleTaxes[i] != '' && vehicleTaxes[i] != '0' && vehicleTaxes[i] != '0.0'){
                        $scope.showVehicleTaxes = true;
                        break;
                   }
                }
                //保险去掉最后面的‘，’
                if(compulsoryInsurance.substring(compulsoryInsurance.length -1, compulsoryInsurance.length) == ','){
                    $scope.applyInfo.compulsoryInsurance = compulsoryInsurance.substring(0, compulsoryInsurance.length -1);
                }
                if(commercialInsurance.substring(commercialInsurance.length -1, commercialInsurance.length) == ','){
                    $scope.applyInfo.commercialInsurance = commercialInsurance.substring(0, commercialInsurance.length -1);
                }
                if(vehicleTax.substring(vehicleTax.length -1, vehicleTax.length) == ','){
                    $scope.applyInfo.vehicleTax = vehicleTax.substring(0, vehicleTax.length -1);
                }
            }else{
                alert(data.error);
                $window.wx.closeWindow();
                return;
            }
        }).error(function(){
                alert('系统错误，请稍后再试');
                $window.wx.closeWindow();
                return;
        });
    }




    $scope.declare = function(){
//        $modal.open({
//            templateUrl: 'tpl/wx_declare.html',
//            controller: 'wxDeclareController',
//            resolve:{
//            }
//        });
    };



    function getLocation(){
      wx.checkJsApi({
           jsApiList: ['getLocation','getNetworkType'],// 需要检测的JS接口列表，
           success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
               if(!res.checkResult.getLocation || !res.checkResult.getNetworkType){
                    alert('当前客户端版本不支持');
//                    $window.wx.closeWindow();
                    return;
               }
           },
           fail: function(res){
                   alert('调用微信失败');
                   return;
           }
      });
      $window.wx.error(function (res) {
          alert('检测接口失败');
          return;
      });
      $window.wx.ready(function(){
        $window.wx.getLocation({
            type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
            success: function (res) {
                $scope.device.lat = new Number(res.latitude).toFixed(5);
                $scope.device.lon = new Number(res.longitude).toFixed(5);
            },
            fail: function(res){
                alert('获取位置信息失败');
                return;
            },
            cancel: function (res) {
                alert('您已拒绝授权获取地理位置，请进入【HPL太盟融资租赁】公众号主体页面打开提供位置信息按钮');
                $window.wx.closeWindow();
                return;
            }
        });
        $window.wx.error(function (res) {
            alert('获取地理位置错误');
            console.log('获取地理位置错误' + res);
        });
      });
    }

    function getNetworkType(){
        $window.wx.getNetworkType({
        success: function (res) {
             $scope.networkType = res.networkType; // 返回网络类型2g，3g，4g，wifi
        },
        fail: function(res){
            alert('获取网络状态失败');
            return;
        }
        });
        $window.wx.error(function (res) {
            alert('获取网络状态异常');
            console.log('获取网络状态异常' + res);
        });
    }

    try {
        getLocation();
        getNetworkType();
    } catch(err) {
        alert("地理位置,网络状态" + err);
        return;
    }
    $scope.form={};
    function checkbox(){
        if($scope.form.protocol){
            var log = [];
            angular.forEach($scope.form.protocol,function(v){
                    if(v==true)
                        this.push(v);
            },log);
            if(log.length < 1){
                alert('请仔细阅读，并同意所有协议');
                return false;
            }else {
                return true;
            }
        }else{
            alert('请仔细阅读，并同意所有协议');
            return false;
        }
    }

    //申请提交
    $scope.submit = function(){
        //check协议是否都勾选
        if(!checkbox()){
            return;
        }
        try {
            getLocation();
            getNetworkType();
        } catch(err) {
            alert("地理位置,网络状态" + err);
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            alert('请填写验证码');
            return;
        }
        if(timestamp  == ''){
            alert('请获取验证码');
            return;
        }
        try {
            if($scope.device.lat == undefined ||  $scope.device.lon  == undefined || $scope.device.lat == '' || $scope.device.lon == ''){
                alert('获取地理位置失败，请进入【HPL太盟融资租赁】公众号主体页面打开提供位置信息按钮');
                $window.wx.closeWindow();
                return;
            }
        } catch(err) {
            alert("地理位置" + err);
            return;
        }
        $http.post('/systems/loginWeb?timestamp=' + timestamp + '&code=' + $scope.user.code).success(function(data){
            if(data.status == 'SUCCESS'){
                var modalInstance = $modal.open({backdrop : 'static',size:'lg',
                    templateUrl: 'tpl/wx_applyFinance.html',
                    controller: 'wxApplyFinanceController',
                    size: 'small'
                });
                modalInstance.result.then(function (item) {
                    if(item == '1'){
                        //若ip获取不到,后台获取
                        try {
                            //ip地址
                            var cip = returnCitySN["cip"];
                        } catch(err) {
//                            alert("获取ip异常，请关闭页面或更换网络再试" + err);
//                            return;
                        }
                        $scope.user.ip = cip;
                        $scope.user.netWortType = $scope.networkType;
                        $scope.user.applyNum = $scope.applyInfo.applyNum;
                        $scope.user.phoneNum = phoneNum;
                        try {
                            if($scope.device.lat != undefined && $scope.device.lon != undefined && $scope.device.lat != '' && $scope.device.lon != ''){
                                $scope.user.locationData = $scope.device.lat + ',' + $scope.device.lon;
                            }else{
                                $scope.user.locationData = '';
                            }
                        } catch(err) {
                            alert("保存地理位置" + err);
                            return;
                        }
                        $scope.$emit("BUSY");
                         $scope.user.notShow = true;
                        $http.post('/apply/signSubmit', $scope.user).success(function(result){
                            $scope.$emit("NOTBUSY");
                            $scope.user.notShow = false;
                            if(result.status == 'SUCCESS'){
                                alert("恭喜您，签约成功!");
                                $window.wx.closeWindow();
                            }else{
                                alert("签约失败" + result.error);
                            }
                        }).error(function(){
                              alert('签约失败，请稍后再试');
                              $scope.user.notShow = false;
                              $scope.$emit("NOTBUSY");
                        });
                    }
                },function(){
                });
            }else {
                 alert(data.error);
                 return;
            }
        }).error(function(){
              alert('短信验证失败，请稍后再试');
        });
    };


    $scope.getSmsCode = function(){
       if(!checkbox()){
           return;
       }
        if($scope.startCountdown){
            return;
        }
        if(phoneNum == null || phoneNum == ''){
            alert('未查询到手机号码');
            return;
         }
        $http.post('/systems/sendRandomCode?phoneNum=' + phoneNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.$broadcast('timer-start');//开始倒计时
                $scope.showMsg = 's 后重新获取';
                $scope.startCountdown = true;
                timestamp = data.data;
            }else{
                alert(data.error);
            }
        }).error(function(){
                alert('发送短信验证码失败，请稍后再试');
        });
    };

        $scope.saveData = function(type){
            var url = '';
            if(type == 'c'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract4v12.html";
            }else if(type == 'd'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract6v10.html";
            }
            $window.location.href = url;
        };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}]);
