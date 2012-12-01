'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('foosball ranker', function() {

  beforeEach(function() {
    browser().navigateTo('../../index.html');
  });

  it('should automatically redirect to /login when location hash/fragment is empty', function() {
    expect(browser().location().url()).toBe("/login");
  });


  describe('login', function() {

    beforeEach(function() {
      browser().navigateTo('#/login');
    });

    it('should render login when user navigates to /login', function() {
      expect(element('[ng-view] p:first').text()).
        toMatch(/Register your foosball matches/);
    });

    it('should navigate to opponents when user press login button', function() {
        element('.btn').click();
        expect(browser().location().url()).toBe('/opponents');
    });

  });


  describe('opponents', function() {

    beforeEach(function() {
      browser().navigateTo('#/opponents');
    });

    it('should render /opponents when user navigates to /opponents', function() {
      expect(element('[ng-view] legend:first').text()).
        toMatch(/Opponents/);
    });

  });
});
