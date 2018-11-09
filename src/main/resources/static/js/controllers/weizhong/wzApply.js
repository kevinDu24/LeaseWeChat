app.controller('wzApplyController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };

        $scope.applyCode = $location.search().applyCode;
        var timeStamp = $location.search().timeStamp;
        if(!$scope.applyCode){
            alert("邀请码为空，请联系经销商重新生成二维码");
            $window.wx.closeWindow();
            return;
        }
        if(timeStamp){
            try {
                  var time_diff = new Date().getTime() - timeStamp; //时间差的毫秒数
                  if(!time_diff){
                     alert("二维码有误，请联系销售人员重新生成二维码");
                     $window.wx.closeWindow();
                     return;
                  }
                 //计算出相差天数
                 var days = Math.floor(time_diff / (24 * 3600 * 1000));
                 if(days >= 1){
                    alert("二维码或消息已过期，请联系销售人员重新生成二维码");
                    $window.wx.closeWindow();
                    return;
                 }
            }catch(err) {
            }
        } else {
                alert("二维码或消息已过期，请联系销售人员重新生成二维码");
                $window.wx.closeWindow();
                return;
        }
        var openId = $cookies.get("wzAuthorization");
        if(openId != null){
            getUserInfo();
        }
        if(openId == null){
            $scope.code = $location.search().code;
            if($scope.code == null){
                 $scope.code1 = ($location.$$absUrl.split('?code=')[1]);
                 if($scope.code1 == null){
                    // var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx743de4a4fa762293&redirect_uri=http%3A%2F%2Ffwh.xftm.com%2F%23%2Fwx%2FwzApply%3FapplyCode=' + $scope.applyCode + '%26timeStamp=' + timeStamp + '&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect';
                     var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc6b7a0fe2ee928fb&redirect_uri=http%3A%2F%2Fzb2bjz.natappfree.cc%2F%23%2Fwx%2FwzApply%3FapplyCode=' + $scope.applyCode + '%26timeStamp=' + timeStamp + '&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect';
                     $window.location.href = url;
                     return;
                 }
                  $scope.code = ($location.$$absUrl.split('?code=')[1]).split('&state')[0];
                  getOpenId();
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
//    $scope.statement = '1';
    $scope.monthlyIncome = 'R02';
    var timestamp = '';
    $scope.bank = {};
    $scope.bankNum = '';
    $scope.device = {};
    $scope.form = {protocol: {
        a:false,b:false,c:true
    }};
    $scope.networkType= '';
    var pageData = window.localStorage;
    var dataString = pageData.getItem("wzUserData");
    $scope.user = JSON.parse(dataString);
    if($scope.user != null){
        $scope.statement = $scope.user.statement;
        $scope.monthlyIncome = $scope.user.monthlyIncome;
        $scope.bank.bank = $scope.user.bank;
        $scope.bankNum = $scope.user.bankNum;
        $scope.bank = {name : $scope.bank.bank, bankNum : $scope.bankNum};
        $scope.networkType = $scope.user.netWortType;
        timestamp = $scope.user.timestamp
    }
    function getOpenId(){
        $http.get('/apply/getUserInfo?code=' + $scope.code).success(function(data){
            if(data.status == 'SUCCESS'){
                openId = data.data.openId;
                var expireDate = new Date();
                expireDate.setDate(expireDate.getDate() + 30);
                $cookies.put("wzAuthorization", data.data.openId, {'expires': expireDate});

            }else{
                if(data.status == 'ERROR' && data.error == '1'){
                    alert('您的微信号暂未绑定【HPL太盟融资租赁】公众号，请先进入公众号完成绑定再录单！');
                    $window.wx.closeWindow();
                    return;
                }else{
                    alert(data.error);
                    $window.wx.closeWindow();
                    return;
                }
            }
        }).error(function(){
                alert('获取信息失败，请稍后再试');
                $window.wx.closeWindow();
                return;
        });
    }

    function getUserInfo(){
        $http.get('/apply/getUserInfoByOpenId?openId=' + openId).success(function(data){
            if(data.status == 'SUCCESS'){
            }else{
                if(data.status == 'ERROR' && data.error == '1'){
                    alert('您的微信号暂未绑定【HPL太盟融资租赁】公众号，请先进入公众号完成绑定再录单！');
                    $window.wx.closeWindow();
                    return;
                }else{
                    alert(data.error);
                    $window.wx.closeWindow();
                    return;
                }
            }
        }).error(function(){
                alert('系统错误，请稍后再试');
                $window.wx.closeWindow();
                return;
        });
    }

//    function getBankList(){
//        if(!is_weixn()){
//            $scope.pop('error', '', '请在微信浏览器中打开');
//            return;
//        }
//        $http.get('/apply/getBankList').success(function(data){
//            if(data.status == 'SUCCESS'){
//                $scope.banks = data.data;
//                if($scope.bank.bank){
//                    for(var k in $scope.banks){
//                      if($scope.banks[k].bank == $scope.bank.bank){
//                        $scope.bank = $scope.banks[k];
//                      }
//                   }
//                    $scope.bankNum = $scope.bank.bankNum;
//                }else {
//                    $scope.bank = $scope.banks[0];
//                    $scope.bankNum = $scope.bank.bankNum;
//                }
//            }else{
//                $scope.pop('error', '', data.error);
//                return;
//            }
//        }).error(function(){
//              $scope.pop('error', '', '系统错误，请稍后再试');
//              return;
//       });
//    }
//    getBankList();
    $scope.getBank = function(){
        //清空上次填入的信息;
        $scope.bank = '';
        if(!is_weixn()){
            alert('请在微信浏览器中打开');
            return;
        }
        if(!$scope.user.bankCardNum){
            alert('请输入正确的银行卡号');
            return;
        }
        $http.get('/apply/getBank?bankCardNum=' + $scope.user.bankCardNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.bank = data.data;
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
              alert('获取银行信息错误，请稍后再试');
              return;
       });
    }

//    $scope.optionChange = function(){
//        $scope.bankNum = $scope.bank.bankNum;
//    };


    $scope.declare = function(){
            $modal.open({
                templateUrl: 'tpl/weizhong/testa.html',
                controller: 'wxDeclareController',
                resolve:{
                }
            });
    };


    function getLocation(){
               wx.checkJsApi({
                    jsApiList: ['getLocation','getNetworkType'],
                    success: function(res) {
                        console.log(res.checkResult);
//                         if(!res.checkResult.getLocation || !res.checkResult.getNetworkType){
//                              alert('当前客户端版本不支持');
// //                             $window.wx.closeWindow();
//                              return;
//                         }
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
    getLocation();
    getNetworkType();


    function checkbox(){
        if($scope.form.protocol){
            var log = [];
            angular.forEach($scope.form.protocol,function(v){
                    if(v==true)
                        this.push(v);
            },log);
            if(log.length < 3){
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
        //check协议是否都勾选
        if($scope.statement == null || $scope.statement == undefined){
            alert('请选择人纳税声明');
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            alert('请填写验证码');
            return;
        }
/*        if(timestamp  == ''){
            alert('请获取验证码');
            return;
        }*/
        if(!is_weixn()){
            alert('请在微信浏览器中打开');
            return;
        }
        try {
          if($scope.device.lat == undefined ||  $scope.device.lon  == undefined || $scope.device.lat == '' || $scope.device.lon == ''){
              alert('获取地理位置失败，请进入【HPL太盟融资租赁】公众号主体页面打开提供位置信息按钮!');
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
                            //ip地址,引用搜狐js
                            var cip = returnCitySN["cip"];
                        }catch(err) {
//                            alert("获取ip异常，请关闭页面或更换网络再试" + err);
//                            return;
                        }
                        $scope.user.ip = cip;
                        $scope.user.bank = $scope.bank.name;
                        $scope.user.bankNum = $scope.bank.bankNum;
                        $scope.user.openId = openId;
                        $scope.user.netWortType = $scope.networkType;
                        $scope.user.fpName = $scope.applyCode;
                        $scope.user.statement = $scope.statement;
                        $scope.user.monthlyIncome = $scope.monthlyIncome;
                        try {
                            if($scope.device.lat != undefined  &&  $scope.device.lon != undefined && $scope.device.lat != '' && $scope.device.lon != ''){
                                $scope.user.locationData = $scope.device.lat + ',' + $scope.device.lon;
                            }else{
                                $scope.user.locationData = '';
                            }
                        } catch(err) {
                            alert("保存地理位置" + err);
                            return;
                        }
                        $scope.user.notShow = true;
                        $scope.$emit("BUSY");
                        $http.post('/apply/applySubmit', $scope.user).success(function(result){
                            $scope.user.notShow = false;
                            $scope.$emit("NOTBUSY");
                            if(result.status == 'SUCCESS'){
                                alert("您的申请已提交，请耐心等待审核!");
                                pageData.clear();
                                $window.wx.closeWindow();
                            }else{
                                alert("申请失败" + result.error);
                            }
                        }).error(function(){
                            alert('提交失败，请稍后再试');
                            $scope.user.notShow = false;
                            $scope.$emit("NOTBUSY");
                        });
                    }
                },function(){
                });
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
          alert('短信校验失败，请稍后再试');
        });
    };

    $scope.openStatement = function(){
        var modalInstance = $modal.open({
            templateUrl: 'tpl/weizhong/wx_statement.html',
            controller: 'wxStatementController',
            size: 'small'
        });
        modalInstance.result.then(function (item) {
        },function(){
        });
    }
    $scope.saveData = function(type){
            var data = window.localStorage;
            if($scope.user == null){
                $scope.user = {};
            }
             $scope.user.netWortType = $scope.networkType;
             $scope.user.bank = $scope.bank.name;
             $scope.user.bankNum = $scope.bank.bankNum;
             $scope.user.locationData = $scope.device.lat + ',' + $scope.device.lon;
             $scope.user.protocol = $scope.form.protocol;
             $scope.user.timestamp = timestamp;
             $scope.user.statement = $scope.statement;
             $scope.user.monthlyIncome = $scope.monthlyIncome;
            var d=JSON.stringify($scope.user);
            data.setItem("wzUserData",d);
            console.log(data.wzUserData);
            var json=data.getItem("wzUserData");
            var jsonObj=JSON.parse(json);
            console.log(typeof jsonObj);
            var url = '';
            if(type == 'b'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract1v31.html";
            }else if(type == 'a'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract2v31.html";
            }else if(type == 'd'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract3v41.html";
            }
            $window.location.href = url;
    };

    $scope.getSmsCode = function(){
        if(!checkbox()){
            return;
        }
        if($scope.startCountdown){
            return;
        }
        if($scope.statement == null || $scope.statement == undefined){
            alert('请选择人纳税声明');
            return;
        }
        var phoneNum = $scope.user.phoneNum;
        if(phoneNum == null || phoneNum == ''){
            alert('未填写手机号码');
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

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}])
;
