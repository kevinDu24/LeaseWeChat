app.controller('wxWzRepaymentCardModifyDetailsController',['toaster', '$scope', '$state', '$stateParams', '$http', '$modal', '$window',  function(toaster, $scope,$state, $stateParams, $http, $modal, $window) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.gotoPage = function(page){
        $state.go(page);
    };
    var applyNum = $stateParams.applyNum;
    $scope.showMsg = '获取验证码';
    $scope.modifyData = {};
    $scope.networkType= '';
    $scope.device = {};
    $scope.bank = {};
    $scope.bankNum = '';
    var timestamp = '';
    var openId = '';
    var pageData = window.localStorage;
    var dataString = pageData.getItem("wzUserData");
    $scope.modifyData = JSON.parse(dataString);
    if($scope.modifyData != null){
        $scope.bank.name = $scope.modifyData.bank;
        $scope.bank.bankNum = $scope.modifyData.bankNum;
        $scope.bankNum = $scope.modifyData.bankNum;
        $scope.bank = {name : $scope.bank.name, bankNum : $scope.bankNum};
        $scope.networkType = $scope.modifyData.netWortType;
        timestamp = $scope.modifyData.timestamp;
        $scope.frontUrl = $scope.modifyData.frontMsg;
        $scope.behindUrl = $scope.modifyData.behindMsg;
        $scope.frontUrl = $scope.modifyData.frontImg;
        $scope.behindUrl =  $scope.modifyData.behindImg;
    }
    function init(){
        $http.get('/contracts/'+$stateParams.contractNum+'/repayCard').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.data = result.data;
//                $http.get('/banks').success(function(res){
//                    if(res.status == 'SUCCESS'){
//                        $scope.banks = res.data;
//                        $scope.bank = $scope.banks[0];
//                    }
//                });
            }else{
                alert(result.error);
                return;
            }
        });
    }
     init();
     function is_weixn(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
      }
    $scope.getBank = function(){
        //清空上次填入的信息;
        $scope.bank = '';
        if(!is_weixn()){
            alert('请在微信浏览器中打开');
            return;
        }
        if(!$scope.modifyData.bankCardNum){
            alert('请输入正确的银行卡号');
            return;
        }
        $http.get('/apply/getBank?bankCardNum=' + $scope.modifyData.bankCardNum).success(function(data){
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

    $scope.uploadFront = function(){
        if($scope.cardFront == null){
            alert('请上传银行卡正面照片');
            return;
        }
        var file = new FormData();
        file.append('file', $scope.cardFront);
        $http.post('/files', file, {
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}
               }).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.frontUrl = result.data.url;
                alert('上传成功');
            }else{
                alert(result.error);
            }
        }).error(function(){
                alert('上传失败，请稍后再试');
                return;
        });
    };

    $scope.uploadBehind = function(){
        if($scope.cardBehind == null){
            $scope.pop('error', '', '请上传银行卡反面照片');
            return;
        }
        var file = new FormData();
        file.append('file', $scope.cardBehind);
        $http.post('/files', file, {
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}
               }).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.behindUrl = result.data.url;
                alert( '上传成功');
            }else{
                alert( result.error);
            }
        }).error(function(){
                alert('上传失败，请稍后再试');
                return;
        });
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
            if($scope.frontUrl == null){
                $scope.pop('error', '', '请上传银行卡正面照片');
                return;
            }
            if($scope.behindUrl == null){
                $scope.pop('error', '', '请上传银行卡反面照片');
                return;
            }
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
            if($scope.modifyData.code == null || $scope.modifyData.code == ''){
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
            if(!is_weixn()){
                alert('请在微信浏览器中打开');
                return;
            }
            $http.post('/systems/loginWeb?timestamp=' + timestamp + '&code=' + $scope.modifyData.code).success(function(data){
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
//                                alert("获取ip异常，请关闭页面或更换网络再试" + err);
//                                return;
                            }
                            try {
                                if($scope.device.lat != undefined && $scope.device.lon != undefined && $scope.device.lat != '' && $scope.device.lon != ''){
                                    $scope.modifyData.locationData = $scope.device.lat + ',' + $scope.device.lon;
                                }else{
                                    $scope.modifyData.locationData = '';
                                }
                            } catch(err) {
                                alert("保存地理位置" + err);
                                return;
                            }
                            $scope.modifyData.bank = $scope.bank.name;
                            $scope.modifyData.bankNum = $scope.bank.bankNum;
                            $scope.modifyData.ip = cip;
                            $scope.modifyData.openId = openId;
                            $scope.modifyData.netWortType = $scope.networkType;
                            $scope.modifyData.frontImg = $scope.frontUrl;
                            $scope.modifyData.behindImg = $scope.behindUrl;
                            $scope.modifyData.bankMsg = $scope.bank.BAHKYH;
                            $scope.modifyData.contractNum =$stateParams.contractNum;
                            $scope.modifyData.applyNum = applyNum;
//                            if($scope.device.lat != undefined  &&  $scope.device.lon != undefined && $scope.device.lat != '' && $scope.device.lon != ''){
//                                $scope.modifyData.locationData = $scope.device.lat + ',' + $scope.device.lon;
//                            }else{
//                                $scope.modifyData.locationData = '';
//                            }
                            $scope.modifyData.notShow = true;
                            $scope.$emit("BUSY");
                            $http.post('/contracts/changeRepayCardSubmit', $scope.modifyData).success(function(result){
                                $scope.modifyData.notShow = false;
                                $scope.$emit("NOTBUSY");
                                if(result.status == 'SUCCESS'){
                                    alert("您的还款卡变更提交成功!");
                                    pageData.clear();
                                    $window.wx.closeWindow();
                                }else{
                                    alert("申请失败" + result.error);
                                }
                            }).error(function(){
                                alert('提交失败，请稍后再试');
                                $scope.modifyData.notShow = false;
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
                alert('提交校验失败，请稍后再试');
                $scope.modifyData.notShow = false;
                $scope.$emit("NOTBUSY");
           });
        };


    $scope.declare = function(){
        $modal.open({
            templateUrl: 'tpl/wx_declare.html',
            controller: 'wxDeclareController',
            resolve:{
            }
        });
    };



    $scope.getSmsCode = function(){
        if(!checkbox()){
            return;
        }
        if($scope.startCountdown){
            return;
        }
        var phoneNum = $scope.modifyData.phoneNum;
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

    $scope.saveData = function(type){
            var data = window.localStorage;
            if($scope.modifyData == null){
                $scope.modifyData = {};
            }
             $scope.modifyData.netWortType = $scope.networkType;
             $scope.modifyData.bank = $scope.bank.name;
             $scope.modifyData.bankNum = $scope.bank.bankNum;
             $scope.modifyData.locationData = $scope.device.lat + ',' + $scope.device.lon;
             $scope.modifyData.timestamp = timestamp;
             $scope.modifyData.frontMsg = $scope.frontUrl;
             $scope.modifyData.behindMsg = $scope.behindUrl;
             $scope.modifyData.openId = openId;
             $scope.modifyData.frontImg = $scope.frontUrl;
             $scope.modifyData.behindImg = $scope.behindUrl;

            var d=JSON.stringify($scope.modifyData);
            data.setItem("wzUserData",d);
            console.log(data.wzUserData);
            var json=data.getItem("wzUserData");
            var jsonObj=JSON.parse(json);
            console.log(typeof jsonObj);
            var url = '';
            if(type == 'e'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract5v10.html"
            }
            $window.location.href = url;
    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}]);
