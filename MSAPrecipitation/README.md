
About this Application:
MSAPrecipitation application calculaes MSA wetness for may 2015 and sorts the MSA list by amount of wetness.
MSA wetness is calculated by 
(total population in the month * total rain for the duration) 

Considering people stayed indoors between 7am - 12am, this duration wetness is ignored.
 
This application takes 3 csv files as arguments.
hourly.txt - hourly precipitation data for MSA
population.csv - Population in MSA are in 2010 and %increase from 2000 to 2010
station.txt - MSA area and their respective id (wban)
It also needs file pasring related information like, how many header lines to skip reading, field separator.
After parsing the three files a list is created with MSA wetness and sorted by wetness information. MSA wetness list is displayed 
on console for before and after sorting.
If the wetness of 2 cities are same, sorting is done by alphabetical order city name.

Assumptions:  
Precipitation is considered 0 if the field is non numeric.
For each file reading, there is a reader class which documents details about assumption for Javadoc.


How to build and run in Eclipse:
-------------------------------
Download the project to local directory first. I call it 'source directory'. eg. C:\users\myuser\projects

1. In eclipse create a new java project with src folder=java, build directory = build
2. File->General->File System -> From Directory -> Browse to the root of project directory which is in 'SOURCE directory'. For our example case, it will be C:\users\myuser\projects\MSAPrecipitation. 
3. Check project on left panel. Uncheck .classpath, .project, build.gradle 
(Leave Overwrite existing resources, create top level folder options unchecked)
4. It will prompt you for overwriting settings file. select 'yes to all'
5. Right click on project -> Build Path -> Configure Build Path -> Select Source Tab.
6. Add folder conf, java. Expand java folder and check tempresource.
7. Select Libraries tab -> Add JARs -> in the browser that opens, expand project folder -> expand lib folder -> clcik and select all jars -> click OK
8. Download JUnit4.12 and add to classpath as JUnit 4.12 in classpath 
9. Right click on the Driver file and run as Java application.
10. Testcases are in java/test/folder package and can be run by right clicking on the file and run as JUnit test.