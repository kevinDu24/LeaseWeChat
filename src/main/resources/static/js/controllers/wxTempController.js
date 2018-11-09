/**
 * Created by LEO on 16/10/20.
 */
app.controller('wxTempController', ['$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', '$window', function($scope, $http, $state, $rootScope, $location, $cookies, $localStorage, $window) {
    var code = ($location.$$absUrl.split('?code=')[1]).split('&state')[0];
    $window.location.href = 'http://fwh.xftm.com/#/access/bind?code='+code;
}]);