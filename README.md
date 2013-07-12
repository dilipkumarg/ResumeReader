ResumeReader
============
Resume Reader is java application used to index and search resumes.

###Supported Formats:
Presently we are supporting following three document formats for reading resumes.
 * doc,
 * docx and
 * pdf.

###Commands:
 * indexdir <Path> : which is used to set the index directory path
 * resumedir <Path> : which is used to set the resume directory path
 * update: which is used to update the index, it will read the files in the "resumedir" folder, and adds to the index
 * search <Search key>: which is used to search the resumes for given search key.

###Libraries Used: 
 * Apache Lucene: for indexing the resumes.
 * Apache Poi: for reading doc and docx files.
 * Itextpdf: for reading pdf files.
  
###Installation:
This project used maven build. so no need to worry about classpath settings  and all.

###Running:
Presently we are supporting only command mode operations.
Before indexing and searching initially you have to set the index directory path and resume directory path by using above commands.
(or) else you can directly set this details by editing *config.properties* file

