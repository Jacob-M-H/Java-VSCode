This is going to be a java workspace. 

Tutorial we'll follow
https://code.visualstudio.com/docs/languages/java

Extension Pack for Java installed as recommended.
Runtime will be JDK, a new terminal run :'java -version' to see if it is installed


JVM hotspot is preselected, and version 17,
HotSpot, released as Java HotSpot Performance Engine, is a Java virtual machine for desktop and server computers, developed by Sun Microsystems and now maintained and distributed by Oracle Corporation. It features improved performance via methods such as just-in-time compilation and adaptive optimization
wikipedia^, seems harmless

Adoptium Temurin is the package I believe...

Java is installed and ready to go.
Ctrl+Shift+p -> Java: Tips for beginners.


//NOTE if you open a java file in vscode without opening it's folder, the lang server might not work properly..
//You can also create a java project using Java:Create Java Project, using the command palette.
//FUTURE: See project management in the tutorial.
//FUTURE: See Java Editing
//NOTE: breakpoints can be set like in VS, Run>Start>Debugging menu item, for debug,
or use Run|Debug codelens options in the editor. 

After compiling, you can see all variables and threads in run/debug view.
//Note I can only seem to find the debug view of variables watch and callstack after setting a breakpoint.

//FUTURE: editing java, debugging, testing, java project managment, spring boot,  tomcat and jetty, java web apps.
FUTURE:https://medium.com/@cancerian0684/what-are-four-basic-principles-of-object-oriented-programming-645af8b43727



Note on the bliping of images.
    6/25/23, spent last night and this morning thinking about low level placing of objects on the screen - since I will/have been busy tonight. Decided that blipping a matrix onto the screen makes sense, and the arbitrary/precision values held by each object [to ensure mathematical alignment, but this itself needs to be re-evaluated].
    The rotation of values should happen through formulas, not matrix problems. 
    The rotation of objects around a central point has a test formula to try eventually.Perhaps will make a moc-up of the rotation of an object over a matrix with inputted values.
    Tomorrow JavaFX/Maven will be the primary focus.
    I will plot the path of study tomorrow first, and see if I need Maven, or if I can slide by with just JavaFX for now.
    If I require Maven, I'll try to disect it's 'tutorial' document, otherwise I'll figure out how to inport JavaFX or something similar.
    
    6/26/23 From 8 to 9:20 pm I spent looking for a half decent Maven tutorial, or breakdown. From what I gathered over a few pages of notes is that Maven seems to be related to fullstack, is designed to easily package and borrrow code, and organize resources through set conventions.
    The test folder for my purposes isn't super required, so I will investigate that as a project develops, or I hit a road block that prevents my progress.
    Currently, I'll see about making a hello world program through Maven's src/main/java, and get rid of everything else I can. 
    Then, once I confirm I can make things run in Maven, and the general features a bit more than I currently do, I'll start making some test JavaFX applications.
    