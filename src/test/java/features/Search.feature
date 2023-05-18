Feature: Search
  @Search
  Scenario: Check "Buy Now" button is present
    Given I am able to search specified productId
    When  I open the product page for specified productId
    Then  I should see the Buy Now button

  @Search
  Scenario: Check customer ratings is over 4
    Given I am able to search specified productId
    When  I open the product page for specified productId
    Then  I should check if customer rating for the product is over four

  @Search
  Scenario: Get all the offer details
    Given I am able to search specified productId
    When  I open the product page for specified productId
    Then  Print all the offers available on the description page for the product