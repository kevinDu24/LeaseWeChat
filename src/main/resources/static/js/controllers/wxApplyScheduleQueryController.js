app.controller('wxApplyScheduleQueryController',['$scope', '$state', '$http', '$cookies', '$localStorage', function($scope,$state, $http, $cookies, $localStorage) {

    $localStorage.leaseUrl = 'wx.applyScheduleQuery';
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
        $state.go('wx.applyScheduleDetail', {applyNum: applyNum});
    };
}])
;
