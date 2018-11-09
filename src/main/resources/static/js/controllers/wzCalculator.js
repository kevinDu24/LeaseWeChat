app.controller('wzApplyCalculator',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };

//期数：选择二年的就是24月，三年的就是36月供： (（车价+代码）/10000) * 475(2年就是475，三年就是336)
//(（月供 - （车价/期数））/(车价/10000)   )/10 -0.5


    $scope.user = {};
    $scope.term = '24';

    $scope.submit = function(){
        $scope.payment = '';
        $scope.interest = '';
        var num = '';
        if( $scope.term == '24'){
            num = 475;
        }else {
            num = 336;
        }
        var num01 = new Number($scope.user.car).toFixed(2);
        var num02 = new Number($scope.user.code).toFixed(2);
        var num0 = Number(num01) +Number(num02);
        $scope.payment = num0/10000 * num;
        var num1 = $scope.payment - ($scope.user.car/$scope.term);
        var num2 = $scope.user.car/10000;
        $scope.interest = num1/num2/10-0.5;
    };

    $scope.clear = function(){
        $scope.payment = '';
        $scope.interest = '';
        $scope.term == '24';
        $scope.user = {};
    };

}])
;
