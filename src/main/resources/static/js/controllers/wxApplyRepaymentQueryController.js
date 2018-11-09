app.controller('wxApplyRepaymentQueryController',['$cookies', '$scope', '$state', '$http', '$cookies', '$localStorage', '$modal', function($cookies, $scope,$state, $http, $cookies, $localStorage, $modal) {

    $localStorage.leaseUrl = 'wx.applyRepaymentQuery';
    $scope.gotoPage = function(page){
        $state.go(page);
    };
    function init(){
        var headers = {authorization: $cookies.get("leaseAuthorization")};
        $scope.customer = $cookies.get("leaseCustomer");
        $http.get('/contracts/payAheadCheck', {headers: headers}).success(function(result){
            if(result.error == '1'){
                $state.go('wx.applyRepaymentCheckError');
            } else {
                $http.get('/contracts', {headers: headers}).success(function(result){
                    if(result.status == 'SUCCESS'){
                        $scope.contracts = result.data;
                    }
                });
            }
        });
    }

    init();

    $scope.goToDetail = function(contractId){
        $http.get('/contracts/' + contractId + '/payAhead').success(function(result){
            if(result.status == 'ERROR'){
                $modal.open({
                    templateUrl: 'tpl/wx_applyRepaymentPrompt.html',
                    controller: 'wxApplyRepaymentPromptController',
                    resolve: {
                        promptMessage:function(){return result.error}
                    }
                });
            }else{
                $state.go('wx.applyRepaymentDetails', {contractId: contractId});
            }
        });
    };
}]);
