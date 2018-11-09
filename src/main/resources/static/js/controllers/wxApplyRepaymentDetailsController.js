app.controller('wxApplyRepaymentDetailsController',['$scope', '$state', '$http', '$stateParams', function($scope,$state, $http, $stateParams) {
    $scope.gotoPage = function(page){
        $state.go(page);
    };
    $scope.error = null;
    $scope.canApply = true;
    function init(){
        $http.get('/contracts/'+$stateParams.contractId+'/payAhead').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.contractInfo = result.data.contractInfo;
                $scope.accountInfo = result.data.accountInfo;
                $scope.repaymentData = result.data.earlyRepaymentAmount;
            }else{
                $scope.error = result.error;
                $scope.canApply = false;
            }
        });
    }
    init();

    $scope.submit = function(){
        $scope.error = null;
        if(!$scope.agree){
            $scope.error = '请确认提交申请!';
        }
        if(!$scope.canApply){
            $scope.error = '您无法申请提前还款';
        }
        var payAheadDto = {contractId:$stateParams.contractId,repayMoney:$scope.repaymentData.tqhkje,
            remainMoney:$scope.repaymentData.basybj,monthInterest:$scope.repaymentData.badylx,
            commission:$scope.repaymentData.basxfy,breach:$scope.repaymentData.bawyjf,
            deposit:$scope.repaymentData.babzjf,insurance:$scope.repaymentData.wthbxf,
            date:$scope.repaymentData.ydhkrq};
        $http.put('/contracts/'+$stateParams.contractId+'/payAhead', payAheadDto).success(function(result){
            if(result.status == 'SUCCESS'){
                alert('申请提交成功!');
            }else{
                $scope.error = result.error;
            }
        });
    }
}])
;
