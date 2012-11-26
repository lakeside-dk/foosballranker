basePath = '../../main/webapp/';

files = [
  JASMINE,
  JASMINE_ADAPTER,
  'lib/angular/angular.js',
  'lib/angular/angular-*.js',
  'test/lib/angular/angular-mocks.js',
  'js/app.js',
  'js/controllers.js',
  'js/directives.js',
  'js/filters.js',
  'js/services.js',
  'test/unit/**/*.js'
];

autoWatch = true;

browsers = ['Chrome'];

junitReporter = {
  outputFile: 'test_out/unit.xml',
  suite: 'unit'
};
