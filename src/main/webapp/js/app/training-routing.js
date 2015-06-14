var TH = angular.module('trainingHelper');

TH.config(function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/create', {
            templateUrl: '/views/newAndEditTraining.html',
            controller: 'TrainingController'
        })
        .when('/contact', {
            templateUrl: '/views/contact.html',
            controller: 'ContactController'
        })
        .when('/edit/:trainingId', {
            templateUrl: '/views/newAndEditTraining.html',
            controller: 'TrainingController'
        })
        .when('/createDoc', {
            templateUrl: '/views/doc/createDoc.html',
            controller: 'DocumentController'
        })
        .when('/', {
            templateUrl: '/views/home.html'
        })
        .when('/loginOp/:op', {
            template: '<div></div>',
            controller: 'LoginRedirectController'
        });

    $locationProvider.html5Mode(true);
});