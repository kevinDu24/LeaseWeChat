app.controller('wzRepayCardChangeResultController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location', function(toaster,$scope,$state,$http,$modal,$window,$location ) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.resultMessage = '';
    function init(){
        $scope.applyNum = $location.search().applyNum;
        $http.get('/apply/repayCardSearch?&applyNum=' + $scope.applyNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.contractInfo = data.data;
            }else{
                 alert(data.error);
            }
        }).error(function(){
            alert('系统错误，请稍后再试');
        });
    }
    init();
}]);
