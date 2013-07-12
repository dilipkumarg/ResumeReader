ResumeReader
============
Resume Reader is java application used to index and search resumes.

###Supported Formats:
Presently we are supporting following three document formats for indexing resumes.
 * doc,
 * docx and
 * pdf.

###Commands (All commands are NOT Case Sensitive):
 * <strong>indexdir <Path> :</strong> This is used to set the index directory path.
 * <strong>resumedir <Path> :</strong> This is used to set the resume directory path.
 * <strong>update:</strong> This is used to update the index, it reads all the newly added files in the "resumedir" folder, and adds to the index i.e "indexdir".
 * <strong>search <Search key>:</strong> This is used to search the resumes for a given search key. Use quotes when the search key contains spaces.

###Libraries Used: 
 * Apache Lucene: For indexing and searching the resumes.
 * Apache Poi: For reading doc and docx files.
 * Itextpdf: For reading pdf files.
  
###Installation:
This project uses maven build, which takes care of libraries and classpaths. 

###Running:
Presently we support only command mode operations.
Before indexing or searching, you need to set the index and resume directory paths by using the first two commands.
(or) else you can directly set this details by editing <config.properties> which resides in project root directory. 

