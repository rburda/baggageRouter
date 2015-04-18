Steps to Install and Run

1. Download Reuben_Burda_BaggageRouter.jar to desired location
2. Extract jar
      'jar xvf Reuben_Burda_BaggageRouter.jar'
3. Build with maven
      'mvn clean install'
4. Run the program (pass in an input file)
      'java -cp ./target/BaggageRouter-1.0-SNAPSHOT.jar com.burda.Driver <fullyQualifiedPathToInputFile>'


Notes:
1. You must have maven 3.3.1 installed in your path
2. You must have a Java 1.8 JVM installed in your path
3. The main class (com.burda.Driver) expects a single argument to the path of an input file. The input file must match
   the syntax specified in the requirements.
4. If desired, you may import this code into IntelliJ IDE as a maven project.