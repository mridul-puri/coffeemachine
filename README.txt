COFFEE MACHINE API

This is a working software for the internal functioning of a typical coffee machine.

- There can be N outlets to this machine which can serve beverages in parallel.
- There will be a set of ingredients and their capacity which can be configured when the machine "starts" or "restarts" (APIs exposed)
- Each beverage has a recipe (ie. a set of ingredients and their required quantity)
- Same ingredient can be used for multiple beverages
- Any beverage can be served if all the ingredients with their respective quantities are available in the machine
- Inventory of these ingredients is managed by the program
- There is an indicator which shows when an ingredient is running low and methods have been exposed to refill them
- The key problem here is to serve multiple beverages at the same time which share a common inventory. This is handled using Multi-threading
- Integration tests have been provided for maximum coverage

This is a Maven project written in Java using Spring Boot framework.

How to run Integration test Cases  :- 
1) Open "setup.json" and alter the input data as required
2) Run the following command : mvn clean verify -P integration-test
