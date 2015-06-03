var TH = angular.module('trainingHelper');

TH.config(function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/create', {
            templateUrl: '/views/newTraining.html',
            controller: 'TrainingController'
        })
        .when('/contact', {
            templateUrl: '/views/contact.html',
            controller: 'ContactController'
        })
        .when('/edit/:trainingId', {
            templateUrl: '/views/editTraining.html',
            controller: 'TrainingController'
        })
        .when('/createDoc', {
            templateUrl: '/views/doc/createDoc.html',
            controller: 'DocumentController'
        })
        .when('/', {
            templateUrl: '/views/home.html'
        });

    $locationProvider.html5Mode(true);
});