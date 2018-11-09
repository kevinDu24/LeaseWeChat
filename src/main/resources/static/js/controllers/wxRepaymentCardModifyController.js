app.controller('wxPaymentCardModifyController',['$scope', '$state', '$cookies', '$http', '$localStorage', function($scope,$state, $cookies, $http, $localStorage) {
    $localStorage.leaseUrl = 'wx.repaymentCardModify';
    function init(){
        $scope.customer = $cookies.get("leaseCustomer");
        var headers = {authorization: $cookies.get("leaseAuthorization")};
        $http.get('/contracts', {headers: headers}).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.contracts = result.data;
            }
        });
    }

    init();

    $scope.goToDetail = function(contractNum, applyNum){
        $state.go('wx.wzRepaymentCardModifyDetails', {contractNum: contractNum, applyNum: applyNum});
    };
}])
;
