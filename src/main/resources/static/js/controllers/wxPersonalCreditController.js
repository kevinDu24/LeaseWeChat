app.controller('wxPersonalCreditController',['$scope', '$state', '$http', '$modal', '$window',function($scope,$state,$http,$modal,$window) {
    $scope.gotoPage = function(page){
        $state.go(page);
    };
    $scope.apply = {type:0};
    var cities = [];
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

    function getProvinces(){
        $http.get('/json/province.json').success(function(result){
            $scope.provinces = result;
            $scope.province = $scope.provinces[0];
            getCities();
        });
    }

    function getCities(){
        if(cities.length == 0){
            $http.get('/json/city.json').success(function(result){
                $scope.cities = [];
                for(var i in result){
                    if(result[i].ProID == $scope.province.ProID){
                        $scope.cities.push(result[i]);
                    }
                }
                $scope.city = $scope.cities[0];
                cities = result;
            });
        }else{
            $scope.cities = [];
            for(var i in cities){
                if(cities[i].ProID == $scope.province.ProID){
                    $scope.cities.push(cities[i]);
                }
            }
            $scope.city = $scope.cities[0];
        }
    }

    getCarBrands();
    getProvinces();

    $scope.optionChange = function(type){
        type == 0 ? getCarSeries() : getCities();
    };

    $scope.submit = function(){
        var modalInstance = $modal.open({backdrop : 'static',size:'lg',
            templateUrl: 'tpl/wx_applyFinance.html',
            controller: 'wxApplyFinanceController',
            size: 'small'
        });
        modalInstance.result.then(function (item) {
            if(item == '1'){
                $scope.apply.province = $scope.province.name;
                $scope.apply.city = $scope.city.name;
                $scope.apply.carBrand = $scope.carBrand.name;
                $scope.apply.carSeries = $scope.carSer.name;
                $scope.apply.applyType = 0;
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
