app.controller('wxRepaymentPlanQueryController',['$scope', '$state', '$cookies', '$http', '$localStorage', function($scope,$state, $cookies, $http, $localStorage) {
    $localStorage.leaseUrl = 'wx.repaymentPlanQuery';
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

    $scope.goToDetail = function(applyNum){
        $state.go('wx.wzRepaymentPlanDetail', {applyNum: applyNum});
    };
}])
;
