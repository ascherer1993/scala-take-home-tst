# scala-take-home-tst

## sbt project compiled with Scala 3


### General
This is a normal sbt project created using the metals template for scala3. 

### Running the code
As long as you have sbt and the java JDK installed ([Documentation](https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html)), you should be able to run the following in commandline/terminal to get started:

You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

Note on setup: I used homebrew to install sbt and did have to update my xcode to a newer version for this to work

I set the prompts up as two separate projects, so once running, you will be prompted to enter a 1 or a 2 to match the prompt that you wish to run the code for.

Enter `2` for the problem on best price for rate groups (Problem 1)

Enter `1` for the prompt on promotion combos (Problem 2) 


### Tests
To run tests, you will need to run `sbt test`. This will run all tests for the entire solution.