app.controller('wxRepaymentCardModifyDetailsController',['toaster', '$scope', '$state', '$stateParams', '$http', '$modal', function(toaster, $scope,$state, $stateParams, $http, $modal) {
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
    $scope.modifyData = {};
    function init(){
        $http.get('/contracts/'+$stateParams.contractNum+'/repayCard').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.data = result.data;
                $http.get('/banks').success(function(res){
                    if(res.status == 'SUCCESS'){
                        $scope.banks = res.data;
                        $scope.bank = $scope.banks[0];
                    }
                });
            }
        });
    }

    init();

    $scope.uploadFront = function(){
        if($scope.cardFront == null){
            $scope.pop('error', '', '请上传银行卡正面照片');
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
                $scope.pop('success', '', '上传成功');
            }else{
                $scope.pop('error', '', result.error);
            }
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
                $scope.pop('success', '', '上传成功');
            }else{
                $scope.pop('error', '', result.error);
            }
        });
    };

    $scope.submit = function(){
        if($scope.frontUrl == null){
            $scope.pop('error', '', '请上传银行卡正面照片');
            return;
        }
        if($scope.behindUrl == null){
            $scope.pop('error', '', '请上传银行卡反面照片');
            return;
        }
        $scope.modifyData.frontMsg = $scope.frontUrl;
        $scope.modifyData.behindMsg = $scope.behindUrl;
        $scope.modifyData.bankMsg = $scope.bank.BAHKYH;
        $http.put('/contracts/'+$stateParams.contractNum+'/repayCard', $scope.modifyData).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.pop('success', '', '提交成功');
            }else{
                $scope.pop('error', '', result.error);
            }
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
}]);
