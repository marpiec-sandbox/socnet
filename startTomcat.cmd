rm -r %CATALINA_HOME%\conf\Catalina\localhost\socnet.xml
rm -r %CATALINA_HOME%\conf\Catalina\localhost\h2console.xml
copy %SOCNET_HOME%\config\socnet.xml %CATALINA_HOME%\conf\Catalina\localhost\socnet.xml
%CATALINA_HOME%\bin\startup.bat
