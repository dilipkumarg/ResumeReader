'use strict';

sampleYeomanAppApp.controller('MainCtrl', function ($scope, $http) {
    $scope.activeList = {
        active: "active",
        probable: "",
        inActive: ""
    };
    $http({method: 'GET', url: 'resumereader/search?searchKey=java%20AND%20eclipse&contextKey=eclipse'}).
        success(function (data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.result = data;
            $scope.list = data.activeHits;
            $scope.count = {
                "active": Object.keys(data.activeHits).length,
                "probable": Object.keys(data.probableHits).length,
                "inActive": Object.keys(data.inActiveHits).length
            };
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });
    $scope.showHits = function (val) {
        $scope.activeList = {
            active: "",
            probable: "",
            inActive: ""
        };
        if (val === 'active') {
            $scope.list = $scope.result.activeHits;
            $scope.activeList.active = "active";
        } else if (val === 'probable') {
            $scope.list = $scope.result.probableHits;
            $scope.activeList.probable = "active";
        } else if (val === 'inActive') {
            $scope.list = $scope.result.inActiveHits;
            $scope.activeList.inActive = "active";
        }
    }
});

