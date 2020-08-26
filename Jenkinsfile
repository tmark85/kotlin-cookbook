pipeline {
    agent any
    triggers {
        pollSCM('*/5 * * * *')
    }

    stages {
        stage('Build and tests') {
            steps {
                echo "Running jdk ${env.JAVA_HOME}"
                sh 'java -version'
                sh 'javac -version'
                echo 'Building Assets...'
                 script {
                    gradlew('--info', 'clean', 'build')
                }
            }
        }
    }
}

///////////////////////
// Utility functions //
///////////////////////
def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
