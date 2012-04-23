rm -r %CATALINA_HOME%\conf\Catalina\localhost\socnet.xml
rm -r %CATALINA_HOME%\conf\Catalina\localhost\h2.xml
copy config\h2.xml %CATALINA_HOME%\conf\Catalina\localhost\h2.xml
%CATALINA_HOME%\bin\startup.bat
