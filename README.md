ResumeReader
============
Resume Reader is java application used to index and search resumes.

###Supported Formats:
Presently we support following three document formats for indexing resumes.
 * doc,
 * docx and
 * pdf.

###Commands (All commands are NOT Case Sensitive):
 * <strong>`resumedir <Path>` :</strong> This is used to set the resume directory path.
 * <strong>`employeefile <Path>` :</strong> This is used to set the employee excel file path.
 * <strong>`index` :</strong> This is used to update the index, it reads all the newly added files in the `<resumedir>` folder, and adds to the index i.e `<indexdir>`.
 * * <strong>`cleanandindex` :</strong> This is used to clean the index first and then update it.
 * <strong>`search <Search key>`:</strong> This is used to search the resumes for a given search key. Use quotes when the search key contains spaces.

###Libraries Used: 
 * Apache Lucene: For indexing and searching the resumes.
 * Apache Tika: For reading the above supported document files.
  
###Installation:
This project uses maven build, which takes care of libraries and classpaths.
Steps for creating Jar and configuring:
 * Go to the project root directory.
 * Run the command.
  `mvn package`
   This command will generate jar file in the folder `<project build directory>/ResumeReader`.

NOTE:Before indexing or searching, you need to set the index and resume directory paths by using the first two commands.
(or) else you can directly set this details by editing `config.properties` which resides in ResumeReader directory. 

###Running:
Presently we support only command mode operations.
 * Go to the `<project build directory>/ResumeReader` directory.
 * Execute `java -jar ResumeReader.jar <your command>`

