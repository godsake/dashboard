#!groovy

// A Declarative Pipeline is defined within a 'pipeline' block.
pipeline {

    // agent defines where the pipeline will run.
    agent any 
   
    // triggers {
    //     cron('@daily')
    // }
    tools { 
        maven 'Maven 3.5.0' 
        jdk 'jdk7' 
        nodejs 'nodejs_6.9.1'
        
    }   
    
    environment {   
        CERTIFICATS_DISABLED=" -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true"
        MAVEN_OPTS=" -Xms512m -Xmx2048m -XX:MaxPermSize=512m"  
        PATH = "/home/jenkins/bin:/home/jenkins/bin/autodeploy:$PATH"
        
        skipStage='no'
    }
    
    
 
    
    // Set log rotation, timeout and timestamps in the console
    options {
        buildDiscarder(logRotator(numToKeepStr:'20'))
        timestamps()
        timeout(time: 15, unit: 'MINUTES')
    }
  
    stages {
        
     
        stage("Clean the workspace") {
        
       
           
            steps {
                cleanWs()
                echo "checkout the code: branche ${BRANCH_NAME}"
               checkout scm                 
              //  git credentialsId: 'c383b28a-df5a-4c18-9092-64a79fd1678b', url: 'https://github.com/godsake/dashboard'
                
            }         
        }
    
       
        stage("Build the code") {
            when {
                environment name: 'skipStage', value: 'no'
            }
          
            steps {
                echo "Build the code"
                        
                sh 'mvn  clean deploy org.jacoco:jacoco-maven-plugin:prepare-agent   -Dmaven.test.failure.ignore=true '
                
                
            }
            
        }
     
     
     
     
    
        stage("Analyse the code and deploy to Tomcat"){
            environment {
                SONAR_GOAL="org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
                SONAR_HOST_URL="http://stha2n924:9000"
                //--projects claimsportal-web   org.jacoco:jacoco-maven-plugin:prepare-agent
                MAVEN_ARG="  -DworkspaceType=any -Denvproperties.failOnMissingEnvs=false --fail-never -Dsonar.language=java"         
                   
                
            }
            tools { 
                jdk 'jdk8' 
            }  
            steps {
                parallel(
                  
                    deploy: {
                        echo "deploy to tomcat"
                        sh 'mvn tomcat7:redeploy'
                    },
                    Sonar: {
                                
                        junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
                        //org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar
                        echo "Sonar"   
                                
                        withSonarQubeEnv('sonarqube') { 
                            sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar  -U -X  $CERTIFICATS_DISABLED  $MAVEN_ARG -Dsonar.host.url=$SONAR_HOST_URL -X'                                         
                        }                            
                    }
               
                )
              
            }
        }
    
      
        stage("Archive Build Output Artifacts"){
          
            steps {
                echo 'Archive'
                // archiveArtifacts artifacts: 'output/*.txt', excludes: 'output/*.md'
                archiveArtifacts artifacts:'**/*.war'
            }
        }
 
    
       
        
     
    
      
        
        stage("Communicate the DEVOPS process")
        {
         
            steps {
                //input 'Deploy to TEST?'
                echo 'Communicate the DEVOPS process'
            }
        }
    }
    
    // The order that sections are specified doesn't matter - this will still be run
    // after the stages, even though it's specified before the stages.
    post {
        // No matter what the build status is, run this step. There are other conditions
        // available as well, such as "success", "failed", "unstable", and "changed".
        always {
            //            echo "junit"
            //            junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
            
            echo "cleanup workspace"
            //  step([$class: 'WsCleanup'])
            //  junit '*/target/surefire-reports/*.xml'
        }
        success {
            echo "archive"
            //   archive "**/target/*.hpi"
            //   archive "**/target/site/jacoco/jacoco.xml"
        }
        unstable {
            echo "archive"
            //   archive "**/target/*.hpi"
            //  archive "**/target/site/jacoco/jacoco.xml"
        }
    }

}

