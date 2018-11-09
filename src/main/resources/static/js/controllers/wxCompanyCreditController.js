app.controller('wxCompanyCreditController',['$scope', '$state', '$http', '$modal', '$window',function($scope,$state, $http,$modal,$window) {
    $scope.gotoPage = function(page){
        $state.go(page);
    };

    $scope.apply = {type:0};
    function getCarBrands(){
        $http.get('/cars').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.carBrands = result.data.branditems;
                $scope.carBrand = $scope.carBrands[0];
                getCarSeries();
            }
        });
    }

    function getCarSeries(){
        $http.get('/cars/' + $scope.carBrand.id).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.carSeries = [];
                for(var i in result.data.factoryitems){
                    for(var j in result.data.factoryitems[i].seriesitems){
                        $scope.carSeries.push(result.data.factoryitems[i].seriesitems[j]);
                    }
                }
                $scope.carSer = $scope.carSeries[0];
            }
        });
    }

    getCarBrands();

    $scope.optionChange = function(){
        getCarSeries();
    };

    $scope.submit = function(){
        var modalInstance = $modal.open({backdrop : 'static',size:'lg',
            templateUrl: 'tpl/wx_applyFinance.html',
            controller: 'wxApplyFinanceController',
            size: 'small'
        });
        modalInstance.result.then(function (item) {
            if(item == '1'){
                $scope.apply.carBrand = $scope.carBrand.name;
                $scope.apply.carSeries = $scope.carSer.name;
                $scope.apply.applyType = 1;
                $http.post('/financeApply', $scope.apply).success(function(result){
                    if(result.status == 'SUCCESS'){
                        alert("申请提交成功!");
                        $window.location.reload();
                    }else{
                        $scope.error = result.error;
                    }
                });
            }
        },function(){
        });
    };
}])
;
