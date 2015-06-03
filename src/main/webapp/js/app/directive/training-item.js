var TH = angular.module('trainingHelper');

TH.directive("trainingListItem", function() {

   return {
       restrict: "E",
       replace: true,
       templateUrl: "/js/app/directive/template/trainingListItem.html",
       scope: {
           training: "=training",
           deleteFunc: "&onTrainingDelete",
           openFunc: "&onTrainingChange"
       },
       link: function(scope, element) {
           scope.onDelete = function() {
                scope.deleteFunc({trainingId:scope.training.id});
           };
           scope.onChange = function() {
               scope.openFunc({trainingId:scope.training.id});
           };
       }
   }

});