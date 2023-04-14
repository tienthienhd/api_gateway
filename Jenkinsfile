pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        withMaven(publisherStrategy: 'implicit') {
          sh 'mvn clean'
          sh 'mvn package'
        }

      }
    }

  }
}