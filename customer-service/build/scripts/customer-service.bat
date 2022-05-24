@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  customer-service startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and CUSTOMER_SERVICE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\customer-service-plain.jar;%APP_HOME%\lib\spring-boot-starter-web-2.6.7.jar;%APP_HOME%\lib\spring-boot-starter-data-cassandra-2.6.7.jar;%APP_HOME%\lib\spring-cloud-starter-config-3.1.0.jar;%APP_HOME%\lib\spring-boot-starter-actuator-2.6.7.jar;%APP_HOME%\lib\spring-cloud-starter-netflix-eureka-client-3.1.0.jar;%APP_HOME%\lib\spring-boot-starter-security-2.6.7.jar;%APP_HOME%\lib\spring-boot-starter-json-2.6.7.jar;%APP_HOME%\lib\spring-cloud-starter-loadbalancer-3.1.0.jar;%APP_HOME%\lib\spring-cloud-starter-3.1.0.jar;%APP_HOME%\lib\spring-boot-starter-cache-2.6.7.jar;%APP_HOME%\lib\spring-boot-starter-2.6.7.jar;%APP_HOME%\lib\spring-boot-actuator-autoconfigure-2.6.7.jar;%APP_HOME%\lib\spring-cloud-config-client-3.1.0.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.6.7.jar;%APP_HOME%\lib\spring-boot-actuator-2.6.7.jar;%APP_HOME%\lib\spring-boot-2.6.7.jar;%APP_HOME%\lib\spring-webmvc-5.3.19.jar;%APP_HOME%\lib\spring-security-web-5.6.3.jar;%APP_HOME%\lib\spring-web-5.3.19.jar;%APP_HOME%\lib\spring-data-cassandra-3.3.4.jar;%APP_HOME%\lib\spring-tx-5.3.19.jar;%APP_HOME%\lib\spring-security-config-5.6.3.jar;%APP_HOME%\lib\spring-security-core-5.6.3.jar;%APP_HOME%\lib\spring-context-support-5.3.19.jar;%APP_HOME%\lib\spring-context-5.3.19.jar;%APP_HOME%\lib\spring-aop-5.3.19.jar;%APP_HOME%\lib\spring-data-commons-2.6.4.jar;%APP_HOME%\lib\spring-beans-5.3.19.jar;%APP_HOME%\lib\spring-expression-5.3.19.jar;%APP_HOME%\lib\spring-core-5.3.19.jar;%APP_HOME%\lib\spring-jcl-5.3.19.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.6.7.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\java-driver-query-builder-4.13.0.jar;%APP_HOME%\lib\java-driver-core-4.13.0.jar;%APP_HOME%\lib\eureka-core-1.10.17.jar;%APP_HOME%\lib\eureka-client-1.10.17.jar;%APP_HOME%\lib\netflix-eventbus-0.3.0.jar;%APP_HOME%\lib\servo-core-0.12.21.jar;%APP_HOME%\lib\log4j-to-slf4j-2.17.2.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.36.jar;%APP_HOME%\lib\metrics-core-4.2.9.jar;%APP_HOME%\lib\netflix-infix-0.3.0.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\log4j-api-2.17.2.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-2.6.7.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\snakeyaml-1.29.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.13.2.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.13.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.2.jar;%APP_HOME%\lib\jackson-core-2.13.2.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.13.2.jar;%APP_HOME%\lib\jackson-databind-2.13.2.1.jar;%APP_HOME%\lib\tomcat-embed-websocket-9.0.62.jar;%APP_HOME%\lib\tomcat-embed-core-9.0.62.jar;%APP_HOME%\lib\tomcat-embed-el-9.0.62.jar;%APP_HOME%\lib\native-protocol-1.5.0.jar;%APP_HOME%\lib\netty-handler-4.1.76.Final.jar;%APP_HOME%\lib\netty-codec-4.1.76.Final.jar;%APP_HOME%\lib\netty-transport-4.1.76.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.76.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.76.Final.jar;%APP_HOME%\lib\netty-common-4.1.76.Final.jar;%APP_HOME%\lib\java-driver-shaded-guava-25.1-jre-graal-sub-1.jar;%APP_HOME%\lib\config-1.4.1.jar;%APP_HOME%\lib\jnr-posix-3.1.5.jar;%APP_HOME%\lib\jnr-ffi-2.2.2.jar;%APP_HOME%\lib\jffi-1.3.1.jar;%APP_HOME%\lib\jffi-1.3.1-native.jar;%APP_HOME%\lib\asm-commons-9.1.jar;%APP_HOME%\lib\asm-util-9.1.jar;%APP_HOME%\lib\asm-analysis-9.1.jar;%APP_HOME%\lib\asm-tree-9.1.jar;%APP_HOME%\lib\asm-9.1.jar;%APP_HOME%\lib\jnr-a64asm-1.0.0.jar;%APP_HOME%\lib\jnr-x86asm-1.0.2.jar;%APP_HOME%\lib\jnr-constants-0.10.1.jar;%APP_HOME%\lib\micrometer-core-1.8.5.jar;%APP_HOME%\lib\HdrHistogram-2.1.12.jar;%APP_HOME%\lib\esri-geometry-api-1.2.1.jar;%APP_HOME%\lib\json-20090211.jar;%APP_HOME%\lib\jackson-core-asl-1.9.12.jar;%APP_HOME%\lib\spring-cloud-loadbalancer-3.1.0.jar;%APP_HOME%\lib\reactor-extra-3.4.8.jar;%APP_HOME%\lib\reactor-core-3.4.17.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\jcip-annotations-1.0-1.jar;%APP_HOME%\lib\spotbugs-annotations-3.1.12.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\spring-cloud-context-3.1.0.jar;%APP_HOME%\lib\spring-cloud-commons-3.1.0.jar;%APP_HOME%\lib\spring-security-crypto-5.6.3.jar;%APP_HOME%\lib\spring-security-rsa-1.0.10.RELEASE.jar;%APP_HOME%\lib\bcpkix-jdk15on-1.68.jar;%APP_HOME%\lib\bcprov-jdk15on-1.68.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\httpcore-4.4.15.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\spring-cloud-netflix-eureka-client-3.1.0.jar;%APP_HOME%\lib\xstream-1.4.18.jar;%APP_HOME%\lib\mxparser-1.2.2.jar;%APP_HOME%\lib\xmlpull-1.1.3.1.jar;%APP_HOME%\lib\jsr311-api-1.1.1.jar;%APP_HOME%\lib\guice-4.1.0.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\commons-configuration-1.10.jar;%APP_HOME%\lib\commons-lang-2.6.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\woodstox-core-6.2.1.jar;%APP_HOME%\lib\stax2-api-4.2.1.jar;%APP_HOME%\lib\evictor-1.0.0.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\jettison-1.4.0.jar;%APP_HOME%\lib\commons-math-2.2.jar;%APP_HOME%\lib\commons-jxpath-1.3.jar;%APP_HOME%\lib\joda-time-2.3.jar;%APP_HOME%\lib\antlr-runtime-3.4.jar;%APP_HOME%\lib\gson-2.8.9.jar;%APP_HOME%\lib\stringtemplate-3.2.1.jar;%APP_HOME%\lib\antlr-2.7.7.jar


@rem Execute customer-service
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CUSTOMER_SERVICE_OPTS%  -classpath "%CLASSPATH%" io.hotely.customer.CustomerApplication %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable CUSTOMER_SERVICE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%CUSTOMER_SERVICE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
