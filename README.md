# OrbitTracker

Our Boeing project

Goal is to create a program that can propagate orbits and detect potential collisions.

All resources are listed here:
https://www.orekit.org/static/dependency-info.html

Tutorial:
https://www.orekit.org/site-orekit-tutorials-10.3/tutorials/propagation.html

More on orbits from Orekit:
https://www.orekit.org/site-orekit-latest/architecture/orbits.html


In order to import Orekit, I added in the following
line under the dependencies section

compile 'org.orekit:orekit:10.3'

Links used to create fat jar
https://github.com/eugenp/tutorials/blob/master/gradle/gradle-fat-jar/build.gradle

https://www.baeldung.com/gradle-fat-jar

JUnit tutorial:
https://blog.jetbrains.com/idea/2020/09/writing-tests-with-junit-5/

https://gitlab.orekit.org/orekit-labs/python-wrapper/blob/master/examples/TLE_Propagation.ipynb

PropagationResults {
from Galen Stevens to everyone:    11:31 AM
NamedTle
from Galen Stevens to everyone:    11:31 AM
List<PVCoordinates>

List<TimeStep>
from Galen Stevens to everyone:    11:34 AM
TimeStep {
from Galen Stevens to everyone:    11:34 AM
AbsoluteDate
from Galen Stevens to everyone:    11:34 AM
PVCoordinates (or just Vector3D for position)

will need some class that holds the results for all propagated satellites
from Galen Stevens to everyone:    11:37 AM
which should contain a Map<String, PropagationResults> (where the key/string is the TLE name, for example)

JavaFX implementation help sources:

alternate fat jar w/o shadow plugin:

https://stackoverflow.com/questions/49278063/how-do-i-create-an-executable-fat-jar-with-gradle-with-implementation-dependenci

JavaFX with Gradle:

https://stackoverflow.com/questions/20507591/javafx-location-is-required-even-though-it-is-in-the-same-package
https://github.com/openjfx/javafx-gradle-plugin
https://openjfx.io/openjfx-docs/#gradle
https://blog.jetbrains.com/idea/2021/01/intellij-idea-and-javafx/

jar file won't run fix: use helloFX as medium
https://github.com/javafxports/openjdk-jfx/issues/236

To build: gradlew build

To run: gradlew clean run