app.controller('wzResultController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location', function(toaster,$scope,$state,$http,$modal,$window,$location ) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.device = {};
    $scope.applyInfo = {status: ''};

    $scope.resultMessage = '';

        function init(){
        $scope.uniqueMark = $location.search().uniqueMark;
        $scope.applyNum = $location.search().applyNum;
        $http.get('/apply/stateSearch?uniqueMark=' + $scope.uniqueMark + '&applyNum=' + $scope.applyNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.applyInfo = data.data;
                var status = $scope.applyInfo.status;
                var reason = $scope.applyInfo.reason;
                if(status == '1' || status == '2'){
                    $scope.resultMessage = '很遗憾您的预审批未能通过，' + reason;
                } else if(status == '3'){
                    $scope.resultMessage = '尊敬的客户,您的预审批已通过!'
                } else if(status == '4'){
                    $scope.resultMessage = '尊敬的客户,您的最终审批已通过，请耐心等待签约!'
                } else if(status == '5'){
                    $scope.resultMessage = '很遗憾您的最终审批未能通过，' + reason;
                } else if(status == '7'){
                    $scope.resultMessage = '尊敬的客户,您的预审批被退回,请修改后重新提交:' + reason;
                }
            }else{
                 alert(data.error);
            }
        }).error(function(){
            alert('系统错误，请稍后再试');
        });
    }
    init();

}]);
