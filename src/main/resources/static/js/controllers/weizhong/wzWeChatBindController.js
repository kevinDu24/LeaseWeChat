/**
 * Created by LEO on 16/9/28.
 */
app.controller('wzWeChatBindController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', '$window', '$modal',  function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage, $window, $modal) {
    $scope.code = $location.search().code;
//    $scope.applyCode = ($location.$$absUrl.split('?applyCode=')[1]);
//    if(!$scope.applyCode){
//           alert("邀请码为空，请联系经销商生成邀请码");
//        return
//    }
    if($scope.code == null){
        $scope.code1 = ($location.$$absUrl.split('?code=')[1]);
        if($scope.code1 == null){
            var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx743de4a4fa762293&redirect_uri=http%3A%2F%2Ffwh.xftm.com%2F%23%2Faccess%2FwzBind&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect';
            $window.location.href = url;
            return;
        }
         $scope.code = ($location.$$absUrl.split('?code=')[1]).split('&state')[0];
    }

    function getWzBind(){
        $http.get('/weChats/getWzBind?wxCode=' + $scope.code).success(function(data){
            if(data.status == 'ERROR'){
                   alert(data.error);
                   $window.wx.closeWindow();
                   return;
            }else{
                $scope.user.openId = data.data
            }
        }).error(function(){
                $scope.pop('error', '', '系统错误，请稍后再试');
                return;
        });
    }
    getWzBind();

    var timestamp = '';
    $scope.showMsg = '获取验证码';
    $scope.user = {notShow:false};
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
//    var system = '';
//      //手机操作系统
//     if(/android/i.test(navigator.userAgent)){
//          //这是Android平台下浏览器
//          alert("Android");
//          system = 'Android';
//      }
//      if(/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)){
//          //这是iOS平台下浏览器
//          alert("iOS");
//          system = 'iOS';
//      }
    function is_weixn(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }
    $scope.bind = function(){
        var phoneNum = $scope.user.phoneNum;
        if(!is_weixn()){
            $scope.pop('error', '', '请在微信浏览器中打开');
            return;
        }
        if(phoneNum == null || phoneNum == ''){
            $scope.pop('error', '', '未填写手机号码');
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            $scope.pop('error', '', '请填写验证码');
            return;
        }
        $scope.$emit("BUSY");
        $scope.user.notShow = true;
        $http.post('/systems/loginWeb?timestamp=' + timestamp + '&code=' + $scope.user.code).success(function(data){
            $scope.$emit("NOTBUSY");
            $scope.user.notShow = false;
            if(data.status == 'SUCCESS'){
                $scope.user.wxCode = $scope.code;
                $scope.user.phoneNum = phoneNum;
                $scope.user.notShow = true;
                $http.post('weChats/wzBind', $scope.user).success(function(result){
                    $scope.user.notShow = false;
                    if(result.status == 'SUCCESS'){
//                        $modal.open({
//                            templateUrl: 'tpl/weizhong/wx_wzBindConfirm.html',
//                            controller: 'wxWzBindConfirmController',
//                            resolve: {
//                                openId:function(){return result.data.openid}
//                            }
//                        });
                        var expireDate = new Date();
                        expireDate.setDate(expireDate.getDate() + 30);
                        $cookies.put("wzAuthorization", result.data.openId, {'expires': expireDate});
                        alert("绑定成功,可以前去申请录单了!");
                        $window.wx.closeWindow();
                        $scope.user = {};
                    }else{
                        $scope.pop('error', '', result.error);
                    }
                }).error(function(){
                      $scope.pop('error', '', '绑定异常，请稍后再试');
                });
            }else{
                $scope.pop('error', '', data.error);
            }
        }).error(function(){
              $scope.pop('error', '', '短信验证失败，请稍后再试');
        });
        };

    $scope.getSmsCode = function(){
        if($scope.startCountdown){
            return;
        }
        if(is_weixn()){

        }
        var phoneNum = $scope.user.phoneNum;
        if(phoneNum == null || phoneNum == ''){
            alert('未填写手机号码');
            return;
         }
        $scope.$emit("BUSY");
        $http.post('/systems/sendRandomCode?phoneNum=' + phoneNum).success(function(data){
            $scope.$emit("NOTBUSY");
            if(data.status == 'SUCCESS'){
                $scope.$broadcast('timer-start');//开始倒计时
                $scope.showMsg = 's 后重新获取';
                $scope.startCountdown = true;
                timestamp = data.data;
            }else{
                $scope.pop('error', '', result.error);
            }
        }).error(function(){
                $scope.pop('error', '', '发送短信验证码失败，请稍后再试');
        });
    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}]);