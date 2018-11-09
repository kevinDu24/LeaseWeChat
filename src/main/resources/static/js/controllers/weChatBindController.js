/**
 * Created by LEO on 16/9/28.
 */
app.controller('weChatBindController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {

    $scope.code = $location.search().code;
    if($scope.code == null){
        $scope.code = ($location.$$absUrl.split('?code=')[1]).split('&state')[0];
    }
    $scope.showMsg = '获取验证码';
    var phoneNum = null;
    $scope.user = {};
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    function is_weixn(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }
    $scope.bind = function(){
        if(!is_weixn()){
            $scope.pop('error', '', '请在微信浏览器中打开');
            return;
        }
        if(phoneNum == null || phoneNum == ''){
            $scope.pop('error', '', '未查询到手机号码');
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            $scope.pop('error', '', '请填写验证码');
            return;
        }
        $http.post('/systems/verifyCode?phoneNum=' + phoneNum + '&code=' + $scope.user.code).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.user.wxCode = $scope.code;
                $scope.user.phoneNum = phoneNum;
                $http.post('weChats/bind', $scope.user).success(function(result){
                    if(result.status == 'SUCCESS'){
                        var expireDate = new Date();
                        expireDate.setDate(expireDate.getDate() + 30);
                        $cookies.put("leaseAuthorization", "Basic " + btoa(result.data.openId + ":" + result.data.code), {'expires': expireDate});
                        $cookies.put("leaseCustomer", {name: result.data.name, cardId: result.data.cardId}, {'expires': expireDate});

                        if($localStorage.leaseUrl != null && $localStorage.leaseUrl != ''){
                            $state.go($localStorage.leaseUrl);
                        }else{
                            $state.go('wx.applyScheduleQuery');
                        }
                    }else{
                        $scope.pop('error', '', result.error);
                    }
                });
            }else{
                $scope.pop('error', '', data.error);
            }
        });
    };

    $scope.getSmsCode = function(){
        if($scope.startCountdown){
            return;
        }
        if($scope.user.name == null || $scope.user.cardId == null){
            $scope.pop('error', '', '姓名和身份证不可为空');
            return;
        }
        if(is_weixn()){

        }
        $http.get('/customers/getPhoneNum?name=' + $scope.user.name + '&cardId=' + $scope.user.cardId).success(function(result){
            if(result.status == 'SUCCESS'){
                if(result.data.BASJHM == null || result.data.BASJHM == ''){
                    $scope.pop('error', '', '未查询到手机号码');
                    return;
                }
                phoneNum = result.data.BASJHM;
                $http.post('/systems/sendCode?phoneNum=' + phoneNum).success(function(data){
                    if(data.status == 'SUCCESS'){
                        $scope.$broadcast('timer-start');//开始倒计时
                        $scope.showMsg = 's 后重新获取';
                        $scope.startCountdown = true;
                    }else{
                        $scope.pop('error', '', result.error);
                    }
                });
            }else{
                $scope.pop('error', '', result.error);
            }
        });
    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}]);